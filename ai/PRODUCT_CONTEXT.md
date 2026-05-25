# NeveraChef AI — Product Context

Use this file when an AI agent, Codex task or developer needs product/domain context for NeveraChef AI.

This file explains what the app does and which product rules must not be broken. It is not an architecture guide.

---

## Product overview

NeveraChef AI is a food inventory and shopping assistant.

Its purpose is to help users:

- track food stored at home
- manage pantry/fridge/freezer inventory
- build a shopping list
- mark products as bought
- move bought products into inventory
- reduce forgotten or duplicated food purchases

---

## Core storage locations

| Location | Meaning |
|---|---|
| Nevera | Fridge items. Products that need refrigeration. |
| Despensa | Pantry items. Products stored at room temperature. |
| Congelador | Frozen items. Products stored in the freezer. |

Products may belong to any of these locations. Do not assume every product goes to `Nevera`.

---

## Core entities

### Product / inventory item

A product added manually or moved from shopping list into inventory.

Expected fields:

| Field | Meaning |
|---|---|
| `name` | Product name. |
| `category` | Product category. |
| `amount` | Number of units. |
| `weight` | Product weight or package size. |
| `location` | Nevera, Despensa or Congelador. |

### Shopping item

A product planned for purchase.

Expected fields:

| Field | Meaning |
|---|---|
| `name` | Product name. |
| `category` | Product category when available. |
| `amount` | Number of units to buy. |
| `weight` | Weight/package size when available. |
| `checked` | Whether the product has already been bought. |

---

## Amount vs weight

Amount and weight are different concepts.

| Concept | Meaning | Example |
|---|---|---|
| Amount | Number of units. | `3` apples, `2` milk bottles. |
| Weight | Weight or package size. | `250g`, `500g`, `1kg`. |

Rules:

- Pressing `+` or `-` in amount must not modify weight.
- Selecting `250g`, `500g`, `1kg`, etc. must not modify amount.
- Do not store broken labels such as `3 kg` when the user only increased amount.
- UI must make selected category and selected location visible after tapping.

Recommended UI state split:

```kotlin
var amount by mutableStateOf("1")
var weightAmount by mutableStateOf("")
var weightUnit by mutableStateOf("")
```

---

## Core product workflow

```text
Add product manually
        ↓
Store in local inventory

Add product to shopping list
        ↓
Store in local shopping list

Mark shopping product as bought
        ↓
checked = true

Finalize shopping list
        ↓
checked items move to inventory
unchecked items remain in shopping list
```

---

## Shopping list finalization

Finalizing the shopping list must not clear everything blindly.

Correct behaviour:

1. Load checked shopping list items.
2. Convert checked items into inventory items.
3. Insert converted items into local inventory.
4. Remove or mark completed only checked shopping items.
5. Keep unchecked shopping items pending.
6. Use a transaction when supported.

Forbidden behaviour:

- Do not delete unchecked shopping items during finalization.
- Do not move unchecked items into inventory.
- Do not clear the whole shopping list blindly.
- Do not label finalization as `Añadir a nevera`, because products may go to nevera, despensa or congelador.

Recommended labels:

| Label | Use |
|---|---|
| `Finalizar compra` | Complete checked shopping items and move them to inventory. |
| `Añadir al inventario` | Add manually or move bought items into inventory. |

Avoid:

| Label | Reason |
|---|---|
| `Añadir a nevera` | Too narrow; products may belong to pantry or freezer. |

---

## Add product flow

Expected fields:

- product name
- category
- location
- amount
- weight

Rules:

- Category must remain visibly selected after tapping.
- Location must remain visibly selected after tapping.
- Amount and weight must remain independent.
- The UI should not lose user input during recomposition.
- Product creation should persist locally when the user confirms the action.

---

## Inventory rules

- Inventory items should persist locally.
- Inventory should distinguish between Nevera, Despensa and Congelador.
- Moving or adding products must preserve category, amount, weight and location when available.
- UI filters/tabs should not change stored product data.
- Visual labels must match the product location.

---

## Shopping list rules

- Shopping items should persist locally.
- Checked means bought, not deleted.
- Finalization decides what happens with checked items.
- Unchecked items remain in the shopping list.
- Shopping state must survive app restart when persistence is available.

---

## Screen migration product rules

When migrating screens from HTML/Stitch/Pencil/Figma:

- Do not redesign product flows.
- Do not change product terminology without explicit request.
- Do not change amount/weight semantics.
- Do not remove selected category/location states.
- Do not change shopping finalization behaviour.
- Do not introduce labels that imply all products go to the fridge.

---

## Product-risk checklist

Before marking product-related work complete, check:

| Check | Required |
|---|---|
| Amount and weight are independent. | Yes |
| Location supports Nevera, Despensa and Congelador. | Yes |
| Checked shopping items can be finalized. | Yes |
| Unchecked shopping items remain pending. | Yes |
| Inventory persists locally when required. | Yes |
| UI labels do not narrow inventory to only fridge. | Yes |
| No product data is silently lost. | Yes |
