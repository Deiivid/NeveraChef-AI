package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.MealType

internal data class RecipeKnowledgeCard(
    val id: String,
    val title: String,
    val imageKey: String = "",
    val mealType: MealType,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val servings: Int,
    val healthScore: Int,
    val requiredIngredients: List<String>,
    val optionalIngredients: List<String>,
    val steps: List<String>,
    val tags: Set<String> = emptySet(),
)

internal data class RecipeMatch(
    val card: RecipeKnowledgeCard,
    val usedIngredients: List<String>,
    val missingIngredients: List<String>,
    val optionalMatches: List<String>,
    val score: Int,
)
