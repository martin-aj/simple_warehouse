package cz.rohlik.warehouse.controller;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cz.rohlik.warehouse.config.SwaggerConfig;
import cz.rohlik.warehouse.model.ProductDto;
import cz.rohlik.warehouse.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(value = "/products", tags = {SwaggerConfig.TAG_PRODUCTS})
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * A creation endpoint for new {@link cz.rohlik.warehouse.domain.Product} entity.
     *
     * @param product describe product
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates a new product.")
    public void createProduct(@RequestBody @Valid ProductDto product) {
        productService.createProduct(product);
    }

    /**
     * A deletion endpoint for existing {@link cz.rohlik.warehouse.domain.Product} entity.
     *
     * @param productId identifier of deleted product
     */
    @DeleteMapping(value = "/{productId}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Creates a new product.")
    public void deleteProduct(@PathVariable @NonNull Long productId) {
        productService.deleteProduct(productId);
    }

    /**
     * An update endpoint for existing {@link cz.rohlik.warehouse.domain.Product} entity.
     *
     * @param product describe product
     */
    @PatchMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Update an existing product.")
    public void updateProduct(@RequestBody @Valid ProductDto product) {
        productService.updateProduct(product);
    }

    @GetMapping
    @ResponseStatus(OK)
    @ApiOperation(value = "List of all products.")
    public List<ProductDto> getProducts() {
        return productService.getProducts();
    }
}
