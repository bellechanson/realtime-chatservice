package com.example.userservice.controller;

import com.example.userservice.dto.LoginRequestDTO;
import com.example.userservice.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 🔐 LoginController
 *
 * - 사용자 로그인 및 인증된 사용자 정보 조회 기능을 제공합니다.
 * - 로그인 시 JWT 토큰을 생성해 응답하며,
 * - 이후 요청에서 토큰을 검증하여 사용자 정보를 반환합니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * ✅ 로그인 엔드포인트
     * POST /api/users/login
     *
     * @param request 로그인 요청 DTO (email + password)
     * @return 로그인 성공 시 JWT 토큰 반환
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        String token = loginService.login(request); // 인증 및 토큰 발급
        return ResponseEntity.ok(Map.of("token", token)); // 토큰 JSON으로 반환
    }

}
