package es.neverachefai.domain.model

data class UserPreferences(
    val peopleCount: Int = 2,
    val hasChildren: Boolean = false,
    val goal: MealGoal = MealGoal.FAST,
    val maxCookingMinutes: Int = 30,
    val restrictions: String = "",
)

enum class MealGoal {
    FAST,
    HEALTHY,
    LOW_COST,
    HIGH_PROTEIN,
}
