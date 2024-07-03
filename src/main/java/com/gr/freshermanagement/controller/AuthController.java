package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.service.AccountService;
import com.gr.freshermanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(accountService.signup(signupRequest));
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(accountService.login(request));
    }
}
