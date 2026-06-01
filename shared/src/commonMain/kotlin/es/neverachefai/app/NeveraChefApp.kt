package es.neverachefai.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.ShoppingItemRecord
import es.neverachefai.core.preferences.AppPreferences
import es.neverachefai.core.ui.components.NeveraMainScaffold
import es.neverachefai.feature.navigation.MainTab
import es.neverachefai.feature.navigation.PantryFlow
import es.neverachefai.feature.navigation.RecipesFlow
import es.neverachefai.feature.navigation.RootFlow
import es.neverachefai.feature.onboarding.ui.InitialPreferencesScreen
import es.neverachefai.feature.onboarding.ui.OnboardingScreen
import es.neverachefai.feature.pantry.ui.AddIngredientsScreen
import es.neverachefai.feature.pantry.ui.FoodDetailScreen
import es.neverachefai.feature.pantry.ui.IngredientReviewScreen
import es.neverachefai.feature.pantry.ui.PantryFoodUi
import es.neverachefai.feature.pantry.ui.PantryScreen
import es.neverachefai.feature.pantry.ui.pantryFoodRecordToUi
import es.neverachefai.feature.recipes.ui.RecipeDetailScreen
import es.neverachefai.feature.recipes.ui.RecipeGenerationScreen
import es.neverachefai.feature.recipes.ui.RecipeResultsScreen
import es.neverachefai.feature.settings.ui.SettingsScreen
import es.neverachefai.feature.shopping.ui.AddShoppingProductScreen
import es.neverachefai.feature.shopping.ui.AddShoppingProductUiState
import es.neverachefai.feature.shopping.ui.ShoppingListScreen

