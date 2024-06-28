package com.gr.freshermanagement.service.impl;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Role;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.AccountRoleRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import com.gr.freshermanagement.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;
    private AccountRoleRepository accountRoleRepository;
    private RoleRepository roleRepository;

    @Override
    public List<Role> findRolesByAccountUsername(String username) {
        return roleRepository.findRolesByAccountUsername(username);
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
