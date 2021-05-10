package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Order;
import com.codegym.model.Posts;
import com.codegym.model.Product;
import com.codegym.service.impl.CategoryServiceImpl;
import com.codegym.service.impl.PostServiceImpl;
import com.codegym.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/shop-me")
public class Ui_PostController {
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/blog")
    public String showPosts(Model model,
                            @RequestParam(name = "page", defaultValue = "1") int page,
                            @RequestParam(name = "cate", required = false) Integer cate){
        model.addAttribute("cateList", categoryService.findAllByIsDeleteIsFalseOrderByIdDesc());
        model.addAttribute("productTop3", productService.findTop3ByOrderByIdDesc());

        PageRequest pageRequest = PageRequest.of(page - 1, 4, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Posts> postsPage = null;
        if (cate == null) {
            postsPage = postService.findAllByIsDeleteIsFalse(pageRequest);
        } else {
            postsPage = postService.findAllByCategoryIdAndIsDeleteIsFalseOrderByPublishDateDesc(cate, pageRequest);
        }

        postsPage.getPageable().getPageNumber();
        model.addAttribute("postsPage", postsPage);
        return "home-page/post";
    }


}
