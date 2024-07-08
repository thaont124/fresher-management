package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    @GetMapping("info/{accountId}")
    public ResponseEntity<?> getInfoFresher(@PathVariable Long accountId){
        try {
            EmployeeResponse createdFresher = employeeService.getInfo(accountId);
            return new ResponseEntity<>(
                    ResponseGeneral.of(201, "Add success", createdFresher), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ResponseGeneral.of(400, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
    @PatchMapping("update/{accountId}")
    public ResponseEntity<?> update(@PathVariable Long accountId,
            @RequestBody EmployeeUpdateRequest request){
        try {
            EmployeeResponse createdFresher = employeeService.updateInfo(request, accountId);
            return new ResponseEntity<>(
                    ResponseGeneral.of(201, "Add success", createdFresher), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ResponseGeneral.of(400, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
