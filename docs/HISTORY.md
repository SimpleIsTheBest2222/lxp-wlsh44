# HISTORY

## 2026-04-22 — 콘솔 메뉴 아키텍처 규칙 정리

### 결정 사항

**View / Controller 책임 분리**
- `view`는 응답 DTO를 받아 출력 형식과 화면 흐름을 결정
- `controller`는 화면에 필요한 데이터를 완성된 형태로 반환
- `handle()`에는 화면 전환과 컨트롤러 호출만 남기고, 비즈니스 판단은 넣지 않기로 정리

**메뉴 화면 상태 모델**
- 메뉴 화면의 출력 정보는 `MenuScreen` 값 객체로 묶음
- `MenuStrategy`는 `title()` / `body()` / `commands()` 개별 노출 대신 `screen()`으로 화면 상태를 제공
- `MenuRenderer`가 `MenuScreen`을 받아 공통 렌더링 순서를 처리

**공통화 원칙**
- 공통화는 실행 골격만 묶고, 메뉴 의미까지 억지로 추상화하지 않음
- 공용 `ListCommand`는 제거하고 `CourseListCommand`, `InstructorListCommand`로 분리
- 메뉴 라벨은 각 command enum이 완성형 문구를 직접 보유

**문서화**
- 현재 콘솔 UI 구조와 유지 규칙을 `docs/architecture.md`에 정리
- `CLAUDE.md` 참조 문서 목록에 아키텍처 가이드 추가

### 영향 범위
- `MenuRunner.java` → `MenuRenderer.java` 명칭 변경
- `MenuScreen.java` 추가
- `MenuStrategy.java` — `screen()` 중심 계약으로 변경
- `MainView.java`, `CourseView.java`, `CourseListView.java`, `InstructorView.java`, `InstructorListView.java` — `MenuScreen` 반환 방식으로 전환
- `CourseListCommand.java`, `InstructorListCommand.java` 추가
- `ListCommand.java` 삭제
- `docs/architecture.md` 추가
- `CLAUDE.md` 참조 문서 목록 갱신

---

## 2026-04-22 — View 중심 콘솔 구조로 전환

### 결정 사항

**메인 실행 흐름**
- `MainController` 중심 루프를 제거하고 `MainView`가 애플리케이션 진입 루프를 담당
- `LxpApplication`은 `new AppConfig().mainView().run()`으로 시작

**View 중심 메뉴 구조**
- `MainView`, `CourseView`, `InstructorView`, `CourseListView`, `InstructorListView` 추가
- 각 View가 자신의 `printMenu()` / `printList()`와 입력 루프를 직접 관리
- 컨트롤러는 상세 구현 없이 호출 지점만 제공하는 형태로 축소
  - `CourseController`: `register()`, `findAll()`, `findById()`
  - `InstructorController`: `register()`, `findAll()`, `findById()`

**command 패키지 위치 조정**
- 메뉴 선택 enum은 Controller 책임이 아니라 View 책임으로 판단
- `MainCommand`, `CourseCommand`, `InstructorCommand`, `ListCommand`를 `view/command`로 이동
- 공통 메뉴 출력을 위해 `MenuCommand` 인터페이스 추가

**출력 구조 정리**
- `OutputView`는 화면 출력을 `header` / `body` / `menu`로 분리
- 메뉴 문구는 하드코딩 문자열이 아니라 command enum의 `value`, `label`을 기반으로 동적 생성
- 리스트 화면의 `선택`은 prefix를 받아 `강의 선택`, `강사 선택`으로 출력

**InputView / OutputView 유틸화**
- 두 클래스는 `view` 패키지 공용 util 성격으로 사용
- 클래스 접근제어자는 package-private으로 변경
- 메서드는 모두 `static`으로 전환
- `AppConfig`는 더 이상 `InputView`, `OutputView` 인스턴스를 생성하거나 주입하지 않음

**중복 제거 방식 조정**
- `Abstract` 상속 기반 공통화 대신 Strategy 패턴 선택
- `MenuRunner`가 공통 루프(`while`, `try-catch`, 공통 출력)를 담당
- 각 View는 `MenuStrategy`를 구현해 화면별 제목, 본문, command 파싱, 처리 로직만 제공

