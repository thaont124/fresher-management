package com.gr.freshermanagement.service;

import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Role;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AuthenticationResponse signup(SignupRequest signupRequest);
    AuthenticationResponse login (LoginRequest loginRequest);


    //    Response sendOtp();
//    Response validateOtp();
//    Response resetPassword();
//    Response changePassword();
}
