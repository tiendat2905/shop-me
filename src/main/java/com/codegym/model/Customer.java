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
@Table(name = "customers")
public class Customer implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String customerName;


    private String email;
    private String address;
    private String phoneNumber;

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "idProvince", referencedColumnName = "id")
    private Province province;


    @OneToMany(mappedBy = "customer")
    private List<Order> orders;


    public Customer() {
    }

    public Customer(Integer id, String customerName, String email, String address, String phoneNumber) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Customer(Integer id, String customerName, String email, String address, String phoneNumber, Province province) {
        this.id = id;
        this.customerName = customerName;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.province = province;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Customer customer = (Customer) o;
        String cusName = customer.getCustomerName();
        String cusAddress = customer.getAddress();
        String cusPhone = customer.getPhoneNumber();
        String cusEmail = customer.getEmail();

        ValidationUtils.rejectIfEmpty(errors, "customerName", "cusName.empty");
        ValidationUtils.rejectIfEmpty(errors, "email", "cusEmail.empty");
        ValidationUtils.rejectIfEmpty(errors, "address", "cusAddress.empty");
        ValidationUtils.rejectIfEmpty(errors, "phoneNumber", "cusPhone.empty");

        if (cusName.length() > 30 || cusName.length() < 5) {
            errors.rejectValue("customerName", "cusName.length");
        }
        if (cusAddress.length() > 40 || cusAddress.length() < 10) {
            errors.rejectValue("address", "cusAddress.length");
        }
        if (cusPhone.length() > 11 || cusPhone.length() < 10) {
            errors.rejectValue("phoneNumber", "cusPhone.length");
        }
        if (cusEmail.length() > 30 || cusEmail.length() < 10) {
            errors.rejectValue("email", "cusEmail.length");
        }
        if (!cusPhone.startsWith("0")){
            errors.rejectValue("phoneNumber", "cusPhone.startsWith");
        }
        if (!cusPhone.matches("^(03|05|07|08|09)+([0-9]{8})\\b$")){
            errors.rejectValue("phoneNumber", "cusPhone.matches");
        }
        if (!cusName.matches("(^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$)")){
            errors.rejectValue("customerName", "cusName.matches");
        }
    }
}