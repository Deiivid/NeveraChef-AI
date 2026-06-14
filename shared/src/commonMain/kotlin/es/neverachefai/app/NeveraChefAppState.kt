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
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult
import es.neverachefai.feature.recipes.domain.usecase.GenerateRecipesUseCase
import es.neverachefai.feature.shopping.data.ShoppingRepositoryImpl
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem
import es.neverachefai.feature.shopping.domain.usecase.GetShoppingItemsUseCase
import es.neverachefai.feature.shopping.domain.usecase.SaveShoppingItemsUseCase
import es.neverachefai.feature.shopping.ui.AddProductTarget
import es.neverachefai.feature.shopping.ui.AddShoppingMode
import es.neverachefai.feature.shopping.ui.AddShoppingProductUiState
import es.neverachefai.feature.shopping.ui.GuidedVoiceAddPhase
import es.neverachefai.feature.shopping.ui.inferShoppingCategoryLabel
import es.neverachefai.feature.shopping.ui.inferShoppingIconKey
import es.neverachefai.feature.shopping.ui.inferShoppingProduct
import es.neverachefai.feature.shopping.ui.shoppingCategoryIconKeyForProductIcon
import es.neverachefai.feature.shopping.ui.shoppingCategoryIconKey
import es.neverachefai.feature.shopping.ui.shoppingCategoryLabelForIconKey
import es.neverachefai.feature.shopping.ui.shoppingDestinationForIconKey

private const val KEY_EXPIRY_REMINDER_DAYS = "settings.expiry_reminder_days"
private const val KEY_UNLOCKED_REWARD_RECIPE_IDS = "recipes.reward_unlocked_ids"
private const val KEY_RECIPE_NOTICE_SEEN = "recipes.suggested_notice_seen"
private const val MAX_RECIPES_PER_SEARCH = 32

@Composable
fun rememberNeveraChefAppState(): NeveraChefAppState {
    return remember { NeveraChefAppState() }
}

