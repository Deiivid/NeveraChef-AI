package es.neverachefai.feature.shopping.data.mapper

import es.neverachefai.core.persistence.ShoppingItemRecord
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem

fun ShoppingItemRecord.toDomain(): ShoppingListItem = ShoppingListItem(
    id = id,
    name = name,
    quantity = quantity,
    quantityValue = quantityValue,
    quantityUnit = quantityUnit,
    checked = checked,
    category = category,
    destinationKey = destinationKey,
    iconKey = iconKey,
)

fun ShoppingListItem.toRecord(): ShoppingItemRecord = ShoppingItemRecord(
    id = id,
    name = name,
    quantity = quantity,
    quantityValue = quantityValue,
    quantityUnit = quantityUnit,
    checked = checked,
    category = category,
    destinationKey = destinationKey,
    iconKey = iconKey,
)
