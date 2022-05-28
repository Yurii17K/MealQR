package com.example.mealqr.security;


import com.example.mealqr.pojos.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWT {

    public static String generateToken (User user) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, user.getEmail());
    }

    public static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(SignatureAlgorithm.HS512, System.getenv("JWT_KEY"))
                .compact();
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(System.getenv("JWT_KEY"))
                .parseClaimsJws(token)
                .getBody();
    }

    // validate if token is signed for this username and is not expired
    public static Boolean validateToken (String token, MyUserDetails myUserDetails) {
        return (myUserDetails.getUsername().equals(extractAllClaims(token).getSubject())
                && !extractAllClaims(token).getExpiration().before(new Date(System.currentTimeMillis())));
    }
}