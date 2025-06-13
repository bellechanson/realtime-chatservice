package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 👤 UserInfoService
 *
 * ✅ 역할:
 * - 로그인된 사용자의 정보를 JWT 기반으로 추출하고, 닉네임을 DB에서 조회합니다.
 * - 외부 서비스에서 이메일 기반 닉네임 조회가 필요할 때도 사용됩니다.
 */
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserRepository userRepository;

    /**
     * ✅ JWT 기반 사용자 정보(email, nickname) 조회
     *
     * - 인증 필터(JwtAuthenticationFilter)에서 request에 세팅한 사용자 정보를 꺼냅니다.
     * - nickname은 email로 DB에서 직접 조회합니다.
     *
     * @param request JWT 필터를 거친 HttpServletRequest
     * @return email, nickname을 담은 Map<String, String>
     * @throws RuntimeException 인증 정보가 없거나 nickname 조회 실패 시 예외 발생
     */
    public Map<String, String> getUserInfoFromJwt(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail"); // 🔐 JwtAuthenticationFilter에서 저장한 email
        String nickname = getNicknameByEmail(email);               // 🗂️ DB에서 nickname 조회

        if (email == null || nickname == null) {
            throw new RuntimeException("JWT에 사용자 정보가 없습니다.");
        }

        Map<String, String> result = new HashMap<>();
        result.put("email", email);
        result.put("nickname", nickname);
        return result;
    }

    /**
     * ✅ 이메일 기반 닉네임 조회
     *
     * - 유저가 존재하지 않을 경우 예외를 던집니다.
     * - 외부 시스템에서도 호출 가능하도록 별도 메서드로 분리되어 있습니다.
     *
     * @param email 사용자 이메일
     * @return nickname 해당 이메일을 가진 사용자의 닉네임
     * @throws RuntimeException 존재하지 않는 이메일일 경우
     */
    public String getNicknameByEmail(String email) {
        return userRepository.findByEmail(email.trim()) // 공백 제거는 안전성 위해 추가
                .map(User::getNickname)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저를 찾을 수 없습니다."));
    }
}
