/*
 * Copyright (c) 2018. CreativeDock s.r.o. All rights reserved.
 */
package cz.rohlik.warehouse.repository;

import cz.rohlik.warehouse.domain.OrderLine;
import org.springframework.data.repository.CrudRepository;

public interface OrderLineRepository extends CrudRepository<OrderLine, Long> {

}
