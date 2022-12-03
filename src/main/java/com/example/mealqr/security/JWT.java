package com.example.mealqr.security;


import com.example.mealqr.domain.User;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.exceptions.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.vavr.API;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JWT {

    public static String generateToken (User user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getEmail());
    }

    public static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()//
                .setClaims(claims)//
                .setSubject(subject)//
                .setIssuedAt(new Date(System.currentTimeMillis()))//
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))//
                .signWith(SignatureAlgorithm.HS512, System.getenv("JWT_KEY"))//
                .compact();
    }

    public static Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(System.getenv("JWT_KEY"))//
                .parseClaimsJws(token)//
                .getBody();
    }

    public static CustomPrincipal extractCustomPrincipal(String jwtToken, Function<String, CustomPrincipal> loadByUsername) {
        return tokenFormValid(jwtToken)//
                .flatMap(JWT::tokenNotExpired)//
                .flatMap(token -> tokenSignatureValid(token, loadByUsername))//
                .toEither()//
                .getOrElseThrow(ApiException::new);
    }

    private static Validation<ApiError, String> tokenFormValid(String jwtToken) {
        return API.Some(jwtToken)//
                .filter(token -> !token.isBlank() && token.matches(".*\\..*\\..*"))//
                .toValidation(ApiError.buildError("Unauthorized", HttpStatus.FORBIDDEN));
    }

    private static Validation<ApiError, Claims> tokenNotExpired(String jwtToken) {
        return Try.of(() -> extractAllClaims(jwtToken))
                .toValidation(ApiError.buildError("Unauthorized", HttpStatus.FORBIDDEN));
    }

    private static Validation<ApiError, CustomPrincipal> tokenSignatureValid(Claims claims, Function<String, CustomPrincipal> loadByUsername) {
        String subject = claims.getSubject();
        CustomPrincipal customPrincipal = loadByUsername.apply(subject);
        if (!customPrincipal.getUsername().equals(subject)) {
            return Validation.invalid(ApiError.buildError("Unauthorized", HttpStatus.FORBIDDEN));
        }
        return Validation.valid(customPrincipal);
    }
}