import React, { useEffect, useState } from 'react';
import { fetchChatRooms } from '../api/ChatApi'; // 채팅방 목록 불러오기 API
import { useNavigate } from 'react-router-dom';
import Header from '../components/layout/Header.jsx';
import Footer from '../components/layout/Footer.jsx';
import '../index.css';
import '../style/MainPage.css';

const MainPage = () => {
    const navigate = useNavigate(); // 페이지 이동용
    const [rooms, setRooms] = useState([]); // 채팅방 목록 상태

    // ✅ 컴포넌트 마운트 시 채팅방 목록 로딩
    useEffect(() => {
        const loadRooms = async () => {
            try {
                const res = await fetchChatRooms(); // API 요청
                setRooms(res.data); // 채팅방 목록 상태로 설정
            } catch (err) {
                console.error('채팅방 목록 불러오기 실패:', err);
            }
        };
        loadRooms();
    }, []);

    // ✅ 채팅방 생성 페이지로 이동
    const goToCreateRoom = () => {
        navigate('/chat/create');
    };

    // ✅ 채팅방 입장 (해당 roomId로 이동)
    const enterRoom = (roomId) => {
        navigate(`/chat/room/${roomId}`);
    };

    return (
        <div className="main-page">
            <Header />

            <main className="main-content">
                <div className="main-inner">
                    <h2>환영합니다!</h2>
                    <p>실시간 채팅 서비스에 오신 것을 환영합니다.</p>

                    {/* 채팅방 생성 버튼 */}
                    <button className="create-room-btn" onClick={goToCreateRoom}>
                        채팅방 만들기
                    </button>

                    {/* 채팅방 목록 카드 렌더링 */}
                    <div className="room-list">
                        {rooms.map((room) => (
                            <div key={room.id} className="room-card" onClick={() => enterRoom(room.id)}>
                                <h3>{room.roomName}</h3>
                                <p className="creator-text">{room.creator}님의 채팅방</p> {/* ✅ 생성자 정보 표시 */}
                            </div>
                        ))}
                    </div>
                </div>
            </main>

            <Footer />
        </div>
    );
};

export default MainPage;
