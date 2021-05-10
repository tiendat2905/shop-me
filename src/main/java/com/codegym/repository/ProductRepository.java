package com.codegym.repository;

import com.codegym.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByIsDeleteIsFalse();

    Page<Product> findAllByIsDeleteIsFalse( Pageable pageable);

    List<Product> findAllByProductType(ProductType productType);

    Page<Product> findAllByProductTypeIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM products where isDelete = 0 ORDER BY rand() LIMIT 8 ")
    List<Product> findTop8Random();

    List<Product> findTop3ByOrderByIdDesc();

}
