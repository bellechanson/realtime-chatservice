package com.example.realtimechatservice.controller;

import com.example.realtimechatservice.entity.ChatRoom;
import com.example.realtimechatservice.repository.ChatRoomRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 🌐 ChatRoomController
 * - 채팅방을 생성하고 전체 채팅방 목록을 반환하는 REST API 컨트롤러
 * - 프론트엔드에서 새로운 채팅방을 생성하거나 목록을 불러올 때 사용됩니다.
 */
@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * ✅ 새로운 채팅방 생성
     * POST /api/chat/room
     *
     * @param request 생성할 채팅방 정보 (roomName, creator)
     * @return 생성된 채팅방 정보 또는 400 Bad Request
     */
    @PostMapping
    public ResponseEntity<ChatRoom> createRoom(@RequestBody ChatRoom request) {
        // 필수값 검증 (creator가 비어있으면 잘못된 요청)
        if (request.getCreator() == null || request.getCreator().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // 새 채팅방 객체 생성 및 저장
        ChatRoom room = ChatRoom.builder()
                .roomName(request.getRoomName())
                .creator(request.getCreator())
                .build();

        return ResponseEntity.ok(chatRoomRepository.save(room));
    }

    /**
     * ✅ 모든 채팅방 목록 조회
     * GET /api/chat/room
     *
     * @return 전체 채팅방 리스트
     */
    @GetMapping
    public ResponseEntity<List<ChatRoom>> getAllRooms() {
        return ResponseEntity.ok(chatRoomRepository.findAll());
    }
}
