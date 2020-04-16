package cz.rohlik.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

    @JsonProperty("order")
    private List<OrderLineDto> orderLineDtos;

    private ZonedDateTime created = ZonedDateTime.now();

    private OrderState state = OrderState.ACTIVE;

    private BigDecimal price;
}
