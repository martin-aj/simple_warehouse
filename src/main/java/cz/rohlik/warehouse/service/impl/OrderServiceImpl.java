package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.domain.Product;
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
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    @Value("${warehouse.order.expiration:30}")
    private long orderExpiration;

    private static final long CHECK_ORDER = 60000; // in millis
//    private static final String STOCKPILE_ERR_MSG = "Product are not available in this amount";

    @Override
    @Transactional
    public void invalidateOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
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
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        order.setState(OrderState.PAID);
        orderRepository.save(order);
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
    protected void invalidateActiveOrders() {
        List<Order> expiredOrders = orderRepository.findAll().stream()
                .filter(order -> OrderState.ACTIVE.equals(order.getState()))
                .filter(order -> order.getCreated().plusMinutes(orderExpiration).isBefore(ZonedDateTime.now()))
                .collect(Collectors.toList());
        for (Order order : expiredOrders) {
            invalidateOrder(order.getId());
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
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, missingOrderLinesDto.toString());
        }
    }
}
