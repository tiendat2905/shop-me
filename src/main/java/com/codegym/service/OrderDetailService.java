package com.codegym.service;

import com.codegym.model.Customer;
import com.codegym.model.Order;
import com.codegym.model.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    List<OrderDetail> findAllByIsDeleteIsFalse();

    void save (OrderDetail orderDetail);

    void remove(Integer id);

    Optional<OrderDetail> findById(Integer id);

    List<OrderDetail> findAllByOrderDetail(Order order);

}
