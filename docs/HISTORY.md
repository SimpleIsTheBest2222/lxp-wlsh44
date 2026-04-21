# HISTORY

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
