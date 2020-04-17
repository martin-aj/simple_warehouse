package cz.rohlik.warehouse.model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private List<OrderLineDto> orderLines;

    private ZonedDateTime created = ZonedDateTime.now();

    private OrderState state = OrderState.ACTIVE;

    private BigDecimal price;
}
