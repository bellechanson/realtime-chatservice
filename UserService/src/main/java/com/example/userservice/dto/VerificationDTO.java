package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 🔐 VerificationDTO
 * - 이메일 인증 코드 검증 요청 시 사용되는 DTO입니다.
 * - 사용자의 이메일과 입력한 인증 코드를 서버로 전달합니다.
 */
@Getter
@Setter
public class VerificationDTO {

    /** 인증 대상 이메일 주소 */
    private String email;

    /** 사용자가 입력한 인증 코드 */
    private String code;
}
