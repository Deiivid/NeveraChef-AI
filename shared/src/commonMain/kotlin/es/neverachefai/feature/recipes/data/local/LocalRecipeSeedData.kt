package es.neverachefai.feature.recipes.data.local

import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.IngredientUnit
import es.neverachefai.feature.recipes.domain.model.MealType
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeCategory
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationMode
import es.neverachefai.feature.recipes.domain.model.RecipeIngredient
import es.neverachefai.feature.recipes.domain.model.RecipeSource
import es.neverachefai.feature.recipes.domain.model.RecipeStep
import es.neverachefai.feature.recipes.domain.model.RecipeTag

internal object LocalRecipeSeedData {
    private val categoryEntries = listOf(
        entries(
            RecipeCategory.BREAKFAST,
            "Tostada con tomate",
            "Tostada con aceite",
            "Tostada con jamón",
            "Tostada con aguacate",
            "Tostada con anchoas",
            "Tostada con queso fresco",
            "Tostada con pavo",
            "Tostada con pechuga de pollo",
            "Tostada con mermelada",
            "Yogur natural con fruta",
            "Yogur con avena",
            "Yogur con muesli",
            "Yogur con frutos secos",
            "Avena con plátano",
            "Avena con manzana",
            "Macedonia",
            "Tortilla francesa",
            "Tortilla francesa con queso",
            "Huevos revueltos",
            "Huevos revueltos con pavo",
            "Pan con aceite y fruta",
            "Queso fresco con tomate",
            "Batido de plátano y yogur",
            "Porridge básico",
            "Fruta con yogur",
        ),
        entries(
            RecipeCategory.SALAD,
            "Ensalada mixta",
            "Ensalada de pasta",
            "Ensalada de arroz",
            "Ensalada campera",
            "Ensalada César",
            "Ensalada de garbanzos",
            "Ensalada de lentejas",
            "Ensalada de atún",
            "Ensalada de tomate",
            "Ensalada de pepino",
            "Ensalada de tomate, pepino y aguacate",
            "Ensalada de pollo",
            "Ensalada de huevo cocido",
            "Ensalada de queso fresco",
            "Ensalada de salmón",
            "Ensalada griega",
            "Ensalada de pasta con atún",
            "Ensalada de arroz con gambas",
            "Ensalada templada de verduras",
            "Ensalada de tomate y mozzarella",
        ),
        entries(
            RecipeCategory.PASTA,
            "Espaguetis boloñesa",
            "Espaguetis carbonara",
            "Espaguetis con tomate",
            "Espaguetis con atún",
            "Espaguetis con gambas",
            "Espaguetis con gulas y gambas",
            "Espaguetis al ajillo",
            "Espaguetis con pollo",
            "Espaguetis con verduras",
            "Espaguetis con salmón",
            "Macarrones con tomate",
            "Macarrones con atún y tomate",
            "Macarrones boloñesa",
            "Macarrones carbonara",
            "Macarrones con gulas y gambas",
            "Macarrones con chorizo",
            "Macarrones con pollo",
            "Macarrones con verduras",
            "Tallarines con gambas",
            "Tallarines con pollo",
            "Pasta al pesto",
            "Pasta con nata y champiñones",
            "Pasta con espinacas",
            "Pasta con queso",
            "Pasta gratinada",
            "Lasaña boloñesa",
            "Lasaña de verduras",
            "Canelones de carne",
            "Canelones de atún",
            "Fideos con pollo",
        ),
        entries(
            RecipeCategory.RICE,
            "Arroz con tomate",
            "Arroz a la cubana",
            "Arroz tres delicias",
            "Arroz con pollo",
            "Arroz con verduras",
            "Arroz con gambas",
            "Arroz con atún",
            "Arroz con ternera",
            "Arroz con carne picada",
            "Arroz con salchichas",
            "Arroz con conejo",
            "Arroz con costillas",
            "Arroz caldoso de pollo",
            "Arroz caldoso de marisco",
            "Arroz meloso con verduras",
            "Paella mixta",
            "Paella de marisco",
            "Paella de pollo",
            "Paella de verduras",
            "Fideuá",
            "Risotto sencillo de champiñones",
            "Arroz al horno sencillo",
        ),
        entries(
            RecipeCategory.CHICKEN,
            "Pollo al horno con patatas",
            "Pollo al horno con cebolla",
            "Pollo al ajillo",
            "Pollo al limón",
            "Pollo a la plancha",
            "Pechuga con cebolla",
            "Pechuga con pimientos verdes",
            "Pechuga con verduras",
            "Pechuga empanada",
            "Nuggets caseros",
            "Alitas al horno",
            "Alitas fritas",
            "Muslos de pollo al horno",
            "Pollo con arroz",
            "Pollo con tomate",
            "Pollo con champiñones",
            "Pollo al curry suave",
            "Pollo guisado con patatas",
            "Ensalada de pollo",
            "Hamburguesa de pollo",
        ),
        entries(
            RecipeCategory.PORK,
            "Lomo a la plancha",
            "Lomo con pimientos",
            "Lomo al ajillo",
            "Libritos de lomo",
            "Costillas al horno",
            "Costillas guisadas",
            "Solomillo de cerdo",
            "Solomillo con champiñones",
            "Salchichas con cebolla y vino",
            "Longaniza con cebolla",
            "Chorizo al vino",
            "Magra con tomate",
            "Chuletas de cerdo",
            "Cinta de lomo al horno",
            "Bocaditos de lomo empanado",
            "Arroz con costillas",
            "Pasta con chorizo",
            "Huevos rotos con chorizo",
        ),
        entries(
            RecipeCategory.BEEF,
            "Ternera a la plancha",
            "Ternera al ajillo",
            "Ternera con pimientos",
            "Ternera con cebolla",
            "Ternera guisada",
            "Estofado de ternera",
            "Filetes empanados",
            "Albóndigas con tomate",
            "Hamburguesa casera",
            "Carne picada con tomate",
            "Arroz con ternera",
            "Macarrones boloñesa",
            "Lasaña boloñesa",
            "Ternera con champiñones",
            "Ternera salteada con verduras",
        ),
        entries(
            RecipeCategory.FISH,
            "Merluza rebozada",
            "Merluza al horno",
            "Merluza a la plancha",
            "Merluza en salsa verde",
            "Lubina al horno con patatas",
            "Lubina a la plancha",
            "Dorada al horno con patatas",
            "Dorada a la plancha con limón",
            "Salmón a la plancha",
            "Salmón al horno",
            "Bacalao con tomate",
            "Bacalao al horno",
            "Atún a la plancha",
            "Bonito con tomate",
            "Sardinas al horno",
            "Sepia a la plancha",
            "Calamares a la romana",
            "Gambas al ajillo",
            "Gulas con gambas",
            "Arroz con gambas",
            "Pasta con gambas",
            "Ensalada de atún",
        ),
        entries(
            RecipeCategory.LEGUMES,
            "Lentejas con chorizo",
            "Lentejas con verduras",
            "Lentejas con arroz",
            "Lentejas con patata y chorizo",
            "Garbanzos con espinacas",
            "Garbanzos con tomate",
            "Garbanzos con chorizo",
            "Potaje de garbanzos",
            "Garbanzos salteados",
            "Ensalada de garbanzos",
            "Alubias con verduras",
            "Alubias con chorizo",
            "Judías blancas guisadas",
            "Fabada sencilla",
            "Hummus casero",
            "Lentejas rápidas",
        ),
        entries(
            RecipeCategory.QUICK_DINNER,
            "Tortilla francesa",
            "Revuelto de huevos",
            "Ensalada de atún",
            "Ensalada de pollo",
            "Sándwich mixto",
            "Tostada de aguacate y huevo",
            "Quesadillas",
            "Hamburguesa casera",
            "Pizza casera rápida",
            "Crema de verduras",
            "Sopa de fideos",
            "Pechuga a la plancha",
            "Merluza a la plancha",
            "Dorada a la plancha",
            "Arroz con tomate",
            "Pasta con atún",
            "Verduras salteadas",
            "Gulas con gambas",
            "Huevos con tomate",
            "Yogur con fruta y frutos secos",
        ),
        entries(
            RecipeCategory.KIDS,
            "Macarrones con tomate",
            "Macarrones con atún",
            "Arroz con tomate",
            "Arroz a la cubana",
            "Nuggets caseros",
            "Hamburguesa casera",
            "Albóndigas con tomate",
            "Croquetas",
            "Tortilla francesa",
            "Pizza casera",
            "Pechuga empanada",
            "Lasaña boloñesa",
            "Canelones",
            "Salchichas con arroz",
            "Sopa de fideos",
            "Puré de verduras",
            "Pescado rebozado",
            "Patatas con huevo",
        ),
    ).flatten()

    val categoryEntryCount: Int get() = categoryEntries.size

    val recipes: List<Recipe> = categoryEntries
        .groupBy { it.title }
        .map { (title, entries) -> recipe(title, entries.map { it.category }.distinct()) }
        .sortedWith(compareBy<Recipe> { it.category.ordinal }.thenBy { it.title })

    private fun entries(category: RecipeCategory, vararg titles: String): List<CatalogEntry> {
        return titles.map { CatalogEntry(category = category, title = it) }
    }

    private fun recipe(title: String, categories: List<RecipeCategory>): Recipe {
        val primary = categories.first()
        val normalized = title.normalized()
        val ingredients = ingredientsFor(title, primary)
        val prep = prepMinutes(primary, normalized)
        val cook = cookMinutes(primary, normalized)
        val baseServings = baseServings(primary)
        return Recipe(
            id = "seed-${title.slug()}",
            title = title,
            imageKey = imageKeyFor(normalized, primary),
            mealType = mealTypesFor(primary).first(),
            estimatedMinutes = prep + cook,
            difficulty = difficultyFor(primary, normalized),
            servings = baseServings,
            matchScore = 0,
            healthScore = healthScoreFor(primary, normalized),
            ingredientsUsed = emptyList(),
            missingIngredients = emptyList(),
            optionalIngredients = ingredients.filter { it.optional }.map { it.name },
            ingredientAmounts = ingredients.associate { it.name to it.displayAmount() },
            steps = stepsFor(title, primary, ingredients, cook),
            sourceRecipeIds = listOf("recipes-md:${categories.joinToString("+")}:${title.slug()}"),
            generationMode = RecipeGenerationMode.LOCAL_SEED,
            groundingSummary = "Receta local estructurada desde recipes.md",
            name = title,
            description = descriptionFor(title, primary),
            category = primary,
            categories = categories,
            mealTypes = mealTypesFor(primary),
            baseServings = baseServings,
            preparationTimeMinutes = prep,
            cookingTimeMinutes = cook,
            totalTimeMinutes = prep + cook,
            tags = tagsFor(categories, normalized),
            ingredients = ingredients,
            selectedServings = baseServings,
            source = RecipeSource.Internal,
        )
    }

