package es.neverachefai.feature.shopping.data

import es.neverachefai.feature.shopping.data.datasource.ShoppingLocalDataSource
import es.neverachefai.feature.shopping.data.mapper.toDomain
import es.neverachefai.feature.shopping.data.mapper.toRecord
import es.neverachefai.feature.shopping.domain.ShoppingRepository
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem

class ShoppingRepositoryImpl(
    private val localDataSource: ShoppingLocalDataSource = ShoppingLocalDataSource(),
) : ShoppingRepository {
    override fun loadItems(): List<ShoppingListItem> = localDataSource.loadItems().map { it.toDomain() }

    override fun saveItems(items: List<ShoppingListItem>) = localDataSource.saveItems(items.map { it.toRecord() })
}
