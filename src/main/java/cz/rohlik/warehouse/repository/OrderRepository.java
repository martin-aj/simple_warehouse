package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
