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
            Tu unica tarea es ordenar las tarjetas por utilidad para este inventario.
            No escribas pasos nuevos ni ingredientes nuevos.
            Tiempo máximo: ${request.maxMinutes} minutos.
            Devuelve solo JSON valido con esta forma:
            {"selected_recipe_ids":["id-1","id-2"]}

            Inventario:
            $foods

            Tarjetas RAG permitidas:
            $cards
        """.trimIndent()
    }

    fun buildFallback(request: RecipeGenerationRequest): String {
        val foods = request.pantryFoods.joinToString(separator = "\n") { food ->
            "- ${food.name}: ${food.quantity} (${food.locationKey})"
        }

        return """
            Eres el fallback local de recetas de NeveraChef.
            Se usa solo cuando no hay ninguna tarjeta RAG util para este inventario.
            Crea una unica receta sencilla y segura usando principalmente alimentos del inventario.
            No pongas como disponible un alimento que no aparezca en el inventario.
            Si un ingrediente importante no esta en el inventario, ponlo en missing_ingredients.
            Usa nombres concretos de alimentos, no categorias genericas como pescado si el plato necesita merluza, bacalao, atun, etc.
            Tiempo maximo: ${request.maxMinutes} minutos.
            Devuelve solo JSON valido con esta forma exacta:
            {
              "title": "Nombre de receta",
              "estimated_minutes": 20,
              "servings": 1,
              "difficulty": "EASY",
              "ingredients_used": ["ingrediente disponible"],
              "missing_ingredients": ["ingrediente necesario no disponible"],
              "optional_ingredients": ["ingrediente opcional"],
              "steps": ["paso 1", "paso 2", "paso 3"]
            }

            Inventario:
            $foods
        """.trimIndent()
    }
}
