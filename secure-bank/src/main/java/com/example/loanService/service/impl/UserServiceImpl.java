package com.example.loanService.service.impl;

import com.example.loanService.entity.Role;
import com.example.loanService.entity.User;
import com.example.loanService.repository.UserRepository;
import com.example.loanService.service.UserService;
import com.example.loanService.util.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user) {
        Role role = new Role();
        role.setName(RoleEnum.ROLE_USER.toString());
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
