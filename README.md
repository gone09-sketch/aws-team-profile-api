# AWS Team Profile API

## 1. 프로젝트 소개

AWS Team Profile API는 Spring Boot로 구현한 팀원 프로필 API를 AWS 클라우드 환경에 배포하는 프로젝트입니다.

이 프로젝트는 팀원 정보 등록·조회와 프로필 이미지 업로드 기능을 구현하고, EC2, RDS, S3, IAM Role, Parameter Store를 활용해 실제 운영 환경에 가까운 백엔드 배포 구조를 구성하는 것을 목표로 합니다.

핵심은 단순한 CRUD API 구현이 아니라, 애플리케이션 서버가 상태를 직접 들고 있지 않도록 서버, 데이터베이스, 파일 저장소, 비밀정보, 권한을 각각 분리한 Stateless 아키텍처를 구성하는 것입니다.

이를 통해 로컬에서 동작하는 Spring Boot 애플리케이션을 AWS 클라우드 환경에서 운영 가능한 백엔드 서비스 구조로 확장하는 과정을 학습합니다.

---

## 2. 프로젝트 목표

이 프로젝트의 핵심 목표는 Spring Boot 애플리케이션을 AWS 환경에 배포하고, 애플리케이션 서버가 상태를 직접 저장하지 않는 Stateless 아키텍처를 구성하는 것입니다.

구체적인 목표는 다음과 같습니다.

- 팀원 정보를 등록하고 조회하는 REST API를 구현합니다.
- 로컬 개발 환경에서는 빠른 테스트를 위해 H2 Database를 사용합니다.
- AWS 운영 환경에서는 Amazon RDS 서비스에서 생성한 MySQL 데이터베이스를 사용합니다.
- 이를 위해 Spring Profile을 `local`과 `prod`로 분리합니다.
- 프로필 이미지는 EC2 서버 로컬 디스크가 아닌 AWS S3에 저장합니다.
- S3 버킷은 Public Access를 차단하고, Presigned URL을 통해 제한된 시간 동안만 이미지에 접근할 수 있도록 합니다.
- DB 접속 정보와 확인용 설정값은 AWS Parameter Store에 저장하여 민감 정보가 코드에 노출되지 않도록 합니다.
- EC2에는 S3 접근 권한을 가진 IAM Role을 연결하여 Access Key를 코드나 설정 파일에 직접 저장하지 않습니다.
- Actuator를 통해 애플리케이션의 상태를 확인할 수 있도록 구성합니다.
- API 요청과 예외 상황을 로그로 기록하여 운영 중 문제를 확인할 수 있도록 합니다.

---

## 3. 핵심 아키텍처 방향

이 프로젝트는 애플리케이션 서버가 중요한 데이터를 직접 저장하지 않도록 구성합니다.

Spring Boot 서버는 API 요청을 처리하는 역할을 담당하고, 데이터와 파일, 설정 정보는 각각의 AWS 서비스에 분리하여 저장합니다.

```text
사용자
  ↓
EC2 - Spring Boot Application
  ↓
Amazon RDS for MySQL - 팀원 정보 저장

EC2 - Spring Boot Application
  ↓
S3 - 프로필 이미지 저장

EC2 - Spring Boot Application
  ↓
Parameter Store - DB 접속 정보 및 설정값 저장
```

### 각 구성 요소의 역할

| 구성 요소 | 역할 |
|---|---|
| Spring Boot | 팀원 정보 등록/조회 및 프로필 이미지 API 제공 |
| EC2 | Spring Boot 애플리케이션 실행 서버 |
| Amazon RDS for MySQL | 팀원 정보 저장 |
| S3 | 프로필 이미지 파일 저장 |
| Parameter Store | DB 접속 정보, team-name 등 설정값 저장 |
| IAM Role | EC2가 S3에 접근할 수 있는 권한 부여 |
| Security Group | EC2와 RDS 간 접근 제어 |
| Actuator | 애플리케이션 상태 확인 |

---

## 4. 주요 기능

현재 구현 목표로 하는 주요 기능은 다음과 같습니다.

| 기능 | 설명 |
|---|---|
| 팀원 등록 | 이름, 나이, MBTI를 입력받아 팀원 정보를 저장합니다. |
| 팀원 조회 | 저장된 팀원 정보를 ID 기준으로 조회합니다. |
| 프로필 이미지 업로드 | MultipartFile로 이미지를 받아 S3에 업로드합니다. |
| 프로필 이미지 조회 | Presigned URL을 발급하여 제한된 시간 동안 이미지를 조회할 수 있도록 합니다. |
| 헬스 체크 | Actuator를 통해 애플리케이션 상태를 확인합니다. |
| 운영 설정 분리 | `local` / `prod` Profile을 분리하여 로컬과 운영 환경을 구분합니다. |
| 로그 기록 | API 요청과 예외 상황을 로그로 기록합니다. |

---

## 5. API 명세

### 팀원 등록

```http
POST /api/members
```

#### Request Body

```json
{
  "name": "지원",
  "age": 25,
  "mbti": "INTJ"
}
```

#### Response Body

```json
{
  "id": 1,
  "name": "지원",
  "age": 25,
  "mbti": "INTJ",
  "profileImageKey": null
}
```

---

### 팀원 조회

```http
GET /api/members/{id}
```

#### Response Body

```json
{
  "id": 1,
  "name": "지원",
  "age": 25,
  "mbti": "INTJ",
  "profileImageKey": "members/1/profile-image/example.png"
}
```

---

### 프로필 이미지 업로드

```http
POST /api/members/{id}/profile-image
```

#### Request

```text
Content-Type: multipart/form-data
file: 이미지 파일
```

#### Response Body

```json
{
  "memberId": 1,
  "profileImageKey": "members/1/profile-image/example.png"
}
```

---

### 프로필 이미지 조회 URL 발급

```http
GET /api/members/{id}/profile-image
```

#### Response Body

```json
{
  "memberId": 1,
  "presignedUrl": "https://example-presigned-url",
  "expiresAt": "2026-05-27T14:00:00"
}
```

---

### 애플리케이션 상태 확인

```http
GET /actuator/health
```

#### Response Body

```json
{
  "status": "UP"
}
```

---

### 애플리케이션 정보 확인

```http
GET /actuator/info
```

#### Response Body

```json
{
  "teamName": "example-team"
}
```

---

## 6. 도메인 설계

### Member

| 필드명 | 타입 | 설명 |
|---|---|---|
| id | Long | 팀원 ID |
| name | String | 팀원 이름 |
| age | Integer | 팀원 나이 |
| mbti | String | 팀원 MBTI |
| profileImageKey | String | S3에 저장된 프로필 이미지 객체 Key |
| createdAt | LocalDateTime | 생성 시간 |
| updatedAt | LocalDateTime | 수정 시간 |

프로필 이미지는 DB에 직접 저장하지 않고, S3에 저장한 뒤 DB에는 S3 객체 Key만 저장합니다.

예시:

```text
members/1/profile-image/uuid-profile.png
```

---

## 7. 환경 분리

이 프로젝트는 실행 환경에 따라 Spring Profile을 `local`과 `prod`로 분리합니다.

| Profile | 실행 환경 | Database |
|---|---|---|
| local | 로컬 개발 환경 | H2 Database |
| prod | AWS 운영 환경 | Amazon RDS for MySQL |

### local 환경

로컬 개발 환경에서는 빠른 기능 구현과 테스트를 위해 H2 Database를 사용합니다.

```text
IntelliJ
  ↓
Spring Boot
  ↓
H2 Database
```

### prod 환경

AWS 운영 환경에서는 EC2에서 Spring Boot 애플리케이션을 실행하고, 데이터는 Amazon RDS에서 생성한 MySQL 데이터베이스에 저장합니다.

