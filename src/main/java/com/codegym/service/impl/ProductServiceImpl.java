package com.codegym.service.impl;

import com.codegym.model.Order;
import com.codegym.model.Product;
import com.codegym.model.ProductType;
import com.codegym.repository.ProductRepository;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class ProductServiceImpl extends ValidateService implements ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public ArrayList<String> listResult(Product product) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(product.getProductName(), 5, 20)) {
            listError.add("Lỗi nhập tên sản phẩm");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }

    @Override
    public Page<Product> findAllByIsDeleteIsFalse(Pageable pageable) {
        return productRepository.findAllByIsDeleteIsFalse(pageable);
    }


    @Override
    public List<Product> findAllByIsDeleteIsFalse() {
        return productRepository.findAllByIsDeleteIsFalse();
    }

    @Override
    public List<Product> findAllByProductType(ProductType productType) {
        return productRepository.findAllByProductType(productType);
    }

    @Override
    public ArrayList<String> insert(Product product) throws SQLException {
        ArrayList<String> listError = listResult(product);
        if (listError.size() == 1 && listError.get(0).equalsIgnoreCase("Thành công")) {
            if (checkDuplicate(product)) {
                listError.set(0, "Lỗi: Tên Sản phẩm hoặc nhãn hiệu đã được đăng ký");
            } else {
                this.productRepository.save(product);
            }
        } else {
            listError.remove("Thành công");
        }
        return listError;
    }


    @Override
    public ArrayList<String> update(Product product) throws SQLException {
        ArrayList<String> listError = listResult(product);
        Optional<Product> product1 = productRepository.findById(product.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (product1.get().getProductName().equals(product.getProductName())) {
                listError.set(0, "Lỗi: Không thực hiện chỉnh sửa gì");
            } else if (checkDuplicate(product)) {
                listError.set(0, "Lỗi: Nội dung chỉnh sửa đã bị trùng");
            } else {
                productRepository.save(product);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(Product product) throws SQLException {
        ArrayList<String> listError = listResult(product);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            product.setDelete(true);
            productRepository.save(product);
            listError.set(0, "Xóa thành công");
        } else {
            listError.set(0, "Xóa không thành công");
        }
        return listError;
    }


    @Override
    public Optional<Product> findById(int id) throws SQLException {
        return productRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(Product product) throws SQLException {
        ArrayList<Product> listProduct = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (Product product1 : listProduct) {
            if (product1.getProductName().equals(product.getProductName()) || product1.getImageUrl().equals(product.getImageUrl())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Page<Product> findAllByProductTypeIdAndIsDeleteIsFalseOrderByPublishDateDesc(Integer id, Pageable pageable) {
        return productRepository.findAllByProductTypeIdAndIsDeleteIsFalseOrderByPublishDateDesc(id, pageable);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> findTop8Random() {
        return productRepository.findTop8Random();
    }

    @Override
    public List<Product> findTop3ByOrderByIdDesc() {
        return productRepository.findTop3ByOrderByIdDesc();
    }

}
