package es.neverachefai.feature.recipes.domain.usecase

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeImageSourceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GenerateRecipesUseCaseTest {
    private val useCase = GenerateRecipesUseCase()

    @Test
    fun focusedFoodPrioritizesMatchingRecipes() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Huevos"),
                    food(id = "2", name = "Plátano"),
                    food(id = "3", name = "Fresas"),
                ),
                focusFoodId = "2",
                maxMinutes = 20,
            ),
        )

        assertTrue(result.recipes.isNotEmpty())
        assertTrue(result.recipes.first().ingredientsUsed.contains("platano"))
        assertEquals(RecipeGenerationMode.LOCAL_RAG, result.recipes.first().generationMode)
    }

    @Test
    fun maxMinutesFiltersSlowRecipes() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Patatas"),
                    food(id = "2", name = "Huevos"),
                ),
                maxMinutes = 12,
            ),
        )

        assertTrue(result.recipes.isNotEmpty())
        assertTrue(result.recipes.all { it.estimatedMinutes <= 12 })
        assertNotEquals("Patata salteada con huevo", result.recipes.first().title)
    }

    @Test
    fun generatedRecipesCarryLocalImageMetadata() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Arroz"),
                    food(id = "2", name = "Pollo"),
                ),
                maxMinutes = 30,
            ),
        )

        assertTrue(result.recipes.isNotEmpty())
        assertTrue(result.recipes.all { it.image.assetKey.isNotBlank() })
        assertTrue(result.recipes.all { it.image.licenseName.isNotBlank() })
        assertTrue(
            result.recipes.any {
                it.image.sourceType == RecipeImageSourceType.WIKIMEDIA_COMMONS ||
                    it.image.sourceType == RecipeImageSourceType.OPENVERSE
            },
        )
    }

    @Test
    fun seafoodRecipesUseMoreSpecificImageFamilies() {
        val result = useCase(
            RecipeGenerationRequest(
                pantryFoods = listOf(
                    food(id = "1", name = "Huevos"),
                    food(id = "2", name = "Pescado"),
                    food(id = "3", name = "Fideos"),
                ),
                maxMinutes = 30,
                maxResults = 10,
            ),
        )

        val tortillaBacalao = assertNotNull(result.recipes.firstOrNull { it.title == "Tortilla de bacalao sencilla" })
        val fideua = assertNotNull(result.recipes.firstOrNull { it.title == "Fideuá sencilla de pescado" })
        assertEquals("recipe_tortilla_bacalao", tortillaBacalao.imageKey)
        assertEquals("recipe_fideua_pescado", fideua.imageKey)
        assertTrue(fideua.ingredientsUsed.contains("fideo"))
    }

    private fun food(
        id: String,
        name: String,
    ): PantryFood {
        return PantryFood(
            id = id,
            name = name,
            quantity = "1 unidades",
            quantityValue = "1",
            quantityUnit = "unidades",
            category = "other",
            locationKey = "fridge",
            expiryLabel = null,
            iconKey = "other",
        )
    }
}
