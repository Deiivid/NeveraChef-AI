package es.neverachefai.feature.recipes.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val imageKey: String = "",
    val image: RecipeImage = localRecipeImage(imageKey),
    val mealType: MealType = MealType.ANY,
    val estimatedMinutes: Int = 0,
    val difficulty: Difficulty,
    val servings: Int,
    val matchScore: Int,
    val healthScore: Int,
    val ingredientsUsed: List<String>,
    val missingIngredients: List<String>,
    val optionalIngredients: List<String>,
    val ingredientAmounts: Map<String, String> = emptyMap(),
    val steps: List<RecipeStep>,
    val sourceRecipeIds: List<String>,
    val sourceName: String? = null,
    val sourceUrl: String? = null,
    val generationMode: RecipeGenerationMode,
    val groundingSummary: String,
    val name: String = title,
    val description: String = "",
    val category: RecipeCategory = RecipeCategory.HEALTHY,
    val categories: List<RecipeCategory> = listOf(category),
    val mealTypes: List<MealType> = listOf(mealType),
    val baseServings: Int = servings,
    val preparationTimeMinutes: Int = estimatedMinutes,
    val cookingTimeMinutes: Int = 0,
    val totalTimeMinutes: Int = estimatedMinutes,
    val tags: List<RecipeTag> = emptyList(),
    val ingredients: List<RecipeIngredient> = emptyList(),
    val selectedServings: Int = servings,
    val source: RecipeSource = RecipeSource.Internal,
)

data class RecipeIngredient(
    val foodId: String,
    val name: String,
    val quantity: Double,
    val unit: IngredientUnit,
    val optional: Boolean = false,
    val mainIngredient: Boolean = false,
)

data class RecipeStep(
    val order: Int,
    val title: String,
    val instruction: String,
    val tip: String? = null,
    val estimatedMinutes: Int? = null,
)

data class RecipeRecommendation(
    val recipe: Recipe,
    val matchPercentage: Int,
    val availableIngredients: List<RecipeIngredient>,
    val missingRequiredIngredients: List<RecipeIngredient>,
    val missingOptionalIngredients: List<RecipeIngredient>,
    val expiringSoonIngredients: List<RecipeIngredient>,
    val score: Double,
    val reasons: List<String> = emptyList(),
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
    SNACK,
    ANY,
}

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}

enum class RecipeCategory {
    BREAKFAST,
    SALAD,
    PASTA,
    RICE,
    CHICKEN,
    PORK,
    BEEF,
    FISH,
    LEGUMES,
    QUICK_DINNER,
    KIDS,
    SUMMER,
    WINTER,
    HEALTHY,
    VEGETABLES,
    SOUP,
    DESSERT,
}

enum class RecipeTag {
    QUICK,
    HEALTHY,
    KIDS,
    FAMILY,
    MEDITERRANEAN,
    HIGH_PROTEIN,
    LIGHT,
    BATCH_COOKING,
    CHEAP,
    SUMMER,
    WINTER,
    VEGETARIAN,
    FISH,
    MEAT,
    LEGUMES,
    PASTA,
    RICE,
    BREAKFAST,
    DINNER,
}

enum class IngredientUnit {
    GRAM,
    MILLILITER,
    UNIT,
    TABLESPOON,
    TEASPOON,
    PINCH,
    TO_TASTE,
}

sealed class RecipeSource {
    data object Internal : RecipeSource()
    data class Adapted(
        val sourceName: String?,
        val sourceUrl: String?,
        val sourceAuthor: String?,
    ) : RecipeSource()
    data object GeneratedByLlm : RecipeSource()
}

enum class RecipeGenerationMode {
    LOCAL_SEED,
    LOCAL_RAG,
    LOCAL_LLM,
}

private fun localRecipeImage(assetKey: String): RecipeImage {
    return RecipeImage(
        assetKey = assetKey,
        sourceType = RecipeImageSourceType.IMAGEGEN_PENDING,
        sourceUrl = null,
        licenseName = "Generated local asset",
        licenseUrl = null,
        authorName = "NeveraChef AI",
        attributionRequired = false,
        generationPrompt = null,
    )
}
