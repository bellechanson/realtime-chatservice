package com.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 👤 User
 * - 사용자 정보를 저장하는 JPA 엔티티입니다.
 * - 이메일, 비밀번호, 닉네임, 인증 여부 등을 포함합니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    /** 고유 사용자 ID (PK, 자동 증가) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 이메일 (로그인 ID 역할, 중복 불가) */
    @Column(unique = true)
    private String email;

    /** 암호화된 비밀번호 */
    private String password;

    /** 닉네임 (화면 표시용 이름) */
    private String nickname;

    /** 이메일 인증 완료 여부 */
    private boolean verified;
}
