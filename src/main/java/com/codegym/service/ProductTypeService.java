package com.codegym.service;

import com.codegym.model.ProductType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProductTypeService {
    public ArrayList<String> listResult(ProductType productType);

    public   List<ProductType> findAllByIsDeleteIsFalse();

    public ArrayList<String> insert(ProductType productType) throws SQLException;

    public ArrayList<String> update( ProductType productType) throws SQLException;

    public ArrayList<String> delete(  ProductType productType) throws SQLException;

    public Optional<ProductType> findById(int id) throws SQLException;

    public boolean checkDuplicate(ProductType productType) throws SQLException;
}
