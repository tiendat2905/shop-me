package com.codegym.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

    public class ValidateService {

        public String trimString(String name) {
            String str = name.trim();
            return str;
        }

        public boolean validateLength(String str, int min, int max){
            String names = trimString(str);
            int nameLength = names.length();
            return min <= nameLength && nameLength <= max;
        }

        public boolean validateName(String name, int min, int max) {
            String nameCustomer = trimString(name);
            String regex = "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂẾưăạảấầẩẫậắằẳẵặẹẻẽềềểếỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s\\W|_]+$";
            if (validateLength(nameCustomer, min, max)){
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(nameCustomer);
                return matcher.matches();
            }
            return false;
        }

        public boolean validateEmail(String email) {
            String emailCustomer = trimString(email);
            String regex = "^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,3}){1,2}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(emailCustomer);
            return matcher.matches();
        }

        public boolean validatePassport(String passport) {
            String passportCustomer = trimString(passport);
            String regex = "^[0-9]{9}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(passportCustomer);
            return matcher.matches();
        }

        public boolean validateNumberPhone(String numberPhone) {
            String numberPhoneCustomer = trimString(numberPhone);
            String regex = "(03|05|07|08|09)+([0-9]{8})\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(numberPhoneCustomer);
            return matcher.matches();
        }

        public boolean validateGender(String gender) {
            String genderCustomer = trimString(gender);
            String regex = "^(N)(am|ữ)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(genderCustomer);
            return matcher.matches();
        }

        public boolean validateBirthDay(String birthday) {
            String birthdayCustomer = trimString(birthday);
            String regex = "((0[1-9]|[12]\\d|3[01])(.|-|/)(0[1-9]|1[0-2])(.|-|/)[12]\\d{3})";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(birthdayCustomer);
            return matcher.matches();
        }

        public boolean validatePassword(String pass) {
            String passAccount = trimString(pass);
            String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,20}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(passAccount);
            return matcher.matches();
        }
    }


