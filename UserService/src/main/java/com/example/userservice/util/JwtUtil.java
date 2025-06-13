package com.example.userservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 🔐 JwtUtil
 *
 * - JWT(Json Web Token)를 생성하고 검증하는 유틸리티 클래스입니다.
 * - 로그인 성공 시 사용자 정보를 기반으로 토큰을 발급하며,
 *   이후 요청에서 해당 토큰의 유효성 및 사용자 정보를 추출합니다.
 */
@Component
public class JwtUtil {

    // ✅ 토큰 서명에 사용할 비밀키 (HS256 알고리즘은 최소 256bit 필요)
    private static final String SECRET = "my-very-secret-key-for-jwt-example-2025";

    // ✅ 토큰 유효 시간: 1시간 (1000ms * 60초 * 60분)
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // ✅ HMAC SHA 키 객체 생성
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * ✅ 토큰 생성
     * - subject(email)와 사용자 ID를 Claim에 담아 JWT를 생성합니다.
     * - 발급 시간(iat)과 만료 시간(exp)을 포함합니다.
     *
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @return 생성된 JWT 문자열
     */
    public String createToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email) // 이메일을 subject로 설정
                .claim("userId", userId) // 사용자 ID도 함께 Claim에 포함
                .setIssuedAt(new Date()) // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // HMAC-SHA256으로 서명
                .compact();
    }

    /**
     * ✅ 토큰에서 이메일(subject) 추출
     *
     * @param token JWT 문자열
     * @return 토큰에 포함된 이메일
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) // 서명 검증용 키 설정
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // subject 필드에서 이메일 반환
    }

    /**
     * ✅ 토큰 유효성 검사
     * - 서명이 유효하고, 만료되지 않았는지 확인합니다.
     *
     * @param token JWT 문자열
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 예외 없이 파싱되면 유효
            return true;
        } catch (JwtException e) {
            return false; // 서명 오류, 만료 등 모든 예외 포함
        }
    }

    /**
     * ✅ 토큰에서 사용자 ID 추출
     * - Claims에서 "userId" 필드를 꺼냅니다.
     *
     * @param token JWT 문자열
     * @return 사용자 ID (Long 타입)
     */
    public Long getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class); // 사용자 ID 추출
    }
}
