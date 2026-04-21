---
name: test
description: 단위 테스트 작성 워크플로. /test {도메인 or 클래스명} 형태로 호출. 테스트 대상 분석 → Given/When/Then 작성 → 실행 → 커밋.
---

$ARGUMENTS 로 테스트 대상을 파악한다.

## 파일 위치
```
src/test/java/com/lms/
├── domain/       {Domain}Test.java
├── service/      {Domain}ServiceTest.java
└── repository/   InMemory{Domain}RepositoryTest.java
```

## 작성 규칙

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
            .isInstanceOf(LmsException.class);
    }
}
```

## 도메인별 필수 케이스

**Course**
- [ ] 등록 성공
- [ ] 등록 실패: title>50, instructorName>10, description>200, price<0, level=null
- [ ] 전체 조회 — 등록 순 반환
- [ ] 단건 조회 성공 / 없는 id 실패
- [ ] 수정 성공
- [ ] 삭제(soft) — 이후 조회 목록에서 제외

**Content**
- [ ] 등록 성공 / title>50, content>200 실패
- [ ] 수정 성공
- [ ] 삭제 — 강의 삭제 시 cascade soft delete

**Instructor**
- [ ] 등록 성공 / name>10 실패
- [ ] 동명이인 허용 확인
- [ ] 수정 / 삭제 성공

## 실행 및 커밋
```bash
./gradlew test --tests "com.lms.*.{ClassName}Test"
git commit -m "test: {대상} 단위 테스트 추가"
```
