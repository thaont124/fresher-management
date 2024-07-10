package com.gr.freshermanagement.controller;


import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.entity.Fresher;
import com.gr.freshermanagement.service.FresherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "api/v1/manager/fresher")
@RequiredArgsConstructor
public class FresherManagerController {
    private final FresherService fresherService;

    @GetMapping("add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> uploadListFresher(@RequestParam MultipartFile file) {
        return new ResponseEntity<>(
                ResponseGeneral.of(
                        HttpStatus.CREATED.value(),
                        "Upload list fresher success",
                        fresherService.addListFresher(file)),
                HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> getFreshers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Fresher> fresherPage = fresherService.getFreshers(page, size);
        return new ResponseEntity<>(
                ResponseGeneral.of(HttpStatus.OK.value(), "Get list success", fresherPage),
                HttpStatus.OK);
    }

}
