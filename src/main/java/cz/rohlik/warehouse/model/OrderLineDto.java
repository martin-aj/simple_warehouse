package cz.rohlik.warehouse.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineDto {

    @ApiModelProperty(value = "Product identifier", required = true)
    private Long productId;

    @ApiModelProperty("Product name")
    private String name;

    @ApiModelProperty(value = "Number of ordered units", required = true)
    private Long quantity;

}
