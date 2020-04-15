package cz.rohlik.warehouse.domain;

import cz.rohlik.warehouse.model.OrderState;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The Order identifies an entity for basket of products with quantity and summary price.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "\"Order\"")
public class Order {

    /**
     * Order identifier.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<OrderLine> orderLines;

    /**
     * Timestamp when entity was created.
     */
    @Column(name = "INSERT_DT", nullable = false, updatable = false)
    private ZonedDateTime created = ZonedDateTime.now();

    private OrderState state = OrderState.ACTIVE;
}
