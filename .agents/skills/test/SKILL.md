---
name: test
description: Use when writing or updating tests for this LMS console admin project and you need the repo-specific test structure, naming conventions, and required domain cases.
---

# Test

Use this skill for project-specific test authoring and verification.

## Test Structure

Default locations:

```text
src/test/java/com/lxp/
├── domain/
├── service/
├── repository/
├── controller/
└── view/
```

Match the existing package path of the class under test.

## Test Style

```java
@DisplayName("{ClassName} 테스트")
class CourseServiceTest {

    @Test
    @DisplayName("성공 - {설명}")
    void method_success() {
        // given / when / then
    }

    @Test
    @DisplayName("실패 - {설명}")
    void method_fail() {
        assertThatThrownBy(() -> ...)
            .isInstanceOf(LxpException.class);
    }
}
```

## Minimum Domain Coverage

Instructor:

- 등록 성공
- 등록 실패: 이름 제한 위반, null/blank 등 입력 검증
- 동명이인 허용 여부 확인
- 조회/수정/삭제 성공
- 없는 id 또는 잘못된 입력 실패

Apply the same pattern to Course and Content with the constraints defined in `docs/domain.md`.

## Execution

- Run targeted tests first when the scope is small.
- Run `./gradlew test` before finalizing if the change affects shared behavior.

## Commit Guidance

- Prefer `test: {대상} ...` commit messages for test-only changes.
