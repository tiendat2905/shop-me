package com.codegym.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "productTypes")
public class ProductType implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String typeName;

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    @OneToMany(mappedBy = "productType")
    private List<Product> products;

    public ProductType() {
    }

    public ProductType(Integer id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductType.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductType productType = (ProductType) o;
        String nameTypeProduct = productType.getTypeName();

        ValidationUtils.rejectIfEmpty(errors, "typeName", "nameTypeProduct.empty");
        if (nameTypeProduct.length() > 30 || nameTypeProduct.length() < 2) {
            errors.rejectValue("typeName", "nameTypeProduct.length");
        }
    }
}