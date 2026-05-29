package es.neverachefai.feature.shopping.domain.usecase

import es.neverachefai.feature.shopping.domain.ShoppingRepository
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem

class SaveShoppingItemsUseCase(
    private val repository: ShoppingRepository,
) {
    operator fun invoke(items: List<ShoppingListItem>) = repository.saveItems(items)
}
