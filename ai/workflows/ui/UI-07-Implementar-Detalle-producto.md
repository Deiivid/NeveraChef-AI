# UI-07 Implementar Detalle producto

> Status: implemented
> Date: 2026-06-03
> Scope: detail product screen

## File

- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/ui/FoodDetailScreen.kt`

## Goal

Make the product detail screen feel like a secondary screen with a focused product card, no bottom navigation, and a clear read/edit split.

## Final Direction

Detail mode:

- No bottom navigation bar.
- Header with back button and title.
- Centered product hero card.
- Category icon centered.
- Category chip below the icon.
- Product name inside the hero card.
- Read-only info cards for quantity, location, expiry and added date.
- `Editar` button remains available in the detail screen.

Edit mode:

- Same visual hero language as detail mode.
- Category selection uses the hero carousel.
- Product name is editable inside the hero card.
- Quantity hides the duplicated read value and only shows the stepper.
- Location hides the duplicated read value and shows compact location chips in the same card.
- Location icon color and background follow the selected location.
- Cancel and save actions appear at the bottom of the edit form.

## Visual Rules

- Normal info cards use a neutral white background and the same neutral gray border.
- Expiry is the only highlighted card and uses warm orange styling.
- Card icons use the same visual size.
- Detail and edit typography should stay consistent:
  - hero category chip text: same size in both modes
  - product name text: same size in both modes
  - info card labels and values: same hierarchy across cards
- Avoid duplicated values in edit controls.

## Validation

Use:

```bash
./gradlew :androidApp:assembleDebug
```

Last known result during implementation: passed.
