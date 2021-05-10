package com.codegym.controller;

import com.codegym.model.*;
import com.codegym.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.*;

@Controller
public class AddOrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private OrderDetailServiceImpl orderDetailService;

    @Autowired
    private ProvinceServiceImpl provinceService;

    @ModelAttribute("provinces")
    private List<Province> listProvince() {
        return provinceService.findAllByIsDeleteIsFalse();
    }

    @GetMapping("/shop-me/addOrder/{id}/{quantity}")
    public String addOrder(@PathVariable(value = "id") Integer id,
                           @PathVariable(value = "quantity") Integer quantity,
                           HttpServletRequest request,
                           HttpSession session) throws SQLException {

        Optional<Product> optionalProduct = productService.findById(id);
        if (optionalProduct.isEmpty())
            return "redirect:/shop-me/home";

        Product product = optionalProduct.get();

        // product co
        Order order = (Order) session.getAttribute("order");
        if (order == null) {
            order = new Order();
            order.setOrderDetails(new ArrayList<>());
        }
        // order co
        boolean exists = false;
        // co san trong order
        double total = 0;

        for (OrderDetail od : order.getOrderDetails()) {
            if (od.getProduct().getId() == product.getId()) {
                Integer quantity1 = od.getQuantity();
                Integer newQuantity = quantity + quantity1;
                od.setQuantity(newQuantity);
                od.setPriceOder(newQuantity * product.getPrice());
                exists = true;
                break;
            }
        }

        // chua co trong order
        if (!exists) {
            OrderDetail detail = new OrderDetail();
            detail.setId(product.getId());
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setPriceOder(quantity * product.getPrice());
            order.getOrderDetails().add(detail);
        }
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            total += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
        }
        order.setTotalPrice(total);
        session.setAttribute("order", order);
        return "redirect:" + request.getHeader("Referer");

    }


    @GetMapping(value = "/shop-me/showCart")
    public String showListCart(Model model) {
        Customer customer = new Customer();
        customer.setProvince(customer.getProvince());
        model.addAttribute("customer", customer);
        return "home-page/listShowCart";
    }


    @GetMapping("/shop-me/showCart/deleteProduct/{idProduct}")
    public String deleteProductInCart(@PathVariable(name = "idProduct") Integer idProduct, HttpSession session, HttpServletRequest request) throws SQLException {
        Order order = (Order) session.getAttribute("order");

        if (order == null)
            throw new RuntimeException("Invalid request");

        List<OrderDetail> details = order.getOrderDetails();
        if (details.size() == 1) {
            session.setAttribute("order", null);
            return "redirect:" + request.getHeader("Referer");
        }

//        list co nhieu sp
        OrderDetail removing = null;
        for (OrderDetail detail : details) {
            if (detail.getId() == idProduct) {
                removing = detail;
                break;
            }
        }
        if (removing == null)
            throw new RuntimeException("Invalid request");

        details.remove(removing);

        double total = 0;
        for (OrderDetail detail : details) {
            total += detail.getProduct().getPrice() * detail.getQuantity();
        }
        order.setTotalPrice(total);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/shop-me/showCart/deleteProduct")
    public String deleteAllProduct(HttpSession session, HttpServletRequest request) {
        session.setAttribute("order", null);
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/shop-me/updateOrder/{data}")
    public String updateOrder(@PathVariable(value = "data") String data,
                              HttpSession session, HttpServletRequest request) {
        Map<Integer, Integer> quantityMap = new HashMap<>();
        try {
            String[] parts = data.split("-");
            for (String part : parts) {
                String[] dataPart = part.split(":");
                Integer productId = Integer.parseInt(dataPart[0]);
                Integer productQuantity = Integer.parseInt(dataPart[1]);
                quantityMap.put(productId, productQuantity);
            }
        } catch (Exception e) {
            throw new RuntimeException("request invalid");
        }

        Order order = (Order) session.getAttribute("order");
        List<OrderDetail> detailList = order.getOrderDetails();
        for (OrderDetail detail : detailList) {
            Product currProduct = detail.getProduct();
            Integer quantity = quantityMap.get(currProduct.getId());
            if (quantity != null)
                detail.setQuantity(quantity);
            detail.setPriceOder(detail.getProduct().getPrice() * detail.getQuantity());
        }


        double total = 0;
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            total += orderDetail.getProduct().getPrice() * orderDetail.getQuantity();
        }
        order.setTotalPrice(total);
        session.setAttribute("order", order);
        return "redirect:" + request.getHeader("Referer");
    }


    @PostMapping("/shop-me/showCart/saveCustomer")
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult bindingResult,
                               HttpSession session) throws SQLException {
        new Customer().validate(customer, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "home-page/listShowCart";
        }
        customerService.insert(customer);
        Order order = (Order) session.getAttribute("order");
        order.setCustomer(customer);
        List<OrderDetail> details = order.getOrderDetails();
        orderService.save(order);
        for (OrderDetail detail : details) {
            detail.setOrder(order);
            orderDetailService.save(detail);
        }
        session.setAttribute("order", null);

        return "home-page/check-outSuccess";
    }


}
