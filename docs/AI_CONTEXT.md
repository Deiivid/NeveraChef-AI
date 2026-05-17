# AI_CONTEXT

Last updated: 2026-05-15

## 1. AI role in NeveraChef-AI

AI is a core product mechanism, not decoration.  
Primary MVP role: generate realistic recipes from available ingredients and user preferences.

## 2. Confirmed AI use cases

MVP-focused:

1. Recipe generation from ingredient list.
2. Missing ingredient extraction for shopping list.
3. Preference-aware adaptation (time, goal, family profile).

Post-MVP:

- Voice-to-ingredients pipeline.
- Image-based ingredient detection.
- Weekly menu advanced planning.

## 3. Technical flow (target)

```text
Ingredients + Preferences
 -> PromptBuilder
 -> AI model
 -> JSON response
 -> DTO parser
 -> Domain mapper
 -> UI state
```

## 4. Output contract policy

Required:

- Structured JSON output only.
- Validation and safe parsing.
- Fallback on malformed response.

## 5. Prompting policy

Prompt requirements:

- Prioritize available ingredients.
- Mark missing ingredients.
- Keep realistic, local-context recipes.
- Avoid rare/uncommon ingredients unless necessary.

## 6. Privacy and data considerations

- MVP is local-first.
- Ingredient and preferences are intended to remain on-device by default.
- Any remote AI call must be explicitly documented in UX and privacy docs.

## 7. Risks

1. Invalid JSON.
2. Hallucinated ingredients.
3. Poor alignment with dietary restrictions.
4. Latency/cost volatility for remote models.

## 8. Mitigations

1. Defensive parser + schema checks.
2. User review step before generation and before commitment to shopping list.
3. Error state UX with retry.
4. Prompt versioning and controlled test cases.

## 9. Pending technical decisions

1. AI provider selection for Sprint 3.
2. API key handling strategy for mobile MVP.
3. Prompt version storage location.
