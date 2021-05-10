package com.codegym.model;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
@Getter
@Setter
@Entity
@Table(name = "oderDetails")
public class OrderDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Product product;

    private Integer quantity;
    private double priceOder;

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "idOrder")
    private Order order;

    @Override
    public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", priceOder=" + priceOder +
                ", isDelete=" + isDelete +
                '}';
    }
}