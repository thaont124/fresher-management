package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.NewEmployeeRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.dto.response.NewFresherResponse;
import com.gr.freshermanagement.entity.*;
import com.gr.freshermanagement.exception.account.ExistUsernameException;
import com.gr.freshermanagement.exception.account.UsernamePasswordIncorrectException;
import com.gr.freshermanagement.exception.department.DepartmentNotFoundException;
import com.gr.freshermanagement.exception.role.RoleNotFoundException;
import com.gr.freshermanagement.repository.*;
import com.gr.freshermanagement.security.CustomUserDetailsService;
import com.gr.freshermanagement.security.JWTUtility;
import com.gr.freshermanagement.service.EmployeeService;
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

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
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
    @Autowired
    private DepartmentRepository departmentRepository;

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
            Role role = roleRepository.findByName(roleName)
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

    @Override
    @Transactional
    public NewFresherResponse createNewEmployee(NewEmployeeRequest request) {
        if ("fresher".equalsIgnoreCase(request.getPosition())) {
            return createNewFresher(request);
        } else {
            throw new IllegalArgumentException("Unsupported position: " + request.getPosition());
        }
    }


    private String generateEmployeeCode(String position, Long id) {
        if(position.equalsIgnoreCase("FRESHER")){
            return "FRS" + id;
        }
        if(position.equalsIgnoreCase("DIRECTOR")){
            return "DIR" + id;
        }
        if(position.equalsIgnoreCase("ADMIN")){
            return "ADM" + id;
        }

        return "EMP" + id;
    }

    private String createUsername(String name, LocalDate dob, Long id) {
        // Convert name to ASCII (remove diacritics)
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String nameWithoutDiacritics = pattern.matcher(normalized).replaceAll("");

        // Split name and get the last part
        String[] nameParts = nameWithoutDiacritics.split(" ");
        String lastName = nameParts[nameParts.length - 1].toLowerCase();

        // Get year from dob
        int year = dob.getYear();

        // Combine to form username
        return lastName + year + id;
    }

    @Transactional
    public NewFresherResponse createNewFresher(NewEmployeeRequest request) {
        // Find department
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(DepartmentNotFoundException::new);

        Fresher fresher = new Fresher();
        fresher.setName(request.getName());
        fresher.setDob(request.getDob());
        fresher.setAddress(request.getAddress());
        fresher.setPhone(request.getPhone());
        fresher.setGender(Gender.valueOf(request.getGender()));
        fresher.setEmail(request.getEmail());
        fresher.setDepartment(department);
        fresher.setStatus(EmployeeStatus.ACTIVE);
        fresher.setFresherStatus(FresherStatus.EDUCATING); // Set specific status for Fresher

        // Save Fresher entity to generate ID
        fresher = employeeRepository.save(fresher);

        fresher.setEmployeeCode(generateEmployeeCode(request.getPosition(), fresher.getId()));

        // Generate username and password for the account
        String username = createUsername(fresher.getName(), fresher.getDob(), fresher.getId());
        String encodedPassword = passwordEncoder.encode(username); // Encode password

        // Create Account entity
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(encodedPassword);
        account.setEmployee(fresher);

        // Save Account entity
        accountRepository.save(account);

        // Set the account in fresher
        fresher.setAccount(account);

        return new NewFresherResponse(fresher, username, username); // Assuming NewFresherResponse is defined appropriately
    }




}
