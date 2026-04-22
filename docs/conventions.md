# 코드 컨벤션

## 네이밍
- 클래스: PascalCase / 메서드·변수: camelCase / 상수·enum: UPPER_SNAKE_CASE / 패키지: lowercase

## 도메인 객체 생성
생성자 직접 호출 금지 → `static factory method` 사용

```java
// 신규 생성 (ID 없음 — DB가 부여)
Course.create(title, instructorName, description, price, level);

// DB 조회 후 복원 (ID 있음)
Course.createWithId(id, title, instructorName, description, price, level);
```

## 도메인 클래스 구조
```java
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Course {
    // 상수 — 검증 범위
    private static final int MAXIMUM_TITLE_LENGTH = 50;

    // 필드 — final 없음 (Lombok no-args constructor 요건)
    private Long id;
    private String title;

    // 팩토리 메서드
    public static Course create(...) { ... }
    public static Course createWithId(Long id, ...) { ... }

    // 수정 메서드 — null 이면 해당 필드 유지, 검증은 validate 메서드에 위임
    public void update(String title, ...) {
        if (title != null) this.title = validateTitle(title);
    }

    // private static 검증 메서드 — Assert + 도메인 전용 ErrorCode
    private static String validateTitle(String title) {
        Assert.notNull(title);
        Assert.isTrue(!title.isBlank() && title.length() <= MAXIMUM_TITLE_LENGTH,
                ErrorCode.COURSE_TITLE_OUT_OF_RANGE);
        return title;
    }
}
```

## 유효성 검사
- 도메인 내부의 `private static validate*()` 메서드에서 수행
- `Assert.notNull()` — null 이면 `INVALID_ARGUMENTS` throw
- `Assert.isTrue(condition, ErrorCode)` — 조건 불만족 시 해당 ErrorCode throw
- `update()` 에서는 null 여부만 판단, 실제 검증은 validate 메서드에 위임

## ErrorCode 네이밍
도메인별 전용 코드 사용:
```
INSTRUCTOR_NAME_OUT_OF_RANGE
COURSE_TITLE_OUT_OF_RANGE
COURSE_PRICE_NEGATIVE
CONTENT_TITLE_OUT_OF_RANGE
```

## Repository 메서드 네이밍
```java
Optional<T> findById(Long id);
List<T> findAll();
T save(T entity);         // insert + update 통합
void deleteById(Long id); // soft delete
```

## Layered Architecture 경계
- `view`는 입력을 읽고 controller request를 만들어 넘긴다.
- `controller`는 request/response DTO를 다루며, service 결과를 화면용 response DTO로 변환한다.
- `service`는 비즈니스 흐름을 처리하고 도메인 객체를 반환한다.
- `repository`는 도메인 저장 계약을 제공하고, 구현체 내부에서는 entity를 사용해 영속성 관심사를 캡슐화한다.

## Request / Response DTO 규칙
- request DTO는 controller 계층에 둔다.
- request DTO의 검증은 입력 경계 검증만 담당하며 `INVALID_INPUT` 같은 입력 예외를 사용한다.
- service가 request DTO를 파라미터로 예외적으로 받을 수는 있지만, 반환값은 response DTO가 아니라 도메인 객체로 유지한다.
- response DTO 생성 책임은 controller에 둔다.

## Repository Entity 규칙
- repository 구현은 도메인 객체를 직접 저장하지 않고 `repository.entity`를 사용한다.
- entity는 `BaseEntity`를 상속받아 soft delete 상태와 `createdAt`, `modifiedAt`을 공통으로 가진다.
- soft delete 상태는 `EntityStatus` enum으로 관리한다.
- 조회 시 삭제된 entity는 제외하고, `deleteById()`는 실제 삭제가 아니라 상태 변경으로 처리한다.

## Config 분리 규칙
- 수동 DI는 `AppConfig` 하나에 몰아넣지 않고 계층별 config로 분리한다.
- `RepositoryConfig` → `ServiceConfig` → `ControllerConfig` → `ViewConfig` 순서로 의존성을 조립한다.
- `AppConfig`는 상위 config들을 연결하고 최종 진입점만 노출한다.

## 콘솔 출력 (OutputView 상수 정의)
```java
String RESET  = "[0m";
String CYAN   = "[36m";  // 제목, ID
String GREEN  = "[32m";  // 성공
String YELLOW = "[33m";  // 프롬프트 >
String RED    = "[31m";  // 오류, 삭제
String GRAY   = "[90m";  // 안내, 구분선
String LINE_D = GRAY + "=".repeat(60) + RESET; // 헤더 구분선
String LINE_S = GRAY + "-".repeat(60) + RESET; // 섹션 구분선
```
