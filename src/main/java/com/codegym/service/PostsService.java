package com.codegym.service;

import com.codegym.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostsService {


    List<Posts> findAllByIsDeleteIsFalseOrderByIdDesc();

    List<Posts> findAllByCategory(Category category);

    public ArrayList<String> listResult(Posts posts);


    public ArrayList<String> insert(Posts posts) throws SQLException;

    public ArrayList<String> update(Posts posts) throws SQLException;

    public ArrayList<String> delete(Posts posts) throws SQLException;

    public Optional<Posts> findById(int id) throws SQLException;

    public boolean checkDuplicate(Posts posts) throws SQLException;

    Page<Posts> findAllByCategoryIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable);

    Page<Posts> findAllByIsDeleteIsFalse( Pageable pageable);
}

