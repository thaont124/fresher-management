package com.gr.freshermanagement.dto.request.center;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterUpdateRequest {
    private String name;
    private String address;
    private String status;

}