package es.neverachefai.core.persistence

import es.neverachefai.core.preferences.AppPreferences

private const val STORAGE_VERSION = "v1"
private const val FIELD_SEPARATOR = '#'
private const val PANTRY_FOODS_KEY = "local_pantry_foods"
private const val SHOPPING_ITEMS_KEY = "local_shopping_items"

data class PantryFoodRecord(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val locationKey: String,
    val expiryLabel: String?,
    val iconKey: String,
)

data class ShoppingItemRecord(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val destinationKey: String,
    val iconKey: String,
)

object LocalAppContentStore {
    fun loadPantryFoods(): List<PantryFoodRecord> {
        val stored = decodePantryFoods(AppPreferences.getString(PANTRY_FOODS_KEY))
        if (stored != null) return stored
        return defaultPantryFoods().also { savePantryFoods(it) }
    }

    fun savePantryFoods(foods: List<PantryFoodRecord>) {
        AppPreferences.setString(PANTRY_FOODS_KEY, encodePantryFoods(foods))
    }

    fun loadShoppingItems(): List<ShoppingItemRecord> {
        val stored = decodeShoppingItems(AppPreferences.getString(SHOPPING_ITEMS_KEY))
        if (stored != null) return stored
        return defaultShoppingItems().also { saveShoppingItems(it) }
    }

    fun saveShoppingItems(items: List<ShoppingItemRecord>) {
        AppPreferences.setString(SHOPPING_ITEMS_KEY, encodeShoppingItems(items))
    }

    private fun defaultPantryFoods(): List<PantryFoodRecord> = listOf(
        PantryFoodRecord("1", "Espinacas", "1 bolsa", "Verdura", "fridge", "mañana", "spinach"),
        PantryFoodRecord("2", "Huevos", "6 uds", "Proteína", "fridge", "2 días", "egg"),
        PantryFoodRecord("3", "Arroz integral", "1 kg", "Cereal", "pantry", "bajo", "rice"),
        PantryFoodRecord("4", "Lentejas", "500 g", "Legumbre", "pantry", "ok", "lentils"),
        PantryFoodRecord("5", "Filetes de pescado", "2 uds", "Proteína", "freezer", "20 días", "fish"),
        PantryFoodRecord("6", "Caldo casero", "1 litro", "Preparado", "freezer", "1 mes", "soup"),
    )

    private fun defaultShoppingItems(): List<ShoppingItemRecord> = listOf(
        ShoppingItemRecord("yogurt", "Yogur natural", "4 uds", "lacteos", "fridge", "yogurt"),
        ShoppingItemRecord("tomato", "Tomates cherry", "500 g", "verdura", "fridge", "tomato"),
        ShoppingItemRecord("broth", "Caldo de verduras", "1 brick", "receta sopa", "pantry", "soup"),
        ShoppingItemRecord("rice", "Arroz redondo", "1 kg", "basico", "pantry", "rice"),
        ShoppingItemRecord("fish", "Filetes de pescado", "2 uds", "proteina", "freezer", "fish"),
        ShoppingItemRecord("stock", "Caldo casero", "1 litro", "preparado", "freezer", "soup"),
    )

    private fun encodePantryFoods(foods: List<PantryFoodRecord>): String {
        return buildString {
            append(STORAGE_VERSION)
            foods.forEach { food ->
                append('\n')
                append(encodeLine(food.id, food.name, food.quantity, food.category, food.locationKey, food.expiryLabel.orEmpty(), food.iconKey))
            }
        }
    }

    private fun encodeShoppingItems(items: List<ShoppingItemRecord>): String {
        return buildString {
            append(STORAGE_VERSION)
            items.forEach { item ->
                append('\n')
                append(encodeLine(item.id, item.name, item.quantity, item.category, item.destinationKey, item.iconKey))
            }
        }
    }

    private fun decodePantryFoods(raw: String?): List<PantryFoodRecord>? {
        val rows = decodeRows(raw, 7) ?: return null
        return rows.map {
            PantryFoodRecord(
                id = it[0],
                name = it[1],
                quantity = it[2],
                category = it[3],
                locationKey = it[4],
                expiryLabel = it[5].ifEmpty { null },
                iconKey = it[6],
            )
        }
    }

    private fun decodeShoppingItems(raw: String?): List<ShoppingItemRecord>? {
        val rows = decodeRows(raw, 6) ?: return null
        return rows.map {
            ShoppingItemRecord(
                id = it[0],
                name = it[1],
                quantity = it[2],
                category = it[3],
                destinationKey = it[4],
                iconKey = it[5],
            )
        }
    }

    private fun encodeLine(vararg fields: String): String {
        return buildString {
            fields.forEach { field ->
                append(field.length)
                append(FIELD_SEPARATOR)
                append(field)
            }
        }
    }

    private fun decodeRows(raw: String?, expectedFieldCount: Int): List<List<String>>? {
        if (raw == null) return null
        val lines = raw.split('\n')
        if (lines.firstOrNull() != STORAGE_VERSION) return null

        val rows = mutableListOf<List<String>>()
        for (line in lines.drop(1)) {
            if (line.isEmpty()) continue
            val decoded = decodeLine(line, expectedFieldCount) ?: return null
            rows += decoded
        }
        return rows
    }

    private fun decodeLine(line: String, expectedFieldCount: Int): List<String>? {
        val fields = ArrayList<String>(expectedFieldCount)
        var index = 0

        repeat(expectedFieldCount) {
            val separatorIndex = line.indexOf(FIELD_SEPARATOR, index)
            if (separatorIndex < 0) return null

            val length = line.substring(index, separatorIndex).toIntOrNull() ?: return null
            val start = separatorIndex + 1
            val end = start + length
            if (end > line.length) return null

            fields += line.substring(start, end)
            index = end
        }

        return if (index == line.length) fields else null
    }
}
