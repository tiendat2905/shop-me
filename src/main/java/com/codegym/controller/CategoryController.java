package com.codegym.controller;

import com.codegym.model.Category;
import com.codegym.model.Customer;
import com.codegym.model.Posts;
import com.codegym.model.Province;
import com.codegym.service.impl.CategoryServiceImpl;
import com.codegym.service.impl.PostServiceImpl;
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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private PostServiceImpl postsService;

    @GetMapping("/listCategory")
    public ModelAndView listCategory() {
        List<Category> categories = categoryService.findAllByIsDeleteIsFalseOrderByIdDesc();
        ModelAndView mav = new ModelAndView("category/list");
        mav.addObject("categories", categories);
        return mav;
    }

    @GetMapping("/createCategory")
    public ModelAndView formCreateCategory() {
        ModelAndView mav = new ModelAndView("category/create");
        mav.addObject("category", new Category());
        return mav;
    }

    @PostMapping("/saveCategory")
    public String saveCategory(@Valid @ModelAttribute(value = "category") Category category, BindingResult bindingResult, Model model) throws SQLException {
        new Category().validate(category, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "category/create";
        }
        model.addAttribute("message", categoryService.insert(category));
        model.addAttribute("category", new Category());
        return "category/create";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView formEditCategory(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Category> category = categoryService.findById(id);
        ModelAndView mav;
        if (category == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("category/edit");
            mav.addObject("category", category);
        }
        return mav;
    }

    @PostMapping("/editCategory")
    public String editCategory(@Valid @ModelAttribute(value = "category") Category category, BindingResult bindingResult, Model model) throws SQLException {
        new Category().validate(category, bindingResult);
        if (!bindingResult.hasFieldErrors()) {
            model.addAttribute("message", categoryService.update(category));
//            return "redirect:/province/listProvince";
        }
        return "category/edit";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView formDeleteCategory(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Category> category = categoryService.findById(id);
        ModelAndView mav;
        if (category == null) {
            mav = new ModelAndView("error/errorId");
        } else {
            mav = new ModelAndView("category/delete");
            mav.addObject("category", category);
        }
        return mav;
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(Category category, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("message", categoryService.delete(category));
        return "redirect:/category/listCategory";
    }

    @GetMapping("/{id}/view")
    public ModelAndView formViewCustomerByCategory(@PathVariable(value = "id") Integer id) throws SQLException {
        Optional<Category> category = categoryService.findById(id);
        if (category == null) {
            return new ModelAndView("error/errorId");
        } else {
            List<Posts> postsList = postsService.findAllByCategory(category.get());
            ModelAndView mav;
            if (postsList.size() > 0) {
                mav = new ModelAndView("/category/view");
                mav.addObject("category", category);
                mav.addObject("postsList", postsList);
            } else {
                mav = new ModelAndView("error/notItem");
            }
            return mav;

        }
    }
}
