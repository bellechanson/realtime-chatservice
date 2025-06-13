package com.example.realtimechatservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 👤 UserNicknameService
 * - 사용자 이메일을 기반으로 닉네임을 외부 서비스(UserService)에서 조회하는 서비스 클래스입니다.
 * - RestTemplate을 사용하여 HTTP GET 요청을 보내고, 문자열 형태의 닉네임을 응답받습니다.
 */
@Service
public class UserNicknameService {

    // RestTemplate: 외부 API 호출용 HTTP 클라이언트
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * ✅ 이메일로 닉네임 조회
     * - UserService의 "/api/users/nickname?email=..." 엔드포인트로 요청
     *
     * @param email 조회할 사용자 이메일
     * @return 해당 사용자의 닉네임
     */
    public String getNicknameByEmail(String email) {
        // 요청 URL 생성
        String url = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8123)
                .path("/api/users/nickname")
                .queryParam("email", email)
                .build()
                .toUriString();  // 자동 인코딩 적용

        System.out.println("🎯 요청 URL: " + url); // 개발용 로그

        // HTTP GET 요청 → 닉네임 반환
        return restTemplate.getForObject(url, String.class);
    }
}
