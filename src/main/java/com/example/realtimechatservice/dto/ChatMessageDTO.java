package com.example.realtimechatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 💬 ChatMessageDTO
 *
 * - 클라이언트 ↔ 서버 간 실시간 메시지 교환에 사용되는 DTO 객체
 * - WebSocket 메시지 전송 시 사용되며, RabbitMQ를 통해 비동기 전달 및 DB 저장 용도로도 활용됨
 */
@Data // 👉 @Getter, @Setter, @ToString, @EqualsAndHashCode 포함 (Lombok)
@NoArgsConstructor // 👉 기본 생성자 자동 생성
@AllArgsConstructor // 👉 모든 필드를 매개변수로 받는 생성자 자동 생성
public class ChatMessageDTO implements Serializable {

    // 🧩 메시지 직렬화를 위한 고유 버전 ID
    @Serial
    private static final long serialVersionUID = 1L;

    /** 💬 채팅방 ID (어느 채팅방에 속한 메시지인지 식별) */
    private Long roomId;

    /** 📧 발신자 이메일 (유저 고유 식별자, 닉네임 조회에 사용) */
    private String userEmail;

    /** 🙍 발신자 닉네임 (클라이언트 UI에서 표시될 이름) */
    private String userName;

    /** ✍️ 채팅 메시지 본문 내용 */
    private String content;

    /** 🕒 메시지 생성 시각 (서버에서 설정, 클라이언트 시각 아님) */
    private LocalDateTime createdAt;
}
