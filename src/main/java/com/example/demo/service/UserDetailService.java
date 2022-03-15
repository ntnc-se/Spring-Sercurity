package com.example.demo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {

        System.out.println("load user by user name: " + user);
        // normally, call service implement > call repository > get username and password from db (check if have)
        // this is hardcode to get user info
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String new_pass = bCryptPasswordEncoder.encode("123456789");
        System.out.println("new pass: " + new_pass);

        return new User("chuongntn1", new_pass, new ArrayList<>());

    }

}
