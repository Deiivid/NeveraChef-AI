package es.neverachefai.domain.model

data class ShoppingItem(
    val name: String,
    val quantity: String,
    val category: String,
    val checked: Boolean = false,
)
