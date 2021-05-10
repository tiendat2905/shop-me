package com.codegym.controller;

import com.codegym.service.impl.CategoryServiceImpl;
import com.codegym.service.impl.PostServiceImpl;
import com.codegym.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;

@Controller
@RequestMapping("/shop-me")
public class Ui_PostsDetailController {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("{id}/post-detail")
    public String showPost(@PathVariable Integer id,
             @RequestParam(name = "cate", required = false) Integer cate, Model model) throws SQLException {
        model.addAttribute("post",postService.findById(id).get());
        model.addAttribute("cateList", categoryService.findAllByIsDeleteIsFalseOrderByIdDesc());
        model.addAttribute("productTop3", productService.findTop3ByOrderByIdDesc());
        return "home-page/post-detail";
    }
}
