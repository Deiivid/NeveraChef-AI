package es.neverachefai.feature.recipes.data.local

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.MealType
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeCategory
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeIngredient
import es.neverachefai.feature.recipes.domain.model.RecipeRecommendation
import es.neverachefai.feature.recipes.domain.model.RecipeTag

internal class LocalRecipeRecommendationEngine(
    private val recipes: List<Recipe> = LocalRecipeSeedData.recipes,
) {
    fun recommend(request: RecipeGenerationRequest): List<RecipeRecommendation> {
        val safeRequest = request.copy(
            maxMinutes = request.maxMinutes.coerceIn(5, 180),
            maxResults = request.maxResults.coerceIn(1, 250),
        )
        val pantry = safeRequest.pantryFoods.filter { it.name.isNotBlank() }
        val targetServings = safeRequest.targetServings ?: 0
        val focusedFood = pantry.firstOrNull { it.id == safeRequest.focusFoodId }

        return recipes
            .asSequence()
            .filter { it.matchesFilters(safeRequest) }
            .map { recipe ->
                val scaled = if (targetServings > 0) {
                    RecipeServingsScaler.scale(recipe, targetServings)
                } else {
                    recipe
                }
                scaled.toRecommendation(pantry, focusedFood, safeRequest)
            }
            .sortedWith(
                compareByDescending<RecipeRecommendation> { it.score }
                    .thenBy { it.missingRequiredIngredients.size }
                    .thenBy { it.recipe.totalTimeMinutes },
            )
            .take(safeRequest.maxResults)
            .toList()
    }

    private fun Recipe.matchesFilters(request: RecipeGenerationRequest): Boolean {
        return totalTimeMinutes <= request.maxMinutes &&
            (request.difficulties.isEmpty() || difficulty in request.difficulties) &&
            (request.categories.isEmpty() || categories.any { it in request.categories }) &&
            (request.mealTypes.isEmpty() || mealTypes.any { it in request.mealTypes || it == MealType.ANY }) &&
            (request.tags.isEmpty() || tags.any { it in request.tags })
    }

    private fun Recipe.toRecommendation(
        pantryFoods: List<PantryFood>,
        focusedFood: PantryFood?,
        request: RecipeGenerationRequest,
    ): RecipeRecommendation {
        val required = ingredients.filterNot { it.optional }
        val optional = ingredients.filter { it.optional }
        val availableRequired = required.filter { ingredient -> pantryFoods.any { ingredient.matchesFood(it) } }
        val availableOptional = optional.filter { ingredient -> pantryFoods.any { ingredient.matchesFood(it) } }
        val missingRequired = required - availableRequired.toSet()
        val missingOptional = optional - availableOptional.toSet()
        val expiring = (availableRequired + availableOptional).filter { ingredient ->
            pantryFoods.any { food -> ingredient.matchesFood(food) && food.isExpiringSoon() }
        }
        val matchPercentage = if (required.isEmpty()) {
            100
        } else {
            ((availableRequired.size.toDouble() / required.size.toDouble()) * 100).toInt().coerceIn(0, 100)
        }
        val score = score(
            matchPercentage = matchPercentage,
            missingRequired = missingRequired,
            expiring = expiring,
            focusedFood = focusedFood,
            request = request,
        )
        val recommendationRecipe = copy(
            matchScore = matchPercentage,
            ingredientsUsed = (availableRequired + availableOptional).map { it.name },
            missingIngredients = missingRequired.map { it.name },
            optionalIngredients = optional.map { it.name },
            ingredientAmounts = ingredients.associate { it.name to it.displayAmount() },
            generationMode = RecipeGenerationMode.LOCAL_SEED,
            groundingSummary = "Motor local determinista",
        )
        return RecipeRecommendation(
            recipe = recommendationRecipe,
            matchPercentage = matchPercentage,
            availableIngredients = availableRequired + availableOptional,
            missingRequiredIngredients = missingRequired,
            missingOptionalIngredients = missingOptional,
            expiringSoonIngredients = expiring,
            score = score,
            reasons = reasons(matchPercentage, expiring, request),
        )
    }

    private fun Recipe.score(
        matchPercentage: Int,
        missingRequired: List<RecipeIngredient>,
        expiring: List<RecipeIngredient>,
        focusedFood: PantryFood?,
        request: RecipeGenerationRequest,
    ): Double {
        var score = matchPercentage.toDouble()
        score += when {
            matchPercentage == 100 -> 50.0
            matchPercentage >= 75 -> 35.0
            matchPercentage >= 50 -> 20.0
            else -> 0.0
        }
        if (expiring.isNotEmpty()) score += 35.0
        if (totalTimeMinutes <= request.maxMinutes) score += 10.0
        if (request.difficulties.isEmpty() || difficulty in request.difficulties) score += 10.0
        if (request.mealTypes.isNotEmpty() && mealTypes.any { it in request.mealTypes }) score += 10.0
        if (request.tags.isNotEmpty() && tags.any { it in request.tags }) score += 10.0
        if (focusedFood != null && ingredients.any { it.matchesFood(focusedFood) }) score += 25.0
        if (missingRequired.any { it.mainIngredient }) score -= 20.0
        if (missingRequired.size > 1) score -= 10.0
        return score
    }

    private fun reasons(
        matchPercentage: Int,
        expiring: List<RecipeIngredient>,
        request: RecipeGenerationRequest,
    ): List<String> {
        return buildList {
            add("Coincidencia del $matchPercentage% con tu inventario.")
            if (expiring.isNotEmpty()) add("Aprovecha ingredientes próximos a caducar.")
            if (request.maxMinutes <= 30) add("Encaja con el tiempo disponible.")
        }
    }

    private fun RecipeIngredient.matchesFood(food: PantryFood): Boolean {
        val foodTokens = tokens(food.name)
        val ingredientTokens = tokens(foodId) + tokens(name)
        return ingredientTokens.any { it in foodTokens }
    }

    private fun PantryFood.isExpiringSoon(): Boolean {
        val label = expiryLabel?.lowercase().orEmpty()
        if ("caduca hoy" in label || "caducado" in label || "caduca mañana" in label) return true
        val days = Regex("(\\d+)").find(label)?.groupValues?.getOrNull(1)?.toIntOrNull()
        return days != null && "día" in label && days <= 3
    }

    private fun tokens(value: String): Set<String> {
        val canonical = canonical(value)
        return canonical
            .split(" ")
            .filter { it.isNotBlank() && it !in ignoredTokens }
            .flatMap { token -> listOf(token, aliases[token] ?: token) }
            .toSet() + canonical
    }

    private fun canonical(value: String): String {
        val normalized = value
            .lowercase()
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
            .replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
        return aliases[normalized] ?: normalized
    }

    private val ignoredTokens = setOf("de", "del", "la", "el", "los", "las", "con", "para", "en", "o", "y")

    private val aliases = mapOf(
        "huevos" to "huevo",
        "patatas" to "patata",
        "tomates" to "tomate",
        "espinacas" to "espinaca",
        "garbanzos" to "garbanzo",
        "lentejas" to "lenteja",
        "macarrones" to "macarron",
        "atun" to "atun",
        "atún" to "atun",
        "atun en conserva" to "atun",
        "aceite de oliva" to "aceite oliva",
        "aceite para freir" to "aceite oliva",
        "aceite para freír" to "aceite oliva",
        "tomate frito" to "tomate frito",
        "caldo de verduras" to "caldo",
        "agua o caldo" to "agua",
        "merluzas" to "merluza",
        "fideos" to "fideo",
        "yogur natural" to "yogur",
        "frutas" to "fruta",
    )
}
