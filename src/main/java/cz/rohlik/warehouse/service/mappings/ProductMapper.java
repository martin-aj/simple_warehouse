package cz.rohlik.warehouse.service.mappings;

import cz.rohlik.warehouse.domain.Product;
import cz.rohlik.warehouse.model.ProductDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    Product toDomain(ProductDto productDto);

    ProductDto fromDomain(Product product);

    List<ProductDto> fromDomainList(List<Product> products);
}
