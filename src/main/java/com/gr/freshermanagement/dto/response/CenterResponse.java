package com.gr.freshermanagement.dto.response;

import com.gr.freshermanagement.entity.Center;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterResponse {
    private Long id;
    private String name;
    private String address;
    private String status;

    public CenterResponse(Center center) {
        this.id = center.getId();
        this.name = center.getName();
        this.address = center.getAddress();
        this.status = String.valueOf(center.getStatus());
    }
}
