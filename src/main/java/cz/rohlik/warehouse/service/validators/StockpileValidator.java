/*
 * Copyright (c) 2018. CreativeDock s.r.o. All rights reserved.
 */
package cz.rohlik.warehouse.service.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { StockpileValidatorImpl.class })
@Documented
public @interface StockpileValidator {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}