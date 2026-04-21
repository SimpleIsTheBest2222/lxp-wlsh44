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
