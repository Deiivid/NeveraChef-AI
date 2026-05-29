# Skill: Nevera / Tu inventario

## Task name and goal
- Task: Nevera / Tu inventario
- Goal: Implement the inventory list screen to match the Notion reference design and keep data persisted locally using repository/persistence patterns already present in NeveraChefAI.

## Source screenshot
- Visual source of truth: `/Users/davidnavarro/.codex/generated_images/019e6b5b-0032-7d82-aa29-084eb25abcb6/Tu inventario.png`
- Original requested relative reference: `./codex/generated_images/Tu inventario.png`

## Relevant Notion requirements
- Build the inventory screen for fridge/pantry/freezer.
- Show grouped items by location and counts.
- Keep inventory data persisted across restarts.
- Keep UI state in presentation layer conventions used by this repo.
- Keep data access behind existing persistence abstractions.
- Do not place persistence logic inside Composables.

## Allowed files / scope
- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/ui/PantryScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/app/NeveraChefApp.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/core/persistence/LocalAppContentStore.kt`
- Optional tiny support updates in nearby pantry UI files only when required for compile.

## UI implementation rules
- Use existing `NeveraChefColors` and Material 3 components.
- Match hierarchy from screenshot: title + count, hero illustration, location summary cards, search field, grouped sections, item cards, floating add button.
- Keep section headers with location icon and count badge.
- Keep card rows with category icon, name, quantity, expiration pill, and trailing chevron.
- Preserve existing bottom navigation behavior from app scaffolding.

## Architecture rules
- Preserve current pragmatic architecture; do not introduce a new framework.
- Persist/read inventory through `LocalAppContentStore` (or existing equivalent), not directly in composables.
- Keep screen as rendering + callbacks; mutation/persistence handled by app-level state holder currently used in project.
- Keep existing navigation contracts for pantry list/detail/add.

## Local database persistence rules
- Use existing pantry records in `LocalAppContentStore`.
- Ensure category/location mapping remains stable (`fridge`, `pantry`, `freezer`).
- Any default seed updates must remain backward compatible with existing decode versions.

## Validation commands
- `./gradlew :shared:compileKotlinAndroid`
- `./gradlew :shared:compileKotlinIosX64`

## Final checklist
- Screen visually aligned with reference screenshot.
- Group counts and item rendering are correct.
- Inventory survives restart via local persistence.
- No persistence calls remain inside pantry composables.
- Navigation to detail/add remains functional.
- Build validation commands pass.
