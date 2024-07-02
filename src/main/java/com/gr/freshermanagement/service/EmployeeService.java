package com.gr.freshermanagement.service;


import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.NewFresherRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.dto.response.EmployeeResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;

public interface EmployeeService {
    AuthenticationResponse createEmployee(SignupRequest signupRequest);
    AuthenticationResponse login (LoginRequest loginRequest);

    NewFresherResponse createNewEmployee(NewFresherRequest newFresherRequest);

//    Response sendOtp();
//    Response validateOtp();
//    Response resetPassword();
//    Response changePassword();
}


