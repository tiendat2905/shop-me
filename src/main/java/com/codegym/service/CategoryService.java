package com.codegym.service;

import com.codegym.model.Category;
import com.codegym.model.Province;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface CategoryService {

    public ArrayList<String> listResult(Category category);

    public List<Category> findAllByIsDeleteIsFalseOrderByIdDesc();

    public ArrayList<String> insert(Category category) throws SQLException;

    public ArrayList<String> update( Category category) throws SQLException;

    public ArrayList<String> delete(  Category category) throws SQLException;

    public Optional<Category> findById(int id) throws SQLException;

    public boolean checkDuplicate(Category category) throws SQLException;
}
