import React, { useEffect } from 'react';
import Chat from '../../components/chat/ChatRoom.jsx'; // 채팅 컴포넌트 불러오기
import { useParams } from 'react-router-dom'; // URL 파라미터 추출용 훅
import '../../style/chat/ChatRoom.css'; // CSS 적용

function ChatRoomPage() {
    const { roomId } = useParams(); // 주소에서 roomId 추출

    // ✅ 기본 유저 정보 (비로그인 사용자를 위한 fallback)
    const fallbackUser = {
        uEmail: 'guest@chat.com',
        uName: '게스트',
        uRole: 'GUEST',
    };

    // ✅ localStorage에서 사용자 정보 로드
    const userState = JSON.parse(localStorage.getItem('userState'));

    // userState.user가 문자열로 저장된 경우를 대비한 예외 처리
    const user = typeof userState?.user === 'string'
        ? JSON.parse(userState.user)
        : userState?.user || fallbackUser;

    // ✅ 유저 정보가 있는지 확인 (현재는 아무 작업 없음)
    useEffect(() => {
        if (!userState || !userState.user?.uEmail) return;
        // TODO: 필요 시 리다이렉트 또는 경고 처리 가능
    }, []);

    return (
        <div className="chat-page-container">
            <h2>실시간 채팅방</h2>
            {/* 채팅방 컴포넌트 렌더링 */}
            <Chat
                roomId={roomId}
                userEmail={user.uEmail}
                userName={user.uName}
            />
        </div>
    );
}

export default ChatRoomPage;
