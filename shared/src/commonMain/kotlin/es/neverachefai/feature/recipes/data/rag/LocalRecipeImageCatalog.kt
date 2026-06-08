package es.neverachefai.feature.recipes.data.rag

import es.neverachefai.feature.recipes.domain.model.RecipeImage
import es.neverachefai.feature.recipes.domain.model.RecipeImageSourceType

internal object LocalRecipeImageCatalog {
    private val imagesByRecipeId = mapOf(
        "spanish-tortilla-francesa" to wikimedia(
            assetKey = "recipe_tortilla_francesa",
            sourceUrl = "https://commons.wikimedia.org/wiki/File:Bread_Omelette_3.jpg",
            licenseName = "CC BY-SA 4.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/4.0",
            authorName = "Gannu03",
            generationPrompt = "Fotografía realista de tortilla francesa casera española en plato blanco, mesa de cocina sencilla, luz natural, sin texto ni marcas.",
        ),
        "spanish-tortilla-patata-rapida" to wikimedia(
            assetKey = "recipe_tortilla_patata_rapida",
            sourceUrl = "https://commons.wikimedia.org/wiki/File:Tortilla_Espa%C3%B1ola.jpg",
            licenseName = "CC BY-SA 4.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/4.0",
            authorName = "Juan Emilio Prades Bel",
            generationPrompt = "Fotografía realista de tortilla de patata española casera, corte visible, plato de cerámica, luz natural, sin texto ni marcas.",
        ),
        "spanish-revuelto-espinacas" to wikimedia(
            assetKey = "recipe_revuelto_espinacas",
            sourceUrl = "https://commons.wikimedia.org/w/index.php?curid=105404413",
            licenseName = "CC BY-SA 4.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/4.0",
            authorName = "ProserpinaCore",
            generationPrompt = "Fotografía realista de revuelto de espinacas con huevo, plato casero español, mesa sencilla, luz natural, sin texto ni marcas.",
        ),
        "spanish-lentejas-chorizo" to wikimedia(
            assetKey = "recipe_lentejas_chorizo",
            sourceUrl = "https://commons.wikimedia.org/w/index.php?curid=45589765",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "Jonathan Pincas",
            generationPrompt = "Fotografía realista de lentejas españolas con chorizo y patata en plato hondo, cocina de casa, luz natural, sin texto ni marcas.",
        ),
        "spanish-arroz-pollo" to wikimedia(
            assetKey = "recipe_arroz_pollo",
            sourceUrl = "https://commons.wikimedia.org/wiki/File:Arroz-con-Pollo.jpg",
            licenseName = "CC BY-SA 2.5",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/2.5",
            authorName = "Kobako",
            generationPrompt = "Fotografía realista de arroz con pollo casero español, plato llano, verduras visibles, luz natural, sin texto ni marcas.",
        ),
        "spanish-pescado-romana" to imagegen(
            assetKey = "recipe_pescado_romana",
            generationPrompt = "Pescado a la romana español: filetes de pescado blanco rebozados, dorados, con limón, plato casero, sin texto ni marcas.",
        ),
        "spanish-tortilla-bacalao" to imagegen(
            assetKey = "recipe_tortilla_bacalao",
            generationPrompt = "Tortilla de bacalao española: tortilla de huevo con lascas de bacalao, pimiento y cebolla, corte visible, sin texto ni marcas.",
        ),
        "spanish-fideua-pescado" to imagegen(
            assetKey = "recipe_fideua_pescado",
            generationPrompt = "Fideuá sencilla de pescado valenciana: fideos cortos en paellera con pescado y marisco, sin macarrones, sin texto ni marcas.",
        ),
    )

    private val familyImages = mapOf(
        "recipe_family_tortilla" to openverse(
            assetKey = "recipe_family_tortilla",
            sourceUrl = "https://www.flickr.com/photos/35022955@N06/7026604587",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "Lablascovegmenu",
        ),
        "recipe_family_legumbre" to openverse(
            assetKey = "recipe_family_legumbre",
            sourceUrl = "https://www.flickr.com/photos/136527749@N07/22484316615",
            licenseName = "CC0 1.0",
            licenseUrl = "https://creativecommons.org/publicdomain/zero/1.0/",
            authorName = "Marieloreto",
            attributionRequired = false,
        ),
        "recipe_family_arroz" to openverse(
            assetKey = "recipe_family_arroz",
            sourceUrl = "https://www.flickr.com/photos/10559879@N00/1159068782",
            licenseName = "CC BY-SA 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/2.0/",
            authorName = "avlxyz",
        ),
        "recipe_family_pasta" to openverse(
            assetKey = "recipe_family_pasta",
            sourceUrl = "https://www.flickr.com/photos/35022955@N06/5184624902",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "Lablascovegmenu",
        ),
        "recipe_family_patata" to openverse(
            assetKey = "recipe_family_patata",
            sourceUrl = "https://www.flickr.com/photos/49215102@N00/435925017",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "goodiesfirst",
        ),
        "recipe_family_pollo" to openverse(
            assetKey = "recipe_family_pollo",
            sourceUrl = "https://commons.wikimedia.org/w/index.php?curid=9944679",
            licenseName = "CC BY-SA 3.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/3.0/",
            authorName = "Tamorlan",
        ),
        "recipe_family_carne" to openverse(
            assetKey = "recipe_family_carne",
            sourceUrl = "https://www.flickr.com/photos/10559879@N00/305439997",
            licenseName = "CC BY-SA 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/2.0/",
            authorName = "avlxyz",
        ),
        "recipe_family_pescado" to openverse(
            assetKey = "recipe_family_pescado",
            sourceUrl = "https://commons.wikimedia.org/w/index.php?curid=14501606",
            licenseName = "CC BY-SA 3.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/3.0/",
            authorName = "Valdavia",
        ),
        "recipe_family_ensalada" to openverse(
            assetKey = "recipe_family_ensalada",
            sourceUrl = "https://www.flickr.com/photos/43243154@N07/7068662605",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "PincasPhoto",
        ),
        "recipe_family_sopa" to openverse(
            assetKey = "recipe_family_sopa",
            sourceUrl = "https://commons.wikimedia.org/w/index.php?curid=29473236",
            licenseName = "CC BY-SA 3.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/3.0/",
            authorName = "Xemenendura",
        ),
        "recipe_family_fruta" to openverse(
            assetKey = "recipe_family_fruta",
            sourceUrl = "https://www.flickr.com/photos/22662305@N04/4406566885",
            licenseName = "CC BY 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by/2.0/",
            authorName = "jlastras",
        ),
        "recipe_family_verdura" to openverse(
            assetKey = "recipe_family_verdura",
            sourceUrl = "https://www.flickr.com/photos/56016589@N02/41793193994",
            licenseName = "CC BY-SA 2.0",
            licenseUrl = "https://creativecommons.org/licenses/by-sa/2.0/",
            authorName = "Emiliano García-Page Sánchez",
        ),
    )

