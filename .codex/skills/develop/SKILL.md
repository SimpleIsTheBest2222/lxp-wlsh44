---
name: develop
description: Use when implementing a new feature, refactor, or fix in this LMS console admin project and you need the repo-specific development workflow, architectural order, and commit discipline.
---

# Develop

Use this skill for project-specific implementation flow. Match the task to the current phase in [AGENTS.md](../../../AGENTS.md).

## Workflow

1. Clarify the target domain or feature from the user request.
2. Read only the relevant project docs:
   - `docs/conventions.md`
   - `docs/git.md`
   - `docs/domain.md`
   - `docs/architecture.md`
3. Implement in this order when introducing new behavior:
   - `domain`
   - repository interface
   - `repository/inmemory`
   - `service`
   - `controller`
   - `config`
4. Verify behavior with focused tests, then broader `./gradlew test` when appropriate.
5. Commit in atomic units. One commit should represent one reason.

## Project Checks

- Prefer static factory methods where the codebase already uses them.
- Put validation in the domain or request object layer as the codebase expects.
- Preserve soft delete rules.
- Update dependency wiring in `AppConfig` when adding new concrete dependencies.

## Git Expectations

- Branch names should follow the repo convention in `docs/git.md`.
- Commit messages should use repo style such as:
  - `feat: ...`
  - `refactor: ...`
  - `fix: ...`
  - `test: ...`
  - `chore: ...`
  - `docs: ...`

## Output Expectations

When closing work, summarize:

- what changed
- what was verified
- what remains for `$test` or `$ship`
