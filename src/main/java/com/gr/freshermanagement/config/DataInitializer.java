package com.gr.freshermanagement.config;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.AccountRole;
import com.gr.freshermanagement.entity.Role;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.AccountRoleRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountRoleRepository accountRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize role
        Role adminRole = new Role("ROLE_ADMIN");
        Role fresherRole = new Role("ROLE_FRESHER");
        Role directorRole = new Role("ROLE_DIRECTOR");

        roleRepository.save(adminRole);
        roleRepository.save(fresherRole);
        roleRepository.save(directorRole);

        // Create admin account if not exist
        if (accountRepository.findByUsername("admin").isEmpty()) {
            Account adminAccount = new Account();
            adminAccount.setUsername("admin");
            adminAccount.setPassword(passwordEncoder.encode("adminpassword")); // Mã hóa mật khẩu
            accountRepository.save(adminAccount);

            AccountRole adminAccountRole = new AccountRole(adminAccount, adminRole);
            accountRoleRepository.save(adminAccountRole);
        }
    }
}
