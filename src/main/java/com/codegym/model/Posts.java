package com.codegym.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Table(name = "posts")
public class Posts implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @Column(columnDefinition = "varchar(1250)")
    private String shortContent;

    @Column(columnDefinition = "longtext")
    private String fullContent;
    private LocalDateTime publishDate = LocalDateTime.now();

    @Transient
    private CommonsMultipartFile[] image;
    private String imageUrl;

    @Where(clause = "delete=false")
    private boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "idCategory", referencedColumnName = "id")
    private Category category;

    @Override
    public boolean supports(Class<?> aClass) {
        return Posts.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Posts posts = (Posts) o;
        String postsTi = posts.getTitle();
        String postsShort = posts.getShortContent();
        String postsFull = posts.getFullContent();


        ValidationUtils.rejectIfEmpty(errors,"title","postsTi.empty");
        ValidationUtils.rejectIfEmpty(errors,"shortContent","postsShort.empty");
        ValidationUtils.rejectIfEmpty(errors,"fullContent","postsFull.empty");

        if (postsTi.length() > 200 || postsTi.length() < 10){
            errors.rejectValue("title", "postsTi.length");
        }

    }
}
