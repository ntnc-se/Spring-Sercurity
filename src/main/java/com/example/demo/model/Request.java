package com.example.demo.model;

import lombok.Data;

@Data
public class Request {

    // request body include username and password
    private String username;
    private String password;

}
