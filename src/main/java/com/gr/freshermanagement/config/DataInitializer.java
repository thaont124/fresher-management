package com.gr.freshermanagement.config;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.AccountRole;
import com.gr.freshermanagement.entity.Role;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.AccountRoleRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final AccountRepository accountRepository;

    private final AccountRoleRepository accountRoleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Initialize roles
        Role adminRole = createRoleIfNotExists("ROLE_ADMIN");
        Role fresherRole = createRoleIfNotExists("ROLE_FRESHER");
        Role directorRole = createRoleIfNotExists("ROLE_DIRECTOR");

        // Create admin account if not exist
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(passwordEncoder.encode("adminpassword"));
            adminAccount.setCreateAt(LocalDateTime.now());
            accountRepository.save(adminAccount);

            AccountRole adminAccountRole = new AccountRole(adminAccount, adminRole);
            accountRoleRepository.save(adminAccountRole);
        }
    }

    private Role createRoleIfNotExists(String roleName) {
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isEmpty()) {
            Role role = new Role(roleName, Role.RoleStatus.ACTIVE);
            return roleRepository.save(role);
        } else {
            return roleOptional.get();
        }
    }
}
