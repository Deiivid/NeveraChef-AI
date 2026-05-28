package es.neverachefai.feature.shopping.domain.model

data class ShoppingListItem(
    val id: String,
    val name: String,
    val quantity: String,
    val quantityValue: String = "",
    val quantityUnit: String = "",
    val checked: Boolean = false,
    val category: String,
    val destinationKey: String,
    val iconKey: String,
)
