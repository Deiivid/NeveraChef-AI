package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.domain.model.RecipeImage
import es.neverachefai.feature.recipes.domain.model.RecipeImageSourceType

internal object LocalRecipeImageCatalog {
    fun imageFor(card: RecipeKnowledgeCard): RecipeImage {
        val assetKey = card.specificImageKey() ?: card.imageKey.ifBlank { card.derivedImageKey() }
        return RecipeImage(
            assetKey = assetKey,
            sourceType = RecipeImageSourceType.LOCAL_FAMILY_FALLBACK,
            sourceUrl = null,
            licenseName = "Generated local asset",
            licenseUrl = null,
            authorName = "NeveraChef AI",
            attributionRequired = false,
            generationPrompt = "Fotografía realista de ${card.title.lowercase()}, receta española casera, luz natural, sin texto ni marcas.",
        )
    }

    private fun RecipeKnowledgeCard.derivedImageKey(): String {
        val joined = recipeImageText()
        return when {
            "huevo" in joined || "tortilla" in joined || "revuelto" in joined -> "recipe_family_tortilla"
            "fideua" in joined || "fideuá" in joined || "pasta" in joined || "macarron" in joined ||
                "macarrón" in joined -> "recipe_family_pasta"
            "sopa" in joined || "caldo" in joined || "fideos" in joined -> "recipe_family_sopa"
            "arroz" in joined || "paella" in joined -> "recipe_family_arroz"
            "ensalada" in joined || "pipirrana" in joined || "empedrat" in joined || "esqueixada" in joined -> "recipe_family_ensalada"
            "pollo" in joined -> "recipe_family_pollo"
            "ternera" in joined || "cerdo" in joined || "conejo" in joined || "costilla" in joined ||
                "lomo" in joined || "carne" in joined -> "recipe_family_carne"
            "pescado" in joined || "merluza" in joined || "lubina" in joined || "bacalao" in joined ||
                "atun" in joined || "atún" in joined || "bonito" in joined || "sardina" in joined ||
                "boqueron" in joined || "boquerón" in joined || "calamar" in joined || "sepia" in joined ||
                "mejillon" in joined || "mejillón" in joined || "gamba" in joined || "almeja" in joined -> "recipe_family_pescado"
            "lenteja" in joined || "garbanzo" in joined || "alubia" in joined -> "recipe_family_legumbre"
            "patata" in joined || "bravas" in joined || "riojana" in joined -> "recipe_family_patata"
            "platano" in joined || "plátano" in joined || "fresa" in joined || "arandano" in joined ||
                "arándano" in joined || "manzana" in joined || "naranja" in joined || "fruta" in joined ||
                "yogur" in joined || "avena" in joined -> "recipe_family_fruta"
            else -> "recipe_family_verdura"
        }
    }

    private fun RecipeKnowledgeCard.specificImageKey(): String? {
        val joined = recipeImageText()
        return when {
            "atun a la plancha" in joined -> "recipe_atun_plancha"
            "batido" in joined && "platano" in joined && "yogur" in joined -> "recipe_batido_platano_yogur"
            "huevos revueltos con pavo" in joined -> "recipe_eggs_scrambled_pavo"
            "huevos revueltos" in joined || "revuelto de huevos" in joined -> "recipe_eggs_scrambled"
            "lentejas con arroz" in joined -> "recipe_lentejas_arroz"
            "carbonara" in joined && ("macarron" in joined || "espagueti" in joined || "pasta" in joined) -> "recipe_pasta_carbonara"
            "pasta con espinacas" in joined -> "recipe_pasta_espinacas"
            "gulas" in joined && "gambas" in joined && (
                "pasta" in joined || "macarron" in joined || "espagueti" in joined || "tallarin" in joined
                ) -> "recipe_pasta_gulas_gambas"
            "tortilla francesa con queso" in joined -> "recipe_tortilla_francesa_queso"
            "tortilla francesa" in joined -> "recipe_tortilla_francesa"
            else -> null
        }
    }

    private fun RecipeKnowledgeCard.recipeImageText(): String {
        return (title + " " + requiredIngredients.joinToString() + " " + optionalIngredients.joinToString())
            .lowercase()
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
    }
}
