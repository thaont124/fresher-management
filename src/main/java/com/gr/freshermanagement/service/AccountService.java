package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;

public interface AccountService {
    AuthenticationResponse signup(SignupRequest signupRequest);
    AuthenticationResponse login (LoginRequest loginRequest);

    void updateAccount(String email, String newPassword);


}
