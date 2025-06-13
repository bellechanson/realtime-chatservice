import React from 'react';
import '../../style/layout/Footer.css'; // Footer 전용 CSS import

// 하단 공통 Footer 컴포넌트
const Footer = () => {
    return (
        <footer className="main-footer"> {/* CSS 클래스명과 연동 */}
            <p>© 2025 RealtimeChatService. All rights reserved.</p> {/* 서비스명 및 저작권 */}
        </footer>
    );
};

export default Footer;