    fun imageFor(card: RecipeKnowledgeCard): RecipeImage {
        return imagesByRecipeId[card.id] ?: familyFallback(card)
    }

    private fun wikimedia(
        assetKey: String,
        sourceUrl: String,
        licenseName: String,
        licenseUrl: String,
        authorName: String,
        generationPrompt: String,
    ): RecipeImage {
        return RecipeImage(
            assetKey = assetKey,
            sourceType = RecipeImageSourceType.WIKIMEDIA_COMMONS,
            sourceUrl = sourceUrl,
            licenseName = licenseName,
            licenseUrl = licenseUrl,
            authorName = authorName,
            attributionRequired = true,
            generationPrompt = generationPrompt,
        )
    }

    private fun openverse(
        assetKey: String,
        sourceUrl: String,
        licenseName: String,
        licenseUrl: String,
        authorName: String,
        attributionRequired: Boolean = true,
    ): RecipeImage {
        return RecipeImage(
            assetKey = assetKey,
            sourceType = RecipeImageSourceType.OPENVERSE,
            sourceUrl = sourceUrl,
            licenseName = licenseName,
            licenseUrl = licenseUrl,
            authorName = authorName,
            attributionRequired = attributionRequired,
            generationPrompt = null,
        )
    }

    private fun imagegen(
        assetKey: String,
        generationPrompt: String,
    ): RecipeImage {
        return RecipeImage(
            assetKey = assetKey,
            sourceType = RecipeImageSourceType.IMAGEGEN_PENDING,
            sourceUrl = null,
            licenseName = "Generated local asset",
            licenseUrl = null,
            authorName = "NeveraChef AI",
            attributionRequired = false,
            generationPrompt = generationPrompt,
        )
    }

    private fun familyFallback(card: RecipeKnowledgeCard): RecipeImage {
        val assetKey = card.imageKey.ifBlank { card.derivedImageKey() }
        val familyImage = familyImages[assetKey]
        return familyImage?.copy(
            generationPrompt = "Fotografía realista de ${card.title.lowercase()}, receta española casera, luz natural, sin texto ni marcas.",
        ) ?: RecipeImage(
            assetKey = assetKey,
            sourceType = RecipeImageSourceType.LOCAL_FAMILY_FALLBACK,
            sourceUrl = null,
            licenseName = "Local category illustration",
            licenseUrl = null,
            authorName = "NeveraChef AI",
            attributionRequired = false,
            generationPrompt = "Fotografía realista de ${card.title.lowercase()}, receta española casera, luz natural, sin texto ni marcas.",
        )
    }

    private fun RecipeKnowledgeCard.derivedImageKey(): String {
        val joined = (title + " " + requiredIngredients.joinToString() + " " + optionalIngredients.joinToString()).lowercase()
        return when {
            "huevo" in joined || "tortilla" in joined || "revuelto" in joined -> "recipe_family_tortilla"
            "fideua" in joined || "fideuá" in joined || "pasta" in joined || "macarron" in joined ||
                "macarrón" in joined -> "recipe_family_pasta"
            "arroz" in joined || "paella" in joined -> "recipe_family_arroz"
            "sopa" in joined || "caldo" in joined || "fideos" in joined -> "recipe_family_sopa"
            "ensalada" in joined || "pipirrana" in joined || "empedrat" in joined || "esqueixada" in joined -> "recipe_family_ensalada"
            "pollo" in joined -> "recipe_family_pollo"
            "ternera" in joined || "cerdo" in joined || "conejo" in joined || "costilla" in joined ||
                "lomo" in joined || "carne" in joined -> "recipe_family_carne"
            "pescado" in joined || "lubina" in joined || "bacalao" in joined || "atun" in joined ||
                "atún" in joined || "bonito" in joined || "sardina" in joined || "boqueron" in joined ||
                "boquerón" in joined || "calamar" in joined || "sepia" in joined || "mejillon" in joined ||
                "mejillón" in joined || "gamba" in joined || "almeja" in joined -> "recipe_family_pescado"
            "lenteja" in joined || "garbanzo" in joined || "alubia" in joined -> "recipe_family_legumbre"
            "patata" in joined || "bravas" in joined || "riojana" in joined -> "recipe_family_patata"
            "platano" in joined || "fresa" in joined || "arandano" in joined || "manzana" in joined ||
                "naranja" in joined || "fruta" in joined -> "recipe_family_fruta"
            else -> "recipe_family_verdura"
        }
    }
}