class NeveraChefAppState(
    private val getPantryFoods: GetPantryFoodsUseCase = GetPantryFoodsUseCase(PantryRepositoryImpl()),
    private val savePantryFoods: SavePantryFoodsUseCase = SavePantryFoodsUseCase(PantryRepositoryImpl()),
    private val getShoppingItems: GetShoppingItemsUseCase = GetShoppingItemsUseCase(ShoppingRepositoryImpl()),
    private val saveShoppingItemsUseCase: SaveShoppingItemsUseCase = SaveShoppingItemsUseCase(ShoppingRepositoryImpl()),
    private val generateRecipesUseCase: GenerateRecipesUseCase = GenerateRecipesUseCase(),
) {
    var rootFlow by mutableStateOf(if (AppPreferences.isOnboardingSeen()) RootFlow.MAIN else RootFlow.ONBOARDING)
    var currentTab by mutableStateOf(MainTab.PANTRY)
    var pantryFlow by mutableStateOf(PantryFlow.LIST)
    var selectedFood by mutableStateOf<PantryFoodUi?>(null)
    var pantryFoods by mutableStateOf(loadPantryFoodUi())
    var recipesFlow by mutableStateOf(RecipesFlow.RESULTS)
    var recipeGenerationResult by mutableStateOf<RecipeGenerationResult?>(null)
    var selectedRecipe by mutableStateOf<Recipe?>(null)
    private var recipeFocusFoodId by mutableStateOf<String?>(null)
    var showAddShoppingProduct by mutableStateOf(false)
    var addShoppingState by mutableStateOf(AddShoppingProductUiState())
    var shoppingItems by mutableStateOf(getShoppingItems())
    var expiryReminderDays by mutableStateOf(loadExpiryReminderDays())
    var unlockedRewardRecipeIds by mutableStateOf(loadUnlockedRewardRecipeIds())
    var showRecipeNotice by mutableStateOf(!isRecipeNoticeSeen())
    var showExitConfirmation by mutableStateOf(false)

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
        recipesFlow = RecipesFlow.RESULTS
        recipeGenerationResult = null
        selectedRecipe = null
        recipeFocusFoodId = null
        showAddShoppingProduct = false
        addShoppingState = AddShoppingProductUiState()
        shoppingItems = emptyList()
        expiryReminderDays = loadExpiryReminderDays()
        unlockedRewardRecipeIds = emptySet()
        showRecipeNotice = true
        showExitConfirmation = false
    }

    fun handleSystemBack() {
        showExitConfirmation = false
        when (rootFlow) {
            RootFlow.PREFERENCES -> rootFlow = RootFlow.ONBOARDING
            RootFlow.WELCOME,
            RootFlow.ONBOARDING -> showExitConfirmation = true
            RootFlow.MAIN -> handleMainBack()
        }
    }

    private fun handleMainBack() {
        when (currentTab) {
            MainTab.PANTRY -> when (pantryFlow) {
                PantryFlow.LIST -> showExitConfirmation = true
                PantryFlow.ADD -> {
                    addShoppingState = AddShoppingProductUiState()
                    pantryFlow = PantryFlow.LIST
                }
                PantryFlow.DETAIL -> {
                    selectedFood = null
                    pantryFlow = PantryFlow.LIST
                }
            }
            MainTab.RECIPES -> when (recipesFlow) {
                RecipesFlow.RESULTS -> showExitConfirmation = true
                RecipesFlow.DETAIL -> recipesFlow = RecipesFlow.RESULTS
            }
            MainTab.SHOPPING -> {
                if (showAddShoppingProduct) {
                    addShoppingState = AddShoppingProductUiState()
                    showAddShoppingProduct = false
                } else {
                    showExitConfirmation = true
                }
            }
            MainTab.SETTINGS -> showExitConfirmation = true
        }
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

    fun updateAddShoppingProductName(productName: String) {
        val inferredCategory = inferShoppingCategoryLabel(productName)
        addShoppingState = addShoppingState.copy(
            productName = productName,
            destination = inferredCategory ?: addShoppingState.destination,
            voiceCategoryProvided = addShoppingState.voiceCategoryProvided || inferredCategory != null,
        )
    }

    fun applySpokenProductName(spokenText: String) {
        val productName = spokenText.toVoiceProductName() ?: return
        updateAddShoppingProductName(productName)
    }

    fun updateAddShoppingQuantity(quantity: String) {
        addShoppingState = addShoppingState.copy(
            quantity = quantity.toShoppingQuantityValue(addShoppingState.quantityMode),
            voiceQuantityProvided = true,
        )
    }

    fun updateAddShoppingQuantityMode(quantityMode: String) {
        addShoppingState = addShoppingState.copy(quantityMode = quantityMode)
    }

    fun updateAddShoppingDestination(destination: String) {
        addShoppingState = addShoppingState.copy(
            destination = destination,
            voiceCategoryProvided = true,
        )
    }

    fun updateAddShoppingLocation(location: String) {
        addShoppingState = addShoppingState.copy(
            location = location,
            voiceLocationProvided = true,
        )
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
            val inferred = inferShoppingProduct(itemName)
            val categoryIconKey = inferred?.categoryIconKey ?: addShoppingState.destination.toCategoryIconKey()
            val iconKey = inferred?.productIconKey ?: categoryIconKey
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
                category = categoryIconKey,
                destinationKey = addShoppingState.location.toDestinationKey(),
                iconKey = iconKey,
            )
            saveShoppingItems(currentItems)
        }
        addShoppingState = AddShoppingProductUiState()
        showAddShoppingProduct = false
    }

    fun addInventoryProduct() {
        val itemName = addShoppingState.productName.trim()
        if (itemName.isNotEmpty()) {
            val inferred = inferShoppingProduct(itemName)
            val categoryIconKey = inferred?.categoryIconKey ?: addShoppingState.destination.toCategoryIconKey()
            val iconKey = inferred?.productIconKey ?: categoryIconKey
            val parsedQuantity = parseQuantityInput(
                rawValue = addShoppingState.quantity,
                quantityMode = addShoppingState.quantityMode,
            )
            val currentPantry = getPantryFoods().toMutableList()
            currentPantry += PantryFood(
                id = "pantry_manual_${currentPantry.size + 1}_${itemName.lowercase().replace(" ", "_")}",
                name = itemName,
                quantity = parsedQuantity.displayLabel,
                quantityValue = parsedQuantity.value,
                quantityUnit = parsedQuantity.unit,
                category = shoppingCategoryLabelForIconKey(categoryIconKey),
                locationKey = addShoppingState.location.toDestinationKey(),
                expiryLabel = null,
                expiryDateIso = null,
                addedDateIso = platformTodayIsoDate(),
                iconKey = iconKey,
            )
            savePantryFoods(currentPantry)
            pantryFoods = currentPantry.map { it.toPantryFoodUi() }
        }
        addShoppingState = AddShoppingProductUiState()
        pantryFlow = PantryFlow.LIST
    }

    fun applySpokenShoppingInput(spokenText: String) {
        applyGuidedSpokenProductInput(spokenText, AddProductTarget.ShoppingList)
    }

    fun applySpokenProductInput(spokenText: String, target: AddProductTarget) {
        applyGuidedSpokenProductInput(spokenText, target)
    }

    fun prepareGuidedVoiceInput(): String {
        val phase = resolveGuidedVoicePhase(addShoppingState)
        val prompt = guidedVoicePromptFor(phase)
        addShoppingState = addShoppingState.copy(
            selectedMode = AddShoppingMode.Voice,
            voicePhase = phase,
            voicePrompt = prompt,
        )
        return prompt
    }

    fun applyGuidedSpokenProductInput(spokenText: String, target: AddProductTarget): Boolean {
        if (spokenText.isBlank()) {
            addShoppingState = addShoppingState.copy(
                selectedMode = AddShoppingMode.Voice,
                voicePrompt = "No te he entendido. Toca para repetir",
            )
            return false
        }
        val currentPhase = addShoppingState.voicePhase
        val parsed = parseSpokenShoppingInput(spokenText)
        val productName = when {
            currentPhase == GuidedVoiceAddPhase.ProductName -> {
                parsed.productName ?: spokenText.toVoiceProductName().orEmpty()
            }
            addShoppingState.productName.isBlank() -> parsed.productName.orEmpty()
            else -> addShoppingState.productName
        }
        val inferredCategory = inferShoppingCategoryLabel(productName)
        val parsedCategory = parsed.category.takeIf {
            currentPhase == GuidedVoiceAddPhase.ProductName || currentPhase == GuidedVoiceAddPhase.Category
        }
        val categoryProvided = addShoppingState.voiceCategoryProvided || parsedCategory != null || inferredCategory != null
        val quantityProvided = addShoppingState.voiceQuantityProvided || parsed.quantity != null
        val locationProvided = addShoppingState.voiceLocationProvided || parsed.location != null
        val nextState = addShoppingState.copy(
            selectedMode = AddShoppingMode.Voice,
            productName = productName,
            destination = parsedCategory?.toShoppingCategoryLabel()
                ?: inferredCategory
                ?: addShoppingState.destination,
            quantity = parsed.quantity ?: addShoppingState.quantity,
            quantityMode = parsed.quantityMode ?: addShoppingState.quantityMode,
            location = parsed.location ?: addShoppingState.location,
            voiceCategoryProvided = categoryProvided,
            voiceQuantityProvided = quantityProvided,
            voiceLocationProvided = locationProvided,
        )
        val nextPhase = resolveGuidedVoicePhase(nextState)
        val prompt = guidedVoicePromptFor(nextPhase)
        addShoppingState = addShoppingState.copy(
            selectedMode = nextState.selectedMode,
            productName = nextState.productName,
            destination = nextState.destination,
            quantity = nextState.quantity,
            quantityMode = nextState.quantityMode,
            location = nextState.location,
            voicePhase = nextPhase,
            voicePrompt = prompt,
            voiceCategoryProvided = nextState.voiceCategoryProvided,
            voiceQuantityProvided = nextState.voiceQuantityProvided,
            voiceLocationProvided = nextState.voiceLocationProvided,
        )
        if (nextPhase == GuidedVoiceAddPhase.Ready) {
            when (target) {
                AddProductTarget.ShoppingList -> addShoppingProduct()
                AddProductTarget.Inventory -> addInventoryProduct()
            }
            return false
        }
        return true
    }

    fun updateExpiryReminderDays(days: Int) {
        val clamped = days.coerceIn(2, 10)
        expiryReminderDays = clamped
        AppPreferences.setString(KEY_EXPIRY_REMINDER_DAYS, clamped.toString())
    }

    fun selectTab(tab: MainTab) {
        currentTab = tab
        if (tab == MainTab.RECIPES) {
            generateRecipesFromInventory()
        }
    }

    fun generateRecipesFromInventory() {
        generateRecipes(focusFoodId = null)
    }

    fun generateRecipesForFood(foodId: String) {
        generateRecipes(focusFoodId = foodId)
    }

    fun regenerateRecipes() {
        generateRecipes(focusFoodId = recipeFocusFoodId)
    }

    fun openRecipeDetail(recipe: Recipe) {
        selectedRecipe = recipe
        recipesFlow = RecipesFlow.DETAIL
    }

    fun unlockRecipeWithReward(recipeId: String) {
        unlockRecipesWithReward(listOf(recipeId))
    }

    fun unlockRecipesWithReward(recipeIds: List<String>) {
        val updated = unlockedRewardRecipeIds + recipeIds.filter { it.isNotBlank() }
        unlockedRewardRecipeIds = updated
        AppPreferences.setString(KEY_UNLOCKED_REWARD_RECIPE_IDS, updated.sorted().joinToString("|"))
    }

    fun showRecipeInformation() {
        showRecipeNotice = true
    }

    fun dismissRecipeNotice() {
        showRecipeNotice = false
        AppPreferences.setString(KEY_RECIPE_NOTICE_SEEN, "true")
    }

    fun addRecipeIngredientToShoppingList(ingredient: String) {
        val itemName = ingredient.toRecipeShoppingName()
        if (itemName.isBlank()) return

        val currentItems = getShoppingItems().toMutableList()
        val normalizedName = itemName.normalizeIngredientName()
        if (currentItems.any { it.name.normalizeIngredientName() == normalizedName }) return

        val iconKey = itemName.toRecipeShoppingIconKey()
        currentItems += ShoppingListItem(
            id = "shopping_recipe_${currentItems.size + 1}_${normalizedName.replace(" ", "_")}",
            name = itemName,
            quantity = "1 ud",
            quantityValue = "1",
            quantityUnit = "ud",
            checked = false,
            category = iconKey,
            destinationKey = itemName.toRecipeShoppingDestinationKey(),
            iconKey = iconKey,
        )
        saveShoppingItems(currentItems)
    }

    private fun generateRecipes(focusFoodId: String?) {
        val pantry = getPantryFoods()
        recipeFocusFoodId = focusFoodId
        recipeGenerationResult = generateRecipesUseCase(
            RecipeGenerationRequest(
                pantryFoods = pantry,
                focusFoodId = focusFoodId,
                maxMinutes = 30,
                maxResults = MAX_RECIPES_PER_SEARCH,
            ),
        )
        selectedRecipe = null
        currentTab = MainTab.RECIPES
        recipesFlow = RecipesFlow.RESULTS
    }

    private fun loadPantryFoodUi(): List<PantryFoodUi> = getPantryFoods().map { it.toPantryFoodUi() }

    private fun loadExpiryReminderDays(defaultDays: Int = 2): Int {
        return AppPreferences.getString(KEY_EXPIRY_REMINDER_DAYS)
            ?.toIntOrNull()
            ?.coerceIn(2, 10)
            ?: defaultDays.coerceIn(2, 10)
    }

    private fun loadUnlockedRewardRecipeIds(): Set<String> {
        return AppPreferences.getString(KEY_UNLOCKED_REWARD_RECIPE_IDS)
            ?.split("|")
            ?.map { it.trim() }
            ?.filter { it.isNotBlank() }
            ?.toSet()
            .orEmpty()
    }

    private fun isRecipeNoticeSeen(): Boolean {
        return AppPreferences.getString(KEY_RECIPE_NOTICE_SEEN) == "true"
    }
}

private fun ShoppingListItem.toPantryFood(nextIndex: Int): PantryFood {
    return PantryFood(
        id = "pantry_${id}_$nextIndex",
        name = name,
        quantity = quantity,
        quantityValue = quantityValue,
        quantityUnit = quantityUnit,
        category = shoppingCategoryLabelForIconKey(shoppingCategoryIconKeyForProductIcon(iconKey)),
        locationKey = destinationKey.toDestinationKey(),
        expiryLabel = null,
        expiryDateIso = null,
        addedDateIso = platformTodayIsoDate(),
        iconKey = iconKey,
    )
}

private fun String.toCategoryIconKey(): String {
    return shoppingCategoryIconKey(this)
}

private fun String.toShoppingCategoryLabel(): String {
    return shoppingCategoryLabelForIconKey(toCategoryIconKey())
}

private fun String.toDestinationKey(): String {
    return when (this) {
        "congelador", "freezer" -> "freezer"
        "nevera", "fridge" -> "fridge"
        else -> "pantry"
    }
}

private fun String.toRecipeShoppingName(): String {
    return trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

private fun String.normalizeIngredientName(): String {
    return lowercase()
        .map { char ->
            when (char) {
                'á', 'à', 'ä', 'â' -> 'a'
                'é', 'è', 'ë', 'ê' -> 'e'
                'í', 'ì', 'ï', 'î' -> 'i'
                'ó', 'ò', 'ö', 'ô' -> 'o'
                'ú', 'ù', 'ü', 'û' -> 'u'
                'ñ' -> 'n'
                else -> char
            }
        }
        .joinToString("")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun String.toRecipeShoppingIconKey(): String {
    val normalized = normalizeIngredientName()
    inferShoppingIconKey(normalized)?.let { return it }
    return when {
        normalized in setOf("bacalao", "merluza", "lubina", "atun", "bonito", "sardina") -> "fish"
        normalized in setOf("huevo") -> "eggs"
        normalized in setOf("pollo", "ternera", "cerdo", "chorizo", "jamon") -> "meat"
        normalized in setOf("pasta", "fideo", "macarron", "macarrones", "espagueti", "espaguetis") -> "pasta"
        normalized in setOf("arroz") -> "rice"
        normalized in setOf("lenteja", "lentejas", "garbanzo", "garbanzos", "alubia", "alubias") -> "legumes"
        normalized in setOf("leche") -> "milk"
        normalized in setOf("yogur") -> "yogurts"
        normalized in setOf("queso") -> "cheese"
        normalized in setOf("pan") -> "bread"
        normalized in setOf("aceite", "aceite de oliva") -> "oil"
        normalized in setOf("vinagre") -> "vinegar"
        normalized in setOf("pimenton", "tomate frito") -> "sauces"
        normalized in setOf("manzana", "naranja", "platano", "fresa", "fruta") -> "fruits"
        normalized in setOf(
            "patata",
            "cebolla",
            "pimiento",
            "zanahoria",
            "calabacin",
            "ajo",
            "tomate",
            "espinaca",
            "guisante",
            "alcachofa",
        ) -> "vegetables"
        else -> "other"
    }
}

private fun String.toRecipeShoppingDestinationKey(): String {
    return shoppingDestinationForIconKey(toRecipeShoppingIconKey())
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

private val voiceCategoryMap = listOf(
    "frutas" to listOf("fruta", "frutas", "fresa", "fresas"),
    "verduras" to listOf("verdura", "verduras", "vegetal", "vegetales"),
    "carne" to listOf("carne", "carnes"),
    "pescado" to listOf("pescado", "pescados", "pez"),
    "marisco" to listOf("marisco", "mariscos"),
    "pan" to listOf("pan", "panes"),
    "leche" to listOf("leche", "lacteo", "lacteos"),
    "yogures" to listOf("yogur", "yogures", "yoghurt"),
    "queso" to listOf("queso", "quesos"),
    "huevos" to listOf("huevo", "huevos"),
    "pasta" to listOf("pasta", "macarron", "macarrones", "espagueti", "espaguetis", "fideo", "fideos"),
    "arroz" to listOf("arroz"),
    "legumbres" to listOf("legumbre", "legumbres", "lenteja", "lentejas", "garbanzo", "garbanzos", "alubia", "alubias"),
    "conservas" to listOf("conserva", "conservas"),
    "congelados" to listOf("congelado", "congelados"),
    "agua" to listOf("agua"),
    "refrescos" to listOf("refresco", "refrescos"),
    "zumo" to listOf("zumo", "zumos", "jugo"),
    "vino" to listOf("vino", "vinos"),
    "cerveza" to listOf("cerveza", "cervezas"),
    "cafe" to listOf("cafe", "coffee"),
    "te" to listOf("te", "tea", "infusion"),
    "snacks" to listOf("snack", "snacks", "aperitivo", "aperitivos"),
    "dulces" to listOf("dulce", "dulces"),
    "salsas" to listOf("salsa", "salsas"),
    "aceite" to listOf("aceite"),
    "vinagre" to listOf("vinagre"),
    "platos listos" to listOf("plato listo", "platos listos"),
    "limpieza" to listOf("limpieza"),
    "higiene" to listOf("higiene"),
    "mascotas" to listOf("mascota", "mascotas"),
    "otros" to listOf("otros", "otro"),
)

private val voiceLocationMap = mapOf(
    "nevera" to listOf(
        "nevera",
        "neveras",
        "never",
        "neverita",
        "nebera",
        "frigorifico",
        "frigorificos",
        "frigo",
        "refrigerador",
        "heladera",
    ),
    "despensa" to listOf(
        "despensa",
        "despensas",
        "despnesa",
        "despenza",
        "dispensa",
        "espensa",
        "desp",
        "armario",
        "alacena",
        "almacen",
    ),
    "congelador" to listOf(
        "congelador",
        "congeladores",
        "congeladora",
        "congleador",
        "conjelador",
        "conjeladora",
        "congelar",
        "congela",
        "congel",
        "congelado",
        "congelados",
        "freezer",
    ),
)

private val voiceNumberWords = buildVoiceNumberWords()

private val voiceNumberPattern = "(?:\\d+(?:[\\.,]\\d+)?|${
    voiceNumberWords.keys
        .sortedByDescending { it.length }
        .joinToString("|") { Regex.escape(it) }
})"

internal data class ParsedSpokenShoppingInput(
    val productName: String? = null,
    val category: String? = null,
    val quantity: String? = null,
    val quantityMode: String? = null,
    val location: String? = null,
    val shouldAdd: Boolean = false,
)

private data class ParsedVoiceQuantity(
    val quantity: String,
    val quantityMode: String,
)

private fun parseVoiceQuantity(text: String): ParsedVoiceQuantity? {
    val gramUnits = "g|gr|grs|gram|gramo|gramos"
    val kilogramUnits = "kg|kilo|kilos|kilogramo|kilogramos"
    val unitWords = "pieza|piezas|unidad|unidades|uds|ud|brick|bricks|bote|botes|paquete|paquetes|lata|latas|botella|botellas"
    val compact = text.replace(Regex("\\s+"), "")
    val hasWeightMode = text.hasVoiceWord("peso", "pesa", "pesar", "gramo", "gramos", "gr", "grs")
    val hasKilogramMode = text.hasVoiceWord("kg", "kilo", "kilos", "kilogramo", "kilogramos")
    val hasUnitMode = text.hasVoiceWord("cantidad", "cantid", "unidad", "unidades", "uds", "ud")

    Regex("\\b(?:(($voiceNumberPattern))\\s+)?($kilogramUnits)\\s+y\\s+(?:medio|media)\\b").find(text)?.let { match ->
        val kilos = match.groupValues[1].takeIf { it.isNotBlank() }?.toVoiceNumber() ?: 1.0
        return ParsedVoiceQuantity(((kilos + 0.5) * 1000.0).toInt().coerceAtLeast(0).toString(), "Peso")
    }
    Regex("\\b(?:medio|media)\\s*($kilogramUnits)\\b").find(text)?.let {
        return ParsedVoiceQuantity("500", "Peso")
    }
    Regex("\\b($voiceNumberPattern)\\s*($gramUnits)\\b").find(text)?.let { match ->
        val grams = match.groupValues[1].toVoiceNumber()?.toInt() ?: return null
        return ParsedVoiceQuantity(grams.coerceAtLeast(0).toString(), "Peso")
    }
    Regex("^($voiceNumberPattern)($gramUnits)$").find(compact)?.let { match ->
        val grams = match.groupValues[1].toVoiceNumber()?.toInt() ?: return null
        return ParsedVoiceQuantity(grams.coerceAtLeast(0).toString(), "Peso")
    }
    Regex("\\b($voiceNumberPattern)\\s*($kilogramUnits)\\b").find(text)?.let { match ->
        val kilos = match.groupValues[1].toVoiceNumber() ?: return null
        return ParsedVoiceQuantity((kilos * 1000.0).toInt().coerceAtLeast(0).toString(), "Peso")
    }
    Regex("^($voiceNumberPattern)($kilogramUnits)$").find(compact)?.let { match ->
        val kilos = match.groupValues[1].toVoiceNumber() ?: return null
        return ParsedVoiceQuantity((kilos * 1000.0).toInt().coerceAtLeast(0).toString(), "Peso")
    }
    Regex("(?:peso|pesa|pesar)\\s*($voiceNumberPattern)").find(text)?.let { match ->
        val numeric = match.groupValues[1].toVoiceNumber() ?: return null
        val grams = if (numeric > 20.0) numeric.toInt() else (numeric * 1000.0).toInt()
        return ParsedVoiceQuantity(grams.coerceAtLeast(0).toString(), "Peso")
    }
    Regex("(?:cantidad|cantid|unidades|unidad|uds|ud)\\s*($voiceNumberPattern)").find(text)?.let { match ->
        val units = match.groupValues[1].toVoiceNumber()?.toInt() ?: return null
        return ParsedVoiceQuantity(units.coerceAtLeast(1).toString(), "Unidades")
    }
    Regex("\\b($voiceNumberPattern)\\s*(?:$unitWords)\\b").find(text)?.let { match ->
        val units = match.groupValues[1].toVoiceNumber()?.toInt() ?: return null
        return ParsedVoiceQuantity(units.coerceAtLeast(1).toString(), "Unidades")
    }
    Regex("^\\s*($voiceNumberPattern)\\s*$").find(text)?.let { match ->
        val units = match.groupValues[1].toVoiceNumber()?.toInt() ?: return null
        return ParsedVoiceQuantity(units.coerceAtLeast(1).toString(), "Unidades")
    }
    Regex("^\\s*(\\d+)").find(text)?.let { match ->
        val units = match.groupValues[1].toIntOrNull() ?: return null
        return ParsedVoiceQuantity(units.coerceAtLeast(1).toString(), "Unidades")
    }
    if (hasKilogramMode) return ParsedVoiceQuantity("1000", "Peso")
    if (hasWeightMode) return ParsedVoiceQuantity("100", "Peso")
    if (hasUnitMode) return ParsedVoiceQuantity("1", "Unidades")
    return null
}

internal fun parseSpokenShoppingInput(rawText: String): ParsedSpokenShoppingInput {
    val original = rawText.trim()
    if (original.isBlank()) return ParsedSpokenShoppingInput()
    val text = normalizeVoiceText(original)
    val category = voiceCategoryMap.firstNotNullOfOrNull { (canonical, aliases) ->
        if (aliases.any { alias -> Regex("\\b${Regex.escape(alias)}\\b").containsMatchIn(text) }) canonical else null
    }
    val location = inferVoiceLocation(text)
    val phaseQuantity = parseVoiceQuantity(text)
    val kiloAndHalfMatch = Regex("\\b(?:$voiceNumberPattern\\s+)?(?:kilo|kilos|kg|kilogramo|kilogramos)\\s+y\\s+(?:medio|media)\\b").find(text)
    val halfKgMatch = Regex("\\b(?:medio|media)\\s*(?:kg|kilo|kilos|kilogramo|kilogramos)\\b").find(text)
    val kgMatch = Regex("\\b($voiceNumberPattern)\\s*(kg|kilo|kilos|kilogramo|kilogramos)\\b").find(text)
    val grMatch = Regex("\\b($voiceNumberPattern)\\s*(g|gr|gramo|gramos)\\b").find(text)
    val unitWord = "(?:pieza|piezas|unidad|unidades|uds|ud|brick|bricks|bote|botes|paquete|paquetes|lata|latas|botella|botellas)"
    val unitsMatch = Regex("(?:cantidad|cantid|unidades|unidad|uds|ud)\\s*($voiceNumberPattern)").find(text)
    val piecesMatch = Regex("\\b($voiceNumberPattern)\\s*$unitWord\\b").find(text)
    val explicitWeightWithUnit = Regex("(?:peso|pesa|pesar)\\s*($voiceNumberPattern)\\s*(kg|kilo|kilos|kilogramo|kilogramos|g|gr|gramo|gramos)").find(text)
    val explicitWeightValue = Regex("(?:peso|pesa|pesar)\\s*($voiceNumberPattern)").find(text)
    val explicitQuantityValue = Regex("(?:cantidad|cantid|unidades|unidad|uds|ud)\\s*($voiceNumberPattern)").find(text)
    val looseNumber = Regex("\\b($voiceNumberPattern)\\b").find(text)
    val quantityMode: String?
    val quantity: String?
    when {
        phaseQuantity != null -> {
            quantity = phaseQuantity.quantity
            quantityMode = phaseQuantity.quantityMode
        }
        kiloAndHalfMatch != null -> {
            quantity = "1500"
            quantityMode = "Peso"
        }
        halfKgMatch != null -> {
            quantity = "500"
            quantityMode = "Peso"
        }
        explicitWeightWithUnit != null -> {
            val numeric = explicitWeightWithUnit.groupValues[1].toVoiceNumber() ?: 0.0
            val unit = explicitWeightWithUnit.groupValues[2]
            quantity = if (unit in listOf("kg", "kilo", "kilos", "kilogramo", "kilogramos")) {
                (numeric * 1000.0).toInt().coerceAtLeast(0).toString()
            } else {
                numeric.toInt().coerceAtLeast(0).toString()
            }
            quantityMode = "Peso"
        }
        kgMatch != null -> {
            val kgValue = kgMatch.groupValues[1].toVoiceNumber() ?: 0.0
            quantity = (kgValue * 1000.0).toInt().coerceAtLeast(0).toString()
            quantityMode = "Peso"
        }
        grMatch != null -> {
            quantity = (grMatch.groupValues[1].toVoiceNumber()?.toInt() ?: 0).coerceAtLeast(0).toString()
            quantityMode = "Peso"
        }
        explicitWeightValue != null -> {
            val numeric = explicitWeightValue.groupValues[1].toVoiceNumber() ?: 0.0
            quantity = when {
                numeric <= 0.0 -> "0"
                numeric > 20.0 -> numeric.toInt().toString()
                else -> (numeric * 1000.0).toInt().toString()
            }
            quantityMode = "Peso"
        }
        text.contains("peso") && looseNumber != null -> {
            val numeric = looseNumber.groupValues[1].toVoiceNumber() ?: 0.0
            quantity = if (numeric > 20.0) numeric.toInt().toString() else (numeric * 1000.0).toInt().toString()
            quantityMode = "Peso"
        }
        explicitQuantityValue != null -> {
            quantity = (explicitQuantityValue.groupValues[1].toVoiceNumber()?.toInt() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        piecesMatch != null -> {
            quantity = (piecesMatch.groupValues[1].toVoiceNumber()?.toInt() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        unitsMatch != null -> {
            quantity = (unitsMatch.groupValues[1].toVoiceNumber()?.toInt() ?: 1).coerceAtLeast(1).toString()
            quantityMode = "Unidades"
        }
        looseNumber != null -> {
            quantity = (looseNumber.groupValues[1].toVoiceNumber()?.toInt() ?: 1).coerceAtLeast(1).toString()
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
        shouldAdd = Regex("\\b(añade|anade|añadir|anadir|agrega|meter|mete|guardar|guarda)\\b").containsMatchIn(text),
    )
}

private fun resolveGuidedVoicePhase(state: AddShoppingProductUiState): GuidedVoiceAddPhase {
    val productName = state.productName.trim()
    return when {
        productName.isBlank() -> GuidedVoiceAddPhase.ProductName
        !state.voiceQuantityProvided -> GuidedVoiceAddPhase.Quantity
        !state.voiceLocationProvided -> GuidedVoiceAddPhase.Location
        !state.voiceCategoryProvided && inferShoppingCategoryLabel(productName) == null -> GuidedVoiceAddPhase.Category
        else -> GuidedVoiceAddPhase.Ready
    }
}

private fun guidedVoicePromptFor(phase: GuidedVoiceAddPhase): String {
    return when (phase) {
        GuidedVoiceAddPhase.ProductName -> "Paso 1: dime el nombre del producto"
        GuidedVoiceAddPhase.Quantity -> "Paso 2: dime cantidad o peso"
        GuidedVoiceAddPhase.Location -> "Paso 3: dime nevera, despensa o congelador"
        GuidedVoiceAddPhase.Category -> "Último paso: dime la categoría"
        GuidedVoiceAddPhase.Ready -> "Listo para guardar"
    }
}

private fun inferVoiceLocation(text: String): String? {
    val directMatch = voiceLocationMap.entries
        .flatMap { (canonical, aliases) ->
            aliases.flatMap { alias ->
                Regex("\\b${Regex.escape(alias)}\\b")
                    .findAll(text)
                    .map { canonical to it.range.first }
                    .toList()
            }
        }
        .maxByOrNull { it.second }
        ?.first
    if (directMatch != null) return directMatch

    val compact = text.replace(Regex("[^a-z0-9]"), "")
    return when {
        compact.contains("congel") ||
            compact.contains("congle") ||
            compact.contains("conjel") ||
            compact.contains("freezer") -> "congelador"
        compact.contains("despens") ||
            compact.contains("despenz") ||
            compact.contains("dispens") ||
            compact.contains("espens") -> "despensa"
        compact.contains("never") ||
            compact.contains("neber") ||
            compact.contains("frigo") ||
            compact.contains("frigor") ||
            compact.contains("refriger") -> "nevera"
        else -> null
    }
}

private fun normalizeVoiceText(value: String): String {
    return value
        .lowercase()
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("ü", "u")
}

private fun extractProductNameFromVoice(rawText: String): String? {
    var cleaned = normalizeVoiceText(rawText)
    val categories = voiceCategoryMap.flatMap { it.second }
        .sortedByDescending { it.length }
        .joinToString("|") { Regex.escape(it) }
    val numberWords = voiceNumberWords.keys.joinToString("|")
    val dropPatterns = listOf(
        "\\b(añade|anade|añadir|anadir|agrega|meter|mete|guardar|guarda)(\\s+producto)?\\b",
        "\\b(categoria|categorias)\\s+($categories)\\b",
        "\\b(guardar|guarda|meter|mete)?\\s*(a\\s+la\\s+|a\\s+el\\s+|en\\s+la\\s+|en\\s+el\\s+|en\\s+|al\\s+|a\\s+|para\\s+)?(nevera|neveras|never|neverita|nebera|frigorifico|frigorificos|frigo|refrigerador|heladera|despensa|despensas|despnesa|despenza|dispensa|espensa|desp|armario|alacena|almacen|congelador|congeladores|congeladora|congleador|conjelador|conjeladora|congelar|congela|congel|congelado|congelados|freezer)\\b",
        "\\b(peso|pesa|pesar|cantidad|cantid|unidades?|uds?|ud|gramos?|grs?|kilos?|kilogramos?|kg)\\b",
        "\\b\\d+(?:[\\.,]\\d+)?\\s*(kg|kilo|kilos|kilogramo|kilogramos|g|gr|gramo|gramos)\\b",
        "\\b($numberWords)\\s*(kg|kilo|kilos|kilogramo|kilogramos|g|gr|gramo|gramos)\\b",
        "\\b($voiceNumberPattern)\\s*(pieza|piezas|unidad|unidades|uds|ud|brick|bricks|bote|botes|paquete|paquetes|lata|latas|botella|botellas)\\b",
        "\\b\\d+\\b",
        "\\b($numberWords)\\b",
    )
    dropPatterns.forEach { pattern ->
        cleaned = cleaned.replace(Regex(pattern), " ")
    }
    cleaned = cleaned
        .replace(Regex("[,;:]+"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
        .removePrefix("producto ")
        .trim()
    if (cleaned.isBlank()) return null
    val deduped = cleaned.split(" ")
        .fold(emptyList<String>()) { acc, token ->
            if (acc.lastOrNull() == token) acc else acc + token
        }
    return deduped.joinToString(" ") { token ->
        token.replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase() else ch.toString() }
    }
}

private fun String.toVoiceProductName(): String? {
    val cleaned = trim()
        .replace(Regex("\\s+"), " ")
        .trim()
    if (cleaned.isBlank()) return null
    return cleaned.split(" ").joinToString(" ") { token ->
        token.replaceFirstChar { ch -> if (ch.isLowerCase()) ch.titlecase() else ch.toString() }
    }
}

private fun String.toVoiceNumber(): Double? {
    return voiceNumberWords[this] ?: normalizeVoiceText(this).let { normalized ->
        voiceNumberWords[normalized] ?: normalized.replace(',', '.').toDoubleOrNull()
    }
}

private fun String.hasVoiceWord(vararg words: String): Boolean {
    return words.any { word ->
        Regex("\\b${Regex.escape(word)}\\b").containsMatchIn(this)
    }
}

private fun String.toShoppingQuantityValue(quantityMode: String): String {
    val normalized = normalizeVoiceText(this)
    parseVoiceQuantity(normalized)?.let { return it.quantity }
    if (quantityMode == "Peso") {
        val number = normalized
            .replace(',', '.')
            .filter { it.isDigit() || it == '.' }
            .toDoubleOrNull()
            ?: return "0"
        return if (normalized.contains("kg") || normalized.contains("kilo")) {
            (number * 1000.0).toInt().coerceAtLeast(0).toString()
        } else {
            number.toInt().coerceAtLeast(0).toString()
        }
    }
    val digits = normalized.filter(Char::isDigit)
    return (digits.toIntOrNull() ?: 1).coerceAtLeast(1).toString()
}

private fun buildVoiceNumberWords(): LinkedHashMap<String, Double> {
    val map = linkedMapOf<String, Double>()
    val base = linkedMapOf(
        "cero" to 0,
        "un" to 1,
        "uno" to 1,
        "una" to 1,
        "dos" to 2,
        "tres" to 3,
        "cuatro" to 4,
        "cinco" to 5,
        "seis" to 6,
        "siete" to 7,
        "ocho" to 8,
        "nueve" to 9,
        "diez" to 10,
        "once" to 11,
        "doce" to 12,
        "trece" to 13,
        "catorce" to 14,
        "quince" to 15,
        "dieciseis" to 16,
        "diecisiete" to 17,
        "dieciocho" to 18,
        "diecinueve" to 19,
        "veinte" to 20,
        "veintiuno" to 21,
        "veintiuna" to 21,
        "veintidos" to 22,
        "veintitres" to 23,
        "veinticuatro" to 24,
        "veinticinco" to 25,
        "veintiseis" to 26,
        "veintisiete" to 27,
        "veintiocho" to 28,
        "veintinueve" to 29,
    )
    val tens = linkedMapOf(
        "treinta" to 30,
        "cuarenta" to 40,
        "cincuenta" to 50,
        "sesenta" to 60,
        "setenta" to 70,
        "ochenta" to 80,
        "noventa" to 90,
    )
    val hundreds = linkedMapOf(
        "ciento" to 100,
        "doscientos" to 200,
        "trescientos" to 300,
        "cuatrocientos" to 400,
        "quinientos" to 500,
        "seiscientos" to 600,
        "setecientos" to 700,
        "ochocientos" to 800,
        "novecientos" to 900,
    )

    base.forEach { (word, value) -> map[word] = value.toDouble() }
    tens.forEach { (word, value) -> map[word] = value.toDouble() }
    tens.forEach { (tenWord, tenValue) ->
        base.filterValues { it in 1..9 }.forEach { (unitWord, unitValue) ->
            map["$tenWord y $unitWord"] = (tenValue + unitValue).toDouble()
        }
    }
    map["cien"] = 100.0
    hundreds.forEach { (hundredWord, hundredValue) ->
        map[hundredWord] = hundredValue.toDouble()
        for (rest in 1..99) {
            val restWord = numberBelowHundredToVoiceWord(rest)
            if (restWord != null) {
                map["$hundredWord $restWord"] = (hundredValue + rest).toDouble()
            }
        }
    }
    map["mil"] = 1000.0
    map["medio"] = 0.5
    map["media"] = 0.5
    return linkedMapOf(*map.entries.sortedByDescending { it.key.length }.map { it.key to it.value }.toTypedArray())
}

private fun numberBelowHundredToVoiceWord(value: Int): String? {
    val base = mapOf(
        1 to "uno",
        2 to "dos",
        3 to "tres",
        4 to "cuatro",
        5 to "cinco",
        6 to "seis",
        7 to "siete",
        8 to "ocho",
        9 to "nueve",
        10 to "diez",
        11 to "once",
        12 to "doce",
        13 to "trece",
        14 to "catorce",
        15 to "quince",
        16 to "dieciseis",
        17 to "diecisiete",
        18 to "dieciocho",
        19 to "diecinueve",
        20 to "veinte",
        21 to "veintiuno",
        22 to "veintidos",
        23 to "veintitres",
        24 to "veinticuatro",
        25 to "veinticinco",
        26 to "veintiseis",
        27 to "veintisiete",
        28 to "veintiocho",
        29 to "veintinueve",
    )
    base[value]?.let { return it }
    val tens = mapOf(
        30 to "treinta",
        40 to "cuarenta",
        50 to "cincuenta",
        60 to "sesenta",
        70 to "setenta",
        80 to "ochenta",
        90 to "noventa",
    )
    val tenValue = (value / 10) * 10
    val unit = value % 10
    val tenWord = tens[tenValue] ?: return null
    return if (unit == 0) tenWord else "$tenWord y ${base[unit]}"
}
