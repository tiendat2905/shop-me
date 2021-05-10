package com.codegym.controller;

import com.codegym.model.*;

import com.codegym.service.impl.CustomerServiceImpl;

import com.codegym.service.impl.OrderDetailServiceImpl;
import com.codegym.service.impl.OrderServiceImpl;
import com.codegym.service.impl.ProductServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ProductServiceImpl productService;

    @ModelAttribute("customers")
    private List<Customer> listCustomer() {
        return customerService.findAllByIsDeleteIsFalse();
    }

    @ModelAttribute("products")
    private List<Product> listProduct() {
        return productService.findAllByIsDeleteIsFalse();
    }

    @GetMapping("/listOrder")
    public ModelAndView listOrder() {
        List<Order> listOrder = orderService.findAllByIsDeleteIsFalse();
        ModelAndView mav = new ModelAndView("order/list");
        mav.addObject("listOrder", listOrder);
        return mav;
    }

    @GetMapping("/{id}/shipped")
    public String shipped(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Order> order = orderService.findById(id);
        if (order.get().getStatus() == "pending") {
            order.get().setStatus("processed");
        } else if (order.get().getStatus() == "processed") {
            order.get().setStatus("shipped");
        } else if (order.get().getStatus() == "shipped") {
            order.get().setStatus("success");
        } else if (order.get().getStatus() == "success") {
            order.get().setStatus("pending");
        }
        return "redirect:/order/listOrder";
    }

    @GetMapping("/createOrder")
    public ModelAndView createOrder() {
        ModelAndView mav = new ModelAndView("order/create");
        mav.addObject("order", new Order());
        return mav;
    }

    @PostMapping("saveOrder")
    public String saveOrder(Order order, Model model) throws SQLException {
        orderService.save(order);
        model.addAttribute("message", "Create Order successfully");
        return "order/create";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView deleteOrder(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Order> order = orderService.findById(id);
        ModelAndView mav;
        if (order == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("order/delete");
            mav.addObject("order", order);
        }
        return mav;
    }

    @PostMapping("/deleteOrder")
    public String deleteOrder(Order order, RedirectAttributes redirectAttributes) throws SQLException {
        order.setDelete(true);
        orderService.save(order);
        redirectAttributes.addFlashAttribute("message", "Create Order successfully");
        return "redirect:/order/listOrder";
    }

    @GetMapping("/{id}/view")
    public ModelAndView viewOrderDetail(@PathVariable(value = "id") Integer id) {
        Optional<Order> order = orderService.findById(id);
        ModelAndView mav;
        if (order == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            List<OrderDetail> detailList = orderDetailService.findAllByOrderDetail(order.get());
            if (detailList.size() <= 0) {
                mav = new ModelAndView("error/notItem");
            } else {
                mav = new ModelAndView("order/view");
                mav.addObject("detailList", detailList);
            }
        }
        return mav;
    }

}
