package com.gr.freshermanagement.dto.request.employee;

import lombok.Data;

import java.util.List;

@Data
public class ListAssignFresherRequest {
    List<AssignFresherRequest> listAssign;
}
