package com.example.userservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 🌐 WebConfig - CORS(Cross-Origin Resource Sharing) 설정 클래스
 *
 * ✅ 목적:
 * - 프론트엔드(React)와 백엔드(Spring Boot) 간의 교차 출처 요청(CORS)을 허용합니다.
 * - 특히 개발 환경에서 React 개발 서버(5173 포트)에서 오는 요청을 정상적으로 처리하도록 설정합니다.
 *
 * ✅ CORS란?
 * - 서로 다른 도메인 간의 리소스 요청을 허용할지 제어하는 브라우저 보안 정책입니다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * ✅ CORS 정책 설정 메서드
     *
     * @param registry Spring에서 제공하는 CORS 설정 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 💡 전체 API 경로에 대해 CORS 허용
                .allowedOrigins("http://localhost:5173") // ✅ 프론트엔드 개발 서버 주소 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE") // ✅ 허용할 HTTP 메서드 지정
                .allowedHeaders("Authorization", "Content-Type") // ✅ 클라이언트 요청 허용 헤더
                .allowCredentials(true); // ✅ 쿠키, Authorization 헤더 전송 허용
    }
}
