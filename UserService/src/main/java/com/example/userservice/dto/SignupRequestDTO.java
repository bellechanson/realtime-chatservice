package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 📝 SignupRequestDTO
 * - 회원가입 요청 시 사용자로부터 입력받는 정보를 담는 DTO입니다.
 * - 이메일, 비밀번호, 닉네임, 인증코드가 포함됩니다.
 */
@Getter
@Setter
public class SignupRequestDTO {

    /** 사용자 이메일 (계정 식별자) */
    private String email;

    /** 사용자 비밀번호 (서비스에서 암호화 처리) */
    private String password;

    /** 사용자 닉네임 (화면 표시용 이름) */
    private String nickname;

    /** 이메일 인증 시 사용된 코드 */
    private String verificationCode;
}
