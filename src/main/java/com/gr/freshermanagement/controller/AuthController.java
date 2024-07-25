package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.request.account.TokenRefreshRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.exception.account.TokenRefreshException;
import com.gr.freshermanagement.service.AccountService;
import com.gr.freshermanagement.service.EmailService;
import com.gr.freshermanagement.service.OtpService;
import com.gr.freshermanagement.service.RefreshTokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;
    private final EmailService emailService;

    @PostMapping("signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        return new ResponseEntity<>(ResponseGeneral.of(201, "Signup success",
                accountService.signup(signupRequest)), HttpStatus.CREATED);
    }

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

        try {
            emailService.sendOtpMessage(email, "Your OTP Code", "Your OTP code is: " + otp);
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(ResponseGeneral.of(500, "Error while sending OTP email.", null));
        }

        return ResponseEntity.ok(ResponseGeneral.of(200,"OTP has been sent to your email.", null));
    }


    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String username,
                                            @RequestParam int otp, @RequestParam String newPassword) {
        int cachedOtp = otpService.getOtp(username);

        if (cachedOtp == otp) {
            otpService.clearOTP(username);
            accountService.updateAccount(username, newPassword);

            return ResponseEntity.ok(ResponseGeneral.of(200, "Password has been successfully changed.", null));
        } else {
            return ResponseEntity.status(400).body(ResponseGeneral.of(400,"Invalid OTP.", null));
        }
    }
}
