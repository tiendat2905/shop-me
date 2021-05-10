package com.codegym.model;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status= "pending";

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    private double totalPrice;
    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", isDelete=" + isDelete +
                ", totalPrice=" + totalPrice +
                '}';
    }
}