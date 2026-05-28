package es.neverachefai.feature.pantry.data

import es.neverachefai.feature.pantry.data.datasource.PantryLocalDataSource
import es.neverachefai.feature.pantry.data.mapper.toDomain
import es.neverachefai.feature.pantry.data.mapper.toRecord
import es.neverachefai.feature.pantry.domain.PantryRepository
import es.neverachefai.feature.pantry.domain.model.PantryFood

class PantryRepositoryImpl(
    private val localDataSource: PantryLocalDataSource = PantryLocalDataSource(),
) : PantryRepository {
    override fun loadFoods(): List<PantryFood> = localDataSource.loadFoods().map { it.toDomain() }

    override fun saveFoods(foods: List<PantryFood>) = localDataSource.saveFoods(foods.map { it.toRecord() })
}
