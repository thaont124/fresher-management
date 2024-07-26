package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.request.account.TokenRefreshRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AccountService accountService;
    private final EmployeeService employeeService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        return new ResponseEntity<>(ResponseGeneral.of(201, "Signup success",
                accountService.signup(signupRequest)), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login",
            description = "This endpoint creates a new token with the auth user details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Example with admin account",
                                            value = "{\"username\": \"admin\", \"password\": \"admin\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Example with fresher account",
                                            value = "{\"username\": \"ninh@gmail.com\", \"password\": \"ninh27022002\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Example with fail login",
                                            value = "{\"username\": \"ninh@gmail.com\", \"password\": \"nigtfd7022002\"}"
                                    )
                            }
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "400", description = "Username or Password is incorrect")
    })
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return new ResponseEntity<>(ResponseGeneral.of(200, "Login success",
                accountService.login(request)), HttpStatus.OK);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        AuthenticationResponse response = refreshTokenService.refreshToken(request);
        return ResponseEntity.ok(ResponseGeneral.of(200, "Refresh success", response));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        refreshTokenService.deleteByUserId(username);
        return ResponseEntity.ok(ResponseGeneral.of(200, "Logout success", null));
    }

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String username, @RequestParam String email) {
        int otp = otpService.generateOTP(username);
        boolean validEmail = employeeService.checkEmailWithUsername(username, email);

        try {
            if (validEmail){
                emailService.sendOtpMessage(email, "Your OTP Code", "Your OTP code is: " + otp);
            }
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(ResponseGeneral.of(500, "Error while sending OTP email.", null));
        }

        return ResponseEntity.ok(ResponseGeneral.of(200,"OTP has been sent to your email.", null));
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String username,
                                            @RequestParam String email,
                                            @RequestParam int otp, @RequestParam String newPassword) {

        boolean validEmail = employeeService.checkEmailWithUsername(username, email);
        int cachedOtp = otpService.getOtp(username);

        if (cachedOtp == otp && validEmail) {
            accountService.changePassword(username, email, newPassword);
            otpService.clearOTP(username);

            return ResponseEntity.ok(ResponseGeneral.of(200, "Password has been successfully changed.", null));
        } else {
            return ResponseEntity.status(400).body(ResponseGeneral.of(400,"Invalid OTP.", null));
        }
    }
}
