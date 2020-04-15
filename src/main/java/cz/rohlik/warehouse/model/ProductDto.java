package cz.rohlik.warehouse.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {

    @ApiModelProperty(value = "Identification of product", required = true)
    Long id;

    @ApiModelProperty(value = "Name of product", required = true)
    String name;

    @ApiModelProperty(value = "Unit price", required = true)
    BigDecimal unitPrice;
}
