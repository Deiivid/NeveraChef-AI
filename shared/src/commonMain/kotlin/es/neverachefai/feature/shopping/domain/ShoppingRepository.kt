package es.neverachefai.feature.shopping.domain

import es.neverachefai.feature.shopping.domain.model.ShoppingListItem

interface ShoppingRepository {
    fun loadItems(): List<ShoppingListItem>
    fun saveItems(items: List<ShoppingListItem>)
}
