package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.domain.model.Difficulty

internal data class LocalLlmRecipeDraft(
    val title: String,
    val estimatedMinutes: Int?,
    val servings: Int?,
    val difficulty: Difficulty,
    val ingredientsUsed: List<String>,
    val missingIngredients: List<String>,
    val optionalIngredients: List<String>,
    val steps: List<String>,
)

internal object LocalLlmRecipeParser {
    fun parse(raw: String?): LocalLlmRecipeDraft? {
        val json = raw?.substringAfter("{", missingDelimiterValue = "")?.substringBeforeLast("}")?.let { "{$it}" }
            ?: return null
        val title = stringField(json, "title", "titulo")?.cleanValue().orEmpty()
        val ingredientsUsed = arrayField(json, "ingredients_used", "ingredientes_disponibles", "ingredientes_usados")
        val steps = arrayField(json, "steps", "pasos")

        if (title.isBlank() || ingredientsUsed.isEmpty() || steps.size < 2) return null

        return LocalLlmRecipeDraft(
            title = title,
            estimatedMinutes = intField(json, "estimated_minutes", "estimatedMinutes", "minutes", "minutos"),
            servings = intField(json, "servings", "raciones"),
            difficulty = difficultyField(json),
            ingredientsUsed = ingredientsUsed,
            missingIngredients = arrayField(json, "missing_ingredients", "ingredientes_faltantes"),
            optionalIngredients = arrayField(json, "optional_ingredients", "ingredientes_opcionales"),
            steps = steps,
        )
    }

    private fun stringField(
        json: String,
        vararg keys: String,
    ): String? {
        return keys.firstNotNullOfOrNull { key ->
            Regex("\"${Regex.escape(key)}\"\\s*:\\s*\"([^\"]*)\"", RegexOption.IGNORE_CASE)
                .find(json)
                ?.groupValues
                ?.getOrNull(1)
        }
    }

    private fun intField(
        json: String,
        vararg keys: String,
    ): Int? {
        return keys.firstNotNullOfOrNull { key ->
            Regex("\"${Regex.escape(key)}\"\\s*:\\s*\"?(\\d+)\"?", RegexOption.IGNORE_CASE)
                .find(json)
                ?.groupValues
                ?.getOrNull(1)
                ?.toIntOrNull()
        }
    }

    private fun arrayField(
        json: String,
        vararg keys: String,
    ): List<String> {
        return keys.firstNotNullOfOrNull { key ->
            Regex("\"${Regex.escape(key)}\"\\s*:\\s*\\[(.*?)]", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
                .find(json)
                ?.groupValues
                ?.getOrNull(1)
                ?.let { arrayBody ->
                    Regex("\"([^\"]+)\"")
                        .findAll(arrayBody)
                        .map { it.groupValues[1].cleanValue() }
                        .filter { it.isNotBlank() }
                        .distinct()
                        .toList()
                }
        }.orEmpty()
    }

    private fun difficultyField(json: String): Difficulty {
        return when (stringField(json, "difficulty", "dificultad")?.cleanValue()?.lowercase()) {
            "hard", "dificil", "difícil" -> Difficulty.HARD
            "medium", "media", "medio" -> Difficulty.MEDIUM
            else -> Difficulty.EASY
        }
    }

    private fun String.cleanValue(): String {
        return replace("\\\"", "\"")
            .replace("\\n", " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
