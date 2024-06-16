API 스팩 문서

**요청 방식:** POST  
**요청 경로:** `/api/v1/signup`  
**요청 본문:**  

RequestBody  

MemberDto
```json
{
    "name": "사용자명",
    "password": "비밀번호",
    "email": "이메일주소",
    "phoneNumber": "핸드폰번호",
    "memberShipType": "TEACHER(강사) or STUDENT(학생)"
}
```
MemberShip (memberShipType)
```java
public enum MemberShip {
    STUDENT, // 학생
    TEACHER  // 강사
}

```


**요청 방식:** POST  
**요청 경로:** `/api/v1/login`  
**요청 본문:**  

RequestBody

LoginDto

```json
{
    "name": "사용자명",
    "password": "비밀번호"
}
```

**요청 방식:** POST  
**요청 경로:** `/api/v1/lecture`  
**요청 본문:**  

RequestBody

LectureRequestDto  

```json
{
    "name": "사용자명",
    "maximumStudent": "최대 수강인원",
    "price": "가격"
}
```

**요청 방식:** POST  
**요청 경로:** `/api/v1/lecture/enroll`  
**요청 본문:**  
체크박스로 선택한 강의들의 lectureId list를 요청합니다. 

RequestBody

LectureRegistrationDto  
 
```json
{
    "lectureIds": "강의 id 리스트"
}
```

**요청 방식:** GET  
**요청 경로:** `/api/v1/lecture`  
**요청 본문:**  
QueryParameter  
```text
ex) /api/v1/lecture?page=0&sortedStatus=APPLICATION_RATE
```  
정렬 상태 종류입니다. 위에서부터 최신순, 신청자 순, 신청자 비율입니다.  
```java
public enum SortedStatus {
    LATEST, // 최신순
    APPLICANTS, // 신청자 순
    APPLICATION_RATE // 신청자 비율
}
```


실행 절차

1번
http://localhost:8080/api/v1/signup  
학생 회원 가입.

RequestBody
```json
{
    "name" : "test5",
    "email" :"test@naver.com",
    "password":"12345",
    "phoneNumber":"010-12345-12345",
    "memberShipType" :"STUDENT"
}
```

2 번
http://localhost:8080/api/v1/signup  
강사 회원 가입.  

RequestBody
```json
{
    "name" : "test",
    "email" :"test@naver.com",
    "password":"12345",
    "phoneNumber":"010-12345-12345",
    "memberShipType" :"TEACHER"
}
```

3 번
http://localhost:8080/api/v1/login  
로그인 (강사로 로그인)  

RequestBody
```json
{
"name" : "test",
"password":"12345"
}
```

로그인을 하였을 경우 JWT 토큰이 응답으로 발행됩니다.

RequestBody
```json
{
    "resultCode": "200",
    "data": {
        "grantType": "Bearer",
        "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlJPTEVfTUVNQkVSIiwiaWF0IjoxNzE4MzQ3Mzk3LCJleHAiOjE3MTgzNDkxOTd9.2CTm0jnY8MVcF2_Rx8Chrxhe82TLwcCsKFO0fMN6vAw"
    },
    "message": "SUCCESS"
}
```
accessToken에 있는 jwt 토큰을 인증이 필요한 아래의 api endPoint을 호출 할때 
헤더 Authorization에 입력.
```text
Header Key : Authorization  
Header Value : Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlJPTEVfTUVNQkVSIiwiaWF0IjoxNzE4MzQ3Mzk3LCJleHAiOjE3MTgzNDkxOTd9.2CTm0jnY8MVcF2_Rx8Chrxhe82TLwcCsKFO0fMN6vAw
```

4번
http://localhost:8080/api/v1/lecture

RequestBody
```json
{
    "name": "내집마련 기초반7",
    "maximumStudent" : 10,
    "price" : 20000
}
```  

Header 

3번 기능을 통해 로그인을 해서 얻은 jwt 토큰을 헤더에 입력하여 새로운 강의를 
등록합니다. 선생이 아닐경우 강의 개설은 되지않습니다. 선생으로 로그인을 해야합니다.  
```text
Header Key : Authorization  
Header Value : Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiYXV0aCI6IlJPTEVfTUVNQkVSIiwiaWF0IjoxNzE4MzQ3Mzk3LCJleHAiOjE3MTgzNDkxOTd9.2CTm0jnY8MVcF2_Rx8Chrxhe82TLwcCsKFO0fMN6vAw
```


5번
http://localhost:8080/api/v1/lecture/enroll  

학생으로 로그인을 한 다음 개설된 강의의 Id값들을 리스트에 담아서 요청합니다. 
그러면 해당 학생은 강의를 수강할 수 있습니다.  
강의를 이미 수강하고있는 학생은 수강이 중복으로 되지않으며 강의 인원수가 다 차있어도 등록되지않습니다.  
응답 결과에는 아래와 같이 요청한 강의가 등록이 되었는지 세가지 상태 값으로 응답이 리턴됩니다.  

```java
    ENROLLMENT_FULL("강의 인원 초과"),
    ALREADY_ENROLLED("해당 강의 이미 수강 중"),
    ENROLLMENT_COMPLETED("등록 완료");
```

RequestBody
```json
{
    "lectureIds" : [1]
}
```