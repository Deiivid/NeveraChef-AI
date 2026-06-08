package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.domain.model.MealType

internal class LocalRecipeRetriever(
    private val knowledgeCards: List<RecipeKnowledgeCard> = LocalRecipeKnowledgeBase.cards,
) {
    fun search(
        pantryFoods: List<PantryFood>,
        focusFoodId: String?,
        maxMinutes: Int,
        maxResults: Int,
        mealTypes: Set<MealType>,
    ): List<RecipeMatch> {
        val availableFoods = pantryFoods
            .filter { it.name.isNotBlank() }
            .map { it.name }
        if (availableFoods.isEmpty()) return emptyList()

        val focusedFood = pantryFoods.firstOrNull { it.id == focusFoodId }?.name
        val availableTokens = availableFoods.flatMap { IngredientNormalizer.tokensFor(it) }.toSet()
        val focusedTokens = focusedFood?.let { IngredientNormalizer.tokensFor(it) }.orEmpty()

        return knowledgeCards
            .asSequence()
            .filter { it.estimatedMinutes <= maxMinutes }
            .filter { mealTypes.isEmpty() || it.mealType in mealTypes || it.mealType == MealType.ANY }
            .mapNotNull { card ->
                val requiredMatches = card.requiredIngredients.filter { ingredient ->
                    ingredientMatchesAvailable(ingredient, availableTokens)
                }
                val optionalMatches = card.optionalIngredients.filter { ingredient ->
                    ingredientMatchesAvailable(ingredient, availableTokens)
                }
                val missing = card.requiredIngredients.filterNot { it in requiredMatches }
                val focusBonus = if (focusedTokens.isNotEmpty() && card.allIngredients().any { ingredient ->
                        IngredientNormalizer.tokensFor(ingredient).any { it in focusedTokens }
                    }
                ) 45 else 0
                val score = requiredMatches.size * 30 +
                    optionalMatches.size * 10 +
                    focusBonus -
                    missing.size * 18 -
                    card.estimatedMinutes / 4

                if (requiredMatches.isEmpty() && optionalMatches.isEmpty() && focusBonus == 0) {
                    null
                } else {
                    RecipeMatch(
                        card = card,
                        usedIngredients = (requiredMatches + optionalMatches).distinct(),
                        missingIngredients = missing,
                        optionalMatches = optionalMatches,
                        score = score.coerceAtLeast(1),
                    )
                }
            }
            .sortedWith(
                compareByDescending<RecipeMatch> { it.score }
                    .thenBy { it.missingIngredients.size }
                    .thenBy { it.card.estimatedMinutes },
            )
            .take(maxResults.coerceIn(1, 10))
            .toList()
    }

    private fun ingredientMatchesAvailable(
        ingredient: String,
        availableTokens: Set<String>,
    ): Boolean {
        return IngredientNormalizer.tokensFor(ingredient).any { it in availableTokens }
    }

    private fun RecipeKnowledgeCard.allIngredients(): List<String> {
        return requiredIngredients + optionalIngredients
    }
}
