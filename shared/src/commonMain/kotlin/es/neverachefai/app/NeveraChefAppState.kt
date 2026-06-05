package es.neverachefai.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import es.neverachefai.core.preferences.AppPreferences
import es.neverachefai.feature.navigation.MainTab
import es.neverachefai.feature.navigation.PantryFlow
import es.neverachefai.feature.navigation.RecipesFlow
import es.neverachefai.feature.navigation.RootFlow
import es.neverachefai.feature.pantry.data.PantryRepositoryImpl
import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.pantry.domain.usecase.GetPantryFoodsUseCase
import es.neverachefai.feature.pantry.domain.usecase.SavePantryFoodsUseCase
import es.neverachefai.feature.pantry.ui.PantryFoodUi
import es.neverachefai.feature.pantry.ui.PantryLocation
import es.neverachefai.feature.pantry.ui.platformTodayIsoDate
import es.neverachefai.feature.pantry.ui.toPantryFoodUi
import es.neverachefai.feature.shopping.data.ShoppingRepositoryImpl
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem
import es.neverachefai.feature.shopping.domain.usecase.GetShoppingItemsUseCase
import es.neverachefai.feature.shopping.domain.usecase.SaveShoppingItemsUseCase
import es.neverachefai.feature.shopping.ui.AddShoppingProductUiState

private const val KEY_EXPIRY_REMINDER_DAYS = "settings.expiry_reminder_days"

@Composable
fun rememberNeveraChefAppState(): NeveraChefAppState {
    return remember { NeveraChefAppState() }
}

