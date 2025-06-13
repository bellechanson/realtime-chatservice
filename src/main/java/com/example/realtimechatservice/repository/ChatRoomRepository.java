package com.example.realtimechatservice.repository;

import com.example.realtimechatservice.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 💬 ChatRoomRepository
 * - 채팅방(ChatRoom) 엔티티에 대한 데이터 접근을 처리하는 JPA Repository 인터페이스입니다.
 * - JpaRepository를 상속받아 기본적인 CRUD 기능을 제공합니다.
 * - 필요 시 커스텀 쿼리 메서드를 추가할 수 있습니다.
 */
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 예: 채팅방 이름으로 조회
    // Optional<ChatRoom> findByRoomName(String roomName);
}
