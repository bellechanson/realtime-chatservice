# 📘 RealtimeChatService 통합 문서



## 💬 1. 백엔드 (RealtimeChatService)

# 💬 RealtimeChatService - 채팅 백엔드

**Spring Boot 기반의 실시간 채팅 백엔드 서비스입니다.**
STOMP 기반 WebSocket 통신을 통해 채팅 메시지를 주고받고, 채팅방 생성, 메시지 저장/조회 등의 기능을 제공합니다.

---

## 🛠 기술 스택

| 분류 | 사용 기술 |
|----|-----------|
| Language | Java 17|
| Framework | Spring Boot 3.5.0 |
| WebSocket | STOMP, SockJS |
| 메시지 브로커 | RabbitMQ 3 (AMQP) |
| DB | MySQL 8 (JPA/Hibernate) |
| Build Tool | Gradle |
| 외부 호출 | RestTemplate (UserService 연동) |

---

## 📦 주요 기능

### 1. 채팅 메시지 처리
- WebSocket 경로 /ws 연결 → 클라이언트는 /app/chat/room/{roomId}로 메시지 전송
- 수신한 메시지는 DB 저장 후 /topic/chat/room/{roomId} 구독자에게 브로드캐스트
- 메시지는 RabbitMQ로 비동기 전달되며, Consumer가 MQ로부터 메시지를 수신해 DB에 저장 및 구독자에게 브로드캐스트

### 2. 채팅방 생성 및 목록 조회
- POST /api/chat/room : 채팅방 생성 (roomName, creator 포함)
- GET /api/chat/room : 전체 채팅방 목록 조회

### 3. 채팅 메시지 REST API
- GET /api/chat/room/{roomId}/messages : 특정 채팅방 메시지 조회
- POST /api/chat/room/{roomId}/messages : 메시지 저장
- DELETE /api/chat/messages/{messageId} : 메시지 삭제

### 4. 사용자 닉네임 조회 (외부 연동)
- UserService 호출로 사용자 이메일 기반 닉네임 조회
  → GET http://localhost:8123/api/users/nickname?email=...

### 5. 비동기 메시지 처리 (RabbitMQ)
- WebSocket으로 수신된 채팅 메시지를 MQ에 전달
- Consumer가 메시지를 MQ에서 꺼내 DB에 저장하고 WebSocket으로 다시 전달
- 실시간 처리와 저장 로직 분리를 통해 확장성과 유연성 확보


---

## 🗂️ 패키지 구조
```
com.example.realtimechatservice
├── config              # WebSocket, CORS 설정
├── controller          # WebSocket & REST API 컨트롤러
├── dto                 # 채팅 메시지 DTO
├── entity              # JPA 엔티티 (ChatRoom, ChatMessage)
├── repository          # Spring Data JPA 레포지토리
└── service             # 외부 서비스 연동 (UserNicknameService)

```

---

## 🔧 실행 방법

### 1. MySQL 실행
```
docker run -d -p 3306:3306 --name chat-mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=chat mysql:8
```

### 2. RabbitMQ 실행 (Docker)
```
docker run -d --hostname rabbitmq --name rabbitmq \
 -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

### 3. application.properties 설정
```
spring.application.name=RealtimeChatService
server.port=8787

# MySQL DB
spring.datasource.url=jdbc:mysql://localhost:3306/chatdb?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=1234

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### 4. Gradle 빌드 & 실행
---

## ✅ 테스트 확인 (Postman)
- 채팅방 생성: POST /api/chat/room
- 메시지 전송: WebSocket 연결 후 STOMP 전송
- 메시지 조회: GET /api/chat/room/{roomId}/messages
- 닉네임 확인: GET /api/users/nickname?email=... (UserService 필요)

---

## 📌 주의 사항
- 현재는 닉네임 서비스가 별도 UserService로 구성되어 있어 해당 포트가 열려 있어야 합니다.
- RestTemplate와 System.out.println은 개발용


## 💻 2. 프론트엔드 (chat-front)

# 🖥️ Frontend - RealtimeChatService

**React + Vite 기반의 SPA 구조로, WebSocket 실시간 채팅 기능 및 사용자 인증, 채팅방 생성/입장 등 주요 UI 기능을 제공합니다.**

---

## 📁 프로젝트 구조
```
src/
├── api/
│   └── ChatApi.jsx             # 채팅 관련 axios API 함수
│   └── UserApi.jsx             #  유저 인증/회원가입 API 함수
├── components/
│   ├── chat/
│   │   ├── ChatRoom.jsx        # 실시간 채팅방 컴포넌트 (STOMP + SockJS)
│   │   └── ChatRoomCreate.jsx  # 채팅방 생성 폼
│   └── layout/
│       ├── Header.jsx          # 상단 고정 네비게이션 바
│       └── Footer.jsx          # 하단 고정 푸터
├── pages/
│   ├── chat/
│   │   └── ChatRoom.jsx            # 채팅방 진입 라우트
│   ├── user/
│   │   ├── LoginPage.jsx           # 이메일 기반 로그인
│   │   └── SignupPage.jsx          # 인증코드 기반 회원가입
│   └── MainPage.jsx                # 채팅방 목록 및 생성 진입점  
├── style/
│   ├── chat/
│   │   └── ChatRoom.css            # 채팅창 스타일
│   ├── layout/
│   │   ├── Footer.css              # 푸터 스타일
│   │   └── Header.css              # 헤더 스타일
│   └── MainPage.css                # 메인페이지 전용 스타일
└── index.css                   # 글로벌 CSS
```

---

## ⚙️ 핵심 기능

