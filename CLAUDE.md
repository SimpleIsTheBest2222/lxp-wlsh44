# CLAUDE.md

## LMS Console Admin

Plain Java 콘솔 → JDBC + MySQL 단계적 전환 프로젝트.

## 현재 단계
Phase 1 — Plain Java + InMemory (ArrayList)

## Commands

```bash
# Build
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "wlsh.project.SomeTest"

# Run a single test method
./gradlew test --tests "wlsh.project.SomeTest.methodName"

# Clean build outputs
./gradlew clean
```

## 기술 스택

- Java (version from local toolchain)
- Gradle wrapper (`./gradlew`)
- JUnit 5 (`junit-bom:5.10.0`) for tests

## Source layout

```
src/main/java/   — production code
src/test/java/   — test code
```

## 패키지 구조
```
com.lms/
├── LmsApplication.java
├── config/AppConfig.java
├── dispatcher/
├── controller/
├── service/
├── repository/
│   └── inmemory/
├── domain/
│   └── enums/          # Level, ContentType
├── view/
└── exception/
```

## 도메인 요약
- Course: title(≤50), instructorName(≤10), description(≤200), price(≥0), level(LOW/MIDDLE/HIGH)
- Content: title(≤50), content(≤200), contentType(VIDEO/TEXT/FILE), seq
- Instructor: name(≤10), introduction(≤100)
- 삭제: 모든 도메인 soft delete

## 참조 문서
- 코드 컨벤션: `.claude/docs/conventions.md`
- Git 컨벤션: `.claude/docs/git.md`
- 도메인 상세: `.claude/docs/domain.md`
- Phase 2 전환: `.claude/docs/phase2.md`
