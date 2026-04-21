# 코드 컨벤션

## 네이밍
- 클래스: PascalCase / 메서드·변수: camelCase / 상수·enum: UPPER_SNAKE_CASE / 패키지: lowercase

## 도메인 객체 생성
생성자 직접 호출 금지 → `static factory method` 사용
```java
Course.create(title, instructorName, description, price, level);
```

## 유효성 검사
도메인 내부에서 수행, 실패 시 `LmsException(ErrorCode.XXX)` throw

## Repository 메서드 네이밍
```java
Optional<T> findById(Long id);
List<T> findAll();
T save(T entity);         // insert + update 통합
void deleteById(Long id); // soft delete
```

## 콘솔 출력 (OutputView 상수 정의)
```java
String RESET  = "\u001B[0m";
String CYAN   = "\u001B[36m";  // 제목, ID
String GREEN  = "\u001B[32m";  // 성공
String YELLOW = "\u001B[33m";  // 프롬프트 >
String RED    = "\u001B[31m";  // 오류, 삭제
String GRAY   = "\u001B[90m";  // 안내, 구분선
String LINE_D = GRAY + "=".repeat(60) + RESET; // 헤더 구분선
String LINE_S = GRAY + "-".repeat(60) + RESET; // 섹션 구분선
```