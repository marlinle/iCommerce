package au.com.nab.demo.domain;

import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
public class ProductDetail {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String sku;

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
