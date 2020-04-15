package cz.rohlik.warehouse.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cz.rohlik.warehouse.config.SwaggerConfig;
import cz.rohlik.warehouse.model.ProductDto;
import cz.rohlik.warehouse.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Api(value = "/products", tags = {SwaggerConfig.TAG_PRODUCTS})
public class ProductController {

    private final ProductService productService;

    /**
     * A creation endpoint for new {@link cz.rohlik.warehouse.domain.Product} entity.
     *
     * @param product describe product
     */
    @PostMapping(value = "/products", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates a new product.")
    public void createUser(@Valid ProductDto product) {
        productService.createProduct(product);
    }

}
