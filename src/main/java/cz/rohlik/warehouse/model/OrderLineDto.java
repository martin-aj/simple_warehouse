package cz.rohlik.warehouse.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderLineDto {

    @ApiModelProperty(value = "Product identifier", required = true)
    private Long productId;

    @ApiModelProperty("Product name")
    private String name;

    @ApiModelProperty(value = "Number of ordered units", required = true)
    private Long quantity;

}
