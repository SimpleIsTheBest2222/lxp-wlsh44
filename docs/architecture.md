# 아키텍처 가이드

## 콘솔 View 구조 원칙

현재 콘솔 UI는 `view`가 화면 진입과 출력 포맷을 담당하고, `controller`는 화면에 필요한 데이터를 준비하는 방향을 기준으로 한다.

### 1. View는 표현을 담당한다
- `view`는 응답 DTO를 받아 어떤 문구와 어떤 순서로 출력할지 결정한다.
- 헤더, 본문, 구분선, 메뉴 문구, 프롬프트 출력은 `view` 책임이다.
- `view`는 도메인 규칙이나 비즈니스 판단을 직접 수행하지 않는다.

### 2. Controller는 화면에 필요한 데이터를 완성된 형태로 반환한다
- `controller`는 서비스/도메인 호출 후 화면에 필요한 데이터를 `response dto`로 반환한다.
- `view`는 여러 컨트롤러 결과를 다시 조합하거나 도메인 객체를 직접 해석하지 않는다.
- 예: `CourseDetailResponse`, `InstructorListResponse`

### 2-1. Service는 도메인 경계를 유지한다
- `service`는 controller request를 입력으로 받을 수는 있지만 반환값은 도메인 객체 또는 도메인 컬렉션으로 유지한다.
- controller가 service 결과를 화면용 response DTO로 변환한다.
- service가 response DTO를 직접 만들기 시작하면 화면 표현 관심사가 service로 새기기 때문에 지양한다.

### 3. View의 handle()은 흐름 제어만 담당한다
- 허용:
  - 입력값 파싱 결과 분기
  - 컨트롤러 호출
  - 다른 View 진입
  - 현재 화면 종료 여부 반환
- 지양:
  - 비즈니스 검증
  - 도메인 계산
  - 출력용 데이터 조합 로직의 비대화

### 4. 화면 데이터는 MenuScreen으로 다룬다
- 메뉴 화면의 출력 정보는 `MenuScreen` 값 객체로 묶는다.
- `title`, `body`, `commands`는 하나의 화면 상태로 취급한다.
- 화면이 복잡해지면 `body`를 문자열 하나로 계속 버티지 말고 섹션/라인 단위 구조로 확장한다.

### 5. 공통화는 실행 골격만 묶고, 의미가 다르면 분리한다
- 공통 루프, 예외 처리, 공통 출력 순서는 `MenuRenderer`가 담당한다.
- 의미가 다른 메뉴는 억지로 공용 enum으로 합치지 않는다.
- 예: `CourseListCommand`, `InstructorListCommand`처럼 화면별 의도가 다르면 분리한다.

## 현재 구조 해석

- `MenuRenderer`: 공통 메뉴 렌더링 및 입력 루프 실행
- `MenuStrategy`: 각 화면이 제공해야 하는 화면 상태와 command 처리 계약
- `MenuScreen`: 메뉴 화면 출력 데이터
- `view`: 화면 진입, 출력 포맷, 사용자 입력 흐름
- `controller`: 화면에 필요한 데이터 준비 및 기능 진입점
- `service`: 도메인 흐름 조립 및 예외 처리
- `repository`: 도메인 저장 계약
- `repository.entity`: 저장소 구현 내부의 영속성 모델
- `config`: 계층별 수동 DI 조립

## 유지 규칙

- `view`는 도메인 객체 대신 DTO를 다룬다.
- `controller`는 출력 문자열을 만들지 않는다.
- `controller`는 response DTO 변환 책임을 가진다.
- `service`는 response DTO를 반환하지 않는다.
- 공통화보다 의미 보존을 우선한다.
- 메뉴 구조가 커져도 `MenuRenderer`에는 화면별 조건문을 넣지 않는다.
- 화면 복잡도가 커지면 `MenuScreen`을 먼저 확장하고, `view` 안의 문자열 조합을 무한정 늘리지 않는다.
- 저장소 구현은 entity와 soft delete 정책을 내부에 숨기고, 외부에는 도메인 객체만 노출한다.
