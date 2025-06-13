package com.example.userservice.repository;

import com.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 👤 UserRepository
 *
 * ✅ 역할:
 * - User 엔티티에 대한 CRUD 작업을 담당하는 Spring Data JPA 인터페이스입니다.
 * - 이메일 기반 사용자 조회 기능을 제공합니다.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * ✅ 이메일 기반 사용자 조회
     *
     * - 로그인, 인증 처리, 사용자 정보 조회 등에 사용됩니다.
     *
     * @param email 사용자 이메일
     * @return Optional<User> 객체로 반환 (존재하지 않을 수 있으므로)
     */
    Optional<User> findByEmail(String email);
}
