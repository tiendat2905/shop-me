package com.codegym.repository;

import com.codegym.model.Customer;
import com.codegym.model.Province;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {
    List<Customer> findAllByIsDeleteIsFalse();

    List<Customer> findAllByProvince(Province province);
}
