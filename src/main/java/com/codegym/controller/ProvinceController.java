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
@RequestMapping("/province")
public class ProvinceController {

    @Autowired
    private ProvinceServiceImpl provinceService;

    @Autowired
    private CustomerServiceImpl customerService;


    @GetMapping("/listProvince")
    public ModelAndView listProvince() {
        List<Province> provinceList = provinceService.findAllByIsDeleteIsFalse();
        ModelAndView mav = new ModelAndView("province/list");
        mav.addObject("provinceList", provinceList);
        return mav;
    }

    @GetMapping("/createProvince")
    public ModelAndView formCreateProvince() {
        ModelAndView mav = new ModelAndView("province/create");
        mav.addObject("province", new Province());
        return mav;
    }

    @PostMapping("/saveProvince")
    public String saveProvince(@Valid @ModelAttribute(value = "province") Province province, BindingResult bindingResult, Model model) throws SQLException {
        new Province().validate(province, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "province/create";
        }
        model.addAttribute("message", provinceService.insert(province));
        model.addAttribute("province", new Province());
        return "province/create";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView formEditProvince(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Province> province = provinceService.findById(id);
        ModelAndView mav;
        if (province == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("province/edit");
            mav.addObject("province", province);
        }
        return mav;
    }

    @PostMapping("/editProvince")
    public String editProvince(@Valid @ModelAttribute(value = "province") Province province, BindingResult bindingResult, Model model) throws SQLException {
        new Province().validate(province, bindingResult);
        if (!bindingResult.hasFieldErrors()) {
            model.addAttribute("message", provinceService.update(province));
//            return "redirect:/province/listProvince";
        }
        return "province/edit";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView formDeleteProvince(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Province> province = provinceService.findById(id);
        ModelAndView mav;
        if (province == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("province/delete");
            mav.addObject("province", province);
        }
        return mav;
    }

    @PostMapping("/deleteProvince")
    public String deleteProvince(Province province, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("message", provinceService.delete(province));
        return "redirect:/province/listProvince";
    }

    @GetMapping("/{id}/view")
    public ModelAndView formViewCustomerByProvince(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Province> province = provinceService.findById(id);
        if (province == null) {
            return new ModelAndView("error/errorId");
        } else {
            List<Customer> listCustomer = customerService.findAllByProvince(province.get());
            ModelAndView mav;
            if (listCustomer.size() > 0) {
                mav = new ModelAndView("/province/view");
                mav.addObject("province", province);
                mav.addObject("listCustomer", listCustomer);
            } else {
                mav = new ModelAndView("error/notItem");
            }
            return mav;

        }
    }
}
