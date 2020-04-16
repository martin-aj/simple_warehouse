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
import cz.rohlik.warehouse.service.mappings.ProductMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ProductRepository productRepository;

    @Value("${warehouse.order.expiration:30}")
    private long orderExpiration;

    static final long CHECK_ORDER = 60000; // in millis

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
    public List<OrderDto> getOrders() {
        return Mappers.getMapper(OrderMapper.class).fromDomainList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) throws Exception {
        List<OrderLineDto> orderLinesDto = orderDto.getOrderLineDtos();
        List<OrderLineDto> missingOrderLinesDto = new ArrayList<>();
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();
        for(OrderLineDto orderLineDto : orderLinesDto) {
            final Long productId = orderLineDto.getProductId();
            final Long quantity = orderLineDto.getQuantity();
            Optional<Product> productOptional = productRepository.findById(productId);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                long quantityDecreased = product.getQuantity() - quantity;
                if (quantityDecreased < 0) {
                    missingOrderLinesDto.add(new OrderLineDto(productId, product.getName(), Math.abs(quantityDecreased)));
                } else {
                    OrderLine orderLine = new OrderLine();
                    orderLine.setQuantity(quantity);
                    orderLine.setOrder(order);
                    orderLine.setProduct(product);
                    orderLines.add(orderLine);
                    product.setQuantity(quantityDecreased);
                    productRepository.save(product);
                }
            } else {
                missingOrderLinesDto.add(new OrderLineDto(productId, orderLineDto.getName(), quantity));
            }
        }
        if (!missingOrderLinesDto.isEmpty()) {
            throw new Exception(missingOrderLinesDto.toString());
        }

        orderRepository.save(order);
        orderLineRepository.saveAll(orderLines);
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
}
