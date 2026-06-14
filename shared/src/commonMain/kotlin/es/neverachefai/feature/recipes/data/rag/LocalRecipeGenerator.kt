package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.data.ai.LocalLlmEngine
import es.neverachefai.feature.recipes.data.ai.createPlatformLocalLlmEngine
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeAiStatus
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult
import es.neverachefai.feature.recipes.domain.model.RecipeStep

internal class LocalRecipeGenerator(
    private val retriever: LocalRecipeRetriever = LocalRecipeRetriever(),
    private val promptBuilder: LocalRecipePromptBuilder = LocalRecipePromptBuilder(),
    private val llmRanker: LocalLlmRecipeRanker = LocalLlmRecipeRanker(),
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
        val llmText = when {
            !llmEngine.isReady -> null
            matches.isEmpty() -> llmEngine.generate(promptBuilder.buildFallback(safeRequest))
            matches.size > 1 -> llmEngine.generate(promptBuilder.build(safeRequest, matches))
            else -> null
        }
        val usedLlmFallback = matches.isEmpty() && !llmText.isNullOrBlank()
        val usedLlmRanking = matches.size > 1 && !llmText.isNullOrBlank()
        val recipes = if (matches.isEmpty()) {
            listOfNotNull(llmText.toFallbackRecipe(safeRequest))
        } else {
            llmRanker.rerank(matches, llmText)
                .mapIndexed { index, match ->
                    validator.validate(
                        recipe = match.toRecipe(index = index),
                        pantryFoods = safeRequest.pantryFoods,
                        maxMinutes = safeRequest.maxMinutes,
                    )
                }
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
                message = aiStatusMessage(
                    llmReady = llmEngine.isReady,
                    usedLlmFallback = usedLlmFallback && recipes.isNotEmpty(),
                    usedLlmRanking = usedLlmRanking,
                    hasRecipes = recipes.isNotEmpty(),
                ),
            ),
        )
    }

    private fun RecipeMatch.toRecipe(
        index: Int,
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
            optionalIngredients = card.optionalIngredients,
            ingredientAmounts = card.ingredientAmounts,
            steps = card.steps.toRecipeSteps(),
            sourceRecipeIds = listOf(card.id),
            sourceName = card.sourceName,
            sourceUrl = card.sourceUrl,
            generationMode = RecipeGenerationMode.LOCAL_RAG,
            groundingSummary = card.sourceName?.let { source ->
                "Receta verificada con $source"
            } ?: "Basada en la tarjeta local ${card.id}",
        )
    }

    private fun String?.toFallbackRecipe(request: RecipeGenerationRequest): Recipe? {
        val draft = LocalLlmRecipeParser.parse(this) ?: return null
        val card = RecipeKnowledgeCard(
            id = "llm-fallback-${draft.title.toRecipeIdSlug()}",
            title = draft.title,
            mealType = request.mealTypes.firstOrNull() ?: es.neverachefai.feature.recipes.domain.model.MealType.ANY,
            estimatedMinutes = (draft.estimatedMinutes ?: request.maxMinutes).coerceIn(5, request.maxMinutes),
            difficulty = draft.difficulty,
            servings = (draft.servings ?: 1).coerceIn(1, 6),
            healthScore = 55,
            requiredIngredients = draft.ingredientsUsed + draft.missingIngredients,
            optionalIngredients = draft.optionalIngredients,
            steps = draft.steps.take(5),
        )
        val image = LocalRecipeImageCatalog.imageFor(card)
        val recipe = Recipe(
            id = card.id,
            title = card.title,
            imageKey = image.assetKey,
            image = image,
            mealType = card.mealType,
            estimatedMinutes = card.estimatedMinutes,
            difficulty = card.difficulty,
            servings = card.servings,
            matchScore = 45,
            healthScore = card.healthScore,
            ingredientsUsed = draft.ingredientsUsed,
            missingIngredients = draft.missingIngredients,
            optionalIngredients = draft.optionalIngredients,
            ingredientAmounts = emptyMap(),
            steps = draft.steps.toRecipeSteps(),
            sourceRecipeIds = listOf("llm-fallback"),
            sourceName = null,
            sourceUrl = null,
            generationMode = RecipeGenerationMode.LOCAL_LLM,
            groundingSummary = "Fallback LLM local validado con inventario; sin tarjeta RAG local",
        )
        return validator.validate(
            recipe = recipe,
            pantryFoods = request.pantryFoods,
            maxMinutes = request.maxMinutes,
        ).takeIf { it.ingredientsUsed.isNotEmpty() && it.steps.size >= 2 }
    }

    private fun aiStatusMessage(
        llmReady: Boolean,
        usedLlmFallback: Boolean,
        usedLlmRanking: Boolean,
        hasRecipes: Boolean,
    ): String {
        return when {
            usedLlmFallback -> "RAG local sin coincidencias; fallback LLM local activo"
            usedLlmRanking -> "RAG local activo; LLM local usado solo para ordenar"
            !hasRecipes && llmReady -> "RAG local sin coincidencias; fallback LLM local sin receta valida"
            llmReady -> "RAG local activo; LLM local disponible"
            else -> "RAG local activo; LLM local pendiente de modelo"
        }
    }

    private fun String.toRecipeIdSlug(): String {
        return lowercase()
            .map { char ->
                when (char) {
                    'á', 'à', 'ä', 'â' -> 'a'
                    'é', 'è', 'ë', 'ê' -> 'e'
                    'í', 'ì', 'ï', 'î' -> 'i'
                    'ó', 'ò', 'ö', 'ô' -> 'o'
                    'ú', 'ù', 'ü', 'û' -> 'u'
                    'ñ' -> 'n'
                    else -> char
                }
            }
            .joinToString("")
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .take(48)
            .ifBlank { "receta" }
    }

    private fun List<String>.toRecipeSteps(): List<RecipeStep> {
        return mapIndexed { index, value ->
            RecipeStep(
                order = index + 1,
                title = value.substringBefore(".").trim().ifBlank { "Paso ${index + 1}" },
                instruction = value,
            )
        }
    }
}
