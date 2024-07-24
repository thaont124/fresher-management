package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.role.RoleNotFoundException;
import com.gr.freshermanagement.repository.*;
import com.gr.freshermanagement.security.CustomUserDetailsService;
import com.gr.freshermanagement.security.JWTUtility;
import com.gr.freshermanagement.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Value("${jwt.refreshExp}")
    private Long refreshTokenDurationMs;

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtility jwtUtility;
    private final AccountRoleRepository accountRoleRepository;
    private final CustomUserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        // Check if username already exists
        if (accountRepository.findByUsername(signupRequest.getUsername()).isPresent()) {
            throw new ExistUsernameException();
        }

        // Create new account
        Employee employee = new Employee();
        employee.setAddress(signupRequest.getAddress());
        employee.setPhone(signupRequest.getPhone());
        employee.setEmail(signupRequest.getEmail());
        Employee savedEmployee = employeeRepository.save(employee);

        if (accountRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ExistUsernameException();
        }
        Account account = new Account();
        account.setUsername(signupRequest.getUsername());
        account.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        account.setEmployee(savedEmployee);
        Account savedAccount = accountRepository.save(account);

        for (String roleName : signupRequest.getRoles()) {
            Role role = roleRepository.findByName(("ROLE_" + roleName).toUpperCase())
                    .orElseThrow(() -> new RoleNotFoundException("Role not found: " + roleName));

            AccountRole accountRole = new AccountRole();
            accountRole.setAccount(savedAccount);
            accountRole.setRole(role);
            accountRoleRepository.save(accountRole);
        }

        // Generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(signupRequest.getUsername());
        String token = jwtUtility.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(token).build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            Account account = accountRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
                throw new UsernamePasswordIncorrectException();
            }

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String accessToken = jwtUtility.generateToken(userDetails);

            // Create refreshToken
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setAccount(account);
            refreshToken.setExpDate(Instant.now().plusMillis(refreshTokenDurationMs));
            refreshToken.setRefreshToken(UUID.randomUUID().toString());
            refreshToken = refreshTokenRepository.save(refreshToken);

            return AuthenticationResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        } catch (Exception e) {
            throw new UsernamePasswordIncorrectException();
        }
    }
}
