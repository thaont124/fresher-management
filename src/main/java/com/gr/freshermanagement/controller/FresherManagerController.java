package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.entity.Fresher;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.service.FresherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/manager/fresher")
@RequiredArgsConstructor
public class FresherManagerController {
    private final EmployeeService employeeService;
    private final FresherService fresherService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Page<EmployeeResponse> getAllFreshers(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "15") int size) {
        return fresherService.getFreshersPaginated(page, size);
    }

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createNewFresher(
            @RequestBody @Valid NewEmployeeRequest newFresherRequest){
        try {
            NewFresherResponse createdFresher = employeeService.createNewEmployee(newFresherRequest);
            return new ResponseEntity<>(createdFresher, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
