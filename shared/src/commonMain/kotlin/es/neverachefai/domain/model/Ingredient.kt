package es.neverachefai.domain.model

data class Ingredient(
    val name: String,
    val quantity: String = "",
    val location: IngredientLocation = IngredientLocation.FRIDGE,
)

enum class IngredientLocation {
    FRIDGE,
    PANTRY,
    FREEZER,
}
