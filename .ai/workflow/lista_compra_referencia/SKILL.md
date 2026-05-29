# SKILL: lista_compra_referencia

## Nombre exacto de la tarea
- lista_compra_referencia

## Objetivo
- Implementar `ShoppingListScreen` para que replique visualmente la captura `Lista de compra.png` manteniendo navegación, estado y persistencia existentes.

## Imagen fuente
- `/Users/davidnavarro/.codex/generated_images/019e6b5b-0032-7d82-aa29-084eb25abcb6/Lista de compra.png`

## Requisitos de UI
- Cabecera con título `Lista de compra`, subtítulo de conteo y bloque visual derecho.
- Acciones superiores derechas (notificación y menú).
- Chips de métricas en una fila: añadidos, marcados y filtro.
- Lista de tarjetas con:
  - estado marcado/no marcado
  - nombre y cantidad
  - icono de categoría en bloque de fondo suave
  - acción de avance a la derecha
- Botón flotante circular verde en esquina inferior derecha.
- Mantener bottom navigation existente del scaffold.
- Espaciados, radios y jerarquía visual similares a la referencia.

## Reglas de arquitectura
- No introducir arquitectura nueva.
- Mantener patrón actual: Compose screen + repositorio existente + store local.
- No mover lógica de negocio a composables fuera del patrón ya presente.
- Reusar `ShoppingRepositoryImpl` y `PantryRepositoryImpl`.

## Reglas de persistencia local
- El marcado/desmarcado y borrado deben persistir con `ShoppingRepositoryImpl.saveItems`.
- La finalización de compra debe mantener el flujo actual: mover marcados a despensa y persistir.
- No introducir almacenamiento nuevo ni cambiar contrato de serialización.

## Archivos permitidos
- `shared/src/commonMain/kotlin/es/neverachefai/feature/shopping/ui/ShoppingListScreen.kt`
- `shared/src/commonMain/composeResources/drawable/*` (solo si hace falta asset adicional)
- `.ai/workflow/lista_compra_referencia/SKILL.md`

## Comandos de validación
- `./gradlew :shared:compileKotlinAndroid`

## Checklist final
- UI alineada con la referencia de lista de compra.
- Persistencia de marcado y eliminación comprobada en código.
- Sin cambios fuera de scope.
- Sin dependencias nuevas.
- Compilación de `shared` válida.
