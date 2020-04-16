package cz.rohlik.warehouse.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {

    @ApiModelProperty("Identification of product for updates")
    Long id;

    @ApiModelProperty(value = "Name of product", required = true)
    private String name;

    @ApiModelProperty(value = "Unit price", required = true)
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "Quantity in the storage", required = true)
    private Long quantity;

    @ApiModelProperty("Description of product")
    private String description;
}
