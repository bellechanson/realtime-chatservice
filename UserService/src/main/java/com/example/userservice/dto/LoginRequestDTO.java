package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 🔐 LoginRequestDTO
 * - 로그인 요청 시 사용자로부터 전달받는 이메일과 비밀번호를 담는 DTO입니다.
 * - 이 정보를 기반으로 로그인 검증 및 세션 생성이 이루어집니다.
 */
@Getter
@Setter
public class LoginRequestDTO {

    /** 사용자 이메일 (로그인 ID 역할) */
    private String email;

    /** 사용자 비밀번호 (평문 상태, 서비스에서 암호화 후 비교) */
    private String password;
}
