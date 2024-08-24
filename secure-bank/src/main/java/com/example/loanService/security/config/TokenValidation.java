package com.example.loanService.security.config;

import java.security.Key;
import java.util.Date;

import com.example.loanService.util.ApplicationConstant;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenValidation {

    public String extractUserName(String token) {
        return extractAllClaims(token).get("userId").toString();
    }

    public Date getExpirationDate(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role").toString();
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    Key getSignkey() {
        byte[] keys = Decoders.BASE64.decode(ApplicationConstant.JWT_SECRET_KEY);
        return Keys.hmacShaKeyFor(keys);
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean tokenValidation(String token) {
        return !isTokenExpired(token);
    }
}
