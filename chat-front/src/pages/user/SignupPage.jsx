import React, { useState } from 'react';
import {
    signupUser,              // 회원가입 요청 API
    sendVerificationCode,    // 인증번호 이메일 발송 API
    verifyCode,              // 인증번호 검증 API
} from '../../api/UserApi.jsx';
import { useNavigate } from 'react-router-dom';

function SignupPage() {
    const navigate = useNavigate();

    // 입력값 상태 관리
    const [email, setEmail] = useState('');
    const [code, setCode] = useState('');
    const [isVerified, setIsVerified] = useState(false); // 이메일 인증 여부

    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');

    // 1단계: 인증번호 전송
    const handleSendCode = async () => {
        try {
            await sendVerificationCode(email); // 이메일로 인증번호 전송
            alert('인증번호가 이메일로 전송되었습니다.');
        } catch (err) {
            alert('인증번호 전송 실패: ' + (err.response?.data || err.message));
        }
    };

    // 2단계: 인증번호 확인
    const handleVerifyCode = async () => {
        try {
            await verifyCode(email, code); // 입력한 코드와 이메일 확인
            alert('✅ 인증 성공!');
            setIsVerified(true);
        } catch (err) {
            alert('❌ 인증 실패: ' + (err.response?.data || err.message));
        }
    };

    // 3단계: 회원가입 요청
    const handleSignup = async () => {
        if (!isVerified) {
            alert('이메일 인증을 먼저 완료해주세요.');
            return;
        }

        try {
            await signupUser({
                email,
                nickname,
                password,
                verificationCode: code, // 백엔드에서 필요
            });

            alert('회원가입 성공!');
            navigate('/'); // 메인으로 이동
        } catch (err) {
            alert('회원가입 실패: ' + (err.response?.data || err.message));
        }
    };

    return (
        <form onSubmit={e => { e.preventDefault(); handleSignup(); }}>
            <h2>회원가입</h2>

            {/* 이메일 입력 */}
            <input
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="이메일"
            />
            <button type="button" onClick={handleSendCode}>인증번호 전송</button>

            {/* 인증번호 입력 */}
            <input
                value={code}
                onChange={e => setCode(e.target.value)}
                placeholder="인증번호 입력"
            />
            <button type="button" onClick={handleVerifyCode}>인증 확인</button>

            {/* 인증 성공 후 닉네임/비밀번호 입력 표시 */}
            {isVerified && (
                <>
                    <input
                        value={nickname}
                        onChange={e => setNickname(e.target.value)}
                        placeholder="닉네임"
                    />
                    <input
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        placeholder="비밀번호"
                        type="password"
                    />
                    <button type="submit">회원가입</button>
                </>
            )}
        </form>
    );
}

export default SignupPage;
