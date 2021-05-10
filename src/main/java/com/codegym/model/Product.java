package com.codegym.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime publishDate = LocalDateTime.now();

    private String productName;

    @Transient
    private CommonsMultipartFile[] image;

    private String imageUrl;


    private double price;

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "idType", referencedColumnName = "id")
    private ProductType productType;


    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Product product = (Product) o;
        String nameP = product.getProductName();
        double priceP = product.getPrice();

        ValidationUtils.rejectIfEmpty(errors, "productName", "nameP.empty");
        ValidationUtils.rejectIfEmpty(errors, "price", "priceP.empty");

        if (nameP.length() > 30 || nameP.length() < 5) {
            errors.rejectValue("productName", "nameP.length");
        }
        if (priceP < 50) {
            errors.rejectValue("price", "priceP.length");
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", publishDate=" + publishDate +
                ", productName='" + productName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", isDelete=" + isDelete +
                '}';
    }
}