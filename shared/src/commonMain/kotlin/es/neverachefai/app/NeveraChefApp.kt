package es.neverachefai.app

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import es.neverachefai.core.ads.showNeveraChefRewardedAd
import es.neverachefai.core.ui.components.NeveraMainScaffold
import es.neverachefai.core.ui.system.PlatformBackHandler
import es.neverachefai.feature.navigation.MainTab
import es.neverachefai.feature.navigation.PantryFlow
import es.neverachefai.feature.navigation.RecipesFlow
import es.neverachefai.feature.navigation.RootFlow
import es.neverachefai.feature.onboarding.ui.InitialPreferencesScreen
import es.neverachefai.feature.onboarding.ui.OnboardingScreen
import es.neverachefai.feature.pantry.ui.FoodDetailScreen
import es.neverachefai.feature.pantry.ui.PantryScreen
import es.neverachefai.feature.recipes.ui.RecipeDetailScreen
import es.neverachefai.feature.recipes.ui.RecipeResultsScreen
import es.neverachefai.feature.settings.ui.SettingsScreen
import es.neverachefai.feature.shopping.ui.AddProductTarget
import es.neverachefai.feature.shopping.ui.AddShoppingProductScreen
import es.neverachefai.feature.shopping.ui.AddShoppingProductUiState
import es.neverachefai.feature.shopping.ui.ShoppingListScreen

@Composable
fun NeveraChefApp(
    microphonePermissionGranted: Boolean = false,
    onRequestMicrophonePermission: () -> Unit = {},
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit = { _, _ -> },
    onExitApp: () -> Unit = {},
) {
    val appState = rememberNeveraChefAppState()
    PlatformBackHandler(onBack = appState::handleSystemBack)
    if (appState.showExitConfirmation) {
        ExitConfirmationDialog(
            onDismiss = { appState.showExitConfirmation = false },
            onExit = {
                appState.showExitConfirmation = false
                onExitApp()
            },
        )
    }

    when (appState.rootFlow) {
        RootFlow.WELCOME -> OnboardingScreen(onContinue = { appState.rootFlow = RootFlow.PREFERENCES })
        RootFlow.ONBOARDING -> OnboardingScreen(onContinue = { appState.rootFlow = RootFlow.PREFERENCES })
        RootFlow.PREFERENCES -> InitialPreferencesScreen(onSave = appState::completeOnboarding)
        RootFlow.MAIN -> NeveraMainScaffold(
            selectedTab = appState.currentTab,
            onTabSelected = appState::selectTab,
            showBottomBar = !(appState.currentTab == MainTab.SHOPPING && appState.showAddShoppingProduct) &&
                !(appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.ADD) &&
                !(appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.DETAIL) &&
                !(appState.currentTab == MainTab.RECIPES && appState.recipesFlow == RecipesFlow.DETAIL),
            contentHorizontalPadding = if (
                (appState.currentTab == MainTab.SHOPPING && appState.showAddShoppingProduct) ||
                (appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.ADD)
            ) 0.dp else 12.dp,
            contentVerticalPadding = if (
                appState.currentTab == MainTab.SHOPPING ||
                (appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.LIST) ||
                (appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.ADD)
            ) 0.dp else 8.dp,
            content = {
                when (appState.currentTab) {
                    MainTab.PANTRY -> PantryContent(
                        appState = appState,
                        microphonePermissionGranted = microphonePermissionGranted,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onRequestSpeechToText = onRequestSpeechToText,
                    )
                    MainTab.RECIPES -> RecipesContent(appState)
                    MainTab.SHOPPING -> ShoppingContent(
                        appState = appState,
                        microphonePermissionGranted = microphonePermissionGranted,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onRequestSpeechToText = onRequestSpeechToText,
                    )
                    MainTab.SETTINGS -> SettingsScreen(
                        microphonePermissionGranted = microphonePermissionGranted,
                        expiryReminderDays = appState.expiryReminderDays,
                        onExpiryReminderDaysChange = appState::updateExpiryReminderDays,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onReset = appState::reset,
                    )
                }
            },
        )
    }
}

private fun runGuidedVoiceAddProduct(
    appState: NeveraChefAppState,
    target: AddProductTarget,
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit,
) {
    val prompt = appState.prepareGuidedVoiceInput()
    onRequestSpeechToText(prompt) { spokenText ->
        val shouldContinue = appState.applyGuidedSpokenProductInput(spokenText, target)
        if (shouldContinue) {
            runGuidedVoiceAddProduct(appState, target, onRequestSpeechToText)
        }
    }
}

