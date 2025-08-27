루틴로그 (프로토타입)

사용자의 루틴을 일관되게 기록하고, 동기부여/회고를 통해 성장을 돕는 웹 기반 루틴 기록 플랫폼
상태: 🚧 Prototype · 기능 점진 개발 중

🔗 라이브 데모

URL: https://www.routinelog.site/




📌 프로젝트 개요

목적
사용자의 루틴을 일관되게 기록하고, 동기부여와 회고를 통해 성장할 수 있도록 돕는 웹 기반 루틴 기록 플랫폼

핵심 컨셉

집에서는 아무것도 못하는 사람들을 위한 루틴 기록 서비스

환경이 바뀌면 생각/행동도 바뀐다

핵심 아이디어

위치 기반 루틴 체크: 특정 장소 근접 시 자동 알림/체크

🧩 주요 기능
- 루틴 등록/관리

루틴명, 카테고리, 주기(매일/주 N회) 설정

위치 기반 옵션 선택 가능

- 루틴 수행 체크

수동 체크 / 위치 기반 자동 체크 / 반복 설정

🧭 위치 기반 루틴 체크

도서관·헬스장 등 등록 장소 도착 시 자동 팝업 알림

Geolocation API + 좌표 반경(예: 100m) 기반 인식

ℹ️ 브라우저 지오로케이션은 HTTPS + 사용자 동의가 필수이며, 일부 모바일 브라우저에서 백그라운드 동작에 제약이 있을 수 있습니다.


🛠 기술 스택

Frontend: React (CRA)   

Backend: Spring Boot (Java), REST API

DB: MySQL

Infra (AWS): CloudFront, S3(정적 호스팅), ALB, EC2, RDS(MySQL), ACM(SSL)

기타: Geolocation API

☁️ AWS 아키텍처

<img width="338" height="272" alt="image" src="https://github.com/user-attachments/assets/3a882fca-0c2d-4483-b711-026a3e9df855" />









참고

배포는 진행 중인 프로토타입 단계이며, 인프라/보안/모니터링은 점진적으로 보강됩니다.
