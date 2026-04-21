# 도메인 상세

## Course

| 필드 | 타입 | 제약 |
|---|---|---|
| id | Long | PK |
| title | String | nn, ≤50 |
| instructorName | String | nn, ≤10 |
| description | String | nn, ≤200 |
| price | int | nn, ≥0 |
| level | Level | nn, LOW/MIDDLE/HIGH |
| createdAt | LocalDateTime | nn |
| updatedAt | LocalDateTime | nn |
| deleted | boolean | soft delete flag |

## Content

| 필드 | 타입 | 제약 |
|---|---|---|
| id | Long | PK |
| courseId | Long | FK → Course |
| title | String | nn, ≤50 |
| content | String | nn, ≤200 |
| contentType | ContentType | nn, VIDEO/TEXT/FILE |
| seq | int | nn, 강의 내 순서 |
| createdAt | LocalDateTime | nn |
| updatedAt | LocalDateTime | nn |
| deleted | boolean | soft delete flag |

## Instructor

| 필드 | 타입 | 제약 |
|---|---|---|
| id | Long | PK |
| name | String | nn, ≤10, 동명이인 허용 |
| introduction | String | ≤100 |
| createdAt | LocalDateTime | nn |
| deleted | boolean | soft delete flag |

## 비즈니스 규칙
- 강의 삭제 시 소속 콘텐츠 cascade soft delete
- 강사 미존재 시 강의의 instructorName = null, 소개 = 빈값 표시
- 강사명은 동명이인 허용, id로 구분

## enum 입력 처리 패턴
```java
Level level = switch (input.trim().toUpperCase()) {
    case "1", "LOW"    -> Level.LOW;
    case "2", "MIDDLE" -> Level.MIDDLE;
    case "3", "HIGH"   -> Level.HIGH;
    default -> throw new LmsException(ErrorCode.INVALID_LEVEL);
};

ContentType type = switch (input.trim().toUpperCase()) {
    case "1", "VIDEO" -> ContentType.VIDEO;
    case "2", "TEXT"  -> ContentType.TEXT;
    case "3", "FILE"  -> ContentType.FILE;
    default -> throw new LmsException(ErrorCode.INVALID_CONTENT_TYPE);
};
```

## Spring 전환 매핑 참고

| Plain Java | Spring Boot |
|---|---|
| `AppConfig` (new 조립) | IoC + `@Service`/`@Repository` |
| `LmsDispatcher` (while 루프) | `DispatcherServlet` |
| `switch(command)` | `@GetMapping`/`@PostMapping` |
| `InMemory*Repository` | Spring Data JPA |
| `InputView`/`OutputView` | Request/Response DTO |
| `try-catch` in Dispatcher | `@RestControllerAdvice` |
