# 주택 금융 서비스 API 개발

## 1. 개발 프레임워크
개발 프레임워크는 Spring 5.1.5 를 내장하고 Spring 프레임워크의 설정을 손쉬게 할 수 있는 Spring Boot 2.1.3 을 애플리케이션의 프레임워크로 정하였다.
이때 Persistence 레이어는 JPA 를 활용하여 애플리케이션의 데이터를 객체 지향 관점에 다룰 수 있도록 하였는데 GROUP BY 등의 SQL 이 필요한 경우 JPL 사용하는 전략을 취하였다.

## 2. 문제 해결 전략
'''
전체 년도별 각 금융 기관의 큰 금액 또는 년도별 평균 금액의 가장 작은 금액과 큰 금액을 구하는 경우 SQL 의 GROUP BY 절을 통해 모든 결과 리스트를 구한 후
Stream API 를 활용하여 주어진 list 또는 collection 에서 최소/최대 값을 찾아내는 전략을 사용하였다.   
'''

## 3. 빌드 및 실행 방법
### 3.1 빌드
소스 디렉토리 홈에서 다음과 같이 메이븐 명령어를 실행하여 target/api.jar 를 생성한다.
```
mvn clean package -Dmaven.test.skip=true
```

HTTP 애플리케이션 서버에서 실행하고자 하는 경우 다음의 명령어를 실행한다. context : /api, port : 8080
```
java -jar target/api.jar
```

### 3.2 개별 API 단위의 실행 테스트
모든 REST API 의 테스트 결과는 HTTP 상태가 200(OK) 을 반환하는 경우 성공이고, 이외에 예외 상황에 따라 400, 401, 500 을 반환한다.
테스트 전략은 웹 애플리케이션을 애플리케이션 서버에 배포하지 않고 Spring MVC 를 테스트 하도록 하였다.
생성한 토큰의 만료 시간은 10초인데 토큰 재발급 테스트를 위해 sing 토큰 발급 후 12초 정도의 딜레이를 준 후 토큰을 재발급하도록 테스트 코드를 작성하였다. 

###### signup 계정 생성 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#signupTest
'''

###### signin 로그인 및 토큰 발급 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#signinTest

###### 토큰 재발급 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#tokenRefreshTest
'''

###### CSV 파일로부터 금융 기관별 데이터를 데이터베이스에 저장 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#initBankStatusTest
'''

###### 주택금융 공급 금융기관(은행) 목록을 출력하는 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#getBanksTest
'''

###### 년도별 각 금융기관의 지원 금액 합계를 출력하는 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#getTotalAmountListTest
'''

###### 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#getMaxAmountInstitueListTest
'''

###### 전체 년도(2006 ~ 2015)에서 외환은행의 지원금액 평균 중에서 가장 작은 금액과 큰 금액을 출력하는 API 테스트
'''
mvn test -Dtest=com.jm.test.MockRestAPITest#getInstituteAvgMinMaxAmountTest
'''