package es.neverachefai.feature.recipes.domain.usecase

import es.neverachefai.feature.recipes.data.local.LocalRecipeRecommendationEngine
import es.neverachefai.feature.recipes.domain.model.RecipeAiStatus
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult

class GenerateRecipesUseCase {
    private val engine: LocalRecipeRecommendationEngine = LocalRecipeRecommendationEngine()

    operator fun invoke(request: RecipeGenerationRequest): RecipeGenerationResult {
        val recommendations = engine.recommend(request)
        val focusedFoodName = request.pantryFoods.firstOrNull { it.id == request.focusFoodId }?.name
        return RecipeGenerationResult(
            recipes = recommendations.map { it.recipe },
            recommendations = recommendations,
            focusedFoodName = focusedFoodName,
            pantryFoodCount = request.pantryFoods.size,
            aiStatus = RecipeAiStatus(
                localRagReady = false,
                localLlmReady = false,
                modelName = null,
                message = "Motor local determinista con recetas seed; RAG y LLM preparados para futuras iteraciones",
            ),
        )
    }
}
