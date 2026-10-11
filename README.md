# 백엔드 프레임워크 실습 과제

## 프로젝트 소개
Spring Boot를 이용하여 상품관리 REST API를 구현했다.

## 주요 기능
- 상품 전체 조회 및 단건 조회
- 상품 생성 및 여러 상품 생성
- 상품 정보 수정 및 상품명 수정
- 상품 삭제 및 전체 상품 삭제
- 공통 API 응답 형식 적용
- 요청 메서드와 URI를 출력하는 요청 로그 미들웨어 구현
- HTTP 상태 코드 처리

## API 목록

| Method | Endpoint | 설명 |
|---|---|---|
| GET | `/api/v1/items` | 전체 상품 조회 |
| GET | `/api/v1/items/{id}` | 상품 단건 조회 |
| POST | `/api/v1/items` | 상품 생성 |
| POST | `/api/v1/items/bulk` | 여러 상품 생성 |
| PUT | `/api/v1/items/{id}` | 상품 정보 수정 |
| PUT | `/api/v1/items/{id}/name` | 상품명 수정 |
| DELETE | `/api/v1/items/{id}` | 상품 삭제 |
| DELETE | `/api/v1/items/all` | 전체 상품 삭제 |

## 응답 형식
```json
{
  "status": "success",
  "message": "조회 성공",
  "data": []
}
```

## 테스트
IntelliJ HTTP Client를 이용해 API 요청 및 응답을 확인했다.

- 성공 응답: 200, 201
- 클라이언트 오류: 400, 404
- 서버 오류 테스트: 500, 503

## 실행 방법
1. 프로젝트를 IntelliJ IDEA에서 연다.
2. JDK 21을 사용하도록 설정한다.
3. `Wsd2025Application`을 실행한다.
4. `api-test.http` 파일에서 요청을 실행한다.

## 참고
상품 데이터는 메모리에 임시 저장되며, 서버 재시작 시 초기화된다.
