# UI-02 Implementar Inventario

> Status: implemented
> Scope: inventory / pantry list

## Main Files

- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/ui/PantryScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/ui/FoodDetailScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/data/mapper/PantryFoodMapper.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/feature/pantry/domain/model/PantryFood.kt`

## Result

- Inventory list exists as the main pantry surface.
- Products can open the detail screen.
- Product data keeps category, quantity, location, expiry and added date semantics.

## Notes

- Keep inventory product labels independent from fridge-only wording.
- Preserve `Nevera`, `Despensa` and `Congelador`.

## Validation

Use:

```bash
./gradlew :androidApp:assembleDebug
```
