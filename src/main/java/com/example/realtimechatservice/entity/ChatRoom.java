package com.example.realtimechatservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 🏠 ChatRoom
 * - 실시간 채팅에서 사용되는 채팅방 정보를 저장하는 JPA 엔티티입니다.
 * - 채팅방의 이름, 생성자 정보, 생성 시각을 포함합니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    /** 고유 채팅방 ID (기본키, 자동 증가) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 채팅방 이름 (제목) */
    private String roomName;

    /** 채팅방 생성자 이메일 또는 이름 */
    private String creator;

    /** 채팅방 생성 시각 */
    private LocalDateTime createdAt;

    /**
     * 🕒 채팅방이 저장되기 직전에 자동 호출되어 생성 시각을 현재 시간으로 설정
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
