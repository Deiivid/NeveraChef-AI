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

    private fun defaultPantryFoods(): List<PantryFoodRecord> = defaultCorePantryFoods() + recipeDemoPantryFoods()

    private fun defaultCorePantryFoods(): List<PantryFoodRecord> = listOf(
        PantryFoodRecord("1", "Espinacas", "1 bolsa", "1", "bolsa", "Verdura", "fridge", "Caduca en 2 días", null, null, "spinach"),
        PantryFoodRecord("2", "Huevos", "6 uds", "6", "uds", "Proteína", "fridge", "Quedan 10 días", null, null, "eggs"),
        PantryFoodRecord("3", "Arroz integral", "1 kg", "1", "kg", "Cereal", "pantry", "Perenne", null, null, "rice"),
        PantryFoodRecord("4", "Lentejas", "500 g", "500", "g", "Legumbre", "pantry", "Perenne", null, null, "lentils"),
        PantryFoodRecord("5", "Merluza", "2 filetes", "2", "filetes", "Proteína", "freezer", "Quedan 3 meses", null, null, "fish"),
    )

    private fun recipeDemoPantryFoods(): List<PantryFoodRecord> = listOf(
        PantryFoodRecord("recipe_demo_6", "Patatas", "1 kg", "1", "kg", "Verduras", "pantry", "Perenne", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_7", "Tomates", "6 uds", "6", "uds", "Verduras", "fridge", "Caduca en 5 días", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_8", "Cebollas", "3 uds", "3", "uds", "Verduras", "pantry", "Perenne", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_9", "Pimientos", "3 uds", "3", "uds", "Verduras", "fridge", "Caduca en 6 días", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_10", "Calabacines", "2 uds", "2", "uds", "Verduras", "fridge", "Caduca en 5 días", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_11", "Zanahorias", "500 g", "500", "g", "Verduras", "fridge", "Quedan 12 días", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_12", "Ajos", "1 cabeza", "1", "cabeza", "Verduras", "pantry", "Perenne", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_13", "Garbanzos", "500 g", "500", "g", "Legumbres", "pantry", "Perenne", null, null, "legumes"),
        PantryFoodRecord("recipe_demo_14", "Alubias blancas", "500 g", "500", "g", "Legumbres", "pantry", "Perenne", null, null, "legumes"),
        PantryFoodRecord("recipe_demo_15", "Macarrones", "500 g", "500", "g", "Pasta", "pantry", "Perenne", null, null, "pasta"),
        PantryFoodRecord("recipe_demo_16", "Pan", "1 barra", "1", "barra", "Pan", "pantry", "Consumir pronto", null, null, "bread"),
        PantryFoodRecord("recipe_demo_17", "Pechuga de pollo", "2 uds", "2", "uds", "Carne", "fridge", "Caduca en 2 días", null, null, "meat"),
        PantryFoodRecord("recipe_demo_18", "Jamón serrano", "100 g", "100", "g", "Carne", "fridge", "Quedan 7 días", null, null, "meat"),
        PantryFoodRecord("recipe_demo_19", "Atún en conserva", "3 latas", "3", "latas", "Conservas", "pantry", "Perenne", null, null, "canned_food"),
        PantryFoodRecord("recipe_demo_20", "Guisantes", "300 g", "300", "g", "Congelados", "freezer", "Quedan 4 meses", null, null, "frozen"),
        PantryFoodRecord("recipe_demo_21", "Champiñones", "250 g", "250", "g", "Verduras", "fridge", "Caduca en 3 días", null, null, "vegetables"),
        PantryFoodRecord("recipe_demo_22", "Queso", "200 g", "200", "g", "Queso", "fridge", "Quedan 10 días", null, null, "cheese"),
        PantryFoodRecord("recipe_demo_23", "Leche", "1 litro", "1", "litro", "Leche", "fridge", "Quedan 7 días", null, null, "milk"),
        PantryFoodRecord("recipe_demo_24", "Yogur natural", "4 uds", "4", "uds", "Yogures", "fridge", "Quedan 9 días", null, null, "yogurts"),
        PantryFoodRecord("recipe_demo_25", "Manzanas", "6 uds", "6", "uds", "Frutas", "pantry", "Quedan 12 días", null, null, "fruits"),
        PantryFoodRecord("recipe_demo_26", "Pimentón dulce", "1 bote", "1", "bote", "Salsas", "pantry", "Perenne", null, null, "sauces"),
        PantryFoodRecord("recipe_demo_27", "Chorizo", "1 ristra", "1", "ristra", "Carne", "fridge", "Quedan 15 días", null, null, "meat"),
        PantryFoodRecord("recipe_demo_28", "Aceite de oliva", "1 botella", "1", "botella", "Aceite", "pantry", "Perenne", null, null, "oil"),
    )

    private fun defaultShoppingItems(): List<ShoppingItemRecord> = listOf(
        ShoppingItemRecord("yogurt", "Yogur natural", "4 uds", "4", "uds", false, "lacteos", "fridge", "yogurt"),
        ShoppingItemRecord("tomato", "Tomates cherry", "500 g", "500", "g", false, "verdura", "fridge", "tomato"),
        ShoppingItemRecord("broth", "Caldo de verduras", "1 brick", "1", "brick", false, "receta sopa", "pantry", "soup"),
        ShoppingItemRecord("rice", "Arroz redondo", "1 kg", "1", "kg", false, "basico", "pantry", "rice"),
        ShoppingItemRecord("fish", "Merluza", "2 filetes", "2", "filetes", false, "proteina", "freezer", "fish"),
        ShoppingItemRecord("stock", "Caldo casero", "1 litro", "1", "litro", false, "preparado", "freezer", "soup"),
    )

    private fun migrateDefaultPantryFoods(foods: List<PantryFoodRecord>): List<PantryFoodRecord> {
        val migrated = foods
            .filterNot { it.id == "6" && it.name == "Caldo casero" }
            .map { food ->
                when {
                    food.id == "1" && food.name == "Espinacas" -> food.copy(expiryLabel = "Caduca en 2 días", iconKey = "spinach")
                    food.id == "2" && food.name == "Huevos" -> food.copy(expiryLabel = "Quedan 10 días", iconKey = "eggs")
                    food.id == "3" && food.name == "Arroz integral" -> food.copy(expiryLabel = "Perenne", category = "Arroz", iconKey = "rice")
                    food.id == "4" && food.name == "Lentejas" -> food.copy(expiryLabel = "Perenne", category = "Legumbres", iconKey = "legumes")
                    food.name.contains("garbanzo", ignoreCase = true) -> food.copy(category = "Legumbres", iconKey = "legumes")
                    food.name.contains("alubia", ignoreCase = true) -> food.copy(category = "Legumbres", iconKey = "legumes")
                    food.name.contains("lenteja", ignoreCase = true) -> food.copy(category = "Legumbres", iconKey = "legumes")
                    food.name.contains("macarron", ignoreCase = true) || food.name.contains("macarrón", ignoreCase = true) -> {
                        food.copy(category = "Pasta", iconKey = "pasta")
                    }
                    food.name.contains("arroz", ignoreCase = true) -> food.copy(category = "Arroz", iconKey = "rice")
                    food.name.contains("aceite", ignoreCase = true) -> food.copy(category = "Aceite", iconKey = "oil")
                    food.name.contains("vinagre", ignoreCase = true) -> food.copy(category = "Vinagre", iconKey = "vinegar")
                    food.id == "5" && food.name == "Filetes de pescado" -> food.copy(
                        name = "Merluza",
                        quantity = "2 filetes",
                        quantityValue = "2",
                        quantityUnit = "filetes",
                        expiryLabel = "Quedan 3 meses",
                        iconKey = "fish",
                    )
                    food.id == "5" && food.name == "Merluza" -> food.copy(expiryLabel = "Quedan 3 meses", iconKey = "fish")
                    else -> food
                }
            }
        if (!migrated.isDefaultDemoPantry()) return migrated

        val existingIds = migrated.map { it.id }.toSet()
        val existingNames = migrated.map { it.name.trim().lowercase() }.toSet()
        val missingDemoFoods = recipeDemoPantryFoods().filter { food ->
            food.id !in existingIds && food.name.trim().lowercase() !in existingNames
        }
        return migrated + missingDemoFoods
    }

    private fun List<PantryFoodRecord>.isDefaultDemoPantry(): Boolean {
        val defaultIds = (defaultCorePantryFoods() + recipeDemoPantryFoods()).map { it.id }.toSet()
        if (any { it.id !in defaultIds }) return false

        val names = map { it.name.trim().lowercase() }.toSet()
        return listOf("Espinacas", "Huevos", "Arroz integral", "Lentejas")
            .all { it.lowercase() in names } &&
            ("filetes de pescado" in names || "merluza" in names)
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