class NeveraChefAppState(
    private val getPantryFoods: GetPantryFoodsUseCase = GetPantryFoodsUseCase(PantryRepositoryImpl()),
    private val savePantryFoods: SavePantryFoodsUseCase = SavePantryFoodsUseCase(PantryRepositoryImpl()),
    private val getShoppingItems: GetShoppingItemsUseCase = GetShoppingItemsUseCase(ShoppingRepositoryImpl()),
    private val saveShoppingItemsUseCase: SaveShoppingItemsUseCase = SaveShoppingItemsUseCase(ShoppingRepositoryImpl()),
) {
    var rootFlow by mutableStateOf(if (AppPreferences.isOnboardingSeen()) RootFlow.MAIN else RootFlow.ONBOARDING)
    var currentTab by mutableStateOf(MainTab.PANTRY)
    var pantryFlow by mutableStateOf(PantryFlow.LIST)
    var selectedFood by mutableStateOf<PantryFoodUi?>(null)
    var pantryFoods by mutableStateOf(loadPantryFoodUi())
    var recipesFlow by mutableStateOf(RecipesFlow.GENERATE)
    var showAddShoppingProduct by mutableStateOf(false)
    var addShoppingState by mutableStateOf(AddShoppingProductUiState())
    var shoppingItems by mutableStateOf(getShoppingItems())
    var expiryReminderDays by mutableStateOf(loadExpiryReminderDays())

    fun completeOnboarding() {
        AppPreferences.setOnboardingSeen(true)
        currentTab = MainTab.PANTRY
        rootFlow = RootFlow.MAIN
    }

    fun reset() {
        AppPreferences.clearAll()
        savePantryFoods(emptyList())
        saveShoppingItemsUseCase(emptyList())
        rootFlow = RootFlow.ONBOARDING
        currentTab = MainTab.PANTRY
        pantryFlow = PantryFlow.LIST
        selectedFood = null
        pantryFoods = emptyList()
        recipesFlow = RecipesFlow.GENERATE
        showAddShoppingProduct = false
        addShoppingState = AddShoppingProductUiState()
        shoppingItems = emptyList()
        expiryReminderDays = loadExpiryReminderDays()
    }

    fun deletePantryFoods(ids: Set<String>) {
        if (ids.isEmpty()) return
        val updated = getPantryFoods().filterNot { it.id in ids }
        savePantryFoods(updated)
        pantryFoods = updated.map { it.toPantryFoodUi() }
        if (selectedFood?.id in ids) {
            selectedFood = null
            pantryFlow = PantryFlow.LIST
        }
    }

    fun saveEditedFood(updatedFood: PantryFoodUi) {
        val current = getPantryFoods().toMutableList()
        val index = current.indexOfFirst { it.id == updatedFood.id }
        if (index < 0) return
        current[index] = current[index].copy(
            name = updatedFood.name.trim().ifBlank { current[index].name },
            quantity = updatedFood.quantity,
            quantityValue = parseDetailQuantityValue(updatedFood.quantity),
            quantityUnit = parseDetailQuantityUnit(updatedFood.quantity),
            category = updatedFood.category,
            locationKey = updatedFood.location.toLocationKey(),
            expiryDateIso = updatedFood.expiryDateIso,
            addedDateIso = updatedFood.addedDateIso,
            iconKey = updatedFood.iconKey,
        )
        savePantryFoods(current)
        pantryFoods = current.map { it.toPantryFoodUi() }
        selectedFood = pantryFoods.firstOrNull { it.id == updatedFood.id } ?: updatedFood
    }

    fun saveShoppingItems(items: List<ShoppingListItem>) {
        saveShoppingItemsUseCase(items)
        shoppingItems = items
    }

    fun finalizeShoppingPurchase(items: List<ShoppingListItem>): List<ShoppingListItem> {
        val checkedItems = items.filter { it.checked }
        if (checkedItems.isEmpty()) return items
        val currentPantry = getPantryFoods().toMutableList()
        checkedItems.forEach { item ->
            currentPantry += item.toPantryFood(currentPantry.size + 1)
        }
        val remaining = items.filterNot { it.checked }
        savePantryFoods(currentPantry)
        saveShoppingItems(remaining)
        pantryFoods = currentPantry.map { it.toPantryFoodUi() }
        return remaining
    }

    fun addShoppingProduct() {
        val itemName = addShoppingState.productName.trim()
        if (itemName.isNotEmpty()) {
            val iconKey = addShoppingState.destination.toCategoryIconKey()
            val parsedQuantity = parseQuantityInput(
                rawValue = addShoppingState.quantity,
                quantityMode = addShoppingState.quantityMode,
            )
            val currentItems = getShoppingItems().toMutableList()
            currentItems += ShoppingListItem(
                id = "shopping_${currentItems.size + 1}_${itemName.lowercase().replace(" ", "_")}",
                name = itemName,
                quantity = parsedQuantity.displayLabel,
                quantityValue = parsedQuantity.value,
                quantityUnit = parsedQuantity.unit,
                checked = false,
                category = iconKey,
                destinationKey = addShoppingState.location.toDestinationKey(),
                iconKey = iconKey,
            )
            saveShoppingItems(currentItems)
        }
        addShoppingState = AddShoppingProductUiState()
        showAddShoppingProduct = false
    }

    fun applySpokenShoppingInput(spokenText: String) {
        val parsed = parseSpokenShoppingInput(spokenText)
        addShoppingState = addShoppingState.copy(
            selectedMode = es.neverachefai.feature.shopping.ui.AddShoppingMode.Voice,
            productName = parsed.productName ?: addShoppingState.productName,
            destination = parsed.category ?: addShoppingState.destination,
            quantity = parsed.quantity ?: addShoppingState.quantity,
            quantityMode = parsed.quantityMode ?: addShoppingState.quantityMode,
            location = parsed.location ?: addShoppingState.location,
        )
    }

    fun updateExpiryReminderDays(days: Int) {
        val clamped = days.coerceIn(2, 10)
        expiryReminderDays = clamped
        AppPreferences.setString(KEY_EXPIRY_REMINDER_DAYS, clamped.toString())
    }

    private fun loadPantryFoodUi(): List<PantryFoodUi> = getPantryFoods().map { it.toPantryFoodUi() }

    private fun loadExpiryReminderDays(defaultDays: Int = 2): Int {
        return AppPreferences.getString(KEY_EXPIRY_REMINDER_DAYS)
            ?.toIntOrNull()
            ?.coerceIn(2, 10)
            ?: defaultDays.coerceIn(2, 10)
    }
}

private fun ShoppingListItem.toPantryFood(nextIndex: Int): PantryFood {
    return PantryFood(
        id = "pantry_${id}_$nextIndex",
        name = name,
        quantity = quantity,
        quantityValue = quantityValue,
        quantityUnit = quantityUnit,
        category = iconKey,
        locationKey = destinationKey.toDestinationKey(),
        expiryLabel = null,
        expiryDateIso = null,
        addedDateIso = platformTodayIsoDate(),
        iconKey = iconKey,
    )
}

private fun String.toCategoryIconKey(): String {
    return when (lowercase()) {
        "frutas" -> "fruits"
        "verduras" -> "vegetables"
        "carne" -> "meat"
        "pescado" -> "fish"
        "marisco" -> "seafood"
        "pan" -> "bread"
        "leche" -> "milk"
        "yogures" -> "yogurts"
        "queso" -> "cheese"
        "huevos" -> "eggs"
        "pasta/arroz" -> "grains"
        "conservas" -> "canned_food"
        "congelados" -> "frozen"
        "agua" -> "water"
        "refrescos" -> "soft_drinks"
        "zumo" -> "juice"
        "vino" -> "wine"
        "cerveza" -> "beer"
        "cafe/te" -> "coffee_tea"
        "snacks" -> "snacks"
        "dulces" -> "sweets"
        "salsas" -> "sauces"
        "aceite/vinagre" -> "oil_vinegar"
        "platos listos" -> "ready_meals"
        "limpieza" -> "cleaning"
        "higiene" -> "hygiene"
        "mascotas" -> "pets"
        else -> "other"
    }
}

