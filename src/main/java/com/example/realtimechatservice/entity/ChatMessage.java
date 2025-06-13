package com.example.realtimechatservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 💬 ChatMessage
 * - 실시간 채팅 메시지를 데이터베이스에 저장하기 위한 JPA 엔티티 클래스입니다.
 * - 각 메시지는 특정 채팅방(roomId)에 속하며, 사용자 이메일, 닉네임, 내용, 생성 시각 등을 포함합니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    /** 고유 메시지 ID (기본키, 자동 증가) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 보낸 사람의 이메일 (식별자) */
    private String userEmail;

    /** 보낸 사람의 닉네임 (화면에 표시) */
    private String userName;

    /** 메시지 본문 내용 */
    private String content;

    /** 이 메시지가 속한 채팅방의 ID (foreign key 아님, 단순 참조용) */
    private Long roomId;

    /** 메시지 생성 시각 */
    private LocalDateTime createdAt;

    /**
     * 🕒 메시지 저장 직전 자동 호출되는 메서드
     * createdAt 값이 없을 경우 현재 시간으로 자동 설정합니다.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
