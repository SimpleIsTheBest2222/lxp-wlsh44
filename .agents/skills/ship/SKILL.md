---
name: ship
description: Use when preparing this LMS console admin branch for push or PR creation and you need the repo-specific pre-ship checklist, verification steps, and PR workflow.
---

# Ship

Use this skill when wrapping up a branch.

## Pre-Ship Checklist

1. Check working tree with `git status`.
2. Run `./gradlew test`.
3. Review branch history with `git log main..HEAD --oneline`.
4. Confirm commits are atomic and messages follow `docs/git.md`.

## Push

Push the current branch:

```bash
git push origin $(git branch --show-current)
```

## PR Preparation

If using GitHub CLI:

```bash
gh pr create --title "[{feat|refactor|fix}] {기능 요약}" --base main
```

Include `Closes #{issueNumber}` in the PR body when applicable.

## After Merge

```bash
git checkout main
git pull origin main
git branch -d {branch-name}
```
