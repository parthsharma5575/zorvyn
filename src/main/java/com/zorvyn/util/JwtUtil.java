package com.zorvyn.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .add("role", userDetails.getAuthorities().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .and()
                .signWith(getSignKey()).compact();
    }
    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    public Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String tokenMail=extractEmail(token);
            Date date=extractExpiration(token);
            return tokenMail.equals(userDetails.getUsername()) && date.after(new Date());
        }catch (Exception e){
            return false;
        }

    }
    public SecretKey getSignKey() {
        //convert key to crypt key
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
