# NOTEBOOKLM_CONTEXT

Last updated: 2026-05-15

## 1. Product summary

NeveraChef-AI is a mobile app focused on turning home ingredient lists into practical recipe decisions and shopping actions, with AI as a functional core.

## 2. MVP summary

Included:

1. Manual ingredient input.
2. Preferences input.
3. AI recipe generation by text.
4. Recipe detail and missing ingredients.
5. Shopping list from missing items.

Excluded:

- Auth/login
- Cloud sync
- Full backend platform

## 3. Architecture summary

- Kotlin Multiplatform base.
- Shared Compose UI strategy for Android and iOS.
- Local-first data approach.
- JSON contract for AI integration.
- Incremental modular architecture.

## 4. Screen map summary

- Onboarding (welcome + guidance + initial preferences)
- Home
- Pantry
- Add ingredients
- Review ingredients
- Generate recipes
- Recipe results
- Recipe detail
- Shopping list
- Settings

## 5. Decision summary

Accepted:

1. Android-first MVP.
2. Local-first no-login MVP.
3. JSON structured AI output.
4. Shared Compose UI default for both platforms.
5. Incremental modularization.

Pending:

1. AI provider selection.
2. Persistence milestone split.
3. Weekly menu MVP classification.

## 6. Terminology and glossary

- Pantry: food inventory (fridge/pantry/freezer perspective).
- Missing ingredients: ingredients required by recipe but unavailable locally.
- Local-first: app remains useful without account/cloud dependency.
- PromptBuilder: component that composes the AI request from app state.
- DTO mapper: transform external AI JSON into domain models.

## 7. Questions NotebookLM should answer

1. What is the MVP and what is explicitly out of scope?
2. Why is Android-first selected?
3. Why use shared Compose UI instead of native split now?
4. Which screens are confirmed by Notion and Pencil?
5. What are the riskiest technical dependencies for Sprint 3?
6. What still needs product confirmation?
