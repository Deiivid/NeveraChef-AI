package es.neverachefai.feature.pantry.data.mapper

import es.neverachefai.core.persistence.PantryFoodRecord
import es.neverachefai.feature.pantry.domain.model.PantryFood

fun PantryFoodRecord.toDomain(): PantryFood = PantryFood(
    id = id,
    name = name,
    quantity = quantity,
    quantityValue = quantityValue,
    quantityUnit = quantityUnit,
    category = category,
    locationKey = locationKey,
    expiryLabel = expiryLabel,
    expiryDateIso = expiryDateIso,
    iconKey = iconKey,
)

fun PantryFood.toRecord(): PantryFoodRecord = PantryFoodRecord(
    id = id,
    name = name,
    quantity = quantity,
    quantityValue = quantityValue,
    quantityUnit = quantityUnit,
    category = category,
    locationKey = locationKey,
    expiryLabel = expiryLabel,
    expiryDateIso = expiryDateIso,
    iconKey = iconKey,
)
