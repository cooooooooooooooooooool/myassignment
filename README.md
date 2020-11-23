# 뿌리기 API 개발

## 1. 개발 프레임워크
개발 프레임워크는 Spring 5.1.10 를 내장하고 Spring 프레임워크의 설정을 손쉬게 할 수 있는 Spring Boot 2.1.9 을 애플리케이션의 프레임워크로 정하였다.
DB 는 인메모리 DB 인 H2를 사용하였고, 

## 2. 문제 해결 전략
사용자는 user-0 ~ user04 까지 5명의 사용자와 5명의 사용자가 포함된 대화방을 애플리케이션이 시작될 때 미리 생성한다.  
뿌리기가 실행되면 n 명 만큼의 레코드를 DB 에 미리 생성하고 금액은 1/n 을 하여 저장한다.(받은 사용자는 기본 null data 로 설정)  
토큰 생성은 아스키 코드 `33(!) ~ 126(~)` 사이의 문자열을 랜덤하게 3개의 문자를 생성하여 코드로 사용하는데,  
뿌리기 수행시 (대화방 ID + 토큰) 을 Unique Index 로 설정하여 대화방 내에서는 토큰이 유일한 값을 의미하도록 제약 조건을 걸어 사용하였다.  
토큰은 Base64 인코딩/디코딩 되어 사용하도록 하였다.

## 3. 빌드 및 실행 방법
### 3.1 빌드
소스 디렉토리 홈에서 다음과 같이 메이븐 명령어를 실행하여 target/api.jar 를 생성한다.
```bash
mvn clean package
```

HTTP 애플리케이션 서버에서 실행하고자 하는 경우 다음의 명령어를 실행한다.
```bash
java -jar target/api.jar
```

### 3.2 API 단위 테스트
```bash
mvn clean test
```
단위 테스트는 하나의 메소드(MockRestAPITest#sprayTest)에서 다음의 순서대로 실행한다. 
1. 단위 테스트 클래스 초기화시(@Before) 생성된 대화방 ID 를 조회한다.
2. user-0(전우치0) 이 뿌리기를 실행하고 응답값으로 토큰을 반환한다.
3. user-1(전우치1) 이 받기를 실행하고 응답이 정상임을 확인한다.
4. 뿌리기를 실행한 전우치0 이 받기를 실행하는 경우 에러를 발생하고 권한 없음을 응답으로 반환한다.
5. 뿌리기를 실행한 본인 전우치0 이 현재까지의 뿌리기 상태 조회를 호출하고 응답을 정상적으로 받는다.
6. 뿌리기를 실행하지 않은 전우치1 이 현재까지의 뿌리기 상태 조회를 호출하면 에러를 발생하고 권한 없음을 응답으로 반환한다.
7. 2분의 딜레이 후 전우치2 가 받기를 실행하면 뿌리기 시간 만료 에러를 발생하고 잘못된 요청 응답을 받는다.(테스트 시에는 뿌리기 만료시간은 1분으로 설정)

### 3.3 Swagger UI 테스트
Swagger UI 로 REST API 테스트가 가능한데, 먼저 room-controller 에서 대화방 ID 를 조회한 후 transaction-controller 에서 뿌리기(spray), 받기(receive), 조회(getTransactionStatus) 를 테스트 할수 있다.  
http://localhost:8080/swagger-ui.html
 