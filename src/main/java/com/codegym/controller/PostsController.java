package com.codegym.controller;

import com.codegym.model.*;
import com.codegym.service.impl.CategoryServiceImpl;
import com.codegym.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @ModelAttribute("listCategory")
    public List<Category> categories() {
        return categoryService.findAllByIsDeleteIsFalseOrderByIdDesc();
    }

    @GetMapping("/listPosts")
    public ModelAndView listCustomer() {
        List<Posts> postsList = postService.findAllByIsDeleteIsFalseOrderByIdDesc();
        ModelAndView mav = new ModelAndView("posts/list");
        mav.addObject("postsList", postsList);
        return mav;
    }

    @GetMapping("/createPosts")
    public ModelAndView formCreatePosts() {
        ModelAndView mav = new ModelAndView("posts/create");
        mav.addObject("posts", new Posts());
        return mav;
    }

    @PostMapping("/savePosts")
    public String savePosts(@Valid @ModelAttribute("posts") Posts posts, HttpServletRequest request
            , BindingResult bindingResult, Model model) throws SQLException, SQLException, IOException {

        new Posts().validate(posts, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "posts/create";
        }
        String uploadRootPath = request.getServletContext().getRealPath("upload");
        File uploadRootDir = new File(uploadRootPath);
        String uploadLocalPath = "D:\\Hoc Tap\\Module 4\\CaseStudyM4\\shop-me\\src\\main\\webapp\\upload";
        File uploadLocalDir = new File(uploadLocalPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir();
        }
        CommonsMultipartFile[] files = posts.getImage();
//        Map<File, String> uploadFile = new HashMap<>();
        for (CommonsMultipartFile commonsMultipartFile : files) {
            // Tên file gốc tại Clien
            String name = commonsMultipartFile.getOriginalFilename();
            if (name != null && name.length() > 0) {
                // Tạo file tại Server
                File severFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                // Luồng ghi dữ liệu vào file trên Server
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(severFile));
                stream.write(commonsMultipartFile.getBytes());
                stream.close();
                File localFile = new File(uploadLocalDir.getAbsolutePath() + File.separator + name);
                // Luồng ghi dữ liệu vào file trên Server
                BufferedOutputStream streamLocal = new BufferedOutputStream(new FileOutputStream(localFile));
                streamLocal.write(commonsMultipartFile.getBytes());
                streamLocal.close();
                posts.setImageUrl(name);
            }
        }
        model.addAttribute("message", postService.insert(posts));
        return "posts/create";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editPosts(@PathVariable("id") Integer id) throws SQLException {
        Optional<Posts> posts = postService.findById(id);
        ModelAndView mav;
        if (posts != null) {
            mav = new ModelAndView("posts/edit");
            mav.addObject("posts", posts);
        } else {
            mav = new ModelAndView("error/errorId");
        }
        return mav;
    }

    @PostMapping("/editPosts")
    public String editPostProduct(HttpServletRequest request, @ModelAttribute("posts") Posts posts, Model model) throws Exception {
        String uploadRootPath = request.getServletContext().getRealPath("upload");
        File uploadRootDir = new File(uploadRootPath);
        String uploadLocalPath = "D:\\Hoc Tap\\Module 4\\CaseStudyM4\\shop-me\\src\\main\\webapp\\upload";
        File uploadLocalDir = new File(uploadLocalPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir();
        }
        CommonsMultipartFile[] files = posts.getImage();
//        Map<File, String> uploadFile = new HashMap<>();
        for (CommonsMultipartFile commonsMultipartFile : files) {
            // Tên file gốc tại Clien
            String name = commonsMultipartFile.getOriginalFilename();
            if (name != null && name.length() > 0) {
                // Tạo file tại Server
                File severFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                // Luồng ghi dữ liệu vào file trên Server
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(severFile));
                stream.write(commonsMultipartFile.getBytes());
                stream.close();
                File localFile = new File(uploadLocalDir.getAbsolutePath() + File.separator + name);
                // Luồng ghi dữ liệu vào file trên Server
                BufferedOutputStream streamLocal = new BufferedOutputStream(new FileOutputStream(localFile));
                streamLocal.write(commonsMultipartFile.getBytes());
                streamLocal.close();
                posts.setImageUrl(name);
            }
        }
        if (posts != null) {

            model.addAttribute("message", postService.update(posts));
            return "posts/edit";
        }
        return "error/errorId";
    }


    @GetMapping("/{id}/delete")
    public ModelAndView deletePosts(@PathVariable("id") Integer id) throws SQLException {
        Optional<Posts> posts = postService.findById(id);
        ModelAndView mav;
        if (posts != null) {
            mav = new ModelAndView("posts/delete");
            mav.addObject("posts", posts);
        } else {
            mav = new ModelAndView("error/errorId");
        }
        return mav;
    }

    @PostMapping("/deletePosts")
    public String deletePosts(Posts posts, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("message",  postService.delete(posts));
        return "redirect:/posts/listPosts";
    }

    @GetMapping("/{id}/view")
    public ModelAndView viewPosts(@PathVariable("id") Integer id) throws SQLException {
        Optional<Posts> posts = postService.findById(id);
        ModelAndView mav;
        if (posts != null) {
            mav = new ModelAndView("posts/view");
            mav.addObject("posts", posts);
        } else {
            mav = new ModelAndView("error/errorId");
        }
        return mav;
    }
}
