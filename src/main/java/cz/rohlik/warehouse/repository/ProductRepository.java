package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
