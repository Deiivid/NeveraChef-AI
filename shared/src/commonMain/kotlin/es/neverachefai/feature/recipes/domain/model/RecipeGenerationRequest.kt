package es.neverachefai.feature.recipes.domain.model

import es.neverachefai.feature.pantry.domain.model.PantryFood

data class RecipeGenerationRequest(
    val pantryFoods: List<PantryFood>,
    val focusFoodId: String? = null,
    val maxMinutes: Int = 30,
    val maxResults: Int = 5,
    val mealTypes: Set<MealType> = emptySet(),
)

data class RecipeGenerationResult(
    val recipes: List<Recipe>,
    val focusedFoodName: String?,
    val pantryFoodCount: Int,
    val aiStatus: RecipeAiStatus,
)

data class RecipeAiStatus(
    val localRagReady: Boolean,
    val localLlmReady: Boolean,
    val modelName: String?,
    val message: String,
)
