package com.example.demo.resource;

import com.example.demo.model.Request;
import com.example.demo.model.Response;
import com.example.demo.service.UserDetailService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserResource {

    private final AuthenticationManager authenticationManager;

    private final UserDetailService userDetailService;

    private final JwtUtil jwtUtil;

    public UserResource(AuthenticationManager authenticationManager, UserDetailService userDetailService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailService = userDetailService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello Chuong, you get there with a cool jwt";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJsonWebTokenKey(@RequestBody Request request) throws Exception {

        String username = request.getUsername();
        String password = request.getPassword();

        try{
            System.out.println("call func authenticate start...");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            System.out.println("call func authenticate end....");
        } catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailService.loadUserByUsername(username);

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new Response(jwt));

    }


}
