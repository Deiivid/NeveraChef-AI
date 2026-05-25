# Compose Screen Review Skill

Use this skill when reviewing a Kotlin Multiplatform Compose screen for correctness, architecture, readability, state handling, accessibility and maintainability.

This skill combines Google's Compose guidance with JetBrains/Kotlin Compose Multiplatform constraints.

---

## When to use this skill

Use for:

- reviewing an implemented screen
- reviewing a PR touching Compose UI
- checking HTML/Stitch/Pencil migrations
- checking visual and architectural quality
- checking state/callback boundaries

Do not use for:

- broad data-layer review
- Gradle-only changes
- backend/network-only changes
- full architecture redesign unless requested

---

## Review checklist

| Area | Check |
|---|---|
| Scope | Only intended files were changed. |
| Architecture | Screen renders state and emits events. |
| State | State is immutable, explicit and render-ready. |
| Callbacks | Callbacks are preserved and clearly named. |
| Business logic | No business rules inside Composables. |
| Platform safety | No Android/iOS APIs in `shared/commonMain`. |
| Design system | Existing tokens/components are reused. |
| Performance | No expensive work during composition. |
| Accessibility | Text, touch targets and content descriptions are reasonable. |
| Validation | Build/test/lint status is honest. |

---

## Compose architecture checks

The screen should follow this flow:

```text
State -> Screen -> Event/Callback -> State holder/ViewModel -> State
```

Rules:

- Stateless or mostly stateless screens are preferred.
- State holders/ViewModels own UI logic when complexity requires it.
- Composables should not call repositories, preferences or database APIs directly.
- Navigation callbacks should be passed down, not hidden in deeply nested Composables unless the existing project convention does so.
- State should be hoisted when multiple composables need it or when it must survive recomposition.

---

## Compose implementation checks

Check for:

- unnecessary nested `Box`/`Column`/`Row`
- hardcoded colors when tokens exist
- hardcoded spacing when spacing tokens exist
- duplicated UI that should be a small private composable
- over-abstraction for one-off UI
- unstable objects passed unnecessarily
- expensive calculations during composition
- incorrect `remember` usage
- misuse of `derivedStateOf`
- missing `key`/stable identity in dynamic lists when needed
- excessive `Modifier` chains that hide intent

---

## KMP checks

For shared Compose screens:

- no `android.*` imports
- no Activity/Fragment/Context dependencies
- no UIKit/Swift-only APIs
- use common resources when the UI is shared
- platform-specific behavior is behind source sets or shared contracts
- Android-only UI belongs in `androidApp/` or `androidMain`
- iOS-only UI belongs in `iosApp/` or `iosMain`

---

## Accessibility checks

Check:

- interactive elements have sufficient touch area
- icons have meaningful content descriptions or are explicitly decorative
- text is readable and not clipped
- color alone is not the only signal for critical states
- font scale should not obviously break the layout
- disabled/loading/error states are understandable

---

## Visual migration checks

When reviewing a screen migrated from HTML/screenshot:

- match layout hierarchy first
- match spacing before micro details
- match typography and colors using design tokens when possible
- do not copy CSS structure blindly
- prefer Compose layout intent over HTML markup
- note differences caused by the project design system

---

## Severity levels

Use severity levels:

| Severity | Meaning |
|---|---|
| Critical | Bug, crash, broken architecture boundary, data loss or security issue. |
| Major | Likely maintainability, state, UX or validation problem. |
| Minor | Small readability, naming, style or localized UI issue. |
| Suggestion | Optional improvement. |

---

## Review output

Return:

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

If there are no issues:

```text
Review:
- No blocking issues found.

Validation:
- ...

Recommended next action:
- ...
```
