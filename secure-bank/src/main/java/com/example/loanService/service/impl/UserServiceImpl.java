package com.example.loanService.service.impl;

import com.example.loanService.entity.User;
import com.example.loanService.repository.UserRepository;
import com.example.loanService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {

        return userRepository.save(user);
    }
}
