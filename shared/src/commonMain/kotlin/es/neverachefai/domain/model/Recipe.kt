package es.neverachefai.domain.model

data class Recipe(
    val title: String,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val healthScore: Int,
    val ingredientsUsed: List<String>,
    val missingIngredients: List<String>,
)

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}
