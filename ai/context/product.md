# NeveraChef AI - Product Context

> **Version:** 2026-06-01
> **Scope:** Product/domain rules agents must preserve.

Use this file when a task touches product behaviour, terminology, screens or data semantics. It is not an architecture guide.

## Product

NeveraChef AI is a food inventory and shopping assistant.

It helps users:

- track food stored at home
- manage fridge, pantry and freezer inventory
- build and complete a shopping list
- move bought products into inventory
- reduce forgotten or duplicated purchases

## Storage Locations

| Location | Meaning |
|---|---|
| `Nevera` | Fridge items that need refrigeration |
| `Despensa` | Pantry items stored at room temperature |
| `Congelador` | Frozen items |

Do not assume every product goes to `Nevera`.

## Core Entities

Inventory product:

| Field | Meaning |
|---|---|
| `name` | Product name |
| `category` | Product category |
| `amount` | Number of units |
| `weight` | Weight or package size |
| `location` | `Nevera`, `Despensa` or `Congelador` |

Shopping item:

| Field | Meaning |
|---|---|
| `name` | Product name |
| `category` | Product category when available |
| `amount` | Number of units to buy |
| `weight` | Weight/package size when available |
| `checked` | Persisted bought/selected state used for finalization |

## Product Invariants

- Amount and weight are independent.
- `+` / `-` changes amount only.
- Selecting `250g`, `500g`, `1kg`, etc. changes weight only.
- Category selection must remain visible after tapping.
- Location selection must remain visible after tapping.
- UI must not lose user input during recomposition.
- Product labels must not imply all inventory goes to the fridge.

Example temporary UI input state split, not a domain model:

```kotlin
var amount by mutableStateOf("1")
var weightAmount by mutableStateOf("")
var weightUnit by mutableStateOf("")
```

## Core Workflow

```text
Add product manually -> store in inventory
Add product to shopping list -> store in shopping list
Mark shopping item as bought -> checked = true
Finalize shopping list -> checked items move to inventory; unchecked items stay pending
```

## Shopping Finalization

Correct behaviour:

1. Load checked shopping items.
2. Convert checked items into inventory items.
3. Insert converted items into local inventory.
4. Remove or complete only checked shopping items.
5. Keep unchecked shopping items pending.
6. Use a transaction when supported.

Forbidden behaviour:

- Do not delete unchecked shopping items.
- Do not move unchecked items into inventory.
- Do not clear the whole shopping list blindly.
- Do not label finalization as `Añadir a nevera`.
- Do not run finalization multiple times because of recomposition or repeated effect collection.
- Do not invent inventory merge/deduplication rules unless explicitly defined.

Preferred labels:

| Label | Use |
|---|---|
| `Finalizar compra` | Complete checked shopping items and move them to inventory |
| `Añadir al inventario` | Add manually or move bought items into inventory |

## Inventory Rules

- Inventory should persist locally when persistence exists for the feature.
- Inventory must distinguish `Nevera`, `Despensa` and `Congelador`.
- Moving or adding products must preserve category, amount, weight and location when available.
- Inventory merge/deduplication behaviour is undefined unless explicitly requested.
- UI filters/tabs must not mutate stored product data.
- Visual labels must match product location.

## Shopping Rules

- Shopping items should persist locally when persistence exists for the feature.
- `checked` is persisted shopping state, not transient UI state.
- `checked` means bought/selected for finalization, not deleted.
- `checked` should survive app restart when shopping persistence exists.
- Finalization decides what happens to checked items.
- Unchecked items remain in the shopping list.
- Shopping state should survive app restart when persistence is available.

## Migration Rules

When migrating screens from HTML, Stitch, Pencil, Figma or screenshots:

- Do not redesign product flows.
- Do not change product terminology without explicit request.
- Do not change amount/weight semantics.
- Do not remove selected category/location states.
- Do not change shopping finalization behaviour.
- Do not introduce labels that imply every product goes to the fridge.

## Undefined Behaviour

Do not invent behaviour for:

- inventory merge/deduplication
- product category normalization
- default storage location for new or finalized products
- automatic expiration dates
- remote sync or account behaviour

## Product Checklist

Before completing product-related work, verify:

- Amount and weight remain independent.
- Locations support `Nevera`, `Despensa` and `Congelador`.
- Checked shopping items can be finalized.
- Checked state is persisted when shopping persistence exists.
- Finalization is not triggered by recomposition.
- Unchecked shopping items remain pending.
- Required persistence is preserved.
- UI labels do not narrow inventory to only fridge.
- No product data is silently lost.
- Undefined behaviours were not invented.
