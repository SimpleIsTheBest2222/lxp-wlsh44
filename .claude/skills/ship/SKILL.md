---
name: ship
description: PR 생성 워크플로. 현재 브랜치 작업을 점검하고 PR을 생성한다. 인수 없이 /ship 으로 호출.
---

현재 브랜치 작업을 정리하고 PR을 생성한다.

## 순서

### 1. 사전 점검
```bash
git status
./gradlew test
git log main..HEAD --oneline
```
테스트 실패 시 `/test` 로 돌아가 수정.

### 2. 푸시
```bash
git push origin $(git branch --show-current)
```

### 3. PR 생성
PR 템플릿이 등록되어 있으므로 `--body` 없이 생성 후 에디터에서 작성.
```bash
gh pr create \
  --title "[{feat|refactor|fix}] {기능 요약}" \
  --base main
```
본문 `Closes #{issueNumber}` 반드시 포함.

### 4. 머지 후 정리
```bash
git checkout main && git pull origin main
git branch -d {브랜치명}
```
