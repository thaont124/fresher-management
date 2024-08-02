package com.gr.freshermanagement.dto.request.account;

import lombok.Data;

@Data
public class SignoutRequest {
    private String accessToken;
    private String refreshToken;
}
