package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/manager/fresher")
public class FresherManagerController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("create/{centerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<?> createNewFresher(
            @RequestBody @Valid NewEmployeeRequest newFresherRequest,
            @PathVariable Long centerId){
        return new ResponseEntity<>(employeeService.createNewEmployee(newFresherRequest, centerId), HttpStatus.CREATED);
    }
}
