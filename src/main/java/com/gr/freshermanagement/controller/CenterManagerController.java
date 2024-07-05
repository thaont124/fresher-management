package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.request.CenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/manager/center")
public class CenterManagerController {
    private final CenterService centerService;

    @GetMapping
    public ResponseEntity<List<CenterResponse>> getAllCenters() {
        return ResponseEntity.ok(centerService.getAllCenters());
    }

    @PostMapping
    public ResponseEntity<CenterResponse> addCenter(@RequestBody CenterRequest centerRequest) {
        return new ResponseEntity<>(centerService.addCenter(centerRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CenterResponse> updateCenter(@PathVariable Long id, @RequestBody CenterRequest centerRequest) {
        return ResponseEntity.ok(centerService.updateCenter(id, centerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return ResponseEntity.noContent().build();
    }
}
