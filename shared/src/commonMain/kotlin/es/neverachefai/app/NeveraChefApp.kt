package es.neverachefai.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
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
import es.neverachefai.feature.pantry.ui.PantryScreen
import es.neverachefai.feature.recipes.ui.RecipeDetailScreen
import es.neverachefai.feature.recipes.ui.RecipeGenerationScreen
import es.neverachefai.feature.recipes.ui.RecipeResultsScreen
import es.neverachefai.feature.settings.ui.SettingsScreen
import es.neverachefai.feature.shopping.ui.AddShoppingMode
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
    val appState = rememberNeveraChefAppState()

    when (appState.rootFlow) {
        RootFlow.WELCOME -> OnboardingScreen(onContinue = { appState.rootFlow = RootFlow.PREFERENCES })
        RootFlow.ONBOARDING -> OnboardingScreen(onContinue = { appState.rootFlow = RootFlow.PREFERENCES })
        RootFlow.PREFERENCES -> InitialPreferencesScreen(onSave = appState::completeOnboarding)
        RootFlow.MAIN -> NeveraMainScaffold(
            selectedTab = appState.currentTab,
            onTabSelected = { appState.currentTab = it },
            showBottomBar = !(appState.currentTab == MainTab.SHOPPING && appState.showAddShoppingProduct) &&
                !(appState.currentTab == MainTab.PANTRY && appState.pantryFlow == PantryFlow.DETAIL),
            contentHorizontalPadding = if (appState.currentTab == MainTab.SHOPPING && appState.showAddShoppingProduct) 0.dp else 12.dp,
            contentVerticalPadding = if (appState.currentTab == MainTab.SHOPPING && appState.showAddShoppingProduct) 0.dp else 8.dp,
            content = {
                when (appState.currentTab) {
                    MainTab.PANTRY -> PantryContent(appState)
                    MainTab.RECIPES -> RecipesContent(appState)
                    MainTab.SHOPPING -> ShoppingContent(
                        appState = appState,
                        microphonePermissionGranted = microphonePermissionGranted,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onRequestSpeechToText = onRequestSpeechToText,
                    )
                    MainTab.SETTINGS -> SettingsScreen(
                        cameraPermissionGranted = cameraPermissionGranted,
                        microphonePermissionGranted = microphonePermissionGranted,
                        expiryReminderDays = appState.expiryReminderDays,
                        onExpiryReminderDaysChange = appState::updateExpiryReminderDays,
                        onRequestCameraPermission = onRequestCameraPermission,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onReset = appState::reset,
                    )
                }
            },
        )
    }
}

@Composable
private fun PantryContent(appState: NeveraChefAppState) {
    when (appState.pantryFlow) {
        PantryFlow.LIST -> PantryScreen(
            foods = appState.pantryFoods,
            expiryReminderDays = appState.expiryReminderDays,
            onAdd = { appState.pantryFlow = PantryFlow.ADD },
            onReview = { appState.pantryFlow = PantryFlow.REVIEW },
            onFoodClick = {
                appState.selectedFood = it
                appState.pantryFlow = PantryFlow.DETAIL
            },
            onDeleteFoods = appState::deletePantryFoods,
        )
        PantryFlow.ADD -> AddIngredientsScreen(onBack = { appState.pantryFlow = PantryFlow.LIST })
        PantryFlow.REVIEW -> IngredientReviewScreen(onBack = { appState.pantryFlow = PantryFlow.LIST })
        PantryFlow.DETAIL -> FoodDetailScreen(
            food = appState.selectedFood,
            onBack = { appState.pantryFlow = PantryFlow.LIST },
            onSaveEditedFood = appState::saveEditedFood,
        )
    }
}

@Composable
private fun RecipesContent(appState: NeveraChefAppState) {
    when (appState.recipesFlow) {
        RecipesFlow.GENERATE -> RecipeGenerationScreen(
            onGenerate = { appState.recipesFlow = RecipesFlow.RESULTS },
        )
        RecipesFlow.RESULTS -> RecipeResultsScreen(
            onOpenDetail = { appState.recipesFlow = RecipesFlow.DETAIL },
        )
        RecipesFlow.DETAIL -> RecipeDetailScreen(
            onBack = { appState.recipesFlow = RecipesFlow.RESULTS },
        )
    }
}

@Composable
private fun ShoppingContent(
    appState: NeveraChefAppState,
    microphonePermissionGranted: Boolean,
    onRequestMicrophonePermission: () -> Unit,
    onRequestSpeechToText: ((String) -> Unit) -> Unit,
) {
    if (appState.showAddShoppingProduct) {
        AddShoppingProductScreen(
            state = appState.addShoppingState,
            onModeSelected = { appState.addShoppingState = appState.addShoppingState.copy(selectedMode = it) },
            onProductNameChange = { appState.addShoppingState = appState.addShoppingState.copy(productName = it) },
            onQuantityChange = { appState.addShoppingState = appState.addShoppingState.copy(quantity = it) },
            onQuantityModeChange = { appState.addShoppingState = appState.addShoppingState.copy(quantityMode = it) },
            onDestinationChange = { appState.addShoppingState = appState.addShoppingState.copy(destination = it) },
            onLocationChange = { appState.addShoppingState = appState.addShoppingState.copy(location = it) },
            onVoiceClick = {
                if (!microphonePermissionGranted) {
                    onRequestMicrophonePermission()
                } else {
                    onRequestSpeechToText { spokenText ->
                        appState.addShoppingState = appState.addShoppingState.copy(selectedMode = AddShoppingMode.Voice)
                        appState.applySpokenShoppingInput(spokenText)
                    }
                }
            },
            onCameraClick = {},
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
