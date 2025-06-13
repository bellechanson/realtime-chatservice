package com.example.realtimechatservice.controller;

import com.example.realtimechatservice.entity.ChatMessage;
import com.example.realtimechatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 🌐 ChatRestController
 * - 채팅 메시지를 REST API 방식으로 조회, 저장, 삭제하는 컨트롤러입니다.
 * - 프론트엔드에서 초기 메시지 로딩 또는 관리자 기능 등에 사용됩니다.
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatMessageRepository chatMessageRepository;

    /**
     * ✅ 채팅방의 메시지를 시간순으로 조회
     * GET /api/chat/room/{roomId}/messages
     *
     * @param roomId 조회할 채팅방 ID
     * @return 해당 방의 메시지 목록 (createdAt 오름차순)
     */
    @GetMapping("/room/{roomId}/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatMessageRepository.findByRoomIdOrderByCreatedAt(roomId));
    }

    /**
     * ✅ 메시지를 저장 (REST 방식)
     * POST /api/chat/room/{roomId}/messages
     *
     * @param roomId 채팅방 ID
     * @param request 저장할 메시지 데이터
     * @return 저장된 메시지 객체
     */
    @PostMapping("/room/{roomId}/messages")
    public ResponseEntity<ChatMessage> sendMessage(
            @PathVariable Long roomId,
            @RequestBody ChatMessage request
    ) {
        // 요청으로부터 받은 데이터를 기반으로 새로운 메시지 생성
        ChatMessage message = ChatMessage.builder()
                .userEmail(request.getUserEmail())
                .userName(request.getUserName())
                .content(request.getContent())
                .roomId(roomId)
                .createdAt(LocalDateTime.now())
                .build();

        // 메시지를 DB에 저장 후 저장된 객체 반환
        ChatMessage saved = chatMessageRepository.save(message);
        return ResponseEntity.ok(saved);
    }

    /**
     * ✅ 메시지 삭제
     * DELETE /api/chat/messages/{messageId}
     *
     * @param messageId 삭제할 메시지의 ID
     * @return 삭제 성공 여부 (204 또는 404)
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        if (!chatMessageRepository.existsById(messageId)) {
            return ResponseEntity.notFound().build(); // 메시지가 존재하지 않으면 404 반환
        }
        chatMessageRepository.deleteById(messageId); // 메시지 삭제
        return ResponseEntity.noContent().build();   // 삭제 성공 시 204 No Content
    }
}
