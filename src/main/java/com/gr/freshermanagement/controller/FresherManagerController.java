package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mananger/fresher")
public class FresherManagerController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("create")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<?> createNewFresher(@RequestBody NewEmployeeRequest newFresherRequest){
        return new ResponseEntity<>(employeeService.createNewEmployee(newFresherRequest), HttpStatus.CREATED);
    }
}
