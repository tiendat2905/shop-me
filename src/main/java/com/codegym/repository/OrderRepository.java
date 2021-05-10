package com.codegym.repository;


import com.codegym.model.Order;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.List;
public interface OrderRepository extends PagingAndSortingRepository<Order, Integer> {

    List<Order> findAllByIsDeleteIsFalse();
}
