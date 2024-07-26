package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.account.TokenRefreshRequest;
import com.gr.freshermanagement.dto.response.account.AuthenticationResponse;
import com.gr.freshermanagement.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken verifyExpiration(RefreshToken token);
    void deleteByUserId(String username);

    AuthenticationResponse refreshToken(TokenRefreshRequest request);
}
