package com.example.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    private static String secretKey = "java11SpringBootJWTTokenIssueMethod";

    /**
     * JWT Token 생성하기
     * jjwt 라이브러리 활용해 구현하기
     * @param claims
     * @param expireAt
     * @return
     */
    public String create(
            Map<String,Object> claims,
            LocalDateTime expireAt
    ) {
        // custom key를 통해 생성
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // LocalDateTime -> Date 폼으로 바꿔주는 코드
        var _expireAt = Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setExpiration(_expireAt)
                .compact();
    }

    public void validation(String token) {
        // secret key 가져오기
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());

        // parser 생성하기
        var parser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();

        try {
            var result = parser.parseClaimsJws(token);
            result.getBody().entrySet().forEach(value -> {
                log.info("key = {}, value = {}", value.getKey(), value.getValue());
            });
        } catch (Exception e) {
            if (e instanceof SignatureException) {
                throw new RuntimeException("JWT Token Not Valid Exception");
            }
            else if (e instanceof ExpiredJwtException) {
                throw new RuntimeException("JWT Token Expired Exception");
            }
            else {
                throw new RuntimeException("JWT Token Validation Exception");
            }
        }
    }
}
