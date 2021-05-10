package com.codegym.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categorys")
public class Category implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nameCategory;

    @Where(clause = "delete = false")
    private boolean isDelete = false;

    @OneToMany(mappedBy = "category")
    private List<Posts> posts;

    @Override
    public boolean supports(Class<?> aClass) {
        return Category.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
    Category category = (Category) o;
    String categoryNa = category.getNameCategory();
        ValidationUtils.rejectIfEmpty(errors,"nameCategory", "categoryNa.empty");
        if (categoryNa.length()>50 || categoryNa.length()<3){
            errors.rejectValue("nameCategory","categoryNa.length");
        }
        if (!categoryNa.matches("(^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$)")){
            errors.rejectValue("nameCategory", "categoryNa.matches");
        }
    }
}
