# UI-03 Implementar Lista de compra

> Status: implemented
> Scope: shopping list

## Main Files

- `shared/src/commonMain/kotlin/es/neverachefai/feature/shopping/ui/ShoppingListScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/app/NeveraChefApp.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/core/persistence/LocalAppContentStore.kt`

## Result

- Shopping list screen exists.
- Checked shopping items can be finalized into inventory.
- Shopping state is local-first.

## Notes

- `checked` means selected/bought for finalization.
- Do not clear unchecked shopping items blindly.
- Finalization should preserve category, quantity and destination when available.

## Validation

Use:

```bash
./gradlew :androidApp:assembleDebug
```
