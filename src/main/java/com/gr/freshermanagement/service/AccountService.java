package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.response.account.AuthenticationResponse;

public interface AccountService {
    AuthenticationResponse signup(SignupRequest signupRequest);
    AuthenticationResponse login (LoginRequest loginRequest);

    void changePassword(String username, String email, String newPassword);


    void clearAccessToken(String key);

    void scheduleCacheEviction(String key);
}
