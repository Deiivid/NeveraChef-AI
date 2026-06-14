package es.neverachefai.feature.recipes.ui

import es.neverachefai.feature.recipes.domain.model.Recipe
import neverachefai.shared.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

internal fun recipeDrawableResource(recipe: Recipe): DrawableResource {
    return when (recipe.imageKey) {
        "recipe_atun_plancha" -> Res.drawable.recipe_atun_plancha
        "recipe_batido_platano_yogur" -> Res.drawable.recipe_batido_platano_yogur
        "recipe_eggs_scrambled" -> Res.drawable.recipe_eggs_scrambled
        "recipe_lentejas_arroz" -> Res.drawable.recipe_lentejas_arroz
        "recipe_pasta_carbonara" -> Res.drawable.recipe_pasta_carbonara
        "recipe_pasta_espinacas" -> Res.drawable.recipe_pasta_espinacas
        "recipe_pasta_gulas_gambas" -> Res.drawable.recipe_pasta_gulas_gambas
        "recipe_tortilla_francesa" -> Res.drawable.recipe_tortilla_francesa
        "recipe_family_arroz" -> Res.drawable.recipe_family_arroz
        "recipe_family_carne" -> Res.drawable.recipe_family_carne
        "recipe_family_ensalada" -> Res.drawable.recipe_family_ensalada
        "recipe_family_fruta" -> Res.drawable.recipe_family_fruta
        "recipe_family_legumbre" -> Res.drawable.recipe_family_legumbre
        "recipe_family_pasta" -> Res.drawable.recipe_family_pasta
        "recipe_family_patata" -> Res.drawable.recipe_family_patata
        "recipe_family_pescado" -> Res.drawable.recipe_family_pescado
        "recipe_family_pollo" -> Res.drawable.recipe_family_pollo
        "recipe_family_sopa" -> Res.drawable.recipe_family_sopa
        "recipe_family_tortilla" -> Res.drawable.recipe_family_tortilla
        "recipe_family_verdura" -> Res.drawable.recipe_family_verdura
        else -> fallbackRecipeDrawableResource(recipe)
    }
}

private fun fallbackRecipeDrawableResource(recipe: Recipe): DrawableResource {
    val joined = (recipe.title + " " + recipe.ingredientsUsed.joinToString()).lowercase()
    return when {
        "atun a la plancha" in joined || "atún a la plancha" in joined -> Res.drawable.recipe_atun_plancha
        "batido" in joined && ("platano" in joined || "plátano" in joined) && "yogur" in joined -> Res.drawable.recipe_batido_platano_yogur
        "huevos revueltos" in joined || "revuelto de huevos" in joined -> Res.drawable.recipe_eggs_scrambled
        "lentejas con arroz" in joined -> Res.drawable.recipe_lentejas_arroz
        "carbonara" in joined -> Res.drawable.recipe_pasta_carbonara
        "pasta con espinacas" in joined -> Res.drawable.recipe_pasta_espinacas
        "gulas" in joined && "gambas" in joined && (
            "pasta" in joined || "macarron" in joined || "macarrón" in joined || "espagueti" in joined ||
                "tallarin" in joined || "tallarín" in joined
            ) -> Res.drawable.recipe_pasta_gulas_gambas
        "tortilla francesa" in joined -> Res.drawable.recipe_tortilla_francesa
        "huevo" in joined || "tortilla" in joined || "revuelto" in joined -> Res.drawable.recipe_family_tortilla
        "fideua" in joined || "fideuá" in joined || "pasta" in joined || "macarron" in joined ||
            "macarrón" in joined -> Res.drawable.recipe_family_pasta
        "sopa" in joined || "caldo" in joined -> Res.drawable.recipe_family_sopa
        "arroz" in joined || "paella" in joined -> Res.drawable.recipe_family_arroz
        "ensalada" in joined -> Res.drawable.recipe_family_ensalada
        "pollo" in joined -> Res.drawable.recipe_family_pollo
        "ternera" in joined || "cerdo" in joined || "carne" in joined -> Res.drawable.recipe_family_carne
        "pescado" in joined || "lubina" in joined || "bacalao" in joined || "atun" in joined ||
            "atún" in joined -> Res.drawable.recipe_family_pescado
        "lenteja" in joined || "garbanzo" in joined || "alubia" in joined -> Res.drawable.recipe_family_legumbre
        "patata" in joined -> Res.drawable.recipe_family_patata
        "fruta" in joined || "fresa" in joined || "manzana" in joined || "naranja" in joined -> Res.drawable.recipe_family_fruta
        else -> Res.drawable.recipe_family_verdura
    }
}
