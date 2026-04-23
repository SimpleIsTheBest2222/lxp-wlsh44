# Git 컨벤션

## 브랜치
```
{feature|refactor|fix}/{issueNumber}-{kebab-case}
```
- `feature` 새 기능 / `refactor` 동작 변경 없는 구조 개선 / `fix` 버그 수정

## 커밋
```
{type}: {한 줄 요약}
```
- `feat` `refactor` `fix` `test` `chore` `docs`
- 하나의 커밋 = 하나의 이유 (atomic)

## 이슈
- 이슈 생성은 템플릿 파일을 명시해서 생성한다.
- feature:
```bash
gh issue create --template "✨-feature.md"
```
- refactor:
```bash
gh issue create --template "♻️-refactor.md"
```
- fix:
```bash
gh issue create --template "🐛-fix.md"
```
- 템플릿 선택 후 제목/본문을 템플릿 형식에 맞게 채운다.
- 라벨: `feature` `refactor` `fix` `test` `docs` `chore` `phase-1` `phase-2`

## PR
- 템플릿 등록됨 → `gh pr create` 시 자동 적용
- 제목 형식: `[{feat|refactor|fix}] {기능 요약}`
- 본문 필수: `Closes #{issueNumber}`
- base 브랜치: `main`
- 
