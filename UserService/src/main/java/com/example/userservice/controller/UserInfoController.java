package com.example.userservice.controller;

import com.example.userservice.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 👤 UserInfoController
 *
 * ✅ 역할:
 * - 로그인된 사용자의 정보를 JWT 기반으로 조회합니다.
 * - 이메일 기반 닉네임 조회 API를 제공합니다 (외부 마이크로서비스 연동용).
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    /**
     * ✅ JWT 기반 사용자 정보 조회
     *
     * GET /api/users/me
     * - JwtAuthenticationFilter에서 인증된 사용자 정보를 request에서 꺼내 반환합니다.
     * - 인증 실패 시 401 응답을 반환합니다.
     *
     * @param request JWT 인증 필터를 거친 요청 객체
     * @return 사용자 email, nickname을 포함한 JSON 응답
     */
    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        try {
            Map<String, String> userInfo = userInfoService.getUserInfoFromJwt(request);
            return ResponseEntity.ok(userInfo); // 성공 시 사용자 정보 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage())); // 인증 실패 시 401
        }
    }

    /**
     * ✅ 이메일 기반 닉네임 조회 (외부 시스템 연동용)
     *
     * GET /api/users/nickname?email={email}
     * - 주어진 이메일에 해당하는 사용자의 nickname을 반환합니다.
     * - 닉네임은 DB에서 직접 조회합니다.
     *
     * @param email 사용자 이메일
     * @return nickname
     */
    @GetMapping("/nickname")
    public ResponseEntity<String> getNicknameByEmail(@RequestParam String email) {
        System.out.println("📩 닉네임 요청 들어옴: " + email); // ✅ 디버깅용 콘솔 로그
        return ResponseEntity.ok(userInfoService.getNicknameByEmail(email));
    }
}
