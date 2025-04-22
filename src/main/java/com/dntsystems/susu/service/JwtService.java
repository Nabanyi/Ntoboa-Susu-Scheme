package com.dntsystems.susu.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Component
public class JwtService {

    private static final String SECRET_KEY = "meandmymumgotochurcheverydaylightandsunanddarkandlightandfoodeysuega";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 15;  // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    // Convert the secret key to bytes for signing
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate Access Token (Short-lived)
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    // Generate Refresh Token (Longer-lived)
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }

    // Generate JWT Token
    public String generateToken(String username, long expiration) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract user name from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    // Validate JWT token
    public boolean validateAccessToken(String token, UserDetails userDetails) {
        return (extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validate Refresh Token (checks only expiration)
    public boolean validateRefreshToken(String token) {
        return !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
