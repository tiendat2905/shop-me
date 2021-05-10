package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.ProductType;
import com.codegym.model.Province;
import com.codegym.service.impl.ProductServiceImpl;
import com.codegym.service.impl.ProductTypeServiceImpl;
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
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductTypeServiceImpl productTypeService;

    @ModelAttribute("productType")
    public List<ProductType> productTypeList() {
        return productTypeService.findAllByIsDeleteIsFalse();
    }

    @GetMapping("/listProduct")
    public ModelAndView listProduct() {
        List<Product> listProduct = productService.findAllByIsDeleteIsFalse();
        ModelAndView mav = new ModelAndView("product/list");
        mav.addObject("listProduct", listProduct);
        return mav;
    }

    @GetMapping("/createProduct")
    public ModelAndView createProduct() {
        ModelAndView mav = new ModelAndView("product/create");
        mav.addObject("product", new Product());
        return mav;
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model, HttpServletRequest request) throws IOException, SQLException {
        new Product().validate(product, result);
        if (result.hasFieldErrors()) {
            return "product/create";
        }
        String uploadRootPath = request.getServletContext().getRealPath("upload");
        File uploadRootDir = new File(uploadRootPath);
        String uploadLocalPath = "D:\\Hoc Tap\\Module 4\\CaseStudyM4\\shop-me\\src\\main\\webapp\\upload";
        File uploadLocalDir = new File(uploadLocalPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir();
        }
        CommonsMultipartFile[] files = product.getImage();
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
                product.setImageUrl(name);
            }
        }

        model.addAttribute("message",  productService.insert(product));
        return "product/create";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editProduct(@PathVariable("id") Integer id) throws SQLException {
        Optional<Product> product = productService.findById(id);
        ModelAndView mav;
        if (product != null) {
            mav = new ModelAndView("product/edit");
            mav.addObject("product", product);
        } else {
            mav = new ModelAndView("error/errorId");
        }
        return mav;
    }

    @PostMapping("/editProduct")
    public String editProduct(HttpServletRequest request, @ModelAttribute("product") Product product,Model model) throws Exception {
        String uploadRootPath = request.getServletContext().getRealPath("upload");
        File uploadRootDir = new File(uploadRootPath);
        String uploadLocalPath = "D:\\Hoc Tap\\Module 4\\CaseStudyM4\\shop-me\\src\\main\\webapp\\upload";
        File uploadLocalDir = new File(uploadLocalPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdir();
        }
        CommonsMultipartFile[] files = product.getImage();
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
                product.setImageUrl(name);
            }
        }
        if (product != null) {

            model.addAttribute("message",    productService.update(product));
            return "product/edit";
        }
        return "error/errorId";
    }

    @GetMapping("/{id}/delete")
    public ModelAndView deleteProduct(@PathVariable("id") Integer id) throws SQLException {
        Optional<Product> product = productService.findById(id);
        ModelAndView mav;
        if (product != null) {
            mav = new ModelAndView("product/delete");
            mav.addObject("product", product);
        } else {
            mav = new ModelAndView("error/errorId");
        }
        return mav;
    }

    @PostMapping("/deleteProduct")
    public String deleteProduct(Product product, RedirectAttributes redirectAttributes) throws SQLException {
        redirectAttributes.addFlashAttribute("message",  productService.delete(product));
        return "redirect:/product/listProduct";
    }
}
