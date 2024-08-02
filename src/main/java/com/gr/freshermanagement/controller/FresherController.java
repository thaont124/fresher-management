package com.gr.freshermanagement.controller;


import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.employee.ListAssignFresherRequest;
import com.gr.freshermanagement.dto.response.employee.EmployeeResponse;
import com.gr.freshermanagement.service.FresherService;
import lombok.RequiredArgsConstructor;
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
public class FresherController {
    private final FresherService fresherService;

    @PostMapping("add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> uploadListFresher(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(
                ResponseGeneral.of(
                        HttpStatus.CREATED.value(),
                        "Upload list fresher success",
                        fresherService.addListFresher(file)),
                HttpStatus.CREATED);

    }

    @GetMapping("list")
    public ResponseEntity<?> getFreshers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<EmployeeResponse> fresherPage = fresherService.getFreshers(page, size);
        return new ResponseEntity<>(
                ResponseGeneral.of(HttpStatus.OK.value(), "Get list success", fresherPage),
                HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> searchFreshers(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam Long centerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam String languageName) {

        List<EmployeeResponse> freshers = fresherService.filterFresher(page, size, centerId, startDate, endDate, languageName);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Get list success", freshers));
    }

    @PostMapping("assign-fresher")
    public ResponseEntity<?> assignFresherToCenter(@RequestBody ListAssignFresherRequest request) {
        fresherService.assignFresherToCenter(request);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Assign fresher successfully", null));
    }

}
