package es.neverachefai.feature.recipes.ui

import es.neverachefai.feature.recipes.domain.model.Recipe
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.recipe_arroz_pollo
import neverachefai.shared.generated.resources.recipe_family_arroz
import neverachefai.shared.generated.resources.recipe_family_carne
import neverachefai.shared.generated.resources.recipe_family_ensalada
import neverachefai.shared.generated.resources.recipe_family_fruta
import neverachefai.shared.generated.resources.recipe_family_legumbre
import neverachefai.shared.generated.resources.recipe_family_pasta
import neverachefai.shared.generated.resources.recipe_family_patata
import neverachefai.shared.generated.resources.recipe_family_pescado
import neverachefai.shared.generated.resources.recipe_family_pollo
import neverachefai.shared.generated.resources.recipe_family_sopa
import neverachefai.shared.generated.resources.recipe_family_tortilla
import neverachefai.shared.generated.resources.recipe_family_verdura
import neverachefai.shared.generated.resources.recipe_fideua_pescado
import neverachefai.shared.generated.resources.recipe_lentejas_chorizo
import neverachefai.shared.generated.resources.recipe_pescado_romana
import neverachefai.shared.generated.resources.recipe_revuelto_espinacas
import neverachefai.shared.generated.resources.recipe_tortilla_bacalao
import neverachefai.shared.generated.resources.recipe_tortilla_francesa
import neverachefai.shared.generated.resources.recipe_tortilla_patata_rapida
import org.jetbrains.compose.resources.DrawableResource

internal fun recipeDrawableResource(recipe: Recipe): DrawableResource {
    return when (recipe.imageKey) {
        "recipe_arroz_pollo" -> Res.drawable.recipe_arroz_pollo
        "recipe_lentejas_chorizo" -> Res.drawable.recipe_lentejas_chorizo
        "recipe_pescado_romana" -> Res.drawable.recipe_pescado_romana
        "recipe_revuelto_espinacas" -> Res.drawable.recipe_revuelto_espinacas
        "recipe_tortilla_bacalao" -> Res.drawable.recipe_tortilla_bacalao
        "recipe_tortilla_francesa" -> Res.drawable.recipe_tortilla_francesa
        "recipe_tortilla_patata_rapida" -> Res.drawable.recipe_tortilla_patata_rapida
        "recipe_fideua_pescado" -> Res.drawable.recipe_fideua_pescado
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
        "huevo" in joined || "tortilla" in joined || "revuelto" in joined -> Res.drawable.recipe_family_tortilla
        "fideua" in joined || "fideuá" in joined || "pasta" in joined || "macarron" in joined ||
            "macarrón" in joined -> Res.drawable.recipe_family_pasta
        "arroz" in joined || "paella" in joined -> Res.drawable.recipe_family_arroz
        "sopa" in joined || "caldo" in joined -> Res.drawable.recipe_family_sopa
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
