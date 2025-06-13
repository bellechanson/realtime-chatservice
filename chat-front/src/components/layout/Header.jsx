import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../../style/layout/Header.css'; // 헤더 스타일 CSS

/**
 * 📌 Header 컴포넌트
 * - 로그인 여부에 따라 네비게이션 버튼을 다르게 표시합니다.
 * - 로그아웃 시 localStorage에서 사용자 정보와 토큰을 제거합니다.
 */
const Header = () => {
    const navigate = useNavigate(); // 페이지 이동 함수

    // 🔐 로컬스토리지에서 로그인 상태 및 사용자 이름 가져오기
    const userState = JSON.parse(localStorage.getItem('userState'));
    const isLoggedIn = userState?.isLoggedIn;
    const userName = userState?.user?.uName;

    /**
     * ✅ 로그아웃 핸들러
     * - localStorage에서 사용자 정보 및 토큰 제거
     * - 홈 화면으로 리다이렉트
     */
    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userState');
        navigate('/');
    };

    return (
        <header className="header">
            {/* ⬅️ 로고 영역 */}
            <h1 className="main-logo">
                <Link to="/" className="logo-link">RealtimeChatService</Link>
            </h1>

            {/* ➡️ 인증 관련 버튼 영역 */}
            <div className="auth-buttons">
                {isLoggedIn ? (
                    <>
                        {/* ✅ 로그인 상태일 때 */}
                        <span className="welcome-text">{userName}님 환영합니다</span>
                        <button className="logout-btn" onClick={handleLogout}>로그아웃</button>
                    </>
                ) : (
                    <>
                        {/* ❌ 비로그인 상태일 때 */}
                        <Link to="/login" className="btn">로그인</Link>
                        <Link to="/signup" className="btn">회원가입</Link>
                    </>
                )}
            </div>
        </header>
    );
};

export default Header;
