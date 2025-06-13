package com.example.userservice.filter;

import com.example.userservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 🛡️ JwtAuthenticationFilter
 *
 * - 매 요청마다 실행되며, Authorization 헤더에 포함된 JWT 토큰을 검증합니다.
 * - 유효한 토큰이면 사용자 정보를 request 속성에 추가합니다.
 * - 이후 컨트롤러에서 이 정보를 활용해 인증된 사용자만 접근 가능하도록 처리할 수 있습니다.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // ✅ 1. Authorization 헤더 확인
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // ✅ 2. 토큰 추출
            String token = authHeader.substring(7);

            // ✅ 3. 토큰 검증
            if (jwtUtil.isValidToken(token)) {
                // ✅ 4. 사용자 정보 추출 및 request 속성에 저장
                String email = jwtUtil.getEmailFromToken(token);
                Long userId = jwtUtil.getUserIdFromToken(token);

                request.setAttribute("userEmail", email);
                request.setAttribute("userId", userId);
            }
            // ❌ 유효하지 않은 토큰은 무시하고 다음 필터로 넘김
        }

        // ✅ 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}
