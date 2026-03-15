# ADR-0010: Feature Development

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need a consistent, repeatable process for adding new features to the Struxo KMP project.

## Decision

Sacred implementation order with explicit source set placement rules.

## Rules

**R1 — Sacred Implementation Order:**
```
1.  Domain Entity (commonMain — pure data class)
2.  Repository Interface (commonMain — domain layer)
3.  UseCase (commonMain — calls repository)
4.  DTO (commonMain — @Serializable, toDomain())
5.  DataSource / ApiClient helpers (commonMain — Ktor)
6.  RepositoryImpl (commonMain — implements interface)
7.  ViewModel (commonMain — extends BaseViewModel)
8.  Screen (commonMain — Compose UI)
9.  DI (commonMain — Koin module{}, androidMain/iosMain for platform bindings)
10. Navigation (commonMain — add route + composable)
11. expect/actual (androidMain + iosMain — platform implementations)
```

**R2 — Source set placement:**

| Code Type | Source Set |
|-----------|-----------|
| Domain entities, interfaces, UseCases | commonMain |
| DTOs, mappers, RepositoryImpl | commonMain |
| BaseViewModel, ViewModels | commonMain |
| Compose UI screens, components | commonMain |
| Koin common modules | commonMain |
| Tests (shared logic) | commonTest |
| OkHttp engine, Android DB builder | androidMain |
| Darwin engine, iOS DB builder | iosMain |
| MainActivity, AndroidManifest | androidApp |
| SwiftUI wrapper, ContentView | iosApp |

**R3 — Feature scaffold checklist:**
Before marking a feature complete, verify:
- [ ] Domain layer: zero platform imports
- [ ] All expect declarations have actual in BOTH platforms
- [ ] ViewModel calls UseCase (not Repository)
- [ ] DTOs mapped to domain entities in RepositoryImpl
- [ ] Koin module registered in appModules list
- [ ] Screen registered in navigation graph
- [ ] Tests written in commonTest with Fake implementations

**R4 — No cross-feature dependencies:**
Feature A never imports from Feature B. Shared logic goes to `core/`.

## Consequences

- Consistent feature structure across all 9+ apps
- Sacred order prevents dependency direction violations
- Source set checklist catches platform leaks early