@Composable
private fun ExitConfirmationDialog(
    onDismiss: () -> Unit,
    onExit: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Salir de NeveraChef") },
        text = { Text("¿Seguro que quieres salir de la aplicación?") },
        confirmButton = {
            TextButton(onClick = onExit) {
                Text("Salir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

@Composable
private fun PantryContent(
    appState: NeveraChefAppState,
    microphonePermissionGranted: Boolean,
    onRequestMicrophonePermission: () -> Unit,
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit,
) {
    when (appState.pantryFlow) {
        PantryFlow.LIST -> PantryScreen(
            foods = appState.pantryFoods,
            expiryReminderDays = appState.expiryReminderDays,
            onAdd = {
                appState.addShoppingState = AddShoppingProductUiState()
                appState.pantryFlow = PantryFlow.ADD
            },
            onFoodClick = {
                appState.selectedFood = it
                appState.pantryFlow = PantryFlow.DETAIL
            },
            onDeleteFoods = appState::deletePantryFoods,
        )
        PantryFlow.ADD -> AddShoppingProductScreen(
            state = appState.addShoppingState,
            target = AddProductTarget.Inventory,
            onModeSelected = { appState.addShoppingState = appState.addShoppingState.copy(selectedMode = it) },
            onProductNameChange = appState::updateAddShoppingProductName,
            onQuantityChange = appState::updateAddShoppingQuantity,
            onQuantityModeChange = appState::updateAddShoppingQuantityMode,
            onDestinationChange = appState::updateAddShoppingDestination,
            onLocationChange = appState::updateAddShoppingLocation,
            onProductNameVoiceClick = {
                if (!microphonePermissionGranted) {
                    onRequestMicrophonePermission()
                } else {
                    onRequestSpeechToText("Di solo el nombre del producto") { spokenText ->
                        appState.applySpokenProductName(spokenText)
                    }
                }
            },
            onGuidedVoiceClick = {
                if (!microphonePermissionGranted) {
                    onRequestMicrophonePermission()
                } else {
                    runGuidedVoiceAddProduct(appState, AddProductTarget.Inventory, onRequestSpeechToText)
                }
            },
            onBackClick = {
                appState.addShoppingState = AddShoppingProductUiState()
                appState.pantryFlow = PantryFlow.LIST
            },
            onAddToShoppingListClick = appState::addInventoryProduct,
        )
        PantryFlow.DETAIL -> FoodDetailScreen(
            food = appState.selectedFood,
            onBack = { appState.pantryFlow = PantryFlow.LIST },
            onSaveEditedFood = appState::saveEditedFood,
            onGenerateRecipe = { food -> appState.generateRecipesForFood(food.id) },
        )
    }
}

@Composable
private fun RecipesContent(appState: NeveraChefAppState) {
    when (appState.recipesFlow) {
        RecipesFlow.RESULTS -> RecipeResultsScreen(
            result = appState.recipeGenerationResult,
            unlockedRecipeIds = appState.unlockedRewardRecipeIds,
            showGeneratedRecipeNotice = appState.showRecipeNotice,
            onGeneratedRecipeNoticeDismiss = appState::dismissRecipeNotice,
            onRecipeInfoClick = appState::showRecipeInformation,
            onOpenDetail = appState::openRecipeDetail,
            onWatchRewardedAdForRecipes = { recipeIds ->
                showNeveraChefRewardedAd {
                    appState.unlockRecipesWithReward(recipeIds)
                }
            },
            onGenerateAgain = appState::regenerateRecipes,
        )
        RecipesFlow.DETAIL -> RecipeDetailScreen(
            recipe = appState.selectedRecipe,
            onBack = { appState.recipesFlow = RecipesFlow.RESULTS },
            onAddIngredientToShoppingList = appState::addRecipeIngredientToShoppingList,
            shoppingIngredientNames = appState.shoppingItems.map { it.name },
        )
    }
}

@Composable
private fun ShoppingContent(
    appState: NeveraChefAppState,
    microphonePermissionGranted: Boolean,
    onRequestMicrophonePermission: () -> Unit,
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit,
) {
    if (appState.showAddShoppingProduct) {
        AddShoppingProductScreen(
            state = appState.addShoppingState,
            onModeSelected = { appState.addShoppingState = appState.addShoppingState.copy(selectedMode = it) },
            onProductNameChange = appState::updateAddShoppingProductName,
            onQuantityChange = appState::updateAddShoppingQuantity,
            onQuantityModeChange = appState::updateAddShoppingQuantityMode,
            onDestinationChange = appState::updateAddShoppingDestination,
            onLocationChange = appState::updateAddShoppingLocation,
            onProductNameVoiceClick = {
                if (!microphonePermissionGranted) {
                    onRequestMicrophonePermission()
                } else {
                    onRequestSpeechToText("Di solo el nombre del producto") { spokenText ->
                        appState.applySpokenProductName(spokenText)
                    }
                }
            },
            onGuidedVoiceClick = {
                if (!microphonePermissionGranted) {
                    onRequestMicrophonePermission()
                } else {
                    runGuidedVoiceAddProduct(appState, AddProductTarget.ShoppingList, onRequestSpeechToText)
                }
            },
            onBackClick = {
                appState.addShoppingState = AddShoppingProductUiState()
                appState.showAddShoppingProduct = false
            },
            onAddToShoppingListClick = appState::addShoppingProduct,
        )
    } else {
        ShoppingListScreen(
            items = appState.shoppingItems,
            onItemsChange = appState::saveShoppingItems,
            onFinalizePurchase = appState::finalizeShoppingPurchase,
            onAddProductClick = { appState.showAddShoppingProduct = true },
        )
    }
}
