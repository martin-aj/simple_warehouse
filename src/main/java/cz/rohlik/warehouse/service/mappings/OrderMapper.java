package cz.rohlik.warehouse.service.mappings;

import cz.rohlik.warehouse.domain.Order;
import cz.rohlik.warehouse.domain.OrderLine;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.model.OrderLineDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderMapper {

    OrderLine toDomain(OrderLineDto orderLineDto);

    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.id", target = "productId")
    public OrderLineDto fromOrderLineDomain(OrderLine orderLine);

    @Mapping(source = "id", target = "id")
    public OrderDto fromOrderDomain(Order order);

    List<OrderLine> toDomainList(List<OrderLineDto> orderLineDtos);

    List<OrderDto> fromDomainList(List<Order> order);
}
