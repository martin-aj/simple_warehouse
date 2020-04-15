package cz.rohlik.warehouse.service;

import cz.rohlik.warehouse.model.OrderDto;

public interface OrderService {

    void createOrder(OrderDto order);

    void invalidateOrder(long orderId);

    void paymentOrder(long orderId);
}
