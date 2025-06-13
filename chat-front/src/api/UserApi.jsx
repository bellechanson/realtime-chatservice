import axios from 'axios';

// 🌐 사용자 서비스 API 기본 주소 (백엔드 URL)
const BASE_URL = 'http://localhost:8123/api/users';

/**
 * ✅ 로그인 요청
 * - email, password를 전송하여 JWT 토큰을 응답받습니다.
 * - 응답 받은 토큰을 localStorage에 저장합니다.
 *
 * @param {string} email 사용자 이메일
 * @param {string} password 사용자 비밀번호
 * @returns Axios 응답 객체
 */
export const loginUser = async (email, password) => {
    const res = await axios.post(`${BASE_URL}/login`, { email, password });
    const token = res.data.token;

    // 🔐 로컬 스토리지에 토큰 저장
    localStorage.setItem("token", token);

    return res;
};


/**
 * ✅ 회원가입 요청
 * - 사용자 정보를 전송하여 회원가입을 처리합니다.
 *
 * @param {Object} data 사용자 가입 정보 (email, password 등)
 * @returns Axios 응답 객체
 */
export const signupUser = async (data) => {
    return axios.post(`${BASE_URL}/signup`, data);
};


/**
 * ✅ 현재 로그인한 사용자 정보 조회
 * - 로컬스토리지에 저장된 토큰을 Authorization 헤더로 첨부합니다.
 * - 백엔드는 해당 토큰을 검증하여 사용자 정보를 반환합니다.
 *
 * @returns Axios 응답 객체 (email, nickname 등 포함)
 */
export const getUserInfo = async () => {
    const token = localStorage.getItem("token");

    return axios.get(`${BASE_URL}/me`, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });
};


/**
 * ✅ 이메일 인증코드 전송
 * - 사용자의 이메일로 인증 코드를 전송합니다.
 *
 * @param {string} email 사용자 이메일
 * @returns Axios 응답 객체
 */
export const sendVerificationCode = async (email) => {
    return axios.post(`${BASE_URL}/send-code`, { email });
};


/**
 * ✅ 인증코드 검증
 * - 입력한 인증코드가 서버에 저장된 코드와 일치하는지 확인합니다.
 *
 * @param {string} email 사용자 이메일
 * @param {string} code 인증 코드
 * @returns Axios 응답 객체
 */
export const verifyCode = async (email, code) => {
    return axios.post(`${BASE_URL}/verify-code`, { email, code });
};
