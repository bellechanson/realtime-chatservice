package com.example.realtimechatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

/**
 * 📡 WebSocketConfig - STOMP 기반 WebSocket 설정 클래스
 * 실시간 채팅을 위해 WebSocket + STOMP 프로토콜을 설정하는 핵심 구성입니다.
 */
@Configuration // Spring 설정 클래스임을 명시
@EnableWebSocketMessageBroker // WebSocket 메시징을 위한 STOMP 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 📍 클라이언트가 WebSocket 서버에 연결할 엔드포인트를 등록
     * SockJS를 사용하여 WebSocket 미지원 브라우저도 fallback 처리
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트는 ws://localhost:8787/ws 로 접속
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // CORS 모든 출처 허용 (개발 환경 기준)
                .withSockJS(); // WebSocket 미지원 브라우저를 위한 fallback 지원
    }

    /**
     * 🧭 메시지 라우팅을 위한 브로커 구성
     * /app → 서버 수신 (Controller @MessageMapping)
     * /topic → 클라이언트 수신 (브로드캐스트용)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // 메시지를 구독하는 채널 prefix
        registry.setApplicationDestinationPrefixes("/app"); // 메시지를 보낼 때 prefix
    }
}
