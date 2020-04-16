package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.Product;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();
}
