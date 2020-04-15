package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public void createOrder(OrderDto order) {

    }

    @Override
    public void invalidateOrder(long orderId) {

    }

    @Override
    public void paymentOrder(long orderId) {

    }
}
