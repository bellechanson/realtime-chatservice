package com.example.realtimechatservice.controller;

import com.example.realtimechatservice.config.RabbitConfig;
import com.example.realtimechatservice.dto.ChatMessageDTO;
import com.example.realtimechatservice.service.UserNicknameService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * 💬 ChatController - 실시간 채팅 메시지를 처리하는 WebSocket 컨트롤러
 *
 * - 클라이언트가 WebSocket 경로 "/app/chat/room/{roomId}"로 전송한 메시지를 수신함
 * - 닉네임 정보를 주입하고, RabbitMQ로 메시지를 발행하여 비동기 처리 구조로 넘김
 */
@Controller
@RequiredArgsConstructor
public class ChatController {

    // 🔗 이메일 → 닉네임 변환 서비스 (UserService 호출)
    private final UserNicknameService userNicknameService;

    // 📨 메시지를 RabbitMQ에 전송하기 위한 템플릿
    private final RabbitTemplate rabbitTemplate;

    /**
     * 📥 WebSocket 메시지 수신 핸들러
     *
     * - STOMP 경로 "/app/chat/room/{roomId}"로 들어오는 메시지를 처리함
     * - 메시지를 RabbitMQ로 발행하여 비동기 저장 및 전송 처리를 맡김
     */
    @MessageMapping("/chat/room/{roomId}")
    public void sendMessage(ChatMessageDTO dto) {
        // 1. 이메일을 기반으로 사용자 닉네임 조회
        String nickname = userNicknameService.getNicknameByEmail(dto.getUserEmail());
        dto.setUserName(nickname);

        // 2. 메시지를 MQ로 발행 (Exchange + RoutingKey 사용)
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_NAME,   // 메시지를 보낼 Exchange 이름
                RabbitConfig.ROUTING_KEY,     // 라우팅 키: 어떤 큐로 보낼지 결정
                dto                           // 전송할 메시지 객체 (ChatMessageDTO)
        );
    }
}
