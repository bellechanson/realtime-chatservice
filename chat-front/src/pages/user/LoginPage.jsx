import React, { useState } from 'react';
import { loginUser, getUserInfo } from '../../api/UserApi.jsx'; // 로그인 및 유저 정보 요청 API
import { useNavigate } from 'react-router-dom'; // 페이지 이동 훅
import axios from 'axios'; // axios 기본 헤더 설정용

/**
 * 📌 LoginPage 컴포넌트
 * - 사용자의 이메일/비밀번호를 입력받아 로그인 요청을 수행합니다.
 * - 로그인 성공 시 JWT 토큰을 저장하고, 사용자 정보를 조회하여 상태 저장 후 메인으로 이동합니다.
 */
function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    /**
     * ✅ 로그인 요청 핸들러
     * - 로그인 성공 시 JWT 토큰 저장 + 사용자 정보 저장 + 메인 페이지 이동
     */
    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            // 🔐 로그인 요청 (토큰 발급)
            const res = await loginUser(email, password);
            console.log("✅ 로그인 응답:", res.data);

            const token = res.data.token;

            // ✅ 토큰 로컬스토리지에 저장
            localStorage.setItem("token", token);

            // ✅ axios 전역 헤더에 Authorization 설정
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

            // 👤 사용자 정보 요청
            const infoRes = await getUserInfo();
            console.log("📦 userInfo 응답:", infoRes.data);

            const { email: userEmail, nickname: userName } = infoRes.data;

            // 🧠 로그인 상태 로컬스토리지 저장
            localStorage.setItem('userState', JSON.stringify({
                isLoggedIn: true,
                user: {
                    uEmail: userEmail,
                    uName: userName,
                    uRole: 'USER',
                }
            }));

            alert('로그인 성공!');
            navigate('/');
        } catch (err) {
            console.error("❌ getUserInfo 에러:", err);
            alert('로그인 실패: ' + (err.response?.data || err.message));
        }
    };

    return (
        <form onSubmit={handleLogin}>
            <h2>로그인</h2>
            <input
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="이메일"
            />
            <input
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="비밀번호"
                type="password"
            />
            <button type="submit">로그인</button>
        </form>
    );
}

export default LoginPage;
