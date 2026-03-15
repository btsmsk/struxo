# ADR-0006: Design System

**Status:** Accepted
**Date:** 2026-03-10

## Context

Need a consistent design system for Compose Multiplatform UI that renders natively on both Android and iOS.

## Decision

Material 3 Compose Multiplatform with custom theme tokens. All UI lives in `commonMain`.

## Rules

**R1 — No hardcoded colors:**
Never use `Color(0xFF...)` outside `core/presentation/theme/`. Always use `MaterialTheme.colorScheme.*`.

**R2 — No raw dimension literals:**
Use the `Spacing` object for all spacing/padding. Exceptions: `0.dp`, `1.dp` for dividers.
```kotlin
object Spacing {
    val xxs = 2.dp; val xs = 4.dp; val sm = 8.dp
    val md = 12.dp; val lg = 16.dp; val xl = 24.dp
    val xxl = 32.dp; val xxxl = 64.dp
}
```

**R3 — No raw font sizes:**
Use `MaterialTheme.typography.*` — never hardcode `.sp` values.

**R4 — Composable modifier parameter:**
All reusable composable components must accept `modifier: Modifier = Modifier` as a parameter.

**R5 — Theme colors:**
Primary: `#2E86AB`, Secondary: `#0D6B4E`, Tertiary: `#E8A317`. Light and dark schemes provided.

**R6 — AppTheme wrapping:**
Root composable wrapped in `AppTheme { }` which applies `MaterialTheme` with custom color schemes and typography.

**R7 — Screen composable split:**
- `{Feature}Screen(viewModel)` — stateful, collects state and effects
- `{Feature}Content(state, onEvent)` — stateless, previewable, receives data and callbacks

## Consequences

- Consistent look across both platforms via Material 3
- Theme tokens make design changes propagate automatically
- Stateless Content composables are easily testable/previewable
