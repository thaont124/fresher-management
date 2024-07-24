package com.gr.freshermanagement.dto.request.account;

import com.gr.freshermanagement.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String address;

    private String phone;

    private String email;

    private String username;

    private String password;

    private List<String> roles;
}
