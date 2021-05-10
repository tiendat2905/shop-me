package com.codegym.controller;

import com.codegym.service.impl.ProductServiceImpl;
import com.codegym.service.impl.ProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;


@Controller
@RequestMapping("/shop-me")
public class    ProductDetailsController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @GetMapping("/{idType}/product-details/{idProduct}")
    public String showProduct(@PathVariable Integer idType, @PathVariable Integer idProduct, Model model) throws SQLException {

        model.addAttribute("type", productTypeService.findById(idType).get());

        model.addAttribute("product", productService.findById(idProduct).get());

        model.addAttribute("list8Ran", productService.findTop8Random());

        return "home-page/product-details";
    }
}
