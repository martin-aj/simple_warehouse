package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.model.OrderState;
import cz.rohlik.warehouse.repository.OrderRepository;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.OrderService;
import cz.rohlik.warehouse.service.mappings.OrderMapper;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void createOrder(OrderDto orderDto) {
        List<OrderLine> orderLines = Mappers.getMapper(OrderMapper.class).toDomainList(orderDto.getOrderLineDtos());
        for (OrderLine orderLine : orderLines) {

        }

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
}
