package com.example.realtimechatservice.repository;

import com.example.realtimechatservice.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 💬 ChatMessageRepository
 * - 채팅 메시지 엔티티에 대한 데이터 접근을 처리하는 JPA Repository입니다.
 * - 기본적인 CRUD 기능은 JpaRepository에서 제공하며,
 *   채팅방 ID 기준으로 메시지를 시간순으로 정렬해 조회하는 커스텀 메서드를 추가로 정의합니다.
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * ✅ 특정 채팅방의 메시지를 생성 시각 기준 오름차순으로 조회
     *
     * @param roomId 조회할 채팅방의 ID
     * @return 해당 채팅방의 메시지 목록 (createdAt 기준 오름차순 정렬)
     */
    List<ChatMessage> findByRoomIdOrderByCreatedAt(Long roomId);
}
