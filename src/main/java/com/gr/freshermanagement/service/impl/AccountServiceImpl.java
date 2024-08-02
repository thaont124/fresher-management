package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.account.LoginRequest;
import com.gr.freshermanagement.dto.request.account.SignupRequest;
import com.gr.freshermanagement.dto.response.account.AuthenticationResponse;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Value("${jwt.refreshExp}")
    private Long refreshTokenDurationMs;

    @Value("${jwt.expiration.ms}")
    private Long accessTokenDurationMs;

    private final CacheManager cacheManager;
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
                .accessToken(token).build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            Cache cache = cacheManager.getCache("accessTokens");

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

            if (cache != null) {
                cache.put(accessToken, refreshToken);
            }
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getRefreshToken())
                    .build();
        } catch (Exception e) {
            throw new UsernamePasswordIncorrectException();
        }
    }

    @Override
    public void changePassword(String username, String email, String newPassword) {
        Account account = accountRepository.findByUsername(email).orElseThrow();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        List<RefreshToken> refreshTokens = refreshTokenRepository.findByAccount(account);
        // delete accessToken in cache
        Cache cache = cacheManager.getCache("accessTokens");
        if (cache != null) {
            for (RefreshToken refreshToken : refreshTokens) {
                cache.evictIfPresent(refreshToken);
            }
        }
        refreshTokenRepository.deleteAll(refreshTokens);
    }

    @Override
    public void clearAccessToken(String key) {
        Cache cache = cacheManager.getCache("accessTokens");
        if (cache != null) {
            cache.evict(key);
        }
    }

    @Override
    public void scheduleCacheEviction(String key) {
        new Thread(() -> {
            try {
                TimeUnit.MINUTES.sleep(accessTokenDurationMs);
                clearAccessToken(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }


}
