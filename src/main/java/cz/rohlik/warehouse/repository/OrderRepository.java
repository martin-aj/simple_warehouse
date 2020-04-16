package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.Order;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();
}
