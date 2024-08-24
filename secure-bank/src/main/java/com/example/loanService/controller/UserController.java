package com.example.loanService.controller;

import com.example.loanService.dto.ApiResponse;
import com.example.loanService.entity.User;
import com.example.loanService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> registerUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Success", userService.registerUser(user),null));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<String>> getUser(){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Success", "get",null));
    }
}
