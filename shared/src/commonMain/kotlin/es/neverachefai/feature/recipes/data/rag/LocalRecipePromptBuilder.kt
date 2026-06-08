package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationRequest

internal class LocalRecipePromptBuilder {
    fun build(
        request: RecipeGenerationRequest,
        matches: List<RecipeMatch>,
    ): String {
        val foods = request.pantryFoods.joinToString(separator = "\n") { food ->
            "- ${food.name}: ${food.quantity} (${food.locationKey})"
        }
        val cards = matches.joinToString(separator = "\n\n") { match ->
            """
            id=${match.card.id}
            titulo=${match.card.title}
            minutos=${match.card.estimatedMinutes}
            ingredientes_base=${match.card.requiredIngredients.joinToString()}
            ingredientes_disponibles=${match.usedIngredients.joinToString()}
            ingredientes_faltantes=${match.missingIngredients.joinToString()}
            pasos_base=${match.card.steps.joinToString(" | ")}
            """.trimIndent()
        }

        return """
            Eres el generador local de recetas de NeveraChef.
            No puedes usar conocimiento externo ni inventar recetas fuera de las tarjetas RAG.
            No hay input libre del usuario.
            Devuelve solo recetas sencillas basadas en las tarjetas.
            Si falta un ingrediente, decláralo como faltante.
            Tiempo máximo: ${request.maxMinutes} minutos.

            Inventario:
            $foods

            Tarjetas RAG permitidas:
            $cards
        """.trimIndent()
    }
}
