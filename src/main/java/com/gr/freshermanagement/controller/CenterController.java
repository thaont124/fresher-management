package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.service.CenterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/centers")
@RequiredArgsConstructor
public class CenterController {
    private final CenterService centerService;

    @GetMapping
    public ResponseEntity<?> getAllCenters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Center> centers = centerService.getAllCenters(page, size);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "List of centers", centers));
    }

    @PostMapping
    public ResponseEntity<?> addCenter(@RequestBody Center center) {
        Center addedCenter = centerService.addCenter(center);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center added successfully", addedCenter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCenter(
            @PathVariable Long id,
            @RequestBody Center updatedCenter) {
        Center center = centerService.updateCenter(id, updatedCenter);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center updated successfully", center));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center deleted successfully", null));
    }
}
