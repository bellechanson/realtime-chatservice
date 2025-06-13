package com.example.userservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 🛠️ RedisConfig
 * - Redis 연결 설정 및 RedisTemplate 생성
 * - 인증 코드 TTL 관리 등에 사용됨
 */
@Configuration
public class RedisConfig {

    // 📍 application.properties host/port 설정 주입
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    /**
     * ✅ RedisConnectionFactory
     * - Lettuce 기반 Redis 커넥션 팩토리
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }

    /**
     * ✅ RedisTemplate<String, String>
     * - Redis 문자열 키-값 저장용 템플릿
     * - 기본 직렬화를 StringSerializer로 설정
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // 🔐 Redis CLI에서 사람이 읽을 수 있도록 직렬화 설정
        redisTemplate.setKeySerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());
        redisTemplate.setValueSerializer(new org.springframework.data.redis.serializer.StringRedisSerializer());

        return redisTemplate;
    }
}
