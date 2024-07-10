package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AccountService accountService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        return new ResponseEntity<>(ResponseGeneral.of(201, "Signup success",
                accountService.signup(signupRequest)), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return new ResponseEntity<>(ResponseGeneral.of(200, "Login success",
                accountService.login(request)), HttpStatus.OK);
    }
}
