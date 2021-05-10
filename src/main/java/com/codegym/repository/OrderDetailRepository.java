package com.codegym.repository;

import com.codegym.model.Customer;
import com.codegym.model.Order;
import com.codegym.model.OrderDetail;
import com.codegym.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderDetailRepository extends PagingAndSortingRepository<OrderDetail, Integer> {
    List<OrderDetail> findAllByIsDeleteIsFalse();


    @Query(nativeQuery = true, value = "SELECT * FROM oderdetails \n" +
            "\tINNER JOIN orders \n" +
            "\tON oderdetails.idOrder = orders.id\n" +
            "\tinner join products\n" +
            "\ton oderdetails.product_id= products.id where idOrder= ?")
    List<OrderDetail> findAllByOrderDetail(Order order);
}