```text
EC2 - Spring Boot Application
  ↓
Amazon RDS for MySQL
```

DB 접속 정보는 코드에 직접 작성하지 않고, AWS Parameter Store를 통해 관리합니다.

---

## 8. AWS 인프라 구성 계획

| AWS 리소스 | 사용 목적 |
|---|---|
| AWS Budgets | 월 예산 설정 및 비용 알림 |
| VPC | 네트워크 환경 구성 |
| Public Subnet | EC2 배치 |
| EC2 | Spring Boot 애플리케이션 실행 |
| Amazon RDS for MySQL | 팀원 정보 저장 |
| S3 | 프로필 이미지 저장 |
| IAM Role | EC2의 S3 접근 권한 부여 |
| Parameter Store | DB 접속 정보 및 설정값 저장 |
| Security Group | EC2, RDS 접근 제어 |

---

## 9. 보안 설계 방향

이 프로젝트에서는 과제 요구사항에 따라 다음 보안 원칙을 적용합니다.

- RDS Security Group의 Inbound Source는 전체 IP 대역이 아닌 EC2 Security Group ID로 제한합니다.
- DB 접속 정보(`url`, `username`, `password`)와 확인용 파라미터는 AWS Parameter Store에 저장합니다.
- 애플리케이션은 Spring Boot 실행 시 Parameter Store 값을 주입받아 RDS에 연결합니다.
- S3 버킷은 모든 퍼블릭 액세스를 차단한 상태로 생성합니다.
- 프로필 이미지는 EC2 서버 로컬 디스크가 아닌 S3 버킷에 저장합니다.
- AWS Access Key는 코드나 설정 파일에 직접 저장하지 않습니다.
- EC2에는 S3 접근 권한을 가진 IAM Role을 연결하여 S3에 접근합니다.
- 프로필 이미지는 Presigned URL을 통해서만 다운로드할 수 있도록 합니다.
- Presigned URL의 유효기간은 7일로 설정합니다.

---

## 10. 제출 증빙 자료

과제 제출 시 README에 아래 항목을 추가할 예정입니다.

| 단계 | 제출 자료 |
|---|---|
| LV 0 | AWS Budget 설정 화면 캡처 |
| LV 1 | EC2 Public IP, `/actuator/health` 확인 결과 |
| LV 2 | `/actuator/info` URL, RDS Security Group 인바운드 규칙 캡처 |
| LV 3 | Presigned URL 1개, 만료 시간 또는 접근 성공 스크린샷 |
| LV 4 | GitHub Actions 성공 화면, `docker ps` 실행 화면 |
| LV 5 | HTTPS 도메인 URL, Target Group Healthy 화면 |
| LV 6 | CloudFront 이미지 URL |

---

## 11. 트러블슈팅

구현 과정에서 발생한 문제와 해결 과정을 정리합니다.

| 문제 상황 | 원인 | 해결 방법 |
|---|---|---|
| 예시: EC2에서 애플리케이션 실행 실패 | Java 버전 불일치 | EC2에 프로젝트와 맞는 Java 버전 설치 |
| 예시: RDS 연결 실패 | Security Group 설정 오류 | RDS Inbound에 EC2 Security Group 허용 |
| 예시: S3 업로드 실패 | IAM 권한 부족 | EC2에 S3 접근 권한이 있는 IAM Role 연결 |

---

## 12. 회고

이번 프로젝트를 통해 Spring Boot 애플리케이션을 단순히 로컬에서 실행하는 것을 넘어, AWS 환경에서 운영 가능한 구조로 배포하는 흐름을 학습합니다.

특히 서버, 데이터베이스, 파일 저장소, 비밀정보, 권한을 분리하는 과정을 통해 Stateless 아키텍처의 필요성을 이해하고, 실제 백엔드 서비스 운영에 필요한 기본 인프라 구성을 경험하는 것을 목표로 합니다.