package es.neverachefai.feature.recipes.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val imageKey: String,
    val image: RecipeImage,
    val mealType: MealType,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val servings: Int,
    val matchScore: Int,
    val healthScore: Int,
    val ingredientsUsed: List<String>,
    val missingIngredients: List<String>,
    val optionalIngredients: List<String>,
    val steps: List<String>,
    val sourceRecipeIds: List<String>,
    val generationMode: RecipeGenerationMode,
    val groundingSummary: String,
)

data class RecipeImage(
    val assetKey: String,
    val sourceType: RecipeImageSourceType,
    val sourceUrl: String?,
    val licenseName: String,
    val licenseUrl: String?,
    val authorName: String?,
    val attributionRequired: Boolean,
    val generationPrompt: String?,
)

enum class RecipeImageSourceType {
    WIKIMEDIA_COMMONS,
    OPENVERSE,
    IMAGEGEN_PENDING,
    LOCAL_FAMILY_FALLBACK,
}

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    ANY,
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}

enum class RecipeGenerationMode {
    LOCAL_RAG,
    LOCAL_LLM,
}
