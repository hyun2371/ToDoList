## 공유 ToDo 리스트 서비스


### 요구사항
- [x]  사용자는 ToDo를 생성/조회/수정/삭제할 수 있어야 함
- [x]  ToDo를 생성한 사용자를 구분할 수 있어야 함
- [x]  태그로 ToDo의 카테고리를 분류할 수 있어야 함
- [x]  생성일, 마감일, D-Day 정보를 포함해야 함
- [x]  ToDo 항목의 순서를 변경할 수 있어야 함
- [x]  사용자, 태그, 완료 여부를 조건으로 ToDo를 필터링할 수 있어야 함
- [x]  ToDo를 다른 사용자에게 조회만 가능한 상태로 공유할 수 있어야 함
- [x]  ToDo를 생성한 사용자만 수정/삭제할 수 있어야 함

### 고려사항
- 현재 관리자(admin)의 ToDo 수정/삭제 권한  정책이 결정되지 않은 상황
    - 추후 다음 중 하나로 결정될 예정:
        1. 관리자는 모든 사용자의 ToDo를 수정/삭제 가능
        2. 관리자도 본인이 생성한 ToDo만 수정/삭제 가능
      
  → role admin 추가
- 차후에 삭제한 ToDo를 되돌리는 요구사항이 추가될 수 있음

  → SoftDelete 추가

### 기술스택
- **Language**: Java 17
- **Framework**: Spring Boot 3.3
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA, QueryDSL 5
- **Validation**: Spring Validation
- **API Docs**: Springdoc OpenAPI (Swagger UI)
- **Test**: JUnit5, Spring Boot Test, Testcontainers
- **Build Tool**: Gradle

### ERD
<img src="./docs/ERD.png" width="600" />

### API 명세서
- 애플리케이션 실행 후 접속
    http://localhost:8080/swagger-ui/index.html

### 디렉토리 구조
```
src
├── main
│   └── java/org/pwc/todo
│        ├── common        # 공통 설정, 예외, 공용 DTO, 베이스 엔티티
│        ├── user          # 사용자 도메인 (회원 엔티티, repo)
│        ├── todo          # ToDo CRUD, 순서 변경, 필터링 등 핵심 기능
│        └── share         # ToDo 공유 기능 (조회 권한 관리)
│
└── test
    └── java/org/pwc/todo
         ├── support       # 공통 테스트 베이스 (TestContainer, JPA 설정 등)
         ├── todo          # ToDo 통합 테스트, 단위테스트
         └── share         # 공유 기능 통합 테스트
```

### 빠른 실행
1. 프로젝트 클론
    ```bash
    git clone https://github.com/hyun2371/ToDoList.git
    ```

2. 데이터베이스 설정 
src/main/resources/application.yml파일에서 MySQL 접속 정보를 설정합니다.

    ```yml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/todolist
        username: {mysql 계정 아이디}
        password: {mysql 비밀번호}
    ```
3. 데이터베이스 초기화<br>
src/main/resources/sql/init.sql을 실행해 초기 테이블을 생성합니다. <br><br>

4. 테스트 환경 준비<br>
Docker Desktop을 실행합니다. <br><br>
5. 애플리케이션 실행 <br>

    ```bash
    /gradlew bootRun
    ```

