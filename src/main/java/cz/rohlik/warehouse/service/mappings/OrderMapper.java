package cz.rohlik.warehouse.service.mappings;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.model.OrderLineDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderLine toDomain(OrderLineDto orderLineDto);

    List<OrderLine> toDomainList(List<OrderLineDto> orderLineDtos);

    List<OrderDto> fromDomainList(List<Order> order);
}
