import React, { useEffect, useState, useRef } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import api, { WS_URL } from "../../api/ChatApi.jsx";

const ChatRoom = ({ roomId, userEmail, userName }) => {
    // 채팅 메시지 리스트 상태
    const [messages, setMessages] = useState([]);
    // 입력 중인 메시지 내용 상태
    const [content, setContent] = useState('');
    // 새 메시지 알림 표시 여부 상태
    const [showNewMessageNotice, setShowNewMessageNotice] = useState(false);

    // STOMP 클라이언트 저장용 Ref
    const stompRef = useRef(null);
    // 채팅 메시지 컨테이너 DOM Ref (스크롤 조작용)
    const chatMessagesRef = useRef(null);
    // 본인 메시지 여부 플래그 (자동 스크롤 트리거)
    const isSelfMessageRef = useRef(false);
    // 최신 userEmail/userName 값을 유지하기 위한 Ref
    const userEmailRef = useRef(userEmail);
    const userNameRef = useRef(userName);

    // props가 변경되었을 때 Ref에 최신값 업데이트
    useEffect(() => {
        userEmailRef.current = userEmail;
        userNameRef.current = userName;
    }, [userEmail, userName]);

    // 메시지 영역을 가장 아래로 스크롤
    const scrollToBottom = () => {
        const el = chatMessagesRef.current;
        if (el) {
            el.scrollTop = el.scrollHeight;
            setShowNewMessageNotice(false);
        }
    };

    // 스크롤이 하단 근처인지 판단
    const isNearBottom = () => {
        const el = chatMessagesRef.current;
        return el ? el.scrollHeight - el.scrollTop - el.clientHeight < 50 : false;
    };

    // 컴포넌트 마운트 시 메시지 로딩 + WebSocket 연결
    useEffect(() => {
        if (!roomId) return;

        // 기존 메시지 불러오기
        api.get(`/api/chat/room/${roomId}/messages`)
            .then((res) => {
                setMessages(res.data);
                setTimeout(() => scrollToBottom(), 0); // 바로 스크롤 하단으로
            });

        // STOMP 클라이언트 설정
        const socket = new SockJS(WS_URL);
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                // 채팅방 주제 구독
                stompClient.subscribe(`/topic/chat/room/${roomId}`, (msg) => {
                    const body = JSON.parse(msg.body);
                    setMessages((prev) => [...prev, body]);

                    if (body.userEmail !== userEmailRef.current) {
                        // 다른 사람 메시지일 경우 하단 아니면 알림
                        if (!isNearBottom()) setShowNewMessageNotice(true);
                    } else {
                        // 본인 메시지일 경우 자동 스크롤
                        isSelfMessageRef.current = true;
                        scrollToBottom();
                    }
                });
            },
        });

        stompClient.activate(); // 연결 시작
        stompRef.current = stompClient;

        return () => stompClient.deactivate(); // 컴포넌트 언마운트 시 연결 해제
    }, [roomId]);

    // 본인 메시지가 추가되었을 경우 자동 스크롤 트리거
    useEffect(() => {
        if (isSelfMessageRef.current) {
            scrollToBottom();
            isSelfMessageRef.current = false;
        }
    }, [messages]);

    // 메시지 전송 처리
    const handleSend = () => {
        if (!content.trim() || !stompRef.current?.connected) return;

        const dto = { roomId, userEmail, content };

        stompRef.current.publish({
            destination: `/app/chat/room/${roomId}`,
            body: JSON.stringify(dto),
        });

        isSelfMessageRef.current = true;
        setContent('');
    };

    // 날짜 및 시간 포맷 함수 (카카오톡 스타일)
    const formatKakaoTime = (localDateTimeStr) => {
        if (!localDateTimeStr) return '';
        const trimmedStr = localDateTimeStr.split('.')[0];
        const date = new Date(trimmedStr);
        if (isNaN(date)) return '시간 오류';

        const now = new Date();
        const isToday = date.toDateString() === now.toDateString();

        const days = ['일', '월', '화', '수', '목', '금', '토'];
        const hour = date.getHours();
        const minute = date.getMinutes().toString().padStart(2, '0');
        const ampm = hour < 12 ? '오전' : '오후';
        const displayHour = hour % 12 === 0 ? 12 : hour % 12;

        const timeStr = `${ampm} ${displayHour}:${minute}`;
        return isToday
            ? timeStr
            : <>
                {`${date.getFullYear()}년 ${date.getMonth() + 1}월 ${date.getDate()}일 (${days[date.getDay()]})`}
                <br />
                {timeStr}
            </>;
    };

    // 사용자 이름을 기반으로 색상 생성
    const getColorFromName = (name) => {
        const colors = ['#8884d8', '#82ca9d', '#ffc658', '#ff7f50', '#a9a9a9', '#00bcd4', '#ff69b4', '#4caf50'];
        let hash = 0;
        for (let i = 0; i < name.length; i++) {
            hash = name.charCodeAt(i) + ((hash << 5) - hash);
        }
        return colors[Math.abs(hash) % colors.length];
    };

    // 아바타 렌더링 컴포넌트
    const Avatar = ({ name }) => {
        const initial = name?.charAt(0) || '?';
        const bgColor = getColorFromName(name);
        return (
            <div style={{
                backgroundColor: bgColor,
                color: 'white',
                borderRadius: '50%',
                width: 32,
                height: 32,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                fontWeight: 'bold',
                fontSize: 16,
                marginRight: 8
            }}>
                {initial}
            </div>
        );
    };

    return (
        <div className="chat-container">
            <div className="chat-messages" ref={chatMessagesRef}>
                {messages.map((msg, i) => {
                    const isMe = msg.userEmail?.toLowerCase() === userEmail?.toLowerCase();
                    const isSameUserAsPrevious = i > 0 && messages[i - 1].userEmail === msg.userEmail;
                    const uniqueKey = msg.id || `${msg.userEmail}-${msg.createdAt}-${i}`;

                    return (
                        <div key={uniqueKey} className={`chat-message ${isMe ? 'me' : 'other'}`}>
                            <div className={`chat-message ${isMe ? 'me' : 'other'}`}>
                                {/* 아바타와 유저명은 이전 메시지와 같은 사람이 아니면 표시 */}
                                {!isMe && !isSameUserAsPrevious && (
                                    <div className="chat-profile">
                                        <Avatar name={msg.userName} />
                                        <strong className="chat-username">{msg.userName}</strong>
                                    </div>
                                )}
                                <div className="bubble-row">
                                    <div className="chat-bubble">{msg.content}</div>
                                    <div className="chat-time">{formatKakaoTime(msg.createdAt)}</div>
                                </div>
                            </div>
                        </div>
                    );
                })}
                {/* 새로운 메시지 배지가 필요한 경우 */}
                {showNewMessageNotice && (
                    <div className="new-message-notice" onClick={scrollToBottom}>
                        새로운 메시지
                    </div>
                )}
            </div>

            <div className="chat-input-area">
                <input
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                    placeholder="메시지를 입력하세요"
                    className="chat-input"
                />
                <button onClick={handleSend} className="chat-button">보내기</button>
            </div>
        </div>
    );
};

export default ChatRoom;
