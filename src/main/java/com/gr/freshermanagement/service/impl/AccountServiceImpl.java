package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.AccountRole;
import com.gr.freshermanagement.entity.Employee;
import com.gr.freshermanagement.entity.Role;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.role.RoleNotFoundException;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.AccountRoleRepository;
import com.gr.freshermanagement.repository.EmployeeRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import com.gr.freshermanagement.security.CustomUserDetailsService;
import com.gr.freshermanagement.security.JWTUtility;
import com.gr.freshermanagement.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AuthenticationManager authenticationManager;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public List<Role> findRolesByAccountUsername(String username) {
        return roleRepository.findRolesByAccountUsername(username);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }


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

        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            Account user = accountRepository.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new UsernamePasswordIncorrectException();
            }

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            String token = jwtUtility.generateToken(userDetails);
            return AuthenticationResponse.builder().token(token).build();
        } catch (Exception e) {
            throw new UsernamePasswordIncorrectException();
        }


    }
}
