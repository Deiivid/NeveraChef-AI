package es.neverachefai.feature.recipes.domain.usecase

import es.neverachefai.feature.recipes.data.rag.LocalRecipeGenerator
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult

class GenerateRecipesUseCase {
    private val generator: LocalRecipeGenerator = LocalRecipeGenerator()

    operator fun invoke(request: RecipeGenerationRequest): RecipeGenerationResult {
        return generator.generate(request)
    }
}
