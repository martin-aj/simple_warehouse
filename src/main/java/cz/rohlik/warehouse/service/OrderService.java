package cz.rohlik.warehouse.service;

import cz.rohlik.warehouse.model.OrderDto;
import java.util.List;

public interface OrderService {

    void createOrder(OrderDto order) throws Exception;

    void invalidateOrder(long orderId);

    void paymentOrder(long orderId);

    /**
     * List of all orders.
     */
    List<OrderDto> getOrders();
}
