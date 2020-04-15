package cz.rohlik.warehouse.model;

import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private List<OrderLineDto> orderLineDtos;
}
