package com.example.userservice.config;

import com.example.userservice.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 🔧 FilterConfig
 *
 * - JwtAuthenticationFilter를 필요한 URL 경로에만 적용하기 위한 설정 클래스입니다.
 * - Spring Security를 쓰지 않고 수동으로 JWT 인증을 필터링할 때 활용됩니다.
 */
@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * ✅ JWT 인증 필터 등록
     *
     * - 아래 URL 패턴에만 필터를 적용합니다.
     * - 인증이 필요한 API 경로를 여기에 명시합니다.
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthenticationFilter);

        // 🎯 인증이 필요한 URL 패턴만 지정 (필요에 따라 추가 가능)
        registrationBean.addUrlPatterns(
                "/api/users/info",   // 사용자 정보 조회
                "/api/users/me"      // 세션 기반 사용자 정보 조회 (사용 중이면 유지)
        );

        return registrationBean;
    }
}
