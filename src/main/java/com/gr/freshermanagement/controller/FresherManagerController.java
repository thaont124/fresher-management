package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.employee.EmployeeUpdateRequest;
import com.gr.freshermanagement.dto.request.employee.NewEmployeeRequest;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.service.EmployeeService;
import com.gr.freshermanagement.service.FresherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/manager/fresher")

@RequiredArgsConstructor
public class FresherManagerController {
    private final EmployeeService employeeService;
    private final FresherService fresherService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllFreshers(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "15") int size) {
        return new ResponseEntity<>(
                ResponseGeneral.of(200, "Get list success", fresherService.getFreshersPaginated(page, size).getContent()),
                HttpStatus.CREATED);

    }

    @PostMapping("add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createNewFresher(
            @RequestBody @Valid NewEmployeeRequest newFresherRequest){
        try {
            NewFresherResponse createdFresher = employeeService.createNewEmployee(newFresherRequest);
            return new ResponseEntity<>(
                    ResponseGeneral.of(201, "Add success", createdFresher), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    ResponseGeneral.of(400, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add-list")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> createNewFresher(@RequestParam("file") MultipartFile file){
        try {
            List<EmployeeResponse> createdFresher = employeeService.createListEmployee(file);
            return new ResponseEntity<>(
                    ResponseGeneral.of(201, "Add success", createdFresher), HttpStatus.CREATED);
        } catch (Exception e) {
            String message = "The Excel file is not upload: " + file.getOriginalFilename() + "!";
            return new ResponseEntity<>(
                    ResponseGeneral.of(417, message, null), HttpStatus.EXPECTATION_FAILED
                    );
        }
    }

    @GetMapping("{facilityId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getFreshersByFacilityAndDateRange(
            @PathVariable Long facilityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<EmployeeResponse> freshers = fresherService.getFreshersByFacilityAndDateRange(facilityId, startDate, endDate, page, size);
        return new ResponseEntity<>(
                ResponseGeneral.of(201, "Add success", freshers), HttpStatus.CREATED);
    }

    @DeleteMapping("/del/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<?> deleteFresher(@PathVariable Long id) {
        fresherService.deactivateFresher(id);
        return new ResponseEntity<>(ResponseGeneral.of(200, "Fresher deactivated successfully", null), HttpStatus.OK);
    }

    @PatchMapping("/patch/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> updateFresher(@PathVariable Long id, @RequestBody EmployeeUpdateRequest updatedFresher) {
        EmployeeResponse response = fresherService.updateFresher(id, updatedFresher);
        return new ResponseEntity<>(ResponseGeneral.of(200, "Update success", response), HttpStatus.OK);
    }
}
