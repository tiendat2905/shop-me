package com.codegym.repository;

import com.codegym.model.Category;
import com.codegym.model.Posts;
import com.codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostsRepository extends PagingAndSortingRepository<Posts, Integer> {

    Page<Posts> findAllByIsDeleteIsFalse( Pageable pageable);

    List<Posts> findAllByIsDeleteIsFalseOrderByIdDesc();

    List<Posts> findAllByCategory(Category category);

    Page<Posts> findAllByCategoryIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable);
}
