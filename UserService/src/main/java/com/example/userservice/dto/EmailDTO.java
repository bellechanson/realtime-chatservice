package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 📧 EmailDTO
 * - 이메일 인증 요청 시 사용되는 DTO입니다.
 * - 사용자가 입력한 이메일 값을 서버로 전달할 때 사용합니다.
 */
@Getter
@Setter
public class EmailDTO {

    /** 사용자 이메일 주소 */
    private String email;
}
