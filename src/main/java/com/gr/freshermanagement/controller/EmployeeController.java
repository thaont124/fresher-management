package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.service.AccountService;
import com.gr.freshermanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final AccountService accountService;


    @PatchMapping("update")
    public ResponseEntity<?> updateInfo(@RequestBody Employee employeeDetails){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        EmployeeResponse updatedEmployee = employeeService.updateEmployee(username, employeeDetails);
        return ResponseEntity.ok(ResponseGeneral.of(200, "update success", updatedEmployee));
    }

}
