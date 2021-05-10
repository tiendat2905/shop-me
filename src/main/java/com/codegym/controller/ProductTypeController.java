package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductType;
import com.codegym.service.impl.ProductServiceImpl;
import com.codegym.service.impl.ProductTypeServiceImpl;
import org.apache.commons.io.input.ClosedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @Autowired
    private ProductServiceImpl productService;


    @GetMapping("/listProductType")
    public ModelAndView listProductType() {
        List<ProductType> listProductType = productTypeService.findAllByIsDeleteIsFalse();
        ModelAndView mav = new ModelAndView("productType/list");
        mav.addObject("listProductType", listProductType);
        return mav;
    }

    @GetMapping("/createProductType")
    public ModelAndView createProductType() {
        ModelAndView mav = new ModelAndView("productType/create");
        mav.addObject("productType", new ProductType());
        return mav;
    }

    @PostMapping("/addProductType")
    public ModelAndView addProductType(@Valid @ModelAttribute("productType") ProductType productType, BindingResult bind, Model model) throws SQLException {
        new ProductType().validate(productType, bind);
        ModelAndView mav;
        if (bind.hasFieldErrors()) {
            mav = new ModelAndView("productType/create");
        } else {
            mav = new ModelAndView("productType/create");
            mav.addObject("productType", new ProductType());
            model.addAttribute("message", productTypeService.insert(productType));
        }
        return mav;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView createProductType(@PathVariable("id") Integer id) throws SQLException {
        Optional<ProductType> productType = productTypeService.findById(id);
        ModelAndView mav;
        if (productType == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("productType/edit");
            mav.addObject("productType", productType);
        }
        return mav;
    }


    @PostMapping("/editProductType")
    public String editProductType(@Valid @ModelAttribute("productType") ProductType productType, BindingResult bind,Model model) throws SQLException {
        new ProductType().validate(productType, bind);
        if (bind.hasFieldErrors()) {
            return "productType/edit";
        } else {
            model.addAttribute("message", productTypeService.update(productType));
            return "productType/edit";
        }
    }

    @GetMapping("/{id}/delete")
    public ModelAndView deleteProductType(@PathVariable("id") Integer id) throws SQLException {
        Optional<ProductType> productType = productTypeService.findById(id);
        ModelAndView mav;
        if (productType == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("productType/delete");
            mav.addObject("productType", productType);
        }
        return mav;
    }

    @PostMapping("/deleteProductType")
    public String deleteProductType(ProductType productType, RedirectAttributes redirect) throws SQLException {
        redirect.addFlashAttribute("message", productTypeService.delete(productType));
        return "redirect:/productType/listProductType";
    }

    @GetMapping("/{id}/view")
    public ModelAndView viewProductType(@PathVariable("id") Integer id) throws SQLException {
        Optional<ProductType> productType = productTypeService.findById(id);
        ModelAndView mav;
        if (productType == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            List<Product> listProduct = productService.findAllByProductType(productType.get());
            if (listProduct.size() == 0) {
                mav = new ModelAndView("error/notItem");
            } else {
                mav = new ModelAndView("productType/view");
                mav.addObject("productType", productType);
                mav.addObject("listProduct", listProduct);
            }
        }
        return mav;
    }
}
