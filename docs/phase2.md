# Phase 2 — JDBC + MySQL 전환

## 전환 원칙
- `InMemory*Repository` → `Jdbc*Repository` 구현체 교체
- `AppConfig`에서 `DataSource` 주입으로 변경
- 순수 JDBC (`PreparedStatement`) 사용, ORM 금지
- InMemory 구현체는 삭제하지 않고 유지 (학습 참고용)

## build.gradle 변경
```groovy
// 주석 해제
implementation 'mysql:mysql-connector-java:8.0.33'
```

## AppConfig 변경
```java
// Phase 1
CourseRepository courseRepository() {
    return new InMemoryCourseRepository();
}

// Phase 2
DataSource dataSource() {
    // DriverManager 기반 DataSource
}
CourseRepository courseRepository() {
    return new JdbcCourseRepository(dataSource());
}
```

## 추가 파일
```
repository/
└── jdbc/
    ├── JdbcCourseRepository.java
    ├── JdbcContentRepository.java
    └── JdbcInstructorRepository.java

src/main/resources/
└── schema.sql   # CREATE TABLE 구문
```

## 전환 체크리스트
- [ ] schema.sql 작성
- [ ] DataSource 설정
- [ ] Jdbc*Repository 구현 (인터페이스 동일)
- [ ] AppConfig 구현체 교체
- [ ] soft delete → `deleted = 1` WHERE 조건 처리
- [ ] cascade delete → 트랜잭션 내 순서 처리
