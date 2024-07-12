package com.gr.freshermanagement.dto.request.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignFresherRequest {
    private Long centerId;

    private List<Long> fresherId;

    private LocalDate startDate;
}
