# HISTORY

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
