package es.neverachefai.feature.pantry.domain.usecase

import es.neverachefai.feature.pantry.domain.PantryRepository
import es.neverachefai.feature.pantry.domain.model.PantryFood

class SavePantryFoodsUseCase(
    private val repository: PantryRepository,
) {
    operator fun invoke(foods: List<PantryFood>) = repository.saveFoods(foods)
}