@Composable
fun NeveraChefApp(
    cameraPermissionGranted: Boolean = false,
    microphonePermissionGranted: Boolean = false,
    onRequestCameraPermission: () -> Unit = {},
    onRequestMicrophonePermission: () -> Unit = {},
    onRequestSpeechToText: ((String) -> Unit) -> Unit = {},
) {
    val onboardingSeen = remember { AppPreferences.isOnboardingSeen() }
    var rootFlow by remember { mutableStateOf(if (onboardingSeen) RootFlow.MAIN else RootFlow.ONBOARDING) }
    var currentTab by remember { mutableStateOf(MainTab.PANTRY) }
    var pantryFlow by remember { mutableStateOf(PantryFlow.LIST) }
    var selectedFood by remember { mutableStateOf<PantryFoodUi?>(null) }
    var pantryFoods by remember { mutableStateOf(LocalAppContentStore.loadPantryFoods().map(::pantryFoodRecordToUi)) }
    var recipesFlow by remember { mutableStateOf(RecipesFlow.GENERATE) }
    var showAddShoppingProduct by remember { mutableStateOf(false) }
    var addShoppingState by remember { mutableStateOf(AddShoppingProductUiState()) }

    val completeOnboarding: () -> Unit = {
        AppPreferences.setOnboardingSeen(true)
        currentTab = MainTab.PANTRY
        rootFlow = RootFlow.MAIN
    }

    val resetAppState: () -> Unit = {
        AppPreferences.clearAll()
        rootFlow = RootFlow.ONBOARDING
        currentTab = MainTab.PANTRY
        pantryFlow = PantryFlow.LIST
        selectedFood = null
        pantryFoods = LocalAppContentStore.loadPantryFoods().map(::pantryFoodRecordToUi)
        recipesFlow = RecipesFlow.GENERATE
        showAddShoppingProduct = false
        addShoppingState = AddShoppingProductUiState()
    }

    when (rootFlow) {
        RootFlow.WELCOME -> OnboardingScreen(onContinue = { rootFlow = RootFlow.PREFERENCES })
        RootFlow.ONBOARDING -> OnboardingScreen(onContinue = { rootFlow = RootFlow.PREFERENCES })
        RootFlow.PREFERENCES -> InitialPreferencesScreen(onSave = completeOnboarding)
        RootFlow.MAIN -> NeveraMainScaffold(
            selectedTab = currentTab,
            onTabSelected = { currentTab = it },
            showBottomBar = !(currentTab == MainTab.SHOPPING && showAddShoppingProduct),
            contentHorizontalPadding = if (currentTab == MainTab.SHOPPING && showAddShoppingProduct) 0.dp else 12.dp,
            contentVerticalPadding = if (currentTab == MainTab.SHOPPING && showAddShoppingProduct) 0.dp else 8.dp,
            content = {
                when (currentTab) {
                    MainTab.PANTRY -> when (pantryFlow) {
                        PantryFlow.LIST -> PantryScreen(
                            foods = pantryFoods,
                            onAdd = { pantryFlow = PantryFlow.ADD },
                            onReview = { pantryFlow = PantryFlow.REVIEW },
                            onFoodClick = {
                                selectedFood = it
                                pantryFlow = PantryFlow.DETAIL
                            },
                            onDeleteFoods = { ids ->
                                if (ids.isNotEmpty()) {
                                    LocalAppContentStore.deletePantryFoods(ids)
                                    val current = LocalAppContentStore.loadPantryFoods().toMutableList()
                                    pantryFoods = current.map(::pantryFoodRecordToUi)
                                    if (selectedFood?.id in ids) {
                                        selectedFood = null
                                        pantryFlow = PantryFlow.LIST
                                    }
                                }
                            },
                        )

                        PantryFlow.ADD -> AddIngredientsScreen(onBack = { pantryFlow = PantryFlow.LIST })
                        PantryFlow.REVIEW -> IngredientReviewScreen(onBack = { pantryFlow = PantryFlow.LIST })
                        PantryFlow.DETAIL -> FoodDetailScreen(
                            food = selectedFood,
                            onBack = { pantryFlow = PantryFlow.LIST },
                            onSaveEditedFood = { updated ->
                                selectedFood = updated
                                val current = LocalAppContentStore.loadPantryFoods().toMutableList()
                                val index = current.indexOfFirst { it.id == updated.id }
                                if (index >= 0) {
                                    val existing = current[index]
                                    current[index] = existing.copy(
                                        name = updated.name,
                                        quantity = updated.quantity,
                                        category = updated.category,
                                        locationKey = when (updated.location) {
                                            es.neverachefai.feature.pantry.ui.PantryLocation.FRIDGE -> "fridge"
                                            es.neverachefai.feature.pantry.ui.PantryLocation.PANTRY -> "pantry"
                                            es.neverachefai.feature.pantry.ui.PantryLocation.FREEZER -> "freezer"
                                        },
                                        expiryDateIso = updated.expiryDateIso,
                                    )
                                    LocalAppContentStore.savePantryFoods(current)
                                    pantryFoods = current.map(::pantryFoodRecordToUi)
                                }
                            },
                            onGenerateRecipe = {
                                currentTab = MainTab.RECIPES
                                recipesFlow = RecipesFlow.GENERATE
                            },
                        )
                    }

                    MainTab.RECIPES -> when (recipesFlow) {
                        RecipesFlow.GENERATE -> RecipeGenerationScreen(
                            onGenerate = { recipesFlow = RecipesFlow.RESULTS },
                        )

                        RecipesFlow.RESULTS -> RecipeResultsScreen(
                            onOpenDetail = { recipesFlow = RecipesFlow.DETAIL },
                        )

                        RecipesFlow.DETAIL -> RecipeDetailScreen(
                            onBack = { recipesFlow = RecipesFlow.RESULTS },
                        )
                    }

                    MainTab.SHOPPING -> if (showAddShoppingProduct) {
                        AddShoppingProductScreen(
                            state = addShoppingState,
                            onModeSelected = { addShoppingState = addShoppingState.copy(selectedMode = it) },
                            onProductNameChange = { addShoppingState = addShoppingState.copy(productName = it) },
                            onQuantityChange = { addShoppingState = addShoppingState.copy(quantity = it) },
                            onQuantityModeChange = { addShoppingState = addShoppingState.copy(quantityMode = it) },
                            onDestinationChange = { addShoppingState = addShoppingState.copy(destination = it) },
                            onLocationChange = { addShoppingState = addShoppingState.copy(location = it) },
                            onVoiceClick = {
                                if (!microphonePermissionGranted) {
                                    onRequestMicrophonePermission()
                                } else {
                                    onRequestSpeechToText { spokenText ->
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
                                }
                            },
                            onCameraClick = {},
                            onBackClick = { showAddShoppingProduct = false },
                            onAddToShoppingListClick = {
                                val itemName = addShoppingState.productName.trim()
                                if (itemName.isNotEmpty()) {
                                    val iconKey = addShoppingState.destination.toCategoryIconKey()
                                    val destinationKey = addShoppingState.location.toDestinationKey()
                                    val parsedQuantity = parseQuantityInput(
                                        rawValue = addShoppingState.quantity,
                                        quantityMode = addShoppingState.quantityMode,
                                    )
                                    val currentItems = LocalAppContentStore.loadShoppingItems().toMutableList()
                                    currentItems += ShoppingItemRecord(
                                        id = "shopping_${currentItems.size + 1}_${itemName.lowercase().replace(" ", "_")}",
                                        name = itemName,
                                        quantity = parsedQuantity.displayLabel,
                                        quantityValue = parsedQuantity.value,
                                        quantityUnit = parsedQuantity.unit,
                                        checked = false,
                                        category = iconKey,
                                        destinationKey = destinationKey,
                                        iconKey = iconKey,
                                    )
                                    LocalAppContentStore.saveShoppingItems(currentItems)
                                }
                                addShoppingState = AddShoppingProductUiState()
                                showAddShoppingProduct = false
                            },
                        )
                    } else {
                        ShoppingListScreen(
                            onAddProductClick = { showAddShoppingProduct = true },
                        )
                    }
                    MainTab.SETTINGS -> SettingsScreen(
                        cameraPermissionGranted = cameraPermissionGranted,
                        microphonePermissionGranted = microphonePermissionGranted,
                        onRequestCameraPermission = onRequestCameraPermission,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onReset = resetAppState,
                    )
                }
            },
        )
    }
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
        "congelador" -> "freezer"
        "nevera" -> "fridge"
        else -> "pantry"
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
        return ParsedQuantity(grams.toString(), "gr", "${grams} gr")
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

    val productName = extractProductNameFromVoice(original)

    return ParsedSpokenShoppingInput(
        productName = productName,
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
