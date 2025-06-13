import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/ChatApi'; // axios 기반 API 모듈

function ChatRoomCreate() {
    const [roomName, setRoomName] = useState(''); // 채팅방 이름 입력값 상태
    const navigate = useNavigate(); // 페이지 이동 훅

    // 채팅방 생성 버튼 클릭 시 실행
    const handleCreateRoom = async () => {
        // 빈 문자열일 경우 경고
        if (!roomName.trim()) {
            alert("채팅방 이름을 입력해주세요.");
            return;
        }

        // localStorage에서 사용자 정보 가져오기
        const userState = JSON.parse(localStorage.getItem('userState'));
        const nickname = userState?.user?.uName;

        // 로그인 안 된 상태일 경우 경고
        if (!nickname) {
            alert("로그인이 필요합니다.");
            return;
        }

        try {
            // ✅ 방 이름과 생성자 이름을 서버로 전송
            const res = await api.post('/api/chat/room', {
                roomName,
                creator: nickname,
            });

            // 생성된 방의 ID를 받아와 메인 페이지로 이동
            const roomId = res.data.id;
            navigate(`/`);
        } catch (err) {
            // 에러 메시지 출력
            alert('채팅방 생성 실패: ' + (err.response?.data || err.message));
        }
    };

    return (
        <div>
            <h2>채팅방 만들기</h2>
            <input
                value={roomName}
                onChange={(e) => setRoomName(e.target.value)} // 입력값 변경 처리
                placeholder="채팅방 이름"
            />
            <button onClick={handleCreateRoom}>생성</button>
        </div>
    );
}

export default ChatRoomCreate;
