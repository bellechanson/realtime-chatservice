import './App.css';
import './index.css';

import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import MainPage from './pages/MainPage.jsx';
import ChatRoomPage from './pages/chat/ChatRoom.jsx';
import ChatRoomCreate from './components/chat/ChatRoomCreate.jsx';
import LoginPage from './pages/user/LoginPage.jsx';
import SignupPage from './pages/user/SignupPage.jsx';

/**
 * 🧭 App 컴포넌트 - 전체 라우팅 정의
 * 각 페이지를 URL 경로에 따라 렌더링합니다.
 */
function App() {
    return (
        <Router>
            <Routes>
                {/* 🗨️ 채팅방 상세 페이지 (/chat/room/:roomId) */}
                <Route path="/chat/room/:roomId" element={<ChatRoomPage />} />

                {/* ➕ 채팅방 생성 페이지 */}
                <Route path="/chat/create" element={<ChatRoomCreate />} />

                {/* 🔐 로그인 & 회원가입 페이지 */}
                <Route path="/login" element={<LoginPage />} />
                <Route path="/signup" element={<SignupPage />} />

                {/* 🏠 메인 페이지 */}
                <Route path="/" element={<MainPage />} />
            </Routes>
        </Router>
    );
}

export default App;
