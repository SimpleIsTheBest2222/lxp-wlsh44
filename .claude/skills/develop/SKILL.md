---
name: develop
description: 기능 개발 워크플로. 이슈 생성 → 브랜치 → 구현 → 커밋 순으로 진행. /develop {도메인 or 기능명} 형태로 호출.
---

$ARGUMENTS 로 개발할 도메인/기능을 파악한다.
코드 컨벤션: `.claude/docs/conventions.md` / Git 컨벤션: `.claude/docs/git.md` / 도메인 스펙: `.claude/docs/domain.md`

## 순서

### 1. 이슈 생성
```bash
# 아래 3개 중 하나를 선택해서 실행
gh issue create --template "✨-feature.md"
gh issue create --template "♻️-refactor.md"
gh issue create --template "🐛-fix.md"
```
→ 이슈 번호 기억 (브랜치·PR에 사용)

### 2. 브랜치
```bash
git checkout main && git pull origin main
git checkout -b {feature|refactor|fix}/{issueNumber}-{kebab-case}
```

### 3. 구현 순서
```
domain → repository/인터페이스 → repository/inmemory → service → controller → config
```

체크리스트
- [ ] static factory method 사용
- [ ] 유효성 검사 도메인 내부에서 (LmsException throw)
- [ ] soft delete 적용
- [ ] AppConfig 의존성 조립 추가

### 4. 커밋
작업 단위별로 잘게 나눠 커밋한다 (atomic commit). 하나의 커밋에 여러 이유를 섞지 않는다.
```bash
git add {관련 파일}
git commit -m "{feat|refactor|fix|test|chore|docs}: {요약}"
```
예시:
```
feat: Course 도메인 및 Level enum 구현
feat: InMemoryCourseRepository 구현
feat: CourseService 등록/조회/수정/삭제 구현
feat: CourseController 콘솔 입출력 연결
chore: AppConfig Course 의존성 조립 추가
```

### 5. 완료 보고
```
완료: {기능명}
생성: {파일 목록}
변경: {파일 목록}
다음: /test → /ship
```