    private fun ingredientsFor(title: String, category: RecipeCategory): List<RecipeIngredient> {
        val t = title.normalized()
        val result = mutableListOf<RecipeIngredient>()
        fun add(
            foodId: String,
            name: String,
            quantity: Double,
            unit: IngredientUnit,
            optional: Boolean = false,
            main: Boolean = false,
        ) {
            result += RecipeIngredient(foodId, name, quantity, unit, optional, main)
        }

        when {
            "tostada" in t || t.startsWith("pan con") -> add("pan", "Pan", 2.0, IngredientUnit.UNIT, main = true)
            "sandwich" in t || "sándwich" in t -> add("pan molde", "Pan de molde", 2.0, IngredientUnit.UNIT, main = true)
            "quesadilla" in t -> add("tortilla trigo", "Tortilla de trigo", 2.0, IngredientUnit.UNIT, main = true)
            "pizza" in t -> add("masa pizza", "Masa de pizza", 1.0, IngredientUnit.UNIT, main = true)
            "croqueta" in t -> add("croquetas", "Croquetas", 8.0, IngredientUnit.UNIT, main = true)
        }
        if ("yogur" in t || "porridge" in t || "batido" in t) add("yogur", "Yogur natural", 1.0, IngredientUnit.UNIT, main = true)
        if ("avena" in t || "porridge" in t) add("avena", "Avena", 40.0, IngredientUnit.GRAM, main = true)
        if ("muesli" in t) add("muesli", "Muesli", 35.0, IngredientUnit.GRAM, main = true)
        if ("frutos secos" in t) add("frutos secos", "Frutos secos", 25.0, IngredientUnit.GRAM, main = true)
        if ("fruta" in t || "macedonia" in t) add("fruta", "Fruta", 2.0, IngredientUnit.UNIT, main = true)
        if ("platano" in t) add("platano", "Plátano", 1.0, IngredientUnit.UNIT, main = true)
        if ("manzana" in t) add("manzana", "Manzana", 1.0, IngredientUnit.UNIT, main = true)
        if ("mermelada" in t) add("mermelada", "Mermelada", 1.0, IngredientUnit.TABLESPOON, main = true)
        if ("aguacate" in t) add("aguacate", "Aguacate", 0.5, IngredientUnit.UNIT, main = true)

        if ("huevo" in t || "tortilla" in t || "revuelto" in t || "cubana" in t || "carbonara" in t) {
            add("huevo", "Huevo", if ("cubana" in t || "carbonara" in t) 2.0 else 3.0, IngredientUnit.UNIT, main = true)
        }
        if ("patata" in t || "campera" in t || "horno sencillo" in t) add("patata", "Patata", 500.0, IngredientUnit.GRAM, main = "patata" in t)
        if ("tomate" in t || "boloñesa" in t || "bolonesa" in t || "pizza" in t || "campera" in t) {
            add(if ("frito" in t || "pasta" in t || "macarron" in t || "espagueti" in t) "tomate frito" else "tomate", if ("frito" in t || "pasta" in t || "macarron" in t || "espagueti" in t) "Tomate frito" else "Tomate", if ("frito" in t || "pasta" in t || "macarron" in t || "espagueti" in t) 250.0 else 2.0, if ("frito" in t || "pasta" in t || "macarron" in t || "espagueti" in t) IngredientUnit.GRAM else IngredientUnit.UNIT, main = "tomate" in t)
        }
        if ("pepino" in t) add("pepino", "Pepino", 1.0, IngredientUnit.UNIT, main = true)
        if ("pimiento" in t || "pimientos" in t || "paella" in t) add("pimiento", "Pimiento", 1.0, IngredientUnit.UNIT, main = "pimiento" in t)
        if ("cebolla" in t || category in setOf(RecipeCategory.RICE, RecipeCategory.LEGUMES, RecipeCategory.PORK, RecipeCategory.BEEF)) add("cebolla", "Cebolla", 1.0, IngredientUnit.UNIT)
        if ("verdura" in t || "verduras" in t || "pisto" in t || "crema" in t || "pure" in t || "puré" in t) {
            add("zanahoria", "Zanahoria", 2.0, IngredientUnit.UNIT, main = "verdura" in t || "crema" in t || "pure" in t)
            add("calabacin", "Calabacín", 1.0, IngredientUnit.UNIT)
        }
        if ("espinaca" in t) add("espinaca", "Espinacas", 150.0, IngredientUnit.GRAM, main = true)
        if ("champiñon" in t || "champinon" in t) add("champiñon", "Champiñones", 200.0, IngredientUnit.GRAM, main = true)
        if ("ajo" in t || "ajillo" in t || category in setOf(RecipeCategory.CHICKEN, RecipeCategory.FISH)) add("ajo", "Ajo", 2.0, IngredientUnit.UNIT)

        if ("ensalada" in t || category == RecipeCategory.SALAD) {
            add("lechuga", "Lechuga", 1.0, IngredientUnit.UNIT, main = "mixta" in t || category == RecipeCategory.SALAD)
            if ("cesar" in t || "césar" in t) add("salsa cesar", "Salsa César", 2.0, IngredientUnit.TABLESPOON)
            if ("griega" in t || "mozzarella" in t || "queso fresco" in t) add("queso", if ("mozzarella" in t) "Mozzarella" else "Queso fresco", 120.0, IngredientUnit.GRAM, main = "queso" in t)
        }

        val pastaName = when {
            "espagueti" in t -> "Espaguetis"
            "macarron" in t -> "Macarrones"
            "tallarin" in t -> "Tallarines"
            "lasana" in t || "lasaña" in t -> "Láminas de lasaña"
            "canelon" in t -> "Canelones"
            "fideo" in t || "fideua" in t || "fideuá" in t -> "Fideos"
            "pasta" in t -> "Pasta"
            else -> null
        }
        if (pastaName != null) add(pastaName.normalized(), pastaName, 400.0, IngredientUnit.GRAM, main = true)
        if ("arroz" in t || "paella" in t || "risotto" in t || "cubana" in t) add("arroz", "Arroz", 320.0, IngredientUnit.GRAM, main = true)

        if ("pollo" in t || "pechuga" in t || "alita" in t || "muslo" in t || "nugget" in t) {
            add("pollo", if ("pechuga" in t) "Pechuga de pollo" else "Pollo", 500.0, IngredientUnit.GRAM, main = true)
        }
        if ("ternera" in t || "estofado" in t || "hamburguesa" in t || "albóndiga" in t || "albondiga" in t || "carne picada" in t || "boloñesa" in t || "bolonesa" in t) {
            add("ternera", if ("carne picada" in t || "boloñesa" in t || "bolonesa" in t || "hamburguesa" in t || "albondiga" in t || "albóndiga" in t) "Carne picada de ternera" else "Ternera", 450.0, IngredientUnit.GRAM, main = true)
        }
        if ("lomo" in t || "cerdo" in t || "costilla" in t || "salchicha" in t || "longaniza" in t || "chorizo" in t || "chuleta" in t || "magra" in t || "solomillo" in t) {
            val name = when {
                "costilla" in t -> "Costillas de cerdo"
                "salchicha" in t -> "Salchichas"
                "longaniza" in t -> "Longaniza"
                "chorizo" in t -> "Chorizo"
                "solomillo" in t -> "Solomillo de cerdo"
                "chuleta" in t -> "Chuletas de cerdo"
                "lomo" in t -> "Lomo de cerdo"
                else -> "Cerdo"
            }
            add(name.normalized(), name, if ("chorizo" in t) 180.0 else 500.0, IngredientUnit.GRAM, main = true)
        }

        val fishName = when {
            "merluza" in t -> "Merluza"
            "lubina" in t -> "Lubina"
            "dorada" in t -> "Dorada"
            "salmon" in t || "salmón" in t -> "Salmón"
            "bacalao" in t -> "Bacalao"
            "atun" in t || "atún" in t -> if ("ensalada" in t || "pasta" in t || "macarron" in t || "espagueti" in t || "arroz" in t) "Atún en conserva" else "Atún"
            "bonito" in t -> "Bonito"
            "sardina" in t -> "Sardinas"
            "sepia" in t -> "Sepia"
            "calamar" in t -> "Calamares"
            "gamba" in t -> "Gambas"
            "gula" in t -> "Gulas"
            "pescado" in t -> "Pescado blanco"
            else -> null
        }
        if (fishName != null) add(fishName.normalized(), fishName, if ("conserva" in fishName.normalized()) 160.0 else 400.0, IngredientUnit.GRAM, main = true)
        if ("rebozad" in t || "empanad" in t || "romana" in t || "nugget" in t || "librito" in t || "bocadito" in t) {
            add("harina", "Harina", 80.0, IngredientUnit.GRAM, main = "rebozad" in t || "romana" in t)
            add("huevo", "Huevo", 2.0, IngredientUnit.UNIT, main = true)
            add("pan rallado", "Pan rallado", 80.0, IngredientUnit.GRAM, optional = true)
        }

        if ("garbanzo" in t || "hummus" in t || "potaje" in t) add("garbanzo", "Garbanzos cocidos", 400.0, IngredientUnit.GRAM, main = true)
        if ("lenteja" in t) add("lenteja", "Lentejas", 320.0, IngredientUnit.GRAM, main = true)
        if ("alubia" in t || "judia" in t || "judía" in t || "fabada" in t) add("alubia", "Alubias cocidas", 400.0, IngredientUnit.GRAM, main = true)
        if (category == RecipeCategory.LEGUMES && result.none { it.foodId == "tomate" || it.foodId == "tomate frito" }) {
            add("tomate", "Tomate", 1.0, IngredientUnit.UNIT)
        }
        if ("caldoso" in t || "sopa" in t || "crema" in t || "pure" in t || "puré" in t) add("caldo", "Caldo", 800.0, IngredientUnit.MILLILITER, main = true)
        if ("nata" in t) add("nata", "Nata", 200.0, IngredientUnit.MILLILITER, main = true)
        if ("queso" in t || "gratinada" in t || "librito" in t || "quesadilla" in t || "pizza" in t) add("queso", "Queso", 120.0, IngredientUnit.GRAM, main = "queso" in t)
        if ("jamon" in t || "jamón" in t || "mixto" in t || "librito" in t) add("jamon", "Jamón", 100.0, IngredientUnit.GRAM, main = "jamon" in t || "jamón" in t)
        if ("pavo" in t) add("pavo", "Pavo", 100.0, IngredientUnit.GRAM, main = true)
        if ("anchoa" in t) add("anchoa", "Anchoas", 40.0, IngredientUnit.GRAM, main = true)
        if ("limon" in t || "limón" in t) add("limon", "Limón", 1.0, IngredientUnit.UNIT)
        if ("vino" in t) add("vino blanco", "Vino blanco", 120.0, IngredientUnit.MILLILITER)
        if ("curry" in t) add("curry", "Curry suave", 1.0, IngredientUnit.TEASPOON)
        if ("pesto" in t) add("pesto", "Pesto", 3.0, IngredientUnit.TABLESPOON, main = true)

        if (result.none { it.mainIngredient }) {
            when (category) {
                RecipeCategory.BREAKFAST -> add("pan", "Pan", 2.0, IngredientUnit.UNIT, main = true)
                RecipeCategory.SALAD -> add("lechuga", "Lechuga", 1.0, IngredientUnit.UNIT, main = true)
                RecipeCategory.PASTA -> add("pasta", "Pasta", 400.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.RICE -> add("arroz", "Arroz", 320.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.CHICKEN -> add("pollo", "Pollo", 500.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.PORK -> add("cerdo", "Cerdo", 500.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.BEEF -> add("ternera", "Ternera", 450.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.FISH -> add("pescado blanco", "Pescado blanco", 400.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.LEGUMES -> add("legumbre", "Legumbre cocida", 400.0, IngredientUnit.GRAM, main = true)
                RecipeCategory.QUICK_DINNER -> add("huevo", "Huevo", 2.0, IngredientUnit.UNIT, main = true)
                RecipeCategory.KIDS -> add("pasta", "Pasta", 300.0, IngredientUnit.GRAM, main = true)
                else -> add("verdura", "Verdura", 300.0, IngredientUnit.GRAM, main = true)
            }
        }
        if (category !in setOf(RecipeCategory.BREAKFAST) && result.none { it.foodId == "aceite oliva" }) {
            add("aceite oliva", "Aceite de oliva", 2.0, IngredientUnit.TABLESPOON)
        }
        if (category !in setOf(RecipeCategory.BREAKFAST) && result.none { it.foodId == "sal" }) {
            add("sal", "Sal", 0.0, IngredientUnit.TO_TASTE)
        }
        return result.distinctBy { it.foodId }.take(9)
    }

    private fun stepsFor(
        title: String,
        category: RecipeCategory,
        ingredients: List<RecipeIngredient>,
        cook: Int,
    ): List<RecipeStep> {
        val main = ingredients.firstOrNull { it.mainIngredient }?.name ?: title
        return when (category) {
            RecipeCategory.BREAKFAST -> listOf(
                step(1, "Prepara la base", "Ten listos ${ingredients.take(2).joinToString(" y ") { it.name.lowercase() }}."),
                step(2, "Monta el plato", "Combina los ingredientes sin apelmazar y ajusta cantidad al gusto."),
                step(3, "Sirve", "Sirve al momento para mantener textura."),
            )
            RecipeCategory.SALAD -> listOf(
                step(1, "Lava y corta", "Lava las verduras y corta los ingredientes en piezas de bocado."),
                step(2, "Añade la proteína o base", "Incorpora $main y el resto de ingredientes principales."),
                step(3, "Aliña", "Añade aceite, sal y el aliño que corresponda justo antes de servir."),
            )
            RecipeCategory.PASTA -> listOf(
                step(1, "Cuece la pasta", "Hierve la pasta en agua con sal hasta que quede al dente."),
                step(2, "Prepara el acompañamiento", "Cocina los ingredientes de la salsa o salteado a fuego medio."),
                step(3, "Mezcla", "Escurre la pasta y mézclala con la salsa caliente."),
                step(4, "Sirve", "Sirve recién hecha y ajusta sal si hace falta."),
            )
            RecipeCategory.RICE -> listOf(
                step(1, "Haz el sofrito", "Cocina las verduras o base aromática con aceite durante unos minutos."),
                step(2, "Añade el arroz", "Incorpora el arroz y remueve para nacararlo."),
                step(3, "Cuece", "Añade caldo o agua y cocina ${cook.coerceAtLeast(15)} minutos según el tipo de arroz."),
                step(4, "Reposa", "Deja reposar 5 minutos antes de servir."),
            )
            RecipeCategory.LEGUMES -> listOf(
                step(1, "Prepara el sofrito", "Cocina cebolla, ajo o verduras con aceite a fuego medio."),
                step(2, "Añade la legumbre", "Incorpora $main y remueve para mezclar sabores."),
                step(3, "Cuece suave", "Añade agua o caldo si hace falta y cocina hasta que quede meloso."),
                step(4, "Ajusta", "Prueba de sal y deja reposar unos minutos."),
            )
            else -> listOf(
                step(1, "Prepara ingredientes", "Corta y sazona $main antes de cocinar."),
                step(2, "Cocina", "Cocina a fuego medio controlando que no se seque ni se queme."),
                step(3, "Completa el plato", "Añade guarnición, salsa o verduras de la receta."),
                step(4, "Sirve", "Comprueba punto de sal y sirve caliente."),
            )
        }
    }

    private fun descriptionFor(title: String, category: RecipeCategory): String {
        return when (category) {
            RecipeCategory.BREAKFAST -> "$title para desayuno o merienda con ingredientes habituales."
            RecipeCategory.SALAD -> "$title fresca, sencilla y pensada para aprovechar inventario."
            RecipeCategory.PASTA -> "$title familiar con base de pasta y preparación directa."
            RecipeCategory.RICE -> "$title con base de arroz y cocción controlada."
            RecipeCategory.LEGUMES -> "$title como plato de cuchara o legumbre casera."
            else -> "$title estructurada con ingredientes concretos y pasos domésticos."
        }
    }

    private fun prepMinutes(category: RecipeCategory, title: String): Int {
        return when {
            category == RecipeCategory.BREAKFAST -> 5
            category == RecipeCategory.SALAD -> 12
            category == RecipeCategory.PASTA -> 5
            "horno" in title || "guisad" in title || "estofado" in title -> 12
            else -> 8
        }
    }

    private fun cookMinutes(category: RecipeCategory, title: String): Int {
        return when {
            category == RecipeCategory.BREAKFAST && ("tostada" in title || "pan con" in title) -> 3
            category == RecipeCategory.BREAKFAST -> 0
            category == RecipeCategory.SALAD -> 0
            "plancha" in title -> 8
            "rebozad" in title || "empanad" in title || "romana" in title -> 10
            "horno" in title -> 35
            "guisad" in title || "estofado" in title || "fabada" in title -> 45
            category == RecipeCategory.LEGUMES -> 22
            category == RecipeCategory.RICE -> 25
            category == RecipeCategory.PASTA -> 20
            category == RecipeCategory.QUICK_DINNER -> 12
            else -> 20
        }
    }

    private fun baseServings(category: RecipeCategory): Int {
        return when (category) {
            RecipeCategory.BREAKFAST -> 1
            RecipeCategory.QUICK_DINNER -> 2
            RecipeCategory.KIDS -> 2
            else -> 4
        }
    }

    private fun difficultyFor(category: RecipeCategory, title: String): Difficulty {
        return when {
            "paella" in title || "lasana" in title || "lasaña" in title || "canelon" in title || "estofado" in title || "fabada" in title -> Difficulty.HARD
            "horno" in title || "guisad" in title || category == RecipeCategory.RICE -> Difficulty.MEDIUM
            else -> Difficulty.EASY
        }
    }

    private fun mealTypesFor(category: RecipeCategory): List<MealType> {
        return when (category) {
            RecipeCategory.BREAKFAST -> listOf(MealType.BREAKFAST, MealType.SNACK)
            RecipeCategory.QUICK_DINNER -> listOf(MealType.DINNER)
            else -> listOf(MealType.LUNCH, MealType.DINNER)
        }
    }

    private fun tagsFor(categories: List<RecipeCategory>, title: String): List<RecipeTag> {
        val t = title.normalized()
        return buildSet {
            if (categories.contains(RecipeCategory.BREAKFAST)) add(RecipeTag.BREAKFAST)
            if (categories.contains(RecipeCategory.KIDS)) add(RecipeTag.KIDS)
            if (categories.contains(RecipeCategory.QUICK_DINNER) || "plancha" in t || "tostada" in t) add(RecipeTag.QUICK)
            if (categories.contains(RecipeCategory.LEGUMES)) add(RecipeTag.LEGUMES)
            if (categories.contains(RecipeCategory.PASTA)) add(RecipeTag.PASTA)
            if (categories.contains(RecipeCategory.RICE)) add(RecipeTag.RICE)
            if (categories.contains(RecipeCategory.FISH)) add(RecipeTag.FISH)
            if (categories.any { it in setOf(RecipeCategory.CHICKEN, RecipeCategory.PORK, RecipeCategory.BEEF) }) add(RecipeTag.MEAT)
            if (categories.any { it in setOf(RecipeCategory.SALAD, RecipeCategory.LEGUMES) } || "verdura" in t) add(RecipeTag.HEALTHY)
            if ("guis" in t || "lenteja" in t || "sopa" in t) add(RecipeTag.WINTER)
            if ("ensalada" in t || "gazpacho" in t) add(RecipeTag.SUMMER)
            add(RecipeTag.MEDITERRANEAN)
            add(RecipeTag.FAMILY)
        }.toList()
    }

    private fun healthScoreFor(category: RecipeCategory, title: String): Int {
        return when {
            category in setOf(RecipeCategory.SALAD, RecipeCategory.LEGUMES, RecipeCategory.FISH) -> 84
            "frit" in title || "rebozad" in title || "chorizo" in title -> 68
            category == RecipeCategory.BREAKFAST -> 78
            else -> 74
        }
    }

    private fun imageKeyFor(title: String, category: RecipeCategory): String {
        return when {
            "atun a la plancha" in title -> "recipe_atun_plancha"
            "batido" in title && "platano" in title && "yogur" in title -> "recipe_batido_platano_yogur"
            "huevos revueltos" in title || "revuelto de huevos" in title -> "recipe_eggs_scrambled"
            "lentejas con arroz" in title -> "recipe_lentejas_arroz"
            "carbonara" in title && ("macarron" in title || "espagueti" in title || "pasta" in title) -> "recipe_pasta_carbonara"
            "pasta con espinacas" in title -> "recipe_pasta_espinacas"
            "gulas" in title && "gambas" in title && (
                "pasta" in title || "macarron" in title || "espagueti" in title || "tallarin" in title
                ) -> "recipe_pasta_gulas_gambas"
            "tortilla francesa" in title -> "recipe_tortilla_francesa"
            "huevo" in title || "tortilla" in title || "revuelto" in title -> "recipe_family_tortilla"
            "fideua" in title || "fideuá" in title || "pasta" in title || "macarron" in title ||
                "macarrón" in title -> "recipe_family_pasta"
            "sopa" in title || "caldo" in title || "crema" in title || "puré" in title || "pure" in title -> "recipe_family_sopa"
            "arroz" in title || "paella" in title -> "recipe_family_arroz"
            "ensalada" in title || "gazpacho" in title || "salmorejo" in title -> "recipe_family_ensalada"
            "pollo" in title -> "recipe_family_pollo"
            "ternera" in title || "cerdo" in title || "conejo" in title || "costilla" in title ||
                "lomo" in title || "carne" in title -> "recipe_family_carne"
            "pescado" in title || "merluza" in title || "lubina" in title || "bacalao" in title ||
                "atun" in title || "atún" in title || "bonito" in title || "sardina" in title ||
                "boqueron" in title || "boquerón" in title || "calamar" in title || "sepia" in title ||
                "mejillon" in title || "mejillón" in title || "gamba" in title || "almeja" in title -> "recipe_family_pescado"
            "lenteja" in title || "garbanzo" in title || "alubia" in title -> "recipe_family_legumbre"
            "patata" in title || "bravas" in title || "riojana" in title -> "recipe_family_patata"
            "yogur" in title || "fruta" in title || "macedonia" in title || "avena" in title ||
                "porridge" in title || "manzana" in title || "platano" in title || "plátano" in title -> "recipe_family_fruta"
            category == RecipeCategory.BREAKFAST -> "recipe_family_fruta"
            category == RecipeCategory.SALAD -> "recipe_family_ensalada"
            category == RecipeCategory.PASTA -> "recipe_family_pasta"
            category == RecipeCategory.RICE -> "recipe_family_arroz"
            category == RecipeCategory.CHICKEN -> "recipe_family_pollo"
            category in setOf(RecipeCategory.PORK, RecipeCategory.BEEF) -> "recipe_family_carne"
            category == RecipeCategory.FISH -> "recipe_family_pescado"
            category == RecipeCategory.LEGUMES -> "recipe_family_legumbre"
            else -> "recipe_family_verdura"
        }
    }

    private fun step(order: Int, title: String, instruction: String): RecipeStep {
        return RecipeStep(order = order, title = title, instruction = instruction)
    }

    private data class CatalogEntry(
        val category: RecipeCategory,
        val title: String,
    )
}

internal fun RecipeIngredient.displayAmount(): String {
    return when (unit) {
        IngredientUnit.GRAM -> "${quantity.clean()} g"
        IngredientUnit.MILLILITER -> "${quantity.clean()} ml"
        IngredientUnit.UNIT -> "${quantity.clean()} ud"
        IngredientUnit.TABLESPOON -> "${quantity.clean()} cda"
        IngredientUnit.TEASPOON -> "${quantity.clean()} cdta"
        IngredientUnit.PINCH -> "1 pizca"
        IngredientUnit.TO_TASTE -> "al gusto"
    }
}

private fun Double.clean(): String {
    return if (this % 1.0 == 0.0) toInt().toString() else toString().trimEnd('0').trimEnd('.')
}

private fun String.slug(): String {
    return normalized()
        .replace(Regex("[^a-z0-9]+"), "-")
        .trim('-')
}

private fun String.normalized(): String {
    return lowercase()
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
        .replace(Regex("\\s+"), " ")
        .trim()
}
