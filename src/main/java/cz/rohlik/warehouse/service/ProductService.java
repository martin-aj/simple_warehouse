package cz.rohlik.warehouse.service;

import cz.rohlik.warehouse.model.ProductDto;

public interface ProductService {

    void createProduct(ProductDto product);

    void deleteProduct(long productId);

    void updateProduct(ProductDto product);

    void getProducts();

}
