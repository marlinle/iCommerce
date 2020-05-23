package au.com.nab.demo.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    @Column
    private Float price;

    @Column
    private String brand;

    @Column
    private String color;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ProductDetail detail;
}
