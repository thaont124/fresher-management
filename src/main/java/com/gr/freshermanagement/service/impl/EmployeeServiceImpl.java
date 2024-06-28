package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.dto.request.LoginRequest;
import com.gr.freshermanagement.dto.request.SignupRequest;
import com.gr.freshermanagement.dto.response.AuthenticationResponse;
import com.gr.freshermanagement.dto.response.EmployeePrinciple;
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
import com.gr.freshermanagement.security.FixedSaltBCrypt;
import com.gr.freshermanagement.security.JwtService;
import com.gr.freshermanagement.service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    private JwtService jwtService;

    @Value("${FIXED_SALT}")
    private String salt;

    private final AuthenticationManager authenticationManager;
    @Override
    @Transactional
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        Employee employee = new Employee();
        employee.setAddress(signupRequest.getAddress());
        employee.setPhone(signupRequest.getPhone());
        employee.setEmail(signupRequest.getEmail());
        Employee savedEmployee = employeeRepository.save(employee);

        if (accountRepository.existsByUsername(signupRequest.getUsername())){
            throw new ExistUsernameException();
        }
        Account account = new Account();
        account.setUsername(signupRequest.getUsername());
        account.setPassword(FixedSaltBCrypt.hashWithFixedSalt(signupRequest.getPassword(), salt));
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

        EmployeePrinciple employeePrinciple = EmployeePrinciple.build(savedAccount, roleRepository);
        String jwtToken = jwtService.generateToken(employeePrinciple);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest loginRequest) {
        try {
            Account account = accountRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(UsernamePasswordIncorrectException::new);

            String loginPassword = FixedSaltBCrypt.hashWithFixedSalt(loginRequest.getPassword(), salt);
            System.out.println("so sanh: " + loginPassword + " " + account.getPassword());
            if (!loginPassword.equals(account.getPassword())) {
                throw new UsernamePasswordIncorrectException();
            }
            EmployeePrinciple employeePrinciple = EmployeePrinciple.build(account, roleRepository);
            String jwtToken = jwtService.generateToken(employeePrinciple);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        catch (Exception e){
            throw new UsernamePasswordIncorrectException();
        }

    }
}
