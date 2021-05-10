package com.codegym.service.impl;

import com.codegym.model.Category;
import com.codegym.model.Province;
import com.codegym.repository.CategoryRepository;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends ValidateService implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ArrayList<String> listResult(Category category) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(category.getNameCategory(), 3, 20)) {
            listError.add("Lỗi nhập tên Thể loại bài viết");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }

    @Override
    public List<Category> findAllByIsDeleteIsFalseOrderByIdDesc() {
        return categoryRepository.findAllByIsDeleteIsFalseOrderByIdDesc();
    }

    @Override
    public ArrayList<String> insert(Category category) throws SQLException {
        ArrayList<String> listError = listResult(category);
        if (listError.size() == 1 && listError.get(0).equalsIgnoreCase("Thành công")) {
            if (checkDuplicate(category)) {
                listError.set(0, "Tỉnh thành đã được đăng ký");
            } else {
                this.categoryRepository.save(category);
            }
        } else {
            listError.remove("Thành công");
        }
        return listError;
    }

    @Override
    public ArrayList<String> update(Category category) throws SQLException {
        ArrayList<String> listError = listResult(category);
        Optional<Category> category1 = categoryRepository.findById(category.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (category1.get().getNameCategory().equals(category.getNameCategory())) {
                listError.set(0, "Không thực hiện chỉnh sửa gì");
            } else if (checkDuplicate(category)) {
                listError.set(0, "Nội dung chỉnh sửa đã bị trùng");
            } else {
                categoryRepository.save(category);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(Category category) throws SQLException {
        ArrayList<String> listError = listResult(category);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            category.setDelete(true);
            categoryRepository.save(category);
            listError.set(0,"Xóa thành công");
        } else {
            listError.set(0, "Xóa không thành công");
        }
        return listError;
    }

    @Override
    public Optional<Category> findById(int id) throws SQLException {
        return categoryRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(Category category) throws SQLException {
        ArrayList<Category> categoryArrayList = new ArrayList<>(findAllByIsDeleteIsFalseOrderByIdDesc());
        for (Category category1 : categoryArrayList) {
            if (category1.getNameCategory().equals(category.getNameCategory())) {
                return true;
            }
        }
        return false;
    }
}
