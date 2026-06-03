# UI-04 Implementar Ajustes local-first

> Status: implemented
> Scope: settings

## Main Files

- `shared/src/commonMain/kotlin/es/neverachefai/feature/settings/ui/SettingsScreen.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/app/NeveraChefApp.kt`
- `shared/src/commonMain/kotlin/es/neverachefai/core/preferences/AppPreferences.kt`

## Result

- Settings screen supports local-first preferences and app reset flow.
- Permissions are surfaced through the app host callbacks.

## Notes

- Keep settings local-first unless remote account/sync is explicitly requested.
- Do not expose secrets or platform credentials in UI state.

## Validation

Use:

```bash
./gradlew :androidApp:assembleDebug
```
