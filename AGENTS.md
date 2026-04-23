# AGENTS.md

## LMS Console Admin

Plain Java 콘솔에서 JDBC + MySQL 단계로 전환 중인 프로젝트다.

## 현재 단계

Phase 1 — Plain Java + InMemory (ArrayList)

## Commands

```bash
./gradlew build
./gradlew test
./gradlew test --tests "wlsh.project.SomeTest"
./gradlew test --tests "wlsh.project.SomeTest.methodName"
./gradlew clean
```

## 기술 스택

- Java (local toolchain 기준)
- Gradle wrapper: `./gradlew`
- JUnit 5 (`junit-bom:5.10.0`)
- AssertJ (`assertj-core:3.25.3`)

## Source Layout

```text
src/main/java/
src/test/java/
```

## 패키지 구조

```text
com.lxp/
├── LxpApplication.java
├── common/
│   └── validate/
├── domain/
│   └── enums/
├── exception/
├── config/
├── repository/
│   └── inmemory/
├── service/
├── view/
└── controller/
```

## 도메인 요약

- Course: `title<=50`, `description<=200`, `price>=0`, `level=LOW|MIDDLE|HIGH`
- Content: `title<=50`, `content<=200`, `contentType=VIDEO|TEXT|FILE`, `seq`
- Instructor: `name<=10`, `introduction<=100`
- 모든 삭제는 soft delete 규칙을 따른다.

## 참조 문서

- 코드 컨벤션: `docs/conventions.md`
- Git 컨벤션: `docs/git.md`
- 도메인 상세: `docs/domain.md`
- 아키텍처 가이드: `docs/architecture.md`
- Phase 2 전환: `docs/phase2.md`
- UI 문서: `docs/ui`

## Codex Local Skills

- `.codex/skills/develop`
- `.codex/skills/test`
- `.codex/skills/ship`

## Working Rules

- Prefer `./gradlew test` before finalizing behavior changes.
- Follow the docs above as task context requires.
- Keep commits atomic. Do not mix unrelated reasons in one commit.
- Match all implementation to the current phase constraints.
