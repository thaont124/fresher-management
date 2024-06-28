package com.gr.freshermanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @GetMapping
        @PreAuthorize("hasAuthority('DIRECTOR')")
    private ResponseEntity<?> permitAllControl(){
        Map<String, String> response = new HashMap<>();
        response.put("qq", "d");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("fresher")
    private ResponseEntity<?> permitFresher(){
        Map<String, String> response = new HashMap<>();
        response.put("qq", "f");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    private ResponseEntity<?> permitAdmin(){
        Map<String, String> response = new HashMap<>();
        response.put("qq", "a");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}