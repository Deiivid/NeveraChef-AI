package es.neverachefai.core.persistence

import es.neverachefai.core.preferences.AppPreferences

private const val STORAGE_VERSION = "v4"
private const val FIELD_SEPARATOR = '#'
private const val PANTRY_FOODS_KEY = "local_pantry_foods"
private const val SHOPPING_ITEMS_KEY = "local_shopping_items"

data class PantryFoodRecord(
    val id: String,
    val name: String,
    val quantity: String,
    val quantityValue: String = "",
    val quantityUnit: String = "",
    val category: String,
    val locationKey: String,
    val expiryLabel: String?,
    val expiryDateIso: String? = null,
    val addedDateIso: String? = null,
    val iconKey: String,
)

data class ShoppingItemRecord(
    val id: String,
    val name: String,
    val quantity: String,
    val quantityValue: String = "",
    val quantityUnit: String = "",
    val checked: Boolean = false,
    val category: String,
    val destinationKey: String,
    val iconKey: String,
)

object LocalAppContentStore {
    fun loadPantryFoods(): List<PantryFoodRecord> {
        val stored = decodePantryFoods(AppPreferences.getString(PANTRY_FOODS_KEY))
        if (stored != null) {
            val migrated = migrateDefaultPantryFoods(stored)
            if (migrated != stored) savePantryFoods(migrated)
            return migrated
        }
        return defaultPantryFoods().also { savePantryFoods(it) }
    }

    fun savePantryFoods(foods: List<PantryFoodRecord>) {
        AppPreferences.setString(PANTRY_FOODS_KEY, encodePantryFoods(foods))
    }

    fun deletePantryFoods(ids: Set<String>) {
        if (ids.isEmpty()) return
        val remaining = loadPantryFoods().filterNot { it.id in ids }
        savePantryFoods(remaining)
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
        PantryFoodRecord("1", "Espinacas", "1 bolsa", "1", "bolsa", "Verdura", "fridge", "Caduca en 2 días", null, null, "spinach"),
        PantryFoodRecord("2", "Huevos", "6 uds", "6", "uds", "Proteína", "fridge", "Quedan 10 días", null, null, "eggs"),
        PantryFoodRecord("3", "Arroz integral", "1 kg", "1", "kg", "Cereal", "pantry", "Perenne", null, null, "rice"),
        PantryFoodRecord("4", "Lentejas", "500 g", "500", "g", "Legumbre", "pantry", "Perenne", null, null, "lentils"),
        PantryFoodRecord("5", "Filetes de pescado", "2 uds", "2", "uds", "Proteína", "freezer", "Quedan 3 meses", null, null, "fish"),
    )

    private fun defaultShoppingItems(): List<ShoppingItemRecord> = listOf(
        ShoppingItemRecord("yogurt", "Yogur natural", "4 uds", "4", "uds", false, "lacteos", "fridge", "yogurt"),
        ShoppingItemRecord("tomato", "Tomates cherry", "500 g", "500", "g", false, "verdura", "fridge", "tomato"),
        ShoppingItemRecord("broth", "Caldo de verduras", "1 brick", "1", "brick", false, "receta sopa", "pantry", "soup"),
        ShoppingItemRecord("rice", "Arroz redondo", "1 kg", "1", "kg", false, "basico", "pantry", "rice"),
        ShoppingItemRecord("fish", "Filetes de pescado", "2 uds", "2", "uds", false, "proteina", "freezer", "fish"),
        ShoppingItemRecord("stock", "Caldo casero", "1 litro", "1", "litro", false, "preparado", "freezer", "soup"),
    )

    private fun migrateDefaultPantryFoods(foods: List<PantryFoodRecord>): List<PantryFoodRecord> {
        return foods
            .filterNot { it.id == "6" && it.name == "Caldo casero" }
            .map { food ->
                when {
                    food.id == "1" && food.name == "Espinacas" -> food.copy(expiryLabel = "Caduca en 2 días", iconKey = "spinach")
                    food.id == "2" && food.name == "Huevos" -> food.copy(expiryLabel = "Quedan 10 días", iconKey = "eggs")
                    food.id == "3" && food.name == "Arroz integral" -> food.copy(expiryLabel = "Perenne", iconKey = "rice")
                    food.id == "4" && food.name == "Lentejas" -> food.copy(expiryLabel = "Perenne", iconKey = "lentils")
                    food.id == "5" && food.name == "Filetes de pescado" -> food.copy(expiryLabel = "Quedan 3 meses", iconKey = "fish")
                    else -> food
                }
            }
    }

    private fun encodePantryFoods(foods: List<PantryFoodRecord>): String {
        return buildString {
            append(STORAGE_VERSION)
            foods.forEach { food ->
                append('\n')
                append(
                    encodeLine(
                        food.id,
                        food.name,
                        food.quantity,
                        food.quantityValue,
                        food.quantityUnit,
                        food.category,
                        food.locationKey,
                        food.expiryLabel.orEmpty(),
                        food.expiryDateIso.orEmpty(),
                        food.addedDateIso.orEmpty(),
                        food.iconKey,
                    ),
                )
            }
        }
    }

    private fun encodeShoppingItems(items: List<ShoppingItemRecord>): String {
        return buildString {
            append(STORAGE_VERSION)
            items.forEach { item ->
                append('\n')
                append(
                    encodeLine(
                        item.id,
                        item.name,
                        item.quantity,
                        item.quantityValue,
                        item.quantityUnit,
                        item.checked.toString(),
                        item.category,
                        item.destinationKey,
                        item.iconKey,
                    ),
                )
            }
        }
    }

    private fun decodePantryFoods(raw: String?): List<PantryFoodRecord>? {
        val lines = raw?.split('\n') ?: return null
        val version = lines.firstOrNull() ?: return null
        val expectedFieldCount = when (version) {
            STORAGE_VERSION -> 11
            "v3" -> 10
            "v2" -> 9
            else -> 7
        }
        val rows = decodeRows(raw, expectedFieldCount) ?: return null
        return rows.map {
            if (it.size >= 11) {
                PantryFoodRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = it[3],
                    quantityUnit = it[4],
                    category = it[5],
                    locationKey = it[6],
                    expiryLabel = it[7].ifEmpty { null },
                    expiryDateIso = it[8].ifEmpty { null },
                    addedDateIso = it[9].ifEmpty { null },
                    iconKey = it[10],
                )
            } else if (it.size == 10) {
                PantryFoodRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = it[3],
                    quantityUnit = it[4],
                    category = it[5],
                    locationKey = it[6],
                    expiryLabel = it[7].ifEmpty { null },
                    expiryDateIso = it[8].ifEmpty { null },
                    addedDateIso = null,
                    iconKey = it[9],
                )
            } else if (it.size == 9) {
                PantryFoodRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = it[3],
                    quantityUnit = it[4],
                    category = it[5],
                    locationKey = it[6],
                    expiryLabel = it[7].ifEmpty { null },
                    expiryDateIso = null,
                    iconKey = it[8],
                )
            } else {
                val parsedQuantity = parseQuantityLabel(it[2])
                PantryFoodRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = parsedQuantity.first,
                    quantityUnit = parsedQuantity.second,
                    category = it[3],
                    locationKey = it[4],
                    expiryLabel = it[5].ifEmpty { null },
                    expiryDateIso = null,
                    iconKey = it[6],
                )
            }
        }
    }

    private fun decodeShoppingItems(raw: String?): List<ShoppingItemRecord>? {
        val lines = raw?.split('\n') ?: return null
        val version = lines.firstOrNull() ?: return null
        val expectedFieldCount = if (version == STORAGE_VERSION) 9 else 6
        val rows = decodeRows(raw, expectedFieldCount) ?: return null
        return rows.map {
            if (expectedFieldCount == 9) {
                ShoppingItemRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = it[3],
                    quantityUnit = it[4],
                    checked = it[5].toBooleanStrictOrNull() ?: false,
                    category = it[6],
                    destinationKey = it[7],
                    iconKey = it[8],
                )
            } else {
                val parsedQuantity = parseQuantityLabel(it[2])
                ShoppingItemRecord(
                    id = it[0],
                    name = it[1],
                    quantity = it[2],
                    quantityValue = parsedQuantity.first,
                    quantityUnit = parsedQuantity.second,
                    checked = false,
                    category = it[3],
                    destinationKey = it[4],
                    iconKey = it[5],
                )
            }
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
        if (lines.firstOrNull().isNullOrBlank()) return null

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

    private fun parseQuantityLabel(quantityLabel: String): Pair<String, String> {
        val parts = quantityLabel.trim().split(' ').filter { it.isNotBlank() }
        if (parts.isEmpty()) return "" to ""
        if (parts.size == 1) return parts.first() to ""
        val value = parts.first()
        val unit = parts.drop(1).joinToString(" ")
        return value to unit
    }
}
