package cz.rohlik.warehouse.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cz.rohlik.warehouse.config.SwaggerConfig;
import cz.rohlik.warehouse.model.OrderDto;
import cz.rohlik.warehouse.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Api(value = "/orders", tags = {SwaggerConfig.TAG_ORDERS})
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * A creation endpoint for new {@link cz.rohlik.warehouse.domain.Order} entity.
     *
     * @param order describe order - product id and quantity
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @ApiOperation(value = "Creates a new order.")
    public void createProduct(@RequestBody @Valid OrderDto order) throws Exception{
        orderService.createOrder(order);
    }

    /**
     * An invalidation endpoint for existing {@link cz.rohlik.warehouse.domain.Order} entity.
     *
     * @param orderId identifier of deleted order
     */
    @PutMapping(value = "inactivate/{orderId}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Invalidate order.")
    public void inactivateOrder(@PathVariable @NonNull Long orderId) {
        orderService.invalidateOrder(orderId);
    }

    /**
     * An update endpoint for existing {@link cz.rohlik.warehouse.domain.Order} entity.
     *
     * @param orderId identification of placed order
     */
    @PutMapping(value = "payment/{orderId}")
    @ResponseStatus(NO_CONTENT)
    @ApiOperation(value = "Update status of existing order.")
    public void paymentOrder(@PathVariable @NonNull Long orderId) {
        orderService.paymentOrder(orderId);
    }
}
