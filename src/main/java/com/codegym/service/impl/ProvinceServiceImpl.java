package com.codegym.service.impl;

import com.codegym.model.Province;
import com.codegym.repository.ProvinceRepository;
import com.codegym.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProvinceServiceImpl extends ValidateService implements ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public ArrayList<String> listResult(Province province) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(province.getProvinceName(), 5, 20)) {
            listError.add("Lỗi nhập tên Tỉnh thành");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }

    @Override
    public List<Province> findAllByIsDeleteIsFalse() {
        return provinceRepository.findAllByIsDeleteIsFalse();
    }

    @Override
    public ArrayList<String> insert(Province province) throws SQLException {
        ArrayList<String> listError = listResult(province);
        if (listError.size() == 1 && listError.get(0).equalsIgnoreCase("Thành công")) {
            if (checkDuplicate(province)) {
                listError.set(0, "Tỉnh thành đã được đăng ký");
            } else {
                this.provinceRepository.save(province);
            }
        } else {
            listError.remove("Thành công");
        }
        return listError;
    }


    @Override
    public ArrayList<String> update(Province province) throws SQLException {
        ArrayList<String> listError = listResult(province);
        Optional<Province> province1 = provinceRepository.findById(province.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (province1.get().getProvinceName().equals(province.getProvinceName())) {
                listError.set(0, "Không thực hiện chỉnh sửa gì");
            } else if (checkDuplicate(province)) {
                listError.set(0, "Nội dung chỉnh sửa đã bị trùng");
            } else {
                provinceRepository.save(province);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(Province province) throws SQLException {
        ArrayList<String> listError = listResult(province);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            province.setDelete(true);
            provinceRepository.save(province);
            listError.add("Xóa thành công");
        } else {
            listError.add("Xóa không thành công");
        }
        return listError;
    }


    @Override
    public Optional<Province> findById(int id) throws SQLException {
        return provinceRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(Province province) throws SQLException {
        ArrayList<Province> listProvince = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (Province province1 : listProvince) {
            if (province1.getProvinceName().equals(province.getProvinceName())) {
                return true;
            }
        }
        return false;
    }

}
