package com.example.userservice.service;

import com.example.userservice.dto.SignupRequestDTO;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 📝 SignupService
 * - 회원가입 처리 담당 서비스
 * - 이메일 인증 성공 여부 확인 후, 새 사용자 저장
 */
@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;

    // 인증번호 검증 전담 서비스 (Redis 기반 TTL 포함)
    private final EmailVerificationService emailVerificationService;

    // 비밀번호 암호화
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * ✅ 회원가입 처리
     * - 1. 이메일 인증번호 검증
     * - 2. 중복 이메일 여부 체크
     * - 3. 비밀번호 암호화 및 사용자 저장
     */
    @Transactional
    public void signup(SignupRequestDTO request) {
        // 🔐 1. 인증번호 검증 실패 시 예외
        if (!emailVerificationService.verifyCode(request.getEmail(), request.getVerificationCode())) {
            throw new RuntimeException("인증번호가 일치하지 않습니다.");
        }

        // 📛 2. 중복 이메일 검사
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 🧾 3. 사용자 정보 저장
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화
                .nickname(request.getNickname())
                .verified(true) // 인증 완료 플래그
                .build();

        userRepository.save(user);
    }
}
