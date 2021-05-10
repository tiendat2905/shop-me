package com.codegym.controller;

import com.codegym.model.Order;
import com.codegym.model.OrderDetail;
import com.codegym.model.Product;
import com.codegym.model.ProductType;
import com.codegym.service.impl.ProductServiceImpl;
import com.codegym.service.impl.ProductTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/shop-me")
public class HomeController {

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/home")
    public String homeView(Model model,
                           @RequestParam(name = "page", defaultValue = "1") int page,
                           @RequestParam(name = "type", required = false) Integer type,
                           HttpSession session
    ) {

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
        Order order = (Order) session.getAttribute("order");
        System.out.println(order);
        return "home-page/home";
    }


}
