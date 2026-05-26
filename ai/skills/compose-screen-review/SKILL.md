# Compose Screen Review Skill

Use this skill when reviewing a Kotlin Multiplatform Compose screen for correctness, architecture, state handling, accessibility and maintainability.

## Use For

- implemented screen reviews
- PRs touching Compose UI
- HTML/Stitch/Pencil/Figma migrations
- visual and architectural quality checks
- state/callback boundary checks

Do not use for broad data-layer, Gradle-only, backend/network-only or full architecture redesign reviews unless requested.

## Checklist

| Area | Check |
|---|---|
| Scope | Only intended files changed |
| Architecture | Screen renders state and emits events |
| State | State is immutable, explicit and render-ready |
| Callbacks | Callbacks are preserved and clearly named |
| Business logic | No business rules inside Composables |
| Platform safety | No Android/iOS APIs in `shared/commonMain` |
| Design system | Existing tokens/components are reused |
| Performance | No expensive work during composition |
| Accessibility | Text, touch targets and content descriptions are reasonable |
| Validation | Build/test/lint status is honest |

## Architecture Checks

Expected flow:

```text
State -> Screen -> Event/Callback -> State holder/ViewModel -> State
```

Rules:

- Prefer stateless or mostly stateless screens.
- State holders/ViewModels own UI logic when complexity requires it.
- Composables must not call repositories, preferences, database APIs or AI providers directly.
- Do not trigger AI provider calls from `LaunchedEffect(Unit)` inside a screen.
- Pass navigation callbacks down unless the existing project convention differs.
- Hoist state when multiple composables need it or it must survive recomposition.

## Implementation Checks

Look for:

- unnecessary nested `Box`/`Column`/`Row`
- hardcoded colors or spacing when tokens exist
- duplicated UI that should be a small private composable
- over-abstraction for one-off UI
- unstable objects passed unnecessarily
- expensive calculations during composition
- incorrect `remember` usage
- misuse of `derivedStateOf`
- missing `key`/stable identity in dynamic lists when needed
- modifier chains that hide intent
- UI effects collected without lifecycle awareness

## KMP Checks

For shared Compose screens:

- no `android.*` imports
- no Activity/Fragment/Context dependencies
- no UIKit/Swift-only APIs
- use common resources for shared UI
- keep platform-specific behaviour behind source sets or shared contracts
- Android-only UI belongs in `androidApp/` or `androidMain`
- iOS-only UI belongs in `iosApp/` or `iosMain`

## Accessibility Checks

- Interactive elements have sufficient touch area.
- Meaningful icons have content descriptions.
- Decorative icons are explicitly decorative.
- Text is readable and not clipped.
- Color alone is not the only signal for critical states.
- Font scale should not obviously break layout.
- Disabled, loading and error states are understandable.

Severity guidance:

- Insufficient touch area on primary actions: Major.
- Critical state communicated only by color: Major.
- Missing content descriptions on meaningful icons: Minor unless blocking a critical flow.
- Decorative icons without explicit decorative handling: Minor.

## Visual Migration Checks

Skip if no reference screenshot, visual spec or source screen exists.

- Match layout hierarchy first.
- Match spacing before micro details.
- Match typography and colors using design tokens when possible.
- Do not copy CSS structure blindly.
- Prefer Compose layout intent over HTML markup.
- Note differences caused by the project design system.

## Severity

| Severity | Meaning |
|---|---|
| Critical | Crash, broken architecture boundary, data loss or security issue |
| Major | Likely maintainability, state, UX or validation problem |
| Minor | Small readability, naming, style or localized UI issue |
| Suggestion | Optional improvement |

UI effects collected without lifecycle awareness are Major when the screen handles navigation, snackbar or one-off user feedback.

## Output

With issues:

```text
Review:
- Critical: ...
- Major: ...
- Minor: ...
- Suggestions: ...

Validation:
- ...

Recommended next action:
- ...
```

Without issues:

```text
Review:
- No blocking issues found.

Validation:
- ...

Recommended next action:
- ...
```
