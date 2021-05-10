package com.codegym.repository;

import com.codegym.model.Product;
import com.codegym.model.ProductType;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductTypeRepository extends PagingAndSortingRepository<ProductType, Integer> {
    List<ProductType> findAllByIsDeleteIsFalse();
}
