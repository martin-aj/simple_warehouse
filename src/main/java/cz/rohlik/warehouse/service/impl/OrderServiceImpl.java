package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.model.OrderLineDto;
import cz.rohlik.warehouse.model.OrderState;
import cz.rohlik.warehouse.repository.OrderRepository;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.OrderService;
import cz.rohlik.warehouse.service.mappings.OrderMapper;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private final ProductRepository productRepository;

    @Value("${warehouse.order.expiration:30}")
    private long orderExpiration;

    static final long CHECK_ORDER = 60000; // in millis

    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) throws Exception {
//        List<OrderLine> orderLines = Mappers.getMapper(OrderMapper.class).toDomainList(orderDto.getOrderLineDtos());
//        List<OrderLineDto> orderLinesDto = orderDto.getOrderLineDtos()
//        List<OrderLineDto> missingOrderLineDto = new ArrayList<>();
//        for (OrderLineDto orderLine : orderLines) {
//            Product product = productRepository.findById(orderLine.getPro)
//            if (orderLine.getQuantity() - order)
//            missingOrderLineDto.add()
//        }
//        if (missingOrderLineDto.size() > 0) {
//            throw new Exception(missingOrderLineDto.toString());
//        }
    }

    @Override
    @Transactional
    public void invalidateOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.setState(OrderState.INVALIDATE);
        List<OrderLine> orderLines = order.getOrderLines();
        for (OrderLine orderLine : orderLines) {
            Product product = orderLine.getProduct();
            Long quantity = product.getQuantity() + orderLine.getQuantity();
            product.setQuantity(quantity);
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
