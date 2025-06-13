package com.example.userservice.controller;

import com.example.userservice.dto.SignupRequestDTO;
import com.example.userservice.service.EmailVerificationService;
import com.example.userservice.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 🧩 SignupController
 * - 이메일 인증 및 회원가입 관련 API를 처리하는 컨트롤러
 * - 프론트에서 인증번호 전송 → 검증 → 회원가입 순서대로 호출됨
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SignupController {

    private final EmailVerificationService emailVerificationService;
    private final SignupService signupService;

    /**
     * ✅ 인증번호 전송 API
     * - [POST] /api/users/send-code
     * - Request Body: { "email": "user@example.com" }
     * - Redis에 인증번호 저장 + 이메일 발송
     */
    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody Map<String, String> request) {
        emailVerificationService.sendVerificationCode(request.get("email"));
        return ResponseEntity.ok().build(); // 성공 시 200 OK
    }

    /**
     * ✅ 인증번호 검증 API
     * - [POST] /api/users/verify-code
     * - Request Body: { "email": "user@example.com", "code": "123456" }
     * - 인증 성공 시 200 OK / 실패 시 400 Bad Request
     */
    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyCode(@RequestBody Map<String, String> request) {
        boolean isValid = emailVerificationService.verifyCode(request.get("email"), request.get("code"));
        return isValid ? ResponseEntity.ok().build()
                : ResponseEntity.status(400).build();
    }

    /**
     * ✅ 회원가입 API
     * - [POST] /api/users/signup
     * - Request Body: SignupRequestDTO (이메일, 닉네임, 비밀번호, 인증코드 포함)
     * - 인증번호가 일치할 경우에만 가입 허용
     */
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequestDTO dto) {
        signupService.signup(dto);
        return ResponseEntity.ok().build(); // 성공 시 200 OK
    }
}
