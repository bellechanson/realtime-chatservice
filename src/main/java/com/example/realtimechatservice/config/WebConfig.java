package com.example.realtimechatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🌐 WebConfig - CORS 설정 클래스
 * React(프론트엔드) ↔ Spring Boot(백엔드) 간의 교차 출처 요청을 허용하기 위한 설정을 담당한다.
 */
@Configuration // 이 클래스가 설정 파일임을 명시
public class WebConfig implements WebMvcConfigurer {

    /**
     * 🔧 CORS 설정 메서드
     * 프론트엔드에서 백엔드로의 요청을 허용하도록 설정
     *
     * @param registry CORS 설정을 적용할 레지스트리
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins("http://localhost:5173") // React 개발 서버 주소 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드 지정
                .allowCredentials(true); // 인증 정보(Cookie 등)를 함께 허용
    }
}
