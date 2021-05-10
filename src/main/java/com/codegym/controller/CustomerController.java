package com.codegym.controller;

import com.codegym.model.Customer;
import com.codegym.model.Province;
import com.codegym.service.impl.CustomerServiceImpl;
import com.codegym.service.impl.ProvinceServiceImpl;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private ProvinceServiceImpl provinceService;

    @ModelAttribute("province")
    public Iterable<Province> provinces() {
        return provinceService.findAllByIsDeleteIsFalse();
    }

    @GetMapping("/listCustomer")
    public ModelAndView listCustomer() {
        List<Customer> customerList = customerService.findAllByIsDeleteIsFalse();
        ModelAndView mav = new ModelAndView("customer/list");
        mav.addObject("customerList", customerList);
        return mav;
    }

//    @GetMapping("/createCustomer")
//    public ModelAndView formCreateCustomer() {
//        ModelAndView mav = new ModelAndView("home-page/listShowCart");
//        mav.addObject("customer", new Customer());
//        return mav;
//    }
//
//    @PostMapping("/saveCustomer")
//    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult, Model model) throws SQLException {
//        new Customer().validate(customer, bindingResult);
//        if (bindingResult.hasFieldErrors()) {
//            return "home-page/listShowCart";
//        }
//        model.addAttribute("message",   customerService.insert(customer));
//        model.addAttribute("customer", new Customer());
//        return "home-page/listShowCart";
//    }

    @GetMapping("/{id}/edit")
    public ModelAndView formEditCustomer(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Customer> customer = customerService.findById(id);
        ModelAndView mav;
        if (customer == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("customer/edit");
            mav.addObject("customer", customer);
        }
        return mav;
    }

    @PostMapping("/editCustomer")
    public String editCustomer(@Valid @ModelAttribute("customer") Customer customer, BindingResult bind, Model model) throws SQLException {
        new Customer().validate(customer, bind);
        if (!bind.hasFieldErrors()) {

         model.addAttribute("message", customerService.update(customer));
        }
        return "customer/edit";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView formDeleteCustomer(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Customer> customer = customerService.findById(id);
        ModelAndView mav;
        if (customer == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("customer/delete");
            mav.addObject("customer", customer);
        }
        return mav;
    }

    @PostMapping("/deleteCustomer")
    public String deleteCustomer(Customer customer, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("message",  customerService.delete(customer));
        return "redirect:/customer/listCustomer";
    }

}