**목록 화면 입력 흐름 정리**
- `CourseListView`, `InstructorListView`도 다른 메뉴 화면과 동일하게 반복 입력 루프를 사용
- 오입력 시 현재 목록 화면에 머물며 재입력 가능

### 영향 범위
- `LxpApplication.java` — `mainController()` → `mainView()` 진입점 변경
- `AppConfig.java` — View 조립 중심으로 재구성, 입출력 유틸 주입 제거
- `MainController.java` 삭제
- `CourseController.java`, `InstructorController.java` — placeholder 메서드만 남기는 형태로 단순화
- `InputView.java`, `OutputView.java` — package-private static util 형태로 전환
- `MainView.java`, `CourseView.java`, `InstructorView.java`, `CourseListView.java`, `InstructorListView.java` — 메뉴/리스트 루프와 컨트롤러 호출 연결
- `view/command/*` — 메뉴 enum 및 `MenuCommand` 추가
- `MenuRunner.java`, `MenuStrategy.java` — 공통 메뉴 실행 전략 추가

---

## 2026-04-22 — 도메인 테스트 작성 및 테스트 컨벤션 확립

### 결정 사항

**테스트 도구**
- JUnit assertions(`assertEquals` 등) 대신 AssertJ 사용 (`assertj-core:3.25.3` 추가)
- 예외 검증: `hasMessage(ErrorCode.XXX.getMessage())` — `extracting → isEqualTo` 패턴 사용 금지

**테스트 구조 (BDD)**
- `// given` / `// when` / `// then` 구간 주석 필수
- when과 then이 한 문장으로 합쳐지는 경우(assertThatThrownBy 등) `// when & then`으로 표기

**테스트 대상 변수 분리**
- 검증 의도가 드러나도록 테스트 대상 값을 변수로 추출
  - 경계값: `String tooLongTitle = "가".repeat(51)`, `String maxLengthTitle = "가".repeat(50)`
  - 공백: `String blankTitle = "   "`
  - 경계 숫자: `int negativePrice = -1`, `int zeroPrice = 0`
  - `null`은 자명하므로 변수 불필요

**Course.instructorName 제거**
- 강사 정보는 Instructor 도메인이 단일 책임으로 관리
- Course는 강의 자체(제목, 설명, 가격, 레벨, 타임스탬프)만 보유
- `ErrorCode.COURSE_INSTRUCTOR_NAME_OUT_OF_RANGE` 함께 제거

### 영향 범위
- `build.gradle` — assertj-core 3.25.3 추가
- `Course.java` — `instructorName` 필드·검증 제거, `create()`/`createWithId()`/`update()` 시그니처 변경
- `ErrorCode.java` — `COURSE_INSTRUCTOR_NAME_OUT_OF_RANGE` 제거
- `CourseTest`, `InstructorTest`, `ContentTest` — 도메인 단위 테스트 신규 작성

---

## 2026-04-21 — PR 리뷰 반영 (도메인 검증 강화, 설계 결정)

### 결정 사항

**타임스탬프 관리 주체**
- `createdAt` / `modifiedAt`은 Repository 책임으로 확정
- 도메인 `update()`에서 `modifiedAt` 갱신 제거 — 도메인이 시계(`LocalDateTime.now()`)에 의존하면 테스트가 어렵고 관심사 분리가 깨짐

**불변 도메인 설계 검토 → 미적용 결정**
- 장점: 상태 변이 차단, 추론 용이
- 단점: Phase 2 JDBC 전환 시 변경 필드 추적 복잡도 증가, 현재 패턴(`@NoArgsConstructor(PRIVATE)` + validate)이 이미 통제된 가변성을 제공함
- 현재 구조 유지

**from() 제거 (ContentType, Level)**
- null 방어·Locale 처리 등 구현 부담 대비 현재 사용처 없음
- View 계층 구현 시 실제 필요성이 확인되면 재구현

### 수정 내역
- `Instructor.validateName`: `isEmpty()` → `isBlank()` (공백-only 차단)
- `Course.validateInstructorName`: `isBlank()` + 길이 조건 추가
- `ContentType`, `Level`: `from()` 제거
- `LxpException`: `Objects.requireNonNull`로 null errorCode NPE 방어

