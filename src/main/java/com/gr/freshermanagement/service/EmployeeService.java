package com.gr.freshermanagement.service;


import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;

public interface EmployeeService {
    AuthenticationResponse signup(SignupRequest signupRequest);
    AuthenticationResponse login (LoginRequest loginRequest);

    NewFresherResponse createNewEmployee(NewEmployeeRequest newFresherRequest);

//    Response sendOtp();
//    Response validateOtp();
//    Response resetPassword();
//    Response changePassword();
}


