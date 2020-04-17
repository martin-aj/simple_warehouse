package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.exception.StockpileException;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.model.OrderLineDto;
import cz.rohlik.warehouse.model.OrderState;
import cz.rohlik.warehouse.repository.OrderLineRepository;
import cz.rohlik.warehouse.repository.OrderRepository;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.OrderService;
import cz.rohlik.warehouse.service.mappings.OrderMapper;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    @Value("${warehouse.order.expiration:30}")
    private long orderExpiration;

    private static final long CHECK_ORDER = 60000; // in millis
    private static final String ILLEGAL_STATE_ERR_MSG = "Given order is not active. Current state is: %s";
    private static final String STOCKPILE_ERR_MSG = "Missing products: %s";
    private static final String ENTITY_NOT_FOUND_ERR_MSG = "Unable to find an order with the '%s' id!";

    @Override
    @Transactional
    public void invalidateOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_ERR_MSG, orderId)));
        if (OrderState.ACTIVE != order.getState()) {
            throw new IllegalStateException(String.format(ILLEGAL_STATE_ERR_MSG, order.getState()));
        }
        order.setState(OrderState.INVALIDATE);
        List<OrderLine> orderLines = order.getOrderLines();
        for (OrderLine orderLine : orderLines) {
            Product product = orderLine.getProduct();
            product.setQuantity(product.getQuantity() + orderLine.getQuantity());
            productRepository.save(product);
        }
        orderRepository.save(order);
    }

    @Override
    public void paymentOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ENTITY_NOT_FOUND_ERR_MSG, orderId)));
        if (OrderState.ACTIVE.equals(order.getState())) {
            order.setState(OrderState.PAID);
            orderRepository.save(order);
        } else {
            throw new IllegalStateException(String.format(ILLEGAL_STATE_ERR_MSG, order.getState()));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrders() {
        return Mappers.getMapper(OrderMapper.class).fromDomainList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) {
        List<OrderLineDto> orderLinesDto = orderDto.getOrderLines();
        stockPileValidator(orderLinesDto);
        Order order = orderRepository.save(new Order());
        List<OrderLine> orderLines = new ArrayList<>();
        BigDecimal finalPrice = BigDecimal.ZERO;
        for(OrderLineDto orderLineDto : orderLinesDto) {
            final Long quantity = orderLineDto.getQuantity();
            Optional<Product> productOptional = productRepository.findById(orderLineDto.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                {
                    OrderLine orderLine = new OrderLine(quantity, order, product);
                    orderLines.add(orderLine);
                    product.setQuantity(product.getQuantity() - quantity);
                    productRepository.save(product);
                    finalPrice = finalPrice.add(product.getUnitPrice().multiply(new BigDecimal(quantity)));
                }
            }
        }

        orderLineRepository.saveAll(orderLines);
        order.setPrice(finalPrice);
        order.setOrderLines(orderLines);
        orderRepository.save(order);
    }

    /*
     * Invalidate orders, which are active for longer period than orderExpiration (default 30 minutes).
     * This method is scheduled every minute
     */
    @Scheduled(fixedDelay = CHECK_ORDER)
    @Transactional
    protected void invalidateActiveOrders() {
        List<Long> expiredOrderIds = orderRepository.findAll().stream()
                .filter(order -> OrderState.ACTIVE.equals(order.getState()))
                .filter(order -> order.getCreated().plusMinutes(orderExpiration).isBefore(ZonedDateTime.now()))
                .map(Order::getId)
                .collect(Collectors.toList());
        for (long expiredOrderId : expiredOrderIds) {
            invalidateOrder(expiredOrderId);
        }
    }

    /*
    * Check availability of ordered products against stockpile.
    * If product doesn't exists -> return
    * */
    public void stockPileValidator(List<OrderLineDto> orderLinesDto) {
        List<OrderLineDto> missingOrderLinesDto = new ArrayList<>();
        for(OrderLineDto orderLineDto : orderLinesDto) {
            final Long productId = orderLineDto.getProductId();
            final Long quantity = orderLineDto.getQuantity();
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                long quantityDecreased = product.getQuantity() - quantity;
                if (quantityDecreased < 0) {
                    missingOrderLinesDto.add(new OrderLineDto(productId, product.getName(), Math.abs(quantityDecreased)));
                }
            } else {
                missingOrderLinesDto.add(new OrderLineDto(productId, orderLineDto.getName(), quantity));
            }
        }
        if (!missingOrderLinesDto.isEmpty()) {
            throw new StockpileException(String.format(STOCKPILE_ERR_MSG, missingOrderLinesDto.toString()));
        }
    }
}
