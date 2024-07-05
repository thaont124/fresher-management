package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.EmployeeRequest;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("create")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<?> deactivateFresher(@PathVariable Long id) {
        fresherService.deactivateFresher(id);
        return new ResponseEntity<>(ResponseGeneral.of(200, "Fresher deactivated successfully", null), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> updateFresher(@PathVariable Long id, @RequestBody EmployeeRequest updatedFresher) {
        EmployeeResponse response = fresherService.updateFresher(id, updatedFresher);
        return new ResponseEntity<>(ResponseGeneral.of(200, "Update success", response), HttpStatus.OK);
    }
}
