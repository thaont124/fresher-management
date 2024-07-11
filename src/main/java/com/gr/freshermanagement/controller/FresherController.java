package com.gr.freshermanagement.controller;


import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.service.FresherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}
