package com.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

/**
 * 📧 EmailVerificationService
 * - 이메일 인증번호 생성 및 검증 전담 서비스
 * - Redis를 통해 인증번호를 저장하며 TTL로 자동 만료됨
 * - 인증번호는 이메일로 전송됨
 */
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final RedisTemplate<String, String> redisTemplate; // Redis 템플릿
    private final JavaMailSender mailSender;                   // 이메일 발송기

    // 인증번호 유효 시간 (초 기준) → 5분
    private static final long EMAIL_CODE_TTL_SECONDS = 300;

    /**
     * ✅ 인증번호 생성 → Redis 저장 → 이메일 발송
     * - 랜덤한 6자리 숫자를 생성
     * - Redis에 "verify:{email}" 형식으로 저장 (5분 TTL)
     * - 사용자 이메일로 인증번호 발송
     */
    public void sendVerificationCode(String email) {
        // 🔢 인증번호 생성 (000000 ~ 999999)
        String code = String.format("%06d", new Random().nextInt(999999));

        // 🗃️ Redis 저장 (키: verify:{email}, TTL: 5분)
        redisTemplate.opsForValue().set(
                "verify:" + email,
                code,
                Duration.ofSeconds(EMAIL_CODE_TTL_SECONDS)
        );

        // ✉️ 인증 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("📧 RealtimeChatService 인증번호");
        message.setText("인증번호: " + code);
        mailSender.send(message);
    }

    /**
     * ✅ 인증번호 검증
     * - Redis에서 인증번호를 꺼내 사용자가 입력한 코드와 비교
     * - TTL이 만료되어 값이 없는 경우 검증 실패
     *
     * @param email 사용자 이메일
     * @param inputCode 사용자가 입력한 인증번호
     * @return 일치 여부
     */
    public boolean verifyCode(String email, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get("verify:" + email);
        return inputCode.equals(storedCode);
    }
}
