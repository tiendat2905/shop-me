package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.service.impl.ProductServiceImpl;
import com.codegym.service.impl.ProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop-me")
public class ShopController {

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/shop")
    public String shopView(Model model,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "type", required = false) Integer type) {

        model.addAttribute("typeList", productTypeService.findAllByIsDeleteIsFalse());
        PageRequest pageRequest = PageRequest.of(page - 1, 16, Sort.by(Sort.Direction.DESC, "publishDate"));
        Page<Product> productPage = null;
        if (type == null) {
            productPage = productService.findAllByIsDeleteIsFalse(pageRequest);
        } else {
            productPage = productService.findAllByProductTypeIdAndIsDeleteIsFalseOrderByPublishDateDesc(type, pageRequest);
        }

        productPage.getPageable().getPageNumber();
        model.addAttribute("productPage", productPage);

        return "home-page/shop";
    }
}
