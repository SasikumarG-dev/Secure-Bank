package com.example.registrationService.controller;


import com.example.registrationService.dto.GetUserDetailDto;
import com.example.registrationService.dto.UserDto;
import com.example.registrationService.dto.response.ApiResponse;
import com.example.registrationService.entity.User;
import com.example.registrationService.exception.UserNotFoundException;
import com.example.registrationService.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins="http://localhost:4200")
public class UserController {
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid UserDto user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.successHandler(userService.registerUser(user)));
    }

    @GetMapping("/user-details")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<User>> getUserDetails(@RequestBody GetUserDetailDto getUserDetailDto) throws UserNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successHandler(userService.getUserDetailsById(getUserDetailDto)
        ));    }

//     -->initialize Admin
//    @PostConstruct
//    public void imit(){
//        userService.initAdmin();
//    }

}