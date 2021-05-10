package com.codegym.service.impl;

import com.codegym.model.Customer;
import com.codegym.model.Product;
import com.codegym.model.Province;
import com.codegym.repository.CustomerRepository;
import com.codegym.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class CustomerServiceImpl extends ValidateService implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAllByProvince(Province province) {
        return customerRepository.findAllByProvince(province);
    }

    @Override
    public ArrayList<String> listResult(Customer customer) {
        ArrayList<String> listError = new ArrayList<>();
        if (!validateName(customer.getCustomerName(), 5, 20)) {
            listError.add("Lỗi nhập tên Tỉnh thành");
        }
        if (listError.size() == 0) {
            listError.add("Thành công");
        }
        return listError;
    }

    @Override
    public List<Customer> findAllByIsDeleteIsFalse() {
        return customerRepository.findAllByIsDeleteIsFalse();
    }

    @Override
    public ArrayList<String> insert(Customer customer) throws SQLException {
        ArrayList<String> listError = listResult(customer);
        ArrayList<Customer> listCustomer = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (Customer customer1 : listCustomer) {
            if (customer1.getEmail().equals(customer.getEmail())) {
                listError.add("Email đã được đăng ký");
            }
            if (customer1.getPhoneNumber().equals(customer.getPhoneNumber())) {
                listError.add("Số điện thoại đã được đăng ký");
            }
            if (customer1.getCustomerName().equals(customer.getCustomerName())) {
                listError.add("Tên đã được đăng ký");
            }
        }

        if (listError.size() == 1 && listError.get(0).equals("Thành công")) {
            if (checkDuplicate(customer)) {
                listError.set(0, "Đã tồn tại khách hàng này");
            } else {
                this.customerRepository.save(customer);
            }
        } else {
            listError.remove("Thành công");
        }

        return listError;
    }


    @Override
    public ArrayList<String> update(Customer customer) throws SQLException {
        ArrayList<String> listError = listResult(customer);
        Optional<Customer> customer1 = customerRepository.findById(customer.getId());
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            if (customer1.get().getCustomerName().equals(customer.getCustomerName())) {
                listError.set(0, "Không thực hiện chỉnh sửa gì");
            } else if (checkUpdate(customer)) {
                listError.set(0, "Nội dung chỉnh sửa đã bị trùng");
            } else {
                customerRepository.save(customer);
            }
        }
        return listError;
    }

    @Override
    public ArrayList<String> delete(Customer customer) throws SQLException {
        ArrayList<String> listError = listResult(customer);
        if (listError.get(0).equalsIgnoreCase("Thành công")) {
            customer.setDelete(true);
            customerRepository.save(customer);
            listError.set(0, "Xóa thành công");
        } else {
            listError.set(0, "Xóa không thành công");
        }
        return listError;
    }


    @Override
    public Optional<Customer> findById(int id) throws SQLException {
        return customerRepository.findById(id);
    }

    @Override
    public boolean checkDuplicate(Customer customer) throws SQLException {
        ArrayList<Customer> listCustomer = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (Customer customer1 : listCustomer) {
            if (customer1.getCustomerName().equals(customer.getCustomerName()) ||
                    customer1.getEmail().equals(customer.getEmail()) ||
                    customer1.getPhoneNumber().equals(customer.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkUpdate(Customer customer) throws SQLException {
        ArrayList<Customer> listMiss = new ArrayList<>(findAllByIsDeleteIsFalse());
        for (Customer miss1 : listMiss) {
            if (miss1.getCustomerName().equals(customer.getCustomerName()) &&
                    miss1.getPhoneNumber().equals(customer.getPhoneNumber()) &&
                    miss1.getEmail().equals(customer.getEmail()) &&
                    miss1.getAddress().equals(customer.getAddress()) &&
                    miss1.getProvince().getProvinceName().equals(customer.getProvince().getProvinceName())) {
                return true;
            }
        }
        return false;
    }
}