---

## 2026-04-21 — GitHub Issue / PR 작성 컨벤션 확립

### 결정 사항
- Issue와 PR 본문은 `.github` 템플릿을 반드시 따른다.
- **Feature 이슈** (`✨-feature.md`): `🚀 이슈 내용` / `🎯 작업 내용` / `📌 참고 사항`
- **Refactor 이슈** (`♻️-refactor.md`): `🔧 이슈 내용` / `🎯 작업 내용` / `📌 참고 사항`
- **Fix 이슈** (`🐛-fix.md`): `🐞 이슈 내용` / `🎯 작업 내용` / `📌 참고 사항`
- **PR** (`PULL_REQUEST_TEMPLATE.md`): `🔗 관련 이슈` / `📌 개요` / `✨ 변경 사항` / `🔥 변경 이유` / `🧪 테스트` / `🤔 고민한 부분` / `📸 스크린샷` / `✅ 체크리스트`
- `Closes #N`은 PR 본문 `🔗 관련 이슈` 섹션에 작성한다.
- 이슈 제목에 이모지 접두사 포함: `✨ 기능명`, `♻️ 리팩터명`, `🐛 버그명`

### 적용 내역
- Issue #1: ✨ Feature 템플릿으로 재작성, 완료 항목 체크
- Issue #2: ♻️ Refactor 템플릿으로 재작성
- PR #3: PR 템플릿으로 재작성, `Closes #2` 포함

---

## 2026-04-21 — 도메인 구현 패턴 확립

### 배경
Course·Content 초안은 final 필드 + 수동 getter + 생성자 내부 validate() 방식으로 작성됐으나,
Instructor 구현 시 더 명확한 패턴으로 확립됨.

### 결정 사항

**Lombok 도입**
- `@Getter` — getter 코드 제거
- `@NoArgsConstructor(access = AccessLevel.PRIVATE)` — 외부 직접 생성 차단, Lombok setter 없이 팩토리에서만 필드 할당

**팩토리 메서드 2종 분리**
- `create(...)` — ID 없음, 신규 엔티티 (DB INSERT 전)
- `createWithId(id, ...)` — ID 있음, DB 조회 후 복원용

**Assert 유틸리티**
- `Assert.notNull(obj)` → `INVALID_ARGUMENTS` throw
- `Assert.isTrue(condition, ErrorCode)` → 도메인 전용 ErrorCode throw
- 조건 검증 중복 제거: `update()`에서는 null 여부만 판단, 실제 검증은 `validate*()` 메서드에 위임

**도메인별 ErrorCode**
- 범용 `INVALID_INPUT` 대신 `COURSE_TITLE_OUT_OF_RANGE` 처럼 도메인·필드 단위로 세분화
- 오류 메시지에 원인이 명확히 드러남

**타임스탬프·deleted 필드 원칙**
- `deleted` 는 DB 관심사 → 도메인 객체에 포함하지 않음, soft delete 는 Repository 계층에서 담당
- `createdAt` / `modifiedAt` 는 도메인별로 판단 (아래 정정 참고)

### 영향 범위
- `Course.java`, `Content.java` 위 패턴으로 재작성
- `ErrorCode.java` — Course·Content 전용 코드 추가
- `docs/conventions.md` — 도메인 클래스 구조 섹션 갱신

---

## 2026-04-21 — Course 타임스탬프 필드 복원

### 배경
위 패턴 확립 시 타임스탬프를 DB 관심사로 일괄 제거했으나, `Course` 는 UI 강의 상세 화면에서
"게시일(createdAt)"과 "마지막 수정일(modifiedAt)"을 비즈니스적으로 노출해야 함.

### 정정
- `Course` 에 `createdAt`, `modifiedAt` (LocalDateTime) 필드 추가
- `create()` — 현재 시각으로 두 필드 초기화
- `createWithId()` — DB 저장값을 파라미터로 받아 복원
- `update()` — 필드 변경 시 `modifiedAt` 갱신
- `Instructor`, `Content` 는 타임스탬프 불필요 → 유지
