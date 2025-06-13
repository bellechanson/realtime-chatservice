package com.example.realtimechatservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory; // ✅ Spring 관리용 ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🐰 RabbitMQ 설정 클래스
 * - 메시지 교환소(Exchange), 큐(Queue), 라우팅 키(Routing Key)를 설정
 * - JSON 기반 메시지 직렬화/역직렬화 컨버터 설정
 * - RabbitTemplate에 위 설정들을 연결해 MQ로 메시지를 보내고 받을 수 있도록 구성
 */
@Configuration
public class RabbitConfig {

    // 🔧 메시지를 라우팅할 Exchange, Queue, Routing Key 이름
    public static final String EXCHANGE_NAME = "chat.exchange"; // TopicExchange 이름
    public static final String QUEUE_NAME = "chat.queue";       // 메시지 소비자가 구독할 큐
    public static final String ROUTING_KEY = "chat.message";    // 메시지를 큐로 라우팅할 키

    /**
     * 📦 TopicExchange 설정
     * - 다양한 라우팅 키 패턴을 처리할 수 있는 유연한 교환 방식
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    /**
     * 📬 메시지를 저장할 큐 생성
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    /**
     * 🔗 Exchange와 Queue를 라우팅 키로 바인딩
     * - 즉, "chat.message" 라우팅 키로 전송된 메시지는 이 큐에 도달함
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * 🔄 메시지 직렬화/역직렬화용 컨버터 (JSON 기반)
     * - DTO를 JSON으로 변환하여 메시지 본문에 담고,
     *   다시 꺼낼 땐 JSON → DTO 객체로 자동 역직렬화
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 📨 RabbitTemplate: Spring이 제공하는 MQ 송신 도구
     * - MQ로 메시지를 전송할 때 사용되며,
     *   ConnectionFactory + JSON 컨버터가 연결되어 있음
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter()); // JSON 메시지 사용
        return template;
    }
}
