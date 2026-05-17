# UI_SCREENS

Last updated: 2026-05-15

## 1. Visual references

Primary visual sources:

- Pencil handoff: `/Users/davidnavarro/Desktop/skils ia project/exports/neverachef/neverachef-codex-handoff.md`
- Visual export: `/Users/davidnavarro/Desktop/skils ia project/exports/neverachef/Screens 01-12.pdf`

## 2. Screen inventory

Status legend:

- `confirmed`: explicitly documented in Notion/Pencil.
- `planned`: accepted for MVP but not implemented.
- `future`: post-MVP.

| Screen | Goal | Source | Android | iOS | Status |
|---|---|---|---|---|---|
| Welcome / Onboarding start | Explain value quickly | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Onboarding steps | Explain flow and trust | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Initial Preferences | Collect baseline user profile | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Home | Main actions and shortcuts | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Pantry (Nevera/Despensa) | View stored foods | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Add Ingredients | Manual input for MVP | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Ingredient Review | Edit detected/manual list before AI | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Recipe Generation | Trigger AI flow | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Recipe Results | Show generated recipes | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Recipe Detail | Steps, used/missing ingredients | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Shopping List | Manage missing items | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |
| Settings | Privacy, preferences, local data reset | Notion + Pencil | implemented (shared Compose) | implemented (shared Compose) | done |

## 3. MVP screen priority

Priority P0:

1. Onboarding (Welcome + steps + initial preferences)
2. Home
3. Pantry + Add Ingredients
4. Recipe Results + Recipe Detail
5. Shopping List
6. Settings

## 4. UI state model per screen (minimum)

Each primary screen should define at least:

- `Loading`
- `Content`
- `Empty`
- `Error`

## 5. Navigation structure

Onboarding:
- Welcome -> Onboarding -> InitialPreferences

Main:
- Home
- Pantry
- Shopping
- Settings

Recipe flow:
- Pantry/Home -> AddIngredients -> Review -> Generate -> Results -> Detail -> AddMissingToShopping

## 6. Reusable component set (target)

- `PhoneScaffold`
- `AppHeader`
- `PrimaryButton`
- `SegmentedTabs`
- `FoodItemRow`
- `RecipeCard`
- `BottomPillNavigation`
- `LoadingState`
- `EmptyState`
- `ErrorState`
- `ConfirmDeleteDialog`

## 7. Pending confirmation

1. Final naming for `Pantry` vs `Nevera/Despensa` routes in code.
2. Whether weekly menu gets a dedicated screen in MVP.
3. iOS-specific navigation polish level for MVP demo (currently shared Compose parity).
