package com.gr.freshermanagement.security;

import com.gr.freshermanagement.entity.Account;
import com.gr.freshermanagement.entity.Role;
import com.gr.freshermanagement.repository.AccountRepository;
import com.gr.freshermanagement.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account user = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        List<Role> userRoles = roleRepository.findRolesByAccountUsername(user.getUsername());
        // Tạo danh sách quyền từ role của Employee
        List<GrantedAuthority> authorities = userRoles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }


}