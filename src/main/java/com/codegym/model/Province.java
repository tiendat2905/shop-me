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
@Table(name = "provinces")
public class Province implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String provinceName;

    @Where(clause = "isDelete=false")
    private boolean isDelete = false;

    @OneToMany(mappedBy = "province")
    private List<Customer> customers;

    public Province() {
    }

    public Province(Integer id, String provinceName, List<Customer> customers) {
        this.id = id;
        this.provinceName = provinceName;
        this.customers = customers;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Province.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Province province = (Province) o;
        String name = province.getProvinceName();
        ValidationUtils.rejectIfEmpty(errors, "provinceName","name.empty");
        if (name.length()>19 || name.length()<5){
            errors.rejectValue("provinceName", "name.length");
        }
    }
}