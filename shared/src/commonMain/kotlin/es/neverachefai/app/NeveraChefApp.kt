package es.neverachefai.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import es.neverachefai.feature.recipes.ui.RecipeDetailScreen
import es.neverachefai.feature.recipes.ui.RecipeGenerationScreen
import es.neverachefai.feature.recipes.ui.RecipeResultsScreen
import es.neverachefai.feature.settings.ui.SettingsScreen
import es.neverachefai.feature.shopping.ui.ShoppingCartScreen

@Composable
fun NeveraChefApp(
    cameraPermissionGranted: Boolean = false,
    microphonePermissionGranted: Boolean = false,
    onRequestCameraPermission: () -> Unit = {},
    onRequestMicrophonePermission: () -> Unit = {},
) {
    val onboardingSeen = remember { AppPreferences.isOnboardingSeen() }
    var rootFlow by remember { mutableStateOf(if (onboardingSeen) RootFlow.MAIN else RootFlow.ONBOARDING) }
    var currentTab by remember { mutableStateOf(MainTab.PANTRY) }
    var pantryFlow by remember { mutableStateOf(PantryFlow.LIST) }
    var selectedFood by remember { mutableStateOf<PantryFoodUi?>(null) }
    var recipesFlow by remember { mutableStateOf(RecipesFlow.GENERATE) }

    val completeOnboarding: () -> Unit = {
        AppPreferences.setOnboardingSeen(true)
        currentTab = MainTab.PANTRY
        rootFlow = RootFlow.MAIN
    }

    val openOnboardingFromSettings: () -> Unit = {
        rootFlow = RootFlow.ONBOARDING
        currentTab = MainTab.PANTRY
        pantryFlow = PantryFlow.LIST
        selectedFood = null
        recipesFlow = RecipesFlow.GENERATE
    }

    val resetAppState: () -> Unit = {
        AppPreferences.clearAll()
        rootFlow = RootFlow.ONBOARDING
        currentTab = MainTab.PANTRY
        pantryFlow = PantryFlow.LIST
        selectedFood = null
        recipesFlow = RecipesFlow.GENERATE
    }

    when (rootFlow) {
        RootFlow.WELCOME -> OnboardingScreen(onContinue = { rootFlow = RootFlow.PREFERENCES })
        RootFlow.ONBOARDING -> OnboardingScreen(onContinue = { rootFlow = RootFlow.PREFERENCES })
        RootFlow.PREFERENCES -> InitialPreferencesScreen(onSave = completeOnboarding)
        RootFlow.MAIN -> NeveraMainScaffold(
            selectedTab = currentTab,
            onTabSelected = { currentTab = it },
            content = {
                when (currentTab) {
                    MainTab.PANTRY -> when (pantryFlow) {
                        PantryFlow.LIST -> PantryScreen(
                            onAdd = { pantryFlow = PantryFlow.ADD },
                            onReview = { pantryFlow = PantryFlow.REVIEW },
                            onFoodClick = {
                                selectedFood = it
                                pantryFlow = PantryFlow.DETAIL
                            },
                        )

                        PantryFlow.ADD -> AddIngredientsScreen(onBack = { pantryFlow = PantryFlow.LIST })
                        PantryFlow.REVIEW -> IngredientReviewScreen(onBack = { pantryFlow = PantryFlow.LIST })
                        PantryFlow.DETAIL -> FoodDetailScreen(
                            food = selectedFood,
                            onBack = { pantryFlow = PantryFlow.LIST },
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

                    MainTab.SHOPPING -> ShoppingCartScreen()
                    MainTab.SETTINGS -> SettingsScreen(
                        cameraPermissionGranted = cameraPermissionGranted,
                        microphonePermissionGranted = microphonePermissionGranted,
                        onRequestCameraPermission = onRequestCameraPermission,
                        onRequestMicrophonePermission = onRequestMicrophonePermission,
                        onOpenOnboarding = openOnboardingFromSettings,
                        onReset = resetAppState,
                    )
                }
            },
        )
    }
}
