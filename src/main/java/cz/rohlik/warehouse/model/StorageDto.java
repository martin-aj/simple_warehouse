package cz.rohlik.warehouse.model;

import cz.rohlik.warehouse.domain.Product;
import io.swagger.annotations.ApiModelProperty;

public class StorageDto {

    @ApiModelProperty(value = "Identification of product", required = true)
    Long id;

    @ApiModelProperty(value = "Product", required = true)
    Product product;

    @ApiModelProperty(value = "Number of available units", required = true)
    Long stockpile;

}
