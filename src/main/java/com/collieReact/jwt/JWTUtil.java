// JWTUtil : 0.11.5
package com.collieReact.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.key}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);

        if (byteSecretKey.length < 32) {
            throw new IllegalArgumentException("The decoded key is not long enough. It must be at least 32 bytes.");
        }
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("email", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public String createJwt(String email, Long expirationTimeInSeconds) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInSeconds * 1000);

        Claims claims = Jwts.claims();
        claims.put("email", email);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}