# 📺 VIDCentral

## 프로젝트 개요

VIDCentral은 사용자가 비디오 콘텐츠를 업로드하고 공유하며 소통할 수 있는 사용자 맞춤형 비디오 추천 및 공유 플랫폼입니다.

이 프로젝트는 비디오 플랫폼의 핵심 기능을 구현하는 것을 넘어, 최적화된 아키텍처와 데이터 처리를 통해 빠르고 안정적인 사용자 경험을 제공하는 데 중점을 두었습니다.

## 개발 환경

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white" alt="Java"> 
<img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot"> 
<img src="https://img.shields.io/badge/JPA-007396?style=for-the-badge&logo=java&logoColor=white" alt="JPA"> 
<img src="https://img.shields.io/badge/querydsl-3B3D3B?style=for-the-badge&logo=querydsl&logoColor=white" alt="QueryDSL"> 
<img src="https://img.shields.io/badge/h2-4CAF50?style=for-the-badge&logo=h2&logoColor=white" alt="H2">
<br>
<img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"> 
<img src="https://img.shields.io/badge/intellij-000000?style=for-the-badge&logo=intellij-idea&logoColor=white" alt="IntelliJ IDEA"> 
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5"> 
<img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis">

## 구현 설명

1. 성능 최적화 및 안정성
   - **N+1 문제 해결**: QueryDSL을 사용하여 DTO로 직접 Projection하고, JOIN을 활용함으로써 불필요한 쿼리 발생 비용을 차단했습니다.
   - **데이터 캐싱**: Redis를 활용하여 자주 조회되는 비디오 및 시청 기록 데이터를 캐싱하여 데이터베이스 접근을 최소화하고 응답 속도를 향상시켰습니다.
   - **클린 아키텍처**: 각 레이어의 역할을 명확히 분리하고, 비즈니스 로직과 데이터 접근 로직의 결합도를 낮춰 유지보수성을 확보했습니다.

2. 비디오 추천 알고리즘
    - 사용자의 최근 시청 기록을 분석하여 선호 카테고리 및 태그를 추출합니다.
    - @ElementCollection 필드를 QueryDSL 쿼리에 포함하여 개인화된 맞춤형 비디오 목록을 제공합니다.

3. 사용자 및 콘텐츠 관리
   - **사용자 관리**: Spring Security와 JWT를 활용하여 회원가입, 로그인, 사용자 정보 수정 기능을 구현했습니다.
   - **비디오 관리**: 비디오 업로드, 수정, 삭제는 물론 좋아요 및 싫어요, 댓글 기능을 통해 사용자 상호작용을 활성화했습니다.
   - **API 문서화**: Swagger OpenAPI를 적용하여 모든 API 엔드포인트에 대한 명확한 문서를 제공합니다.

## 요구 사항

- **회원 관리**
    - [X] 사용자 회원가입 및 로그인

- **비디오 관리**
    - [X] 비디오 업로드, 수정, 삭제
    - [X] 비디오 목록/검색어 조회 및 페이징 처리
    - [X] 조회수 및 추천/비추천 기능

- **댓글 관리**
    - [X] 비디오 댓글 업로드, 수정, 삭제
    - [X] 좋아요 및 싫어요 기능

- **추천 알고리즘**
    - [X] 사용자 시청 기록 기반 개인 맞춤 비디오 추천
