package es.neverachefai.feature.pantry.domain.usecase

import es.neverachefai.feature.pantry.domain.PantryRepository
import es.neverachefai.feature.pantry.domain.model.PantryFood

class GetPantryFoodsUseCase(
    private val repository: PantryRepository,
) {
    operator fun invoke(): List<PantryFood> = repository.loadFoods()
}
