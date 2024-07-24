package com.gr.freshermanagement.controller;

import com.gr.freshermanagement.dto.ResponseGeneral;
import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.request.account.TokenRefreshRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.exception.account.TokenRefreshException;
import com.gr.freshermanagement.service.AccountService;
import com.gr.freshermanagement.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AccountService accountService;
    private final RefreshTokenService refreshTokenService;

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
}
