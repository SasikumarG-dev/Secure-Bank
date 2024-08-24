package com.example.loanService.security.controller;

import com.example.loanService.dto.ApiResponse;
import com.example.loanService.security.dto.AuthRequest;
import com.example.loanService.security.dto.AuthResponse;
import com.example.loanService.security.service.JwtService;
import com.example.loanService.util.ApplicationConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bank")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest){
        log.info("inside login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmailId()
                        , authRequest.getPassword()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>(ApplicationConstant.SUCCESS,
                        jwtService.generateToken(authRequest.getEmailId()), null));
    }
}
