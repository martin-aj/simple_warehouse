package cz.rohlik.warehouse.service.impl;

import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.ProductDto;
import cz.rohlik.warehouse.repository.ProductRepository;
import cz.rohlik.warehouse.service.ProductService;
import cz.rohlik.warehouse.service.mappings.ProductMapper;
import cz.rohlik.warehouse.utils.NullPropertiesUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public void createProduct(ProductDto productDto) {
        productRepository.save(Mappers.getMapper(ProductMapper.class).toDomain(productDto));
    }

    @Override
    public void deleteProduct(long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void updateProduct(ProductDto productDto) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        if (product.isPresent()) {
            Product productUpdate = Mappers.getMapper(ProductMapper.class).toDomain(productDto);
            Product updatedProduct = product.get();
            BeanUtils.copyProperties(productUpdate, updatedProduct, NullPropertiesUtils.getNullPropertyNames(productUpdate));
            productRepository.save(updatedProduct);
        }
    }

    @Override
    public List<ProductDto> getProducts() {
        return Mappers.getMapper(ProductMapper.class).fromDomainList(productRepository.findAll());
    }
}
