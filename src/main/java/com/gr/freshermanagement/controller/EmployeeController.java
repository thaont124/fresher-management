package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.employee.UpdateEmployeeRequest;
import com.gr.freshermanagement.dto.response.employee.EmployeeResponse;
import com.gr.freshermanagement.service.AccountService;
import com.gr.freshermanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final AccountService accountService;

    @PatchMapping("employee/info")
    public ResponseEntity<?> viewInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        EmployeeResponse employee = employeeService.getEmployee(username);
        return ResponseEntity.ok(ResponseGeneral.of(200, "update success", employee));
    }

    @PatchMapping("employee/update")
    public ResponseEntity<?> updateInfo(@RequestBody UpdateEmployeeRequest employeeDetails){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        EmployeeResponse updatedEmployee = employeeService.updateEmployee(username, employeeDetails);
        return ResponseEntity.ok(ResponseGeneral.of(200, "update success", updatedEmployee));
    }

    @DeleteMapping("/manager/delete/{employeeId}")
    public ResponseEntity<?> delete(@PathVariable Long employeeId){
        employeeService.deactivateStatus(employeeId);
        return ResponseEntity.ok(ResponseGeneral.of(200, "delete success", null));
    }

    @PatchMapping("employee/change-avatar")
    public ResponseEntity<?> changeAvatar(@RequestParam MultipartFile avatar){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        employeeService.changeAvatar(username, avatar);
        return ResponseEntity.ok(ResponseGeneral.of(200, "change avatar success", null));
    }

}
