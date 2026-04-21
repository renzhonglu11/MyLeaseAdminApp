package com.rz.lease.web.admin.security;

import com.rz.lease.common.exception.LeaseException;
import com.rz.lease.common.result.ResultCodeEnum;
import com.rz.lease.model.entity.SystemUser;
import com.rz.lease.model.enums.BaseStatus;
import com.rz.lease.web.admin.repository.SystemUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load the user from the database in spring security format
        // 1. Find the user in databse
        // 2. If not found, throw UsernameNotFoundException
        // 3. If found, return a UserDetails object with username, password, and
        // authorities (roles/permissions)
        SystemUser systemUser = systemUserRepository.findActiveByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        String encodedPassword = systemUser.getPassword();
        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        boolean disabled = systemUser.getStatus() == BaseStatus.DISABLE;

        return User.builder()
                .username(systemUser.getUsername())
                .password(encodedPassword)
                .disabled(disabled)
                .authorities("ROLE_ADMIN")
                .build();
    }
}
