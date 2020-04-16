package cz.rohlik.warehouse.domain;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    private String description;

    public Product(String name, BigDecimal unitPrice, String description) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.description = description;
    }
}
