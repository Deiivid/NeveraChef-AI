package es.neverachefai.feature.recipes.data.local

import es.neverachefai.feature.recipes.domain.model.IngredientUnit
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeIngredient

internal object RecipeServingsScaler {
    fun scale(
        recipe: Recipe,
        targetServings: Int,
    ): Recipe {
        val safeServings = targetServings.coerceIn(1, 12)
        val factor = safeServings.toDouble() / recipe.baseServings.coerceAtLeast(1).toDouble()
        val scaledIngredients = recipe.ingredients.map { it.scaleBy(factor) }
        val totalTime = recipe.totalTimeMinutes + extraTimeFor(factor)
        return recipe.copy(
            servings = safeServings,
            selectedServings = safeServings,
            estimatedMinutes = totalTime,
            totalTimeMinutes = totalTime,
            ingredients = scaledIngredients,
            ingredientAmounts = scaledIngredients.associate { it.name to it.displayAmount() },
        )
    }

    private fun RecipeIngredient.scaleBy(factor: Double): RecipeIngredient {
        if (unit == IngredientUnit.TO_TASTE || unit == IngredientUnit.PINCH) return this
        val scaled = quantity * factor
        val normalized = when (unit) {
            IngredientUnit.UNIT -> roundUnits(scaled)
            IngredientUnit.TABLESPOON,
            IngredientUnit.TEASPOON -> roundToHalf(scaled)
            IngredientUnit.GRAM,
            IngredientUnit.MILLILITER -> roundToTensIfLarge(scaled)
            IngredientUnit.PINCH,
            IngredientUnit.TO_TASTE -> quantity
        }
        return copy(quantity = normalized)
    }

    private fun extraTimeFor(factor: Double): Int {
        return when {
            factor >= 2.0 -> 8
            factor >= 1.5 -> 5
            else -> 0
        }
    }

    private fun roundUnits(value: Double): Double {
        return when {
            value < 1.0 -> 1.0
            value < 2.0 -> roundToHalf(value)
            else -> kotlin.math.round(value).coerceAtLeast(1.0)
        }
    }

    private fun roundToHalf(value: Double): Double {
        return kotlin.math.round(value * 2.0) / 2.0
    }

    private fun roundToTensIfLarge(value: Double): Double {
        return if (value >= 100.0) {
            kotlin.math.round(value / 10.0) * 10.0
        } else {
            kotlin.math.round(value)
        }
    }
}
