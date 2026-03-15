# ADR-0011: New Library Policy

**Status:** Accepted
**Date:** 2026-03-10

## Context

Adding libraries to a KMP project requires extra diligence — libraries must work on ALL targets.

## Decision

Strict evaluation criteria before any new library is added.

## Rules

**R1 — Must work on ALL KMP targets:**
New libraries must support Android AND iOS (iosArm64, iosX64, iosSimulatorArm64). Android-only libraries are banned from commonMain.

**R2 — Version catalog only:**
All dependencies MUST be declared in `gradle/libs.versions.toml`. No hardcoded version strings in any build.gradle.kts.

**R3 — Wrap platform-only libs behind expect/actual:**
If a library only works on one platform, wrap it behind an expect/actual interface in commonMain. Never import platform-only libraries directly in shared code.

**R4 — Evaluation criteria:**
Before adding a new library:
1. Does it support all KMP targets? (check Kotlin Multiplatform compatibility)
2. Is it actively maintained? (last commit < 6 months)
3. Does it have a version catalog entry? (if not, create one)
4. Is there already an equivalent in the stack? (check ADR-0002)
5. Does it conflict with existing dependencies?

**R5 — No upgrades without approval:**
Library version bumps require explicit user approval and verification on BOTH platforms.

## Consequences

- Prevents runtime crashes on iOS from Android-only libraries
- Version catalog prevents version conflicts
- Evaluation criteria prevent dependency bloat
