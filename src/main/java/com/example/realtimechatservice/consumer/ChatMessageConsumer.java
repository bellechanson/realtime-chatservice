package com.example.realtimechatservice.consumer;

import com.example.realtimechatservice.config.RabbitConfig;
import com.example.realtimechatservice.dto.ChatMessageDTO;
import com.example.realtimechatservice.entity.ChatMessage;
import com.example.realtimechatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 📥 ChatMessageConsumer
 *
 * - RabbitMQ로부터 채팅 메시지를 수신하는 Consumer 역할을 수행
 * - 수신한 메시지를 DB에 저장하고, WebSocket을 통해 구독자에게 브로드캐스트함
 */
@Service
@RequiredArgsConstructor
public class ChatMessageConsumer {

    // 💾 채팅 메시지 DB 저장용 JPA 레포지토리
    private final ChatMessageRepository chatMessageRepository;

    // 📡 STOMP 기반 WebSocket 메시지 전송 도구
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 🐰 MQ 메시지 수신 핸들러
     *
     * - MQ의 "chat.queue"로 들어온 메시지를 자동 수신함
     * - @RabbitListener가 메시지를 바인딩된 큐에서 읽음
     * - 메시지는 자동으로 JSON → ChatMessageDTO로 역직렬화됨
     */
    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(ChatMessageDTO dto) {
        // 1️⃣ MQ로부터 받은 메시지를 DB에 저장
        ChatMessage message = ChatMessage.builder()
                .roomId(dto.getRoomId())
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .build();

        chatMessageRepository.save(message); // 🗂 JPA 저장

        // 2️⃣ 같은 채팅방의 구독자들에게 메시지 브로드캐스트
        messagingTemplate.convertAndSend(
                "/topic/chat/room/" + dto.getRoomId(), // 구독 경로
                dto                                     // 보낼 메시지
        );

        // 3️⃣ 로그 출력 (개발/디버깅용)
        System.out.println("📥 MQ 메시지 수신 및 처리 완료: " + dto);
    }
}
