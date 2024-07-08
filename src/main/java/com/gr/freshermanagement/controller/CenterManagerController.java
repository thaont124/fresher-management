package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.service.CenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manager/center")
public class CenterManagerController {
    private final CenterService centerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllCenters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(ResponseGeneral.of(200, "Get list success", centerService.getAllCenters(page, size).getContent()),
                HttpStatus.OK);

    }

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> addCenter(@RequestBody @Valid NewCenterRequest centerRequest) {
        return new ResponseEntity<>(ResponseGeneral.of(201, "Add success", centerService.addCenter(centerRequest)),
                HttpStatus.CREATED);

    }

    @PatchMapping("/patch/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DIRECTOR')")
    public ResponseEntity<?> updateCenter(@PathVariable Long id, @RequestBody CenterUpdateRequest centerRequest) {
        return new ResponseEntity<>(ResponseGeneral.of(200, "Update success", centerService.updateCenter(id, centerRequest)),
                HttpStatus.OK);

    }

    @DeleteMapping("/del/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return new ResponseEntity<>(ResponseGeneral.of(200, "Delete success", null), HttpStatus.OK);

    }
}
