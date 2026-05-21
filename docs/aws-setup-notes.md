# AWS 설정 기록

## 1. VPC 설정 기록

- 리전: ap-northeast-2
- VPC 이름: member-profile-vpc
- VPC CIDR: 10.0.0.0/16
- Public Subnet:
    - member-profile-subnet-public1-ap-northeast-2a
    - member-profile-subnet-public2-ap-northeast-2b
- Private Subnet:
    - member-profile-subnet-private1-ap-northeast-2a
    - member-profile-subnet-private2-ap-northeast-2b
- Internet Gateway:
    - 이름: member-profile-igw
    - ID: igw-06b2699a79974f75
    - 상태: Attached
- Public Route Table:
    - 이름: member-profile-rtb-public
    - ID: rtb-00b0b3b4254b243dc
    - 연결된 서브넷: Public Subnet 2개
    - 라우팅:
        - 0.0.0.0/0 → igw-06b2699a79974f75
        - 10.0.0.0/16 → local
- Private Route Table:
    - member-profile-rtb-private1-ap-northeast-2a
    - member-profile-rtb-private2-ap-northeast-2b

### 설정 의도

- Public Subnet에는 외부 요청을 받을 EC2를 배치하고, Private Subnet은 이후 RDS 또는 확장 구조를 고려해 분리 
- Public Subnet과 Private Subnet을 서로 다른 AZ에 나누어 생성하여 이후 Multi-AZ 구조로 확장할 수 있도록 구성

### 확인 결과

- Internet Gateway `member-profile-igw`가 VPC에 Attached 상태로 연결되어 있음을 확인
- Public Route Table에 `0.0.0.0/0 → igw-06b2699a79974f75` 경로가 설정되어 있음을 확인
- Public Route Table이 Public Subnet 2개와 연결되어 있음을 확인
- Public Subnet과 Private Subnet이 `ap-northeast-2a`, `ap-northeast-2b`에 나누어 생성되어 있음을 확인

---

## 2. EC2 설정 기록

- EC2 이름: member-profile-ec2-01
- EC2 인스턴스 ID: i-0ebdc6f477e27a081
- AMI: Amazon Linux 2023 ARM64 AMI
- Instance Type: t4g.small
- Key Pair:
  - 이름: member-profile-ec2-key
  - `.pem` 파일은 프로젝트 폴더와 Git 저장소에 포함하지 않음
- 배치 Subnet:
  - member-profile-subnet-public1-ap-northeast-2a
  - Subnet ID: subnet-0303442d533205c7c
  - Availability Zone: ap-northeast-2a
- VPC:
  - member-profile-vpc
  - VPC ID: vpc-0b6668fdc215cc67b
- Public IP:
  - 13.124.148.129
- Public DNS:
  - ec2-13-124-148-129.ap-northeast-2.compute.amazonaws.com
- Private IP:
  - 10.0.3.244
- Private DNS:
  - ip-10-0-3-244.ap-northeast-2.compute.internal
- IAM Role:
  - 없음
- Security Group:
  - 이름: member-profile-ec2-sg
  - ID: sg-0571e21a513a010a3
  - 설명: Security group for member profile EC2 instance
- 허용한 Inbound Port:
  - SSH 22:
    - Protocol: TCP
    - Source: 0.0.0.0/0
    - 용도: EC2 SSH 접속
  - HTTP 80:
    - Protocol: TCP
    - Source: 0.0.0.0/0
    - 용도: 이후 HTTP 접근 또는 확장 구성을 고려해 허용
  - Application 8080:
    - Protocol: TCP
    - Source: 0.0.0.0/0
    - 용도: Spring Boot 애플리케이션 직접 접근 및 `/actuator/health` 확인
- Outbound Rule:
  - 모든 트래픽 허용
  - Destination: 0.0.0.0/0

### 확인 결과

- SSH 접속 확인:
  - `member-profile-ec2-key.pem` 키 파일을 사용해 EC2에 SSH 접속 성공
- Java 설치 확인:
  - Amazon Corretto 17 설치 확인
- Spring Boot 실행 확인:
  - `aws-member-profile-api-0.0.1-SNAPSHOT.jar` 파일을 EC2에 `app.jar`로 업로드 후 실행 확인
- `/actuator/health` 확인:
  - `http://13.124.148.129:8080/actuator/health` 접속 시 `status: UP` 응답 확인