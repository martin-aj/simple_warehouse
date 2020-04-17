package cz.rohlik.warehouse.service;

import cz.rohlik.warehouse.model.OrderDto;
import java.util.List;

public interface OrderService {

    /**
     * Creates a new {@link cz.rohlik.warehouse.domain.Order} entity.
     *
     * @param order   order object for creation
     */
    void createOrder(OrderDto order);

    /**
     * Makes an update of state for existing order.
     * Change of state - Active => Invalidate.
     * Return products quantity to stockpile
     *
     * @param orderId   identifier of product to be invalidated
     */
    void invalidateOrder(long orderId);

    /**
     * Makes an update of state for existing order.
     * Change of state - Active => Paid.
     *
     * @param orderId   identifier of product to be paid
     */
    void paymentOrder(long orderId);

    /**
     * List of all orders.
     */
    List<OrderDto> getOrders();
}
