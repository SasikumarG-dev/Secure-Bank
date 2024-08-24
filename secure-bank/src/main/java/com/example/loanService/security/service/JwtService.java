package com.example.loanService.security.service;

import com.example.loanService.entity.User;
import com.example.loanService.repository.UserRepository;
import com.example.loanService.security.dto.AuthResponse;
import com.example.loanService.util.ApplicationConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {



    @Autowired
    private UserRepository userRepository;

    public AuthResponse generateToken(String emailId) {
        User user = userRepository.findByEmailId(emailId);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid User: " + emailId);
        }

        if (isValidRole(user.getRole())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("role", user.getRole());

            String jwtToken = createToken(claims, emailId);
            return new AuthResponse(jwtToken);
        } else {
            throw new UsernameNotFoundException("Invalid User");
        }
    }
//    public AuthResponse generateToken(String emailId){
//        Map<String, Object> claims = new HashMap<>();
//        User user = userRepository.findByEmailId(emailId);
//        if(user != null){
//            // validate the roles of the user
//
//            if(user.getRole().equals(ApplicationConstant.USER_ROLE)
//                    || user.getRole().equals(ApplicationConstant.ADMIN_ROLE)){
//                claims.put("userId", user.getUserId());
//                claims.put("role", user.getRole());
//                String jwtToken = createToken(claims, emailId);
//                return new AuthResponse(jwtToken);
//            }
//            else {
//                throw new UsernameNotFoundException("Invalid User role");
//            }
//        }
//        else {
//            throw new UsernameNotFoundException("Invalid User: "+emailId);
//        }
//    }

    private String createToken(Map<String, Object> claims, String emailId){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(emailId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 *30)))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey(){
        byte[] key = Decoders.BASE64.decode(ApplicationConstant.JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    private boolean isValidRole(String role) {
        return ApplicationConstant.USER_ROLE.equals(role) || ApplicationConstant.ADMIN_ROLE.equals(role);
    }
}
