package com.codegym.service;

import com.codegym.model.Province;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProvinceService {

        public ArrayList<String> listResult(Province province);

        public   List<Province> findAllByIsDeleteIsFalse();

        public ArrayList<String> insert(Province province) throws SQLException;

        public ArrayList<String> update( Province province) throws SQLException;

        public ArrayList<String> delete(  Province province) throws SQLException;

        public Optional<Province> findById(int id) throws SQLException;

        public boolean checkDuplicate(Province province) throws SQLException;

}