### ✅ 채팅방 기능
- / : 메인 페이지 (채팅방 목록)
- /chat/create : 채팅방 생성
- /chat/room/:roomId : 실시간 채팅방
- ChatRoom.jsx에서 STOMP over SockJS를 통해 실시간 채팅 구현
- 사용자 이메일과 이름을 기준으로 메시지 분기 및 스크롤 관리

### ✅ 사용자 인증
- 회원가입: 이메일 인증 기반 2단계 절차 (인증번호 전송 → 인증번호 확인 → 회원가입)
- 로그인: 이메일 + 비밀번호 (로그인 시 localStorage에 사용자 정보(uEmail, uName, uRole) 저장)

### ✅ 상태 관리
- 로그인 상태와 사용자 정보는 localStorage 기반
- Header.jsx에서 로그인 상태에 따라 버튼 출력

---

## 🎨 UI/UX 특징
- Header 및 Footer는 고정 위치 (position: fixed)
- 채팅방 목록은 grid 기반 카드 레이아웃 (5열, 반응형 확장 가능)
- 버튼 hover 효과, 카드 hover 상승 애니메이션 등 인터랙션 포함

---

## 📦 사용된 주요 라이브러리
| 라이브러리 | 설명 |
|----|-----------|
| React | 프론트엔드 프레임워크|
| React Router DOM | SPA 라우팅 |
| axios | API 요청 처리 |
| SockJS | WebSocket 연결용 라이브러리 |
| @stomp/stompjs | STOMP 프로토콜 구현 라이브러리 |


## 🙍‍♂️ 3. 유저 서비스 (UserService)

# 👤 UserService – 유저 관리 백엔드

**이 서비스는 회원가입, 로그인, 이메일 인증, JWT 기반 인증 관리를 담당합니다.**
채팅 서비스 등 외부 서비스에서 사용자 정보를 요청할 수 있도록 API를 제공합니다.

---

## 🛠 기술 스택

| 분류 | 사용 기술 |
|----|-----------|
| Language | Java 17|
| Framework | Spring Boot 3.5.0 |
| DB | MySQL 8 (JPA/Hibernate) |
| Build Tool | Gradle |
| 인증 |JWT (Json Web Token) |
| 이메일 발송 | Spring Mail (Gmail SMTP 사용) |
| 보안 | BCrypt 비밀번호 암호화 |
| 인증코드 저장| Redis (TTL 기반 인증코드 관리)    |
| 유효성 검증 | Jakarta Bean Validation |

---

## 📦 주요 기능

### 1. 회원가입 + 이메일 인증 ( Redis 기반 인증코드 처리)
- `POST /api/users/send-code` : 이메일로 인증번호 전송  
  → 인증코드는 Redis에 5분간 TTL로 저장됨
- `POST /api/users/verify-code` : 인증번호 확인  
  → Redis에서 인증코드 조회 및 검증 처리
- `POST /api/users/signup` : 인증번호가 일치할 경우 회원가입 진행  
  → 인증이 완료된 사용자만 회원가입 허용

> 인증코드는 Redis에 `verify:email@example.com` 형식으로 저장되며, TTL 300초 후 자동 삭제됩니다.


### 2. 로그인 & JWT 발급
- POST /api/users/login : 이메일, 비밀번호 검증 후 JWT 발급
- GET /api/users/me : JWT 기반 사용자 정보 (email, nickname) 반환
- GET /api/users/nickname?email=... : 이메일 기본 단간 닉네임 조회 (MSA 연동용)

---

## 🗂️ 패키지 구조
```
com.example.userservice
├── config                # CORS, Redis 설정
├── controller            # REST API 컨트롤러
├── dto                   # 요청/응답 DTO
├── entity                # JPA 엔티티
├── filter                # JWT 인증 필터 (요청 시 토큰 검증 및 사용자 정보 주입)
├── repository            # Spring Data JPA 레포지토리
├── service               # 비즈니스 로직 서비스
└── util                  # JWT 등 보안 기능

```

---

## 🔧 실행 방법

### 1. MySQL 실행
```
docker run -d -p 3306:3306 --name chat-mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=chat mysql:8
```

### 2. Redis 실행
```
docker run -d --name redis -p 6379:6379 redis
```

### 3. application.properties 설정
```
spring.application.name=UserService
server.port=8123

# MySQL DB
spring.datasource.url=jdbc:mysql://localhost:3306/userdb?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=1234

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Gmail SMTP
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=sinla9302@gmail.com
spring.mail.password=앱비밀번호
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

```

### 3. Gradle 빌드 & 실행

---

## ✅  테스트 시나리오 (Postman 또는 프론트)

### 1. 회원가입 흐름 (Redis 기반 인증)
- /send-code → 이메일로 인증번호 받기
- /verify-code → Redis에 저장된 인증번호 검증
- /signup → 인증 완료된 사용자만 회원가입 가능


### 2. 로그인 + JWT
- /login → JWT 발급 확인 (응답 토큰은 localStorage에 저장)
- /me → JWT 포함 요청 시 사용자 정보 반환
- /nickname?email=... → 닉네임 단건 조회

---

## 📌 유의사항
- 이메일 인증은 Gmail 계정과 앱 비밀번호 필요
- 인증코드는 Redis에 TTL로 저장되며, 5분 후 자동 삭제됩니다
- JWT는 Authorization 헤더의 Bearer 토큰 형식으로 전달되어야 합니다
- 비밀번호는 BCrypt로 암호화하여 저장됩니다
- /api/users/info API는 /me로 통합되어 더 이상 사용되지 않습니다

---
