package es.neverachefai.feature.pantry.data.datasource

import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.PantryFoodRecord

class PantryLocalDataSource {
    fun loadFoods(): List<PantryFoodRecord> = LocalAppContentStore.loadPantryFoods()
    fun saveFoods(foods: List<PantryFoodRecord>) = LocalAppContentStore.savePantryFoods(foods)
}
