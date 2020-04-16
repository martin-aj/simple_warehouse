package cz.rohlik.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

    @JsonProperty("order")
    private List<OrderLineDto> orderLineDtos;
}
