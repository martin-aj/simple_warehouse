package cz.rohlik.warehouse.controller;

import cz.rohlik.warehouse.config.SwaggerConfig;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Api(value = "/orders", tags = {SwaggerConfig.TAG_ORDERS})
public class OrderController {

}
