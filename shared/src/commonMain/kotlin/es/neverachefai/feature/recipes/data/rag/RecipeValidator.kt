package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.domain.model.Recipe

internal class RecipeValidator {
    fun validate(
        recipe: Recipe,
        pantryFoods: List<PantryFood>,
        maxMinutes: Int,
    ): Recipe {
        val availableTokens = pantryFoods.flatMap { IngredientNormalizer.tokensFor(it.name) }.toSet()
        val validatedUsed = recipe.ingredientsUsed.filter { ingredient ->
            IngredientNormalizer.matchesAvailable(ingredient, availableTokens)
        }
        val validatedMissing = recipe.missingIngredients.filterNot { ingredient ->
            IngredientNormalizer.matchesAvailable(ingredient, availableTokens)
        }
        return recipe.copy(
            estimatedMinutes = recipe.estimatedMinutes.coerceAtMost(maxMinutes),
            ingredientsUsed = validatedUsed,
            missingIngredients = validatedMissing,
            matchScore = recipe.matchScore.coerceIn(1, 100),
            healthScore = recipe.healthScore.coerceIn(1, 100),
            steps = recipe.steps.take(5).filter { it.instruction.isNotBlank() },
        )
    }
}
