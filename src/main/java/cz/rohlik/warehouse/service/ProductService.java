package cz.rohlik.warehouse.service;

import cz.rohlik.warehouse.model.ProductDto;
import java.util.List;

public interface ProductService {

    /**
     * Creates a new {@link cz.rohlik.warehouse.domain.Product} entity.
     *
     * @param product   product object for creation
     */
    void createProduct(ProductDto product);

    /**
     * Makes an update for existing product. Only non-null properties are changed.
     *
     * @param productId   identifier of product to be deleted
     */
    void deleteProduct(long productId);

    /**
     * Makes an update for existing product. Only non-null properties are changed.
     *
     * @param product   properties to be changed
     */
    void updateProduct(ProductDto product);

    /**
     * List of all products.
     */
    List<ProductDto> getProducts();

}
