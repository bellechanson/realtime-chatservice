import axios from 'axios';

// 🌐 서버 주소 설정
export const BASE_URL = 'http://localhost:8787';         // 채팅 백엔드 주소
export const WS_URL = `${BASE_URL}/ws`;                  // WebSocket(STOMP) 연결 엔드포인트

// 🛠️ Axios 인스턴스: withCredentials=true 로 세션 쿠키 자동 포함
const api = axios.create({
    baseURL: BASE_URL,
    withCredentials: true, // 세션 기반 인증을 위해 쿠키 포함
});

export default api;

// 📦 채팅방 목록 불러오기 API (GET /api/chat/room)
export const fetchChatRooms = async () => {
    return api.get('/api/chat/room');
};
