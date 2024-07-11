package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.center.CenterUpdateRequest;
import com.gr.freshermanagement.dto.request.center.NewCenterRequest;
import com.gr.freshermanagement.dto.response.CenterResponse;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.entity.Center;
import com.gr.freshermanagement.service.CenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager/center")
@RequiredArgsConstructor
public class CenterController {
    private final CenterService centerService;

    @GetMapping
    public ResponseEntity<?> getAllCenters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CenterResponse> centers = centerService.getAllCenters(page, size);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "List of centers", centers));
    }

    @PostMapping("add")
    public ResponseEntity<?> addCenter(@RequestBody @Valid NewCenterRequest center) {
        CenterResponse addedCenter = centerService.addCenter(center);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center added successfully", addedCenter));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCenter(
            @PathVariable Long id,
            @RequestBody CenterUpdateRequest updatedCenter) {
        CenterResponse center = centerService.updateCenter(id, updatedCenter);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center updated successfully", center));
    }

    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center deleted successfully", null));
    }

    @PostMapping("assign-fresher/{centerId}")
    public ResponseEntity<?> assignFresherToCenter(@PathVariable Long centerId, List<Long> fresherIds) {

        return ResponseEntity.ok(ResponseGeneral.of(HttpStatus.OK.value(), "Center deleted successfully", null));
    }
}
