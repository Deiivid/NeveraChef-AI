package es.neverachefai.feature.recipes.data.rag

internal object IngredientNormalizer {
    private val aliases = mapOf(
        "huevos" to "huevo",
        "egg" to "huevo",
        "eggs" to "huevo",
        "patatas" to "patata",
        "papas" to "patata",
        "tomates" to "tomate",
        "espinacas" to "espinaca",
        "arandanos" to "arandano",
        "fresas" to "fresa",
        "platanos" to "platano",
        "banana" to "platano",
        "bananas" to "platano",
        "avena" to "avena",
        "arroz integral" to "arroz",
        "lentejas" to "lenteja",
        "garbanzos" to "garbanzo",
        "judias blancas" to "alubia",
        "judias pintas" to "alubia",
        "alubias blancas" to "alubia",
        "alubias pintas" to "alubia",
        "frijoles" to "alubia",
        "macarrones" to "pasta",
        "espaguetis" to "pasta",
        "tallarines" to "pasta",
        "fideo" to "fideo",
        "fideos" to "fideo",
        "fideua" to "fideo",
        "fideuá" to "fideo",
        "atun" to "atun",
        "bonito" to "atun",
        "pollo" to "pollo",
        "pechuga de pollo" to "pollo",
        "muslos de pollo" to "pollo",
        "chorizos" to "chorizo",
        "jamon" to "jamon",
        "guisantes" to "guisante",
        "judias verdes" to "judia verde",
        "acelgas" to "acelga",
        "alcachofas" to "alcachofa",
        "esparragos" to "esparrago",
        "habas" to "haba",
        "puerros" to "puerro",
        "coles" to "col",
        "coliflores" to "coliflor",
        "nabos" to "nabo",
        "almendras" to "almendra",
        "harinas" to "harina",
        "azucares" to "azucar",
        "azúcar" to "azucar",
        "limón" to "limon",
        "canela" to "canela",
        "filetes de pescado" to "pescado",
        "filete de pescado" to "pescado",
        "merluza" to "pescado",
        "bacalao" to "pescado",
        "bonito" to "atun",
        "sardinas" to "sardina",
        "boquerones" to "boqueron",
        "anchoas" to "anchoa",
        "calamares" to "calamar",
        "chipirones" to "calamar",
        "sepias" to "sepia",
        "chocos" to "sepia",
        "gambas" to "gamba",
        "langostinos" to "gamba",
        "mejillones" to "mejillon",
        "almejas" to "almeja",
        "filete de ternera" to "ternera",
        "filetes de ternera" to "ternera",
        "carne de ternera" to "ternera",
        "cerdo" to "cerdo",
        "magro de cerdo" to "cerdo",
        "lomo" to "cerdo",
        "costillas" to "costilla",
        "panceta" to "panceta",
        "morcillas" to "morcilla",
        "longanizas" to "longaniza",
        "salchichas" to "salchicha",
        "conejo" to "conejo",
        "lubinas" to "lubina",
        "ajos" to "ajo",
        "cebollas" to "cebolla",
        "zanahorias" to "zanahoria",
        "calabacines" to "calabacin",
        "pimientos" to "pimiento",
        "pimiento rojo" to "pimiento",
        "pimiento verde" to "pimiento",
        "berenjenas" to "berenjena",
        "calabazas" to "calabaza",
        "champiñones" to "champinon",
        "champinones" to "champinon",
        "setas" to "seta",
        "lechugas" to "lechuga",
        "pepinos" to "pepino",
        "quesos" to "queso",
        "leche" to "leche",
        "yogures" to "yogur",
        "limones" to "limon",
        "naranjas" to "naranja",
        "manzanas" to "manzana",
        "peras" to "pera",
    )

    private val ignoredTokens = setOf(
        "de",
        "del",
        "la",
        "el",
        "los",
        "las",
        "un",
        "una",
        "unos",
        "unas",
        "gr",
        "g",
        "kg",
        "uds",
        "unidad",
        "unidades",
    )

    fun canonicalName(raw: String): String {
        val normalized = normalizeText(raw)
        aliases[normalized]?.let { return it }
        return normalized
            .split(" ")
            .filter { it.isNotBlank() && it !in ignoredTokens }
            .joinToString(" ")
            .ifBlank { normalized }
            .let { aliases[it] ?: it.removeTrailingPlural() }
    }

    fun tokensFor(raw: String): Set<String> {
        val canonical = canonicalName(raw)
        val tokens = canonical
            .split(" ")
            .filter { it.isNotBlank() && it !in ignoredTokens }
            .map { aliases[it] ?: it.removeTrailingPlural() }
        return (tokens + canonical).filter { it.isNotBlank() }.toSet()
    }

    fun containsIngredient(raw: String, ingredient: String): Boolean {
        val rawTokens = tokensFor(raw)
        val ingredientTokens = tokensFor(ingredient)
        return ingredientTokens.any { it in rawTokens } || rawTokens.any { it in ingredientTokens }
    }

    private fun normalizeText(raw: String): String {
        return raw
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
            .replace(Regex("[^a-z0-9 ]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }

    private fun String.removeTrailingPlural(): String {
        return when {
            length > 4 && endsWith("es") -> dropLast(2)
            length > 3 && endsWith("s") -> dropLast(1)
            else -> this
        }
    }
}
