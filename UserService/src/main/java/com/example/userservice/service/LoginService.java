package com.example.userservice.service;

import com.example.userservice.dto.LoginRequestDTO;
import com.example.userservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;

/**
 * 🔐 LoginService
 *
 * - 사용자 로그인 요청을 처리하는 서비스 클래스입니다.
 * - 이메일 및 비밀번호를 검증하고,
 *   성공 시 JWT 토큰을 생성하여 반환합니다.
 */
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // ✅ 비밀번호 비교에 사용되는 해시 함수 (Spring Security 기본 제공)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * ✅ 로그인 처리
     * - 이메일과 비밀번호를 검증하고,
     * - 유효할 경우 JWT 토큰을 생성하여 반환합니다.
     *
     * @param request 로그인 요청 DTO (email + password)
     * @return 발급된 JWT 토큰 문자열
     * @throws RuntimeException 인증 실패 시 예외 발생
     */
    public String login(LoginRequestDTO request) {
        // 🔍 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 이메일입니다."));

        // 🔒 이메일 인증 여부 확인
        if (!user.isVerified()) {
            throw new RuntimeException("이메일 인증이 완료되지 않았습니다.");
        }

        // 🔐 비밀번호 비교 (BCrypt 해시 매칭)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 🪙 로그인 성공 → JWT 발급 및 반환
        return jwtUtil.createToken(user.getId(), user.getEmail());
    }
}
