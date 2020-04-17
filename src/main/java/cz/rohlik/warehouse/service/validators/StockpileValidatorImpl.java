/*
 * Copyright (c) 2018. CreativeDock s.r.o. All rights reserved.
 */
package cz.rohlik.warehouse.service.validators;

import cz.rohlik.warehouse.model.OrderDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StockpileValidatorImpl implements ConstraintValidator<StockpileValidator, OrderDto> {

    @Override
    public boolean isValid(OrderDto value, ConstraintValidatorContext context) {
        return false;
    }
}
