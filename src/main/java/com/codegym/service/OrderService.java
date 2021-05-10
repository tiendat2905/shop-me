package com.codegym.service;


import com.codegym.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAllByIsDeleteIsFalse();

    void save (Order order);

    void remove(Integer id);

    Optional<Order> findById(Integer id);
}
