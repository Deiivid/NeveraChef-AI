package es.neverachefai.feature.shopping.data.datasource

import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.ShoppingItemRecord

class ShoppingLocalDataSource {
    fun loadItems(): List<ShoppingItemRecord> = LocalAppContentStore.loadShoppingItems()
    fun saveItems(items: List<ShoppingItemRecord>) = LocalAppContentStore.saveShoppingItems(items)
}
