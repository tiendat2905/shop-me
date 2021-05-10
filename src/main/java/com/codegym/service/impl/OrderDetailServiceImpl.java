package com.codegym.service.impl;

import com.codegym.model.Customer;
import com.codegym.model.Order;
import com.codegym.model.OrderDetail;
import com.codegym.repository.OrderDetailRepository;
import com.codegym.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl extends ValidateService implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> findAllByIsDeleteIsFalse() {
        return orderDetailRepository.findAllByIsDeleteIsFalse();
    }

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public void remove(Integer id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public Optional<OrderDetail> findById(Integer id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> findAllByOrderDetail(Order order) {
        return orderDetailRepository.findAllByOrderDetail(order);
    }


}
