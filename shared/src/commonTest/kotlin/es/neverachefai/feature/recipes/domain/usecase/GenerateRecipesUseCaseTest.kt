package es.neverachefai.feature.recipes.domain.usecase

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.data.local.LocalRecipeSeedData
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.MealType
import es.neverachefai.feature.recipes.domain.model.RecipeCategory
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeTag
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GenerateRecipesUseCaseTest {
    private val useCase = GenerateRecipesUseCase()

    @Test
    fun loadsFullRecipeCatalogFromRecipesMdCategories() {
        assertEquals(226, LocalRecipeSeedData.categoryEntryCount)
        assertEquals(201, LocalRecipeSeedData.recipes.size)
        assertTrue(LocalRecipeSeedData.recipes.any { RecipeCategory.KIDS in it.categories })
    }

    @Test
    fun calculatesMatchAndSeparatesAvailableAndMissingIngredients() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Macarrones"),
                    food(id = "2", name = "Atún"),
                    food(id = "3", name = "Tomate frito"),
                ),
                maxMinutes = 30,
            ),
        )

        val recipe = assertNotNull(result.recipes.firstOrNull { it.title == "Macarrones con atún y tomate" })
        assertEquals(60, recipe.matchScore)
        assertTrue(recipe.ingredientsUsed.contains("Macarrones"))
        assertTrue(recipe.ingredientsUsed.contains("Atún en conserva"))
        assertTrue(recipe.missingIngredients.contains("Aceite de oliva"))
        assertEquals(RecipeGenerationMode.LOCAL_SEED, recipe.generationMode)
        assertFalse(result.aiStatus.localLlmReady)
    }

    @Test
    fun scalesServingsByRuleOfThreeWithoutLinearTimeScaling() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Macarrones"),
                    food(id = "2", name = "Atún"),
                    food(id = "3", name = "Tomate frito"),
                    food(id = "4", name = "Aceite de oliva"),
                    food(id = "5", name = "Sal"),
                ),
                maxMinutes = 30,
                targetServings = 2,
            ),
        )

        val recipe = assertNotNull(result.recipes.firstOrNull { it.title == "Macarrones con atún y tomate" })
        assertEquals(2, recipe.servings)
        assertEquals("200 g", recipe.ingredientAmounts["Macarrones"])
        assertEquals("80 g", recipe.ingredientAmounts["Atún en conserva"])
        assertEquals(25, recipe.estimatedMinutes)
    }

    @Test
    fun ordersRecommendationsByMatchAndExpiringIngredients() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Garbanzos", expiryLabel = "Caduca en 2 días"),
                    food(id = "2", name = "Espinacas"),
                    food(id = "3", name = "Tomate"),
                    food(id = "4", name = "Macarrones"),
                    food(id = "5", name = "Atún"),
                ),
                maxMinutes = 30,
                maxResults = 5,
            ),
        )

        assertEquals("Garbanzos con espinacas", result.recipes.first().title)
        assertTrue(result.recommendations.first().expiringSoonIngredients.any { it.name == "Garbanzos cocidos" })
    }

    @Test
    fun appliesBasicFilters() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Macarrones"),
                    food(id = "2", name = "Atún"),
                    food(id = "3", name = "Tomate frito"),
                ),
                maxMinutes = 30,
                mealTypes = setOf(MealType.LUNCH),
                difficulties = setOf(Difficulty.EASY),
                categories = setOf(RecipeCategory.PASTA),
                tags = setOf(RecipeTag.PASTA),
            ),
        )

        assertTrue(result.recipes.isNotEmpty())
        assertTrue(result.recipes.all { it.category == RecipeCategory.PASTA })
        assertTrue(result.recipes.all { it.difficulty == Difficulty.EASY })
        assertTrue(result.recipes.all { RecipeTag.PASTA in it.tags })
    }

    @Test
    fun concreteIngredientsDoNotMatchGenericNames() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Pasta"),
                    food(id = "2", name = "Pescado"),
                    food(id = "3", name = "Huevo"),
                ),
                maxMinutes = 30,
                maxResults = 250,
            ),
        )

        val pasta = assertNotNull(result.recipes.firstOrNull { it.title == "Macarrones con atún y tomate" })
        val merluza = assertNotNull(result.recipes.firstOrNull { it.title == "Merluza rebozada" })
        assertTrue(pasta.missingIngredients.contains("Macarrones"))
        assertTrue(merluza.missingIngredients.contains("Merluza"))
    }

    private fun food(
        id: String,
        name: String,
        expiryLabel: String? = null,
    ): PantryFood {
        return PantryFood(
            id = id,
            name = name,
            quantity = "1 unidades",
            quantityValue = "1",
            quantityUnit = "unidades",
            category = "other",
            locationKey = "fridge",
            expiryLabel = expiryLabel,
            iconKey = "other",
        )
    }
}