private fun String.toDestinationKey(): String {
    return when (this) {
        "congelador", "freezer" -> "freezer"
        "nevera", "fridge" -> "fridge"
        else -> "pantry"
    }
}

private fun PantryLocation.toLocationKey(): String {
    return when (this) {
        PantryLocation.FRIDGE -> "fridge"
        PantryLocation.PANTRY -> "pantry"
        PantryLocation.FREEZER -> "freezer"
    }
}

private fun parseDetailQuantityValue(quantity: String): String {
    val match = Regex("""(\d+(?:[.,]\d+)?)""").find(quantity.trim()) ?: return "1"
    return match.groupValues[1]
}

private fun parseDetailQuantityUnit(quantity: String): String {
    val normalized = quantity.trim().lowercase()
    return when {
        normalized.contains("kg") -> "kg"
        normalized.contains("gr") || normalized.contains("gram") || Regex("""\b(g)\b""").containsMatchIn(normalized) -> "gr"
        normalized.contains("unidad") -> "unidades"
        else -> {
            val parts = normalized.split(" ").filter { it.isNotBlank() }
            if (parts.size >= 2) parts.drop(1).joinToString(" ") else "unidades"
        }
    }
}

private data class ParsedQuantity(val value: String, val unit: String, val displayLabel: String)

private fun parseQuantityInput(rawValue: String, quantityMode: String): ParsedQuantity {
    val normalized = rawValue.trim()
    if (normalized.isBlank()) return ParsedQuantity("1", "unidades", "1 unidades")
    if (quantityMode == "Peso") {
        val grams = normalized.toIntOrNull()?.coerceAtLeast(0) ?: 0
        if (grams >= 1000) {
            val kg = grams / 1000.0
            val value = if (kg % 1.0 == 0.0) kg.toInt().toString() else kg.toString().replace('.', ',')
            return ParsedQuantity(value, "kg", "$value kg")
        }
        return ParsedQuantity(grams.toString(), "gr", "$grams gr")
    }
    val parts = normalized.split(" ").filter { it.isNotBlank() }
    if (parts.size >= 2) {
        val value = parts.first()
        val unit = parts.drop(1).joinToString(" ")
        return ParsedQuantity(value, unit, "$value $unit")
    }
    val numericValue = normalized.toIntOrNull()?.coerceAtLeast(1)?.toString() ?: "1"
    return ParsedQuantity(numericValue, "unidades", "$numericValue unidades")
}

private data class ParsedSpokenShoppingInput(
    val productName: String? = null,
    val category: String? = null,
    val quantity: String? = null,
    val quantityMode: String? = null,
    val location: String? = null,
)

