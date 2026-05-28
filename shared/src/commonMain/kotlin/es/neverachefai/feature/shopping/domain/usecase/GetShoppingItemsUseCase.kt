package es.neverachefai.feature.shopping.domain.usecase

import es.neverachefai.feature.shopping.domain.ShoppingRepository
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem

class GetShoppingItemsUseCase(
    private val repository: ShoppingRepository,
) {
    operator fun invoke(): List<ShoppingListItem> = repository.loadItems()
}
