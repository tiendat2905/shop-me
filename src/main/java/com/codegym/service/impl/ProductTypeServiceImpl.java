package com.codegym.service.impl;

import com.codegym.model.ProductType;
import com.codegym.model.Province;
import com.codegym.repository.ProductTypeRepository;
import com.codegym.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeServiceImpl extends  ValidateService implements ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public ArrayList<String> listResult(ProductType productType) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(productType.getTypeName(), 5, 20)) {
            listError.add("Lỗi nhập tên Thể loại sản phẩm");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }



    @Override
    public List<ProductType> findAllByIsDeleteIsFalse() {
        return productTypeRepository.findAllByIsDeleteIsFalse();
    }

    @Override
    public ArrayList<String> insert(ProductType productType) throws SQLException {
        ArrayList<String> listError = listResult(productType);
        if (listError.size() == 1 && listError.get(0).equalsIgnoreCase("Thành công")) {
            if (checkDuplicate(productType)) {
                listError.set(0, "Thể loại sản phẩm đã được đăng ký");
            } else {
                this.productTypeRepository.save(productType);
            }
        } else {
            listError.remove("Thành công");
        }
        return listError;
    }


    @Override
    public ArrayList<String> update(ProductType productType) throws SQLException {
        ArrayList<String> listError = listResult(productType);
        Optional<ProductType> productType1 = productTypeRepository.findById(productType.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (productType1.get().getTypeName().equals(productType.getTypeName())) {
                listError.set(0, "Không thực hiện chỉnh sửa gì");
            } else if (checkDuplicate(productType)) {
                listError.set(0, "Nội dung chỉnh sửa đã bị trùng");
            } else {
                productTypeRepository.save(productType);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(ProductType productType) throws SQLException {
        ArrayList<String> listError = listResult(productType);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            productType.setDelete(true);
            productTypeRepository.save(productType);
            listError.set(0, "Xóa thành công");
        } else {
            listError.set(0, "Xóa không thành công");
        }
        return listError;
    }


    @Override
    public Optional<ProductType> findById(int id) throws SQLException {
        return productTypeRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(ProductType productType) throws SQLException {
        ArrayList<ProductType> listProductType = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (ProductType productType1 : listProductType) {
            if (productType1.getTypeName().equals(productType.getTypeName())) {
                return true;
            }
        }
        return false;
    }
}
