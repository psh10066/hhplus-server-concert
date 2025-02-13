# 콘서트 예약 서비스

## 프로젝트 문서

### 1. [마일스톤](docs/milestone.md)

### 2. [플로우차트](docs/flowchart.md)

### 3. [시퀀스 다이어그램](docs/sequence-diagram.md)

### 4. [ERD](docs/erd.md)

### 5. [API 명세](https://psh10066.github.io/hhplus-server-concert/api-spec.html)

## [4주차] 주요 비즈니스 로직 개발

### 1. [Swagger](docs/swagger.md)

### 2. [프로젝트 및 테스트 코드 설명](docs/project-description.md)

## [5주차] 기능 완성

### 1. [Filter & Interceptor](docs/filter-interceptor.md)

### 2. [동시성 제어 방식 분석 보고서](docs/concurrency-control.md)

## [6주차] 동시성 처리

### 1. [동시성 제어 방식 비교 및 적용 사례 분석](docs/concurrency-control-v2.md)

## [7주차] 캐시 도입 및 Redis 기반 대기열 구현

### 1. [캐시 도입 보고서](docs/caching.md)

### 2. [캐시 전략 패턴 비교 분석](docs/caching-pattern.md)

### 3. [대기열 관리 설계 변경 보고서](docs/redis-queue.md)

## [8주차] DB 인덱스 및 MSA와 트랜잭션

### 1. [데이터베이스 인덱스 기반 성능 개선 보고서](docs/db-index.md)

### 2. [MSA 전환에 따른 트랜잭션 처리 방안](docs/msa.md)

## Getting Started

### Prerequisites

#### Running Docker Containers

`local` profile 로 실행하기 위하여 인프라가 설정되어 있는 Docker 컨테이너를 실행해주셔야 합니다.

```bash
docker-compose up -d
```

### DB 스키마 업데이트

DB 구조가 변경된 이후 DB 스키마가 업데이트되지 않은 경우 아래 절차에 맞춰 진행해 주세요.
1. 프로젝트 루트 디렉토리 내 data 폴더 삭제
2. `docker-compose down` 후 `docker-compose up -d` 실행