package com.codegym.service;

import com.codegym.model.Order;
import com.codegym.model.Product;
import com.codegym.model.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProductService {


    public ArrayList<String> listResult(Product product);

    Page<Product> findAllByIsDeleteIsFalse( Pageable pageable);

    public   List<Product> findAllByIsDeleteIsFalse();

    List<Product> findAllByProductType(ProductType productType);

    public ArrayList<String> insert(Product product) throws SQLException;

    public ArrayList<String> update( Product product) throws SQLException;

    public ArrayList<String> delete(  Product product) throws SQLException;

    public Optional<Product> findById(int id) throws SQLException;

    public boolean checkDuplicate(Product product) throws SQLException;

    Page<Product> findAllByProductTypeIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    List<Product> findTop8Random();

    List<Product> findTop3ByOrderByIdDesc();


}
