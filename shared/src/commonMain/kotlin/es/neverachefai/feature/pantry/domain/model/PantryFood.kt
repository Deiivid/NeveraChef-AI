package es.neverachefai.feature.pantry.domain.model

data class PantryFood(
    val id: String,
    val name: String,
    val quantity: String,
    val quantityValue: String = "",
    val quantityUnit: String = "",
    val category: String,
    val locationKey: String,
    val expiryLabel: String?,
    val expiryDateIso: String? = null,
    val iconKey: String,
)
