package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.ProductDto;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.ProductService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductDto productDto) {
        Product product = new Product(productDto.getName(), productDto.getUnitPrice(), productDto.getDescription());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        if (product.isPresent()) {
            final String description = productDto.getDescription();
            Product updatedProduct = product.get();
            updatedProduct.setName(productDto.getName());
            updatedProduct.setUnitPrice(productDto.getUnitPrice());
            if (description != null && !description.isEmpty()) {
                product.get().setDescription(description);
            }
            productRepository.save(updatedProduct);
        }
    }

    @Override
    public void getProducts() {
        System.out.println(productRepository.findAll());
    }
}
