package es.neverachefai.feature.navigation

enum class RootFlow {
    WELCOME,
    ONBOARDING,
    PREFERENCES,
    MAIN,
}

enum class MainTab(val label: String, val shortLabel: String) {
    HOME("Inicio", "I"),
    PANTRY("Nevera", "N"),
    RECIPES("Recetas", "R"),
    SHOPPING("Compra", "C"),
    SETTINGS("Ajustes", "A"),
}

enum class PantryFlow {
    LIST,
    ADD,
    REVIEW,
    DETAIL,
}

enum class RecipesFlow {
    GENERATE,
    RESULTS,
    DETAIL,
}
