package com.codegym.service.impl;

import com.codegym.model.User;
import com.codegym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException("user name not found");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        String role = user.getRole().getNameRole();
        authorities.add(new SimpleGrantedAuthority(role));
        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassWord()).authorities(authorities).build();

    }
}
