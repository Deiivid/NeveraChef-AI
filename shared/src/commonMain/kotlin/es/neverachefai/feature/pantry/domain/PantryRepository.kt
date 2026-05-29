package es.neverachefai.feature.pantry.domain

import es.neverachefai.feature.pantry.domain.model.PantryFood

interface PantryRepository {
    fun loadFoods(): List<PantryFood>
    fun saveFoods(foods: List<PantryFood>)
}
