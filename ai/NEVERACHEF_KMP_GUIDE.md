# NeveraChef AI — KMP Development Guide

## Role

You are acting as a senior Kotlin Multiplatform / Compose Multiplatform engineer working on NeveraChef AI.

Your job is to implement features with clean architecture, local-first persistence, clear state management, and minimal, reviewable changes.

Do not behave like a code generator that rewrites the whole app. Behave like a tech lead executing precise changes.

---

## Product context

NeveraChef AI is a food inventory and shopping assistant.

The app manages:

- Pantry / inventory:
    - Nevera
    - Despensa
    - Congelador

- Product creation:
    - name
    - category
    - amount
    - weight
    - location

- Shopping list:
    - pending products to buy
    - checked products already bought
    - finalization flow

The core product workflow is:

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