package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.ProductDto;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = new Product(productDto.getName(), productDto.getUnitPrice());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(long productId) {

    }

    @Override
    public void updateProduct(ProductDto product) {

    }
}
