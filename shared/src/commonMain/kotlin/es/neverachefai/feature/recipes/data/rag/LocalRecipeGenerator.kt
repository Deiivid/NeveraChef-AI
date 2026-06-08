package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.data.ai.LocalLlmEngine
import es.neverachefai.feature.recipes.data.ai.createPlatformLocalLlmEngine
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeAiStatus
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult

internal class LocalRecipeGenerator(
    private val retriever: LocalRecipeRetriever = LocalRecipeRetriever(),
    private val promptBuilder: LocalRecipePromptBuilder = LocalRecipePromptBuilder(),
    private val validator: RecipeValidator = RecipeValidator(),
    private val llmEngine: LocalLlmEngine = createPlatformLocalLlmEngine(),
) {
    fun generate(request: RecipeGenerationRequest): RecipeGenerationResult {
        val safeRequest = request.copy(
            pantryFoods = request.pantryFoods.take(50),
            maxMinutes = request.maxMinutes.coerceIn(5, 60),
            maxResults = request.maxResults.coerceIn(1, 10),
        )
        val matches = retriever.search(
            pantryFoods = safeRequest.pantryFoods,
            focusFoodId = safeRequest.focusFoodId,
            maxMinutes = safeRequest.maxMinutes,
            maxResults = safeRequest.maxResults,
            mealTypes = safeRequest.mealTypes,
        )
        val prompt = promptBuilder.build(safeRequest, matches)
        val llmText = if (llmEngine.isReady) llmEngine.generate(prompt) else null
        val recipes = matches
            .mapIndexed { index, match ->
                validator.validate(
                    recipe = match.toRecipe(
                        index = index,
                        generationMode = if (llmText != null) {
                            RecipeGenerationMode.LOCAL_LLM
                        } else {
                            RecipeGenerationMode.LOCAL_RAG
                        },
                    ),
                    pantryFoods = safeRequest.pantryFoods,
                    maxMinutes = safeRequest.maxMinutes,
                )
            }

        val focusedFoodName = safeRequest.pantryFoods.firstOrNull { it.id == safeRequest.focusFoodId }?.name
        return RecipeGenerationResult(
            recipes = recipes,
            focusedFoodName = focusedFoodName,
            pantryFoodCount = safeRequest.pantryFoods.size,
            aiStatus = RecipeAiStatus(
                localRagReady = true,
                localLlmReady = llmEngine.isReady,
                modelName = llmEngine.modelName,
                message = if (llmEngine.isReady) {
                    "RAG local con LLM local activo"
                } else {
                    "RAG local activo; LLM local pendiente de modelo"
                },
            ),
        )
    }

    private fun RecipeMatch.toRecipe(
        index: Int,
        generationMode: RecipeGenerationMode,
    ): Recipe {
        val rawScore = score + usedIngredients.size * 6 - missingIngredients.size * 12
        val matchScore = rawScore.coerceIn(35, 98)
        val image = LocalRecipeImageCatalog.imageFor(card)
        return Recipe(
            id = "${card.id}-$index",
            title = card.title,
            imageKey = image.assetKey,
            image = image,
            mealType = card.mealType,
            estimatedMinutes = card.estimatedMinutes,
            difficulty = card.difficulty,
            servings = card.servings,
            matchScore = matchScore,
            healthScore = card.healthScore,
            ingredientsUsed = usedIngredients,
            missingIngredients = missingIngredients,
            optionalIngredients = card.optionalIngredients.filterNot { it in optionalMatches },
            steps = card.steps,
            sourceRecipeIds = listOf(card.id),
            generationMode = generationMode,
            groundingSummary = "Basada solo en la tarjeta local ${card.id}",
        )
    }
}
