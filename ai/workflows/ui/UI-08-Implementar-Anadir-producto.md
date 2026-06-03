# UI-08 Implementar Añadir producto

> Status: implemented
> Scope: add product flow

## Main Files

- `shared/src/commonMain/kotlin/es/neverachefai/feature/shopping/ui/AddShoppingProductScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/app/NeveraChefApp.kt`

## Result

- Add product screen follows the product-entry mock structure.
- User can provide product name, category, quantity, location and voice/camera entry mode.
- Added shopping products persist into local shopping state.

## Notes

- Keep amount and weight separate.
- Category, quantity and location selection must remain visible after tapping.
- Do not route UI directly to persistence outside app/state wiring.

## Validation

Use:

```bash
./gradlew :shared:compileCommonMainKotlinMetadata
```
