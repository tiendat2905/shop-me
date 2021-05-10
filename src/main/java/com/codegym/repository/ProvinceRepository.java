package com.codegym.repository;

import com.codegym.model.Province;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProvinceRepository extends PagingAndSortingRepository<Province, Integer> {

    List<Province> findAllByIsDeleteIsFalse();

    
}