private fun parseSpokenShoppingInput(rawText: String): ParsedSpokenShoppingInput {
    val original = rawText.trim()
    if (original.isBlank()) return ParsedSpokenShoppingInput()
    val text = normalizeVoiceText(original)
    val categoryMap = listOf(
        "frutas" to listOf("fruta", "frutas", "fresa", "fresas"),
        "verduras" to listOf("verdura", "verduras", "vegetales"),
        "carne" to listOf("carne", "carnes"),
        "pescado" to listOf("pescado", "pescados", "pez"),
        "marisco" to listOf("marisco", "mariscos"),
        "pan" to listOf("pan", "panes"),
        "leche" to listOf("leche", "lacteo", "lacteos"),
        "yogures" to listOf("yogur", "yogures", "yoghurt"),
        "queso" to listOf("queso", "quesos"),
        "huevos" to listOf("huevo", "huevos"),
        "pasta/arroz" to listOf("pasta", "arroz", "legumbres"),
        "conservas" to listOf("conserva", "conservas"),
        "congelados" to listOf("congelado", "congelados"),
        "agua" to listOf("agua"),
        "refrescos" to listOf("refresco", "refrescos"),
        "zumo" to listOf("zumo", "zumos", "jugo"),
        "vino" to listOf("vino", "vinos"),
        "cerveza" to listOf("cerveza", "cervezas"),
        "cafe/te" to listOf("cafe", "te"),
        "snacks" to listOf("snack", "snacks", "aperitivos"),
        "dulces" to listOf("dulce", "dulces"),
        "salsas" to listOf("salsa", "salsas"),
        "aceite/vinagre" to listOf("aceite", "vinagre"),
        "platos listos" to listOf("plato listo", "platos listos"),
        "limpieza" to listOf("limpieza"),
        "higiene" to listOf("higiene"),
        "mascotas" to listOf("mascota", "mascotas"),
        "otros" to listOf("otros", "otro"),
    )
    val locationMap = mapOf(
        "nevera" to listOf("nevera", "frigorifico", "frigo", "refrigerador"),
        "despensa" to listOf("despensa", "despnesa", "armario"),
        "congelador" to listOf("congelador", "congelar"),
    )
    val category = categoryMap.firstNotNullOfOrNull { (canonical, aliases) ->
        if (aliases.any { alias -> Regex("\\b${Regex.escape(alias)}\\b").containsMatchIn(text) }) canonical else null
    }
    val location = locationMap.entries.firstNotNullOfOrNull { (canonical, aliases) ->
        if (aliases.any { alias -> Regex("\\b${Regex.escape(alias)}\\b").containsMatchIn(text) }) canonical else null
    }
    val kgMatch = Regex("(\\d+(?:[\\.,]\\d+)?)\\s*(kg|kilo|kilos|kilogramo|kilogramos)").find(text)
    val grMatch = Regex("(\\d+)\\s*(g|gr|gramo|gramos)").find(text)
    val unitsMatch = Regex("(?:cantidad|cantid|unidades|unidad|uds|ud)\\s*(\\d+)").find(text)
    val explicitWeightValue = Regex("(?:peso|pesa|pesar)\\s*(\\d+(?:[\\.,]\\d+)?)").find(text)
    val explicitQuantityValue = Regex("(?:cantidad|cantid|unidades|unidad|uds|ud)\\s*(\\d+)").find(text)
    val looseNumber = Regex("\\b(\\d+)\\b").find(text)
    val quantityMode: String?
    val quantity: String?
    when {
        kgMatch != null -> {
            val kgValue = kgMatch.groupValues[1].replace(',', '.').toDoubleOrNull() ?: 0.0
            quantity = (kgValue * 1000.0).toInt().coerceAtLeast(0).toString()
            quantityMode = "Peso"
        }
        grMatch != null -> {
            quantity = (grMatch.groupValues[1].toIntOrNull() ?: 0).coerceAtLeast(0).toString()
            quantityMode = "Peso"
        }
        explicitWeightValue != null -> {
            val numeric = explicitWeightValue.groupValues[1].replace(',', '.').toDoubleOrNull() ?: 0.0
            quantity = if (numeric <= 0.0) "0" else (numeric * 1000.0).toInt().toString()
            quantityMode = "Peso"
        }
        text.contains("peso") && looseNumber != null -> {
            quantity = (looseNumber.groupValues[1].toIntOrNull() ?: 0).coerceAtLeast(0).toString()
            quantityMode = "Peso"
        }
        explicitQuantityValue != null -> {
            quantity = (explicitQuantityValue.groupValues[1].toIntOrNull() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        unitsMatch != null -> {
            quantity = (unitsMatch.groupValues[1].toIntOrNull() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        looseNumber != null -> {
            quantity = (looseNumber.groupValues[1].toIntOrNull() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        else -> {
            quantity = null
            quantityMode = null
        }
    }
    return ParsedSpokenShoppingInput(
        productName = extractProductNameFromVoice(original),
        category = category,
        quantity = quantity,
        quantityMode = quantityMode,
        location = location,
    )
}

private fun normalizeVoiceText(value: String): String {
    return value
        .lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
}

private fun extractProductNameFromVoice(rawText: String): String? {
    var cleaned = normalizeVoiceText(rawText)
    val dropPatterns = listOf(
        "\\b(en\\s+)?(nevera|frigorifico|frigo|refrigerador|despensa|despnesa|congelador)\\b",
        "\\b(frutas?|verduras?|vegetales?|carne|carnes|pescado|pescados|marisco|mariscos)\\b",
        "\\b(peso|cantidad|cantid|unidades?|uds?|ud)\\b",
        "\\b\\d+(?:[\\.,]\\d+)?\\s*(kg|kilo|kilos|kilogramo|kilogramos|g|gr|gramo|gramos)\\b",
        "\\b\\d+\\b",
    )
    dropPatterns.forEach { pattern ->
        cleaned = cleaned.replace(Regex(pattern), " ")
    }
    cleaned = cleaned
        .replace(Regex("[,;:]+"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
    if (cleaned.isBlank()) return null
    return cleaned.split(" ").joinToString(" ") { token ->
        token.replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase() else ch.toString() }
    }
}
