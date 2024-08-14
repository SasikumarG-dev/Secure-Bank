package com.example.registrationService.repository;

import com.example.registrationService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailId(String emailId);
    User findByPanNumber(String panNumber);
    User findByPhoneNumber(String panNumber);
}
