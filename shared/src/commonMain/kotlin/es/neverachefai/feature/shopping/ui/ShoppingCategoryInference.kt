package es.neverachefai.feature.shopping.ui

internal data class ShoppingProductInference(
    val categoryIconKey: String,
    val productIconKey: String,
) {
    val categoryLabel: String = shoppingCategoryLabelForIconKey(categoryIconKey)
}

internal fun inferShoppingCategoryLabel(productName: String): String? {
    return inferShoppingProduct(productName)?.categoryLabel
}

internal fun inferShoppingProduct(productName: String): ShoppingProductInference? {
    val productIconKey = inferShoppingIconKey(productName) ?: return null
    return ShoppingProductInference(
        categoryIconKey = shoppingCategoryIconKeyForProductIcon(productIconKey),
        productIconKey = productIconKey,
    )
}

internal fun inferShoppingIconKey(productName: String): String? {
    val normalized = productName.normalizeShoppingCategoryText()
    if (normalized.isBlank()) return null
    return when {
        normalized.hasAny(shoppingCannedFoodTerms) -> "canned_food"
        normalized.hasAny("yogur", "yogures", "yoghurt", "yogurt") -> "yogurts"
        normalized.hasAny("tomate frito", "pimenton", "salsa", "salsas") -> "sauces"
        normalized.hasAny(shoppingBreadBaguetteTerms) -> "bread_baguette"
        normalized.hasAny(shoppingBreadSlicedTerms) -> "bread_sliced"
        normalized.hasAny(shoppingBreadBurgerTerms) -> "bread_burger"
        normalized.hasAny(shoppingBreadHotdogTerms) -> "bread_hotdog"
        normalized.hasAny(shoppingBreadGenericTerms) -> "bread"
        normalized.hasAny(shoppingMilkLactoseFreeTerms) -> "milk_lactose_free"
        normalized.hasAny(shoppingMilkWholeTerms) -> "milk_whole"
        normalized.hasAny(shoppingMilkSemiTerms) -> "milk_semi"
        normalized.hasAny(shoppingMilkSkimmedTerms) -> "milk_skimmed"
        normalized.hasAny("leche") -> "milk"
        normalized.hasAny(shoppingHygieneDentalTerms) -> "hygiene_dental"
        normalized.hasJuiceFlavor("naranja", "naranjas") -> "juice_orange"
        normalized.hasJuiceFlavor("pina", "pinas", "ananás", "ananas") -> "juice_pineapple"
        normalized.hasJuiceFlavor("melocoton", "melocotones", "durazno", "duraznos") -> "juice_peach"
        normalized.hasJuiceFlavor("arandano", "arandanos") -> "juice_blueberry"
        normalized.hasJuiceFlavor("multifruta", "multifrutas", "multi fruta", "multi frutas") -> "juice_multifruit"
        normalized.hasJuiceFlavor("manzana", "manzanas") -> "juice_apple"
        normalized.hasJuiceFlavor("tomate", "tomates") -> "juice_tomato"
        normalized.hasJuiceFlavor("uva", "uvas") -> "juice_grape"
        normalized.hasAny("zumo", "zumos", "jugo", "jugos") -> "juice"
        normalized.hasAny(shoppingFruitTomatoTerms) -> "fruit_tomato"
        normalized.hasAny(shoppingFruitAppleTerms) -> "fruit_apple"
        normalized.hasAny(shoppingFruitPearTerms) -> "fruit_pear"
        normalized.hasAny(shoppingFruitBananaTerms) -> "fruit_banana"
        normalized.hasAny(shoppingFruitOrangeTerms) -> "fruit_orange"
        normalized.hasAny(shoppingFruitMandarinTerms) -> "fruit_mandarin"
        normalized.hasAny(shoppingFruitLemonTerms) -> "fruit_lemon"
        normalized.hasAny(shoppingFruitLimeTerms) -> "fruit_lime"
        normalized.hasAny(shoppingFruitGrapefruitTerms) -> "fruit_grapefruit"
        normalized.hasAny(shoppingFruitStrawberryTerms) -> "fruit_strawberry"
        normalized.hasAny(shoppingFruitRaspberryTerms) -> "fruit_raspberry"
        normalized.hasAny(shoppingFruitBlackberryTerms) -> "fruit_blackberry"
        normalized.hasAny(shoppingFruitBlueberryTerms) -> "fruit_blueberry"
        normalized.hasAny(shoppingFruitGrapeTerms) -> "fruit_grape"
        normalized.hasAny(shoppingFruitKiwiTerms) -> "fruit_kiwi"
        normalized.hasAny(shoppingFruitMelonTerms) -> "fruit_melon"
        normalized.hasAny(shoppingFruitWatermelonTerms) -> "fruit_watermelon"
        normalized.hasAny(shoppingFruitPeachTerms) -> "fruit_peach"
        normalized.hasAny(shoppingFruitPlumTerms) -> "fruit_plum"
        normalized.hasAny(shoppingFruitCherryTerms) -> "fruit_cherry"
        normalized.hasAny(shoppingFruitPomegranateTerms) -> "fruit_pomegranate"
        normalized.hasAny(shoppingFruitPersimmonTerms) -> "fruit_persimmon"
        normalized.hasAny(shoppingFruitLoquatTerms) -> "fruit_loquat"
        normalized.hasAny(shoppingFruitFigTerms) -> "fruit_fig"
        normalized.hasAny(shoppingFruitDateTerms) -> "fruit_date"
        normalized.hasAny(shoppingFruitCoconutTerms) -> "fruit_coconut"
        normalized.hasAny(shoppingFruitCherimoyaTerms) -> "fruit_cherimoya"
        normalized.hasAny(shoppingFruitMangoTerms) -> "fruit_mango"
        normalized.hasAny(shoppingFruitPapayaTerms) -> "fruit_papaya"
        normalized.hasAny(shoppingFruitPineappleTerms) -> "fruit_pineapple"
        normalized.hasAny(shoppingFruitAvocadoTerms) -> "fruit_avocado"
        normalized.hasAny(shoppingSnackChipsTerms) -> "snack_chips"
        normalized.hasAny(shoppingFruitTerms) -> "fruits"
        normalized.hasAny(shoppingVegetablePotatoTerms) -> "vegetable_potato"
        normalized.hasAny(shoppingVegetableOnionTerms) -> "vegetable_onion"
        normalized.hasAny(shoppingVegetableCarrotTerms) -> "vegetable_carrot"
        normalized.hasAny(shoppingVegetablePepperTerms) -> "vegetable_pepper"
        normalized.hasAny(shoppingVegetableZucchiniTerms) -> "vegetable_zucchini"
        normalized.hasAny(shoppingVegetableCucumberTerms) -> "vegetable_cucumber"
        normalized.hasAny(shoppingVegetableLettuceTerms) -> "vegetable_lettuce"
        normalized.hasAny(shoppingVegetableSpinachTerms) -> "vegetable_spinach"
        normalized.hasAny(shoppingVegetableGarlicTerms) -> "vegetable_garlic"
        normalized.hasAny(shoppingVegetablePeasTerms) -> "vegetable_peas"
        normalized.hasAny(shoppingVegetableGreenBeansTerms) -> "vegetable_green_beans"
        normalized.hasAny(shoppingVegetableEggplantTerms) -> "vegetable_eggplant"
        normalized.hasAny(shoppingVegetablePumpkinTerms) -> "vegetable_pumpkin"
        normalized.hasAny(shoppingVegetableArtichokeTerms) -> "vegetable_artichoke"
        normalized.hasAny(shoppingVegetableLeekTerms) -> "vegetable_leek"
        normalized.hasAny(shoppingVegetableCauliflowerTerms) -> "vegetable_cauliflower"
        normalized.hasAny(shoppingVegetableGenericTerms) -> "vegetables"
        normalized.hasAny(shoppingFishHakeTerms) || normalized.hasToken("luz") -> "fish_hake"
        normalized.hasAny(shoppingFishCodTerms) -> "fish_cod"
        normalized.hasAny(shoppingFishSeabassTerms) -> "fish_seabass"
        normalized.hasAny(shoppingFishSalmonTerms) -> "fish_salmon"
        normalized.hasAny(shoppingFishTunaTerms) -> "fish_tuna"
        normalized.hasAny(shoppingFishSardineTerms) -> "fish_sardine"
        normalized.hasAny(shoppingFishTroutTerms) -> "fish_trout"
        normalized.hasAny(shoppingFishGenericTerms) -> "fish"
        normalized.hasAny("marisco", "mariscos", "gamba", "gambas", "langostino", "langostinos", "mejillon", "mejillones", "almeja", "almejas") -> "seafood"
        normalized.hasAny(shoppingMeatSausageTerms) -> "meat_sausage"
        normalized.hasAny(shoppingMeatRabbitTerms) -> "meat_rabbit"
        normalized.hasAny(shoppingMeatTurkeyTerms) -> "meat_turkey"
        normalized.hasAny(shoppingMeatChickenTerms) -> "meat_chicken"
        normalized.hasAny(shoppingMeatLambTerms) -> "meat_lamb"
        normalized.hasAny(shoppingMeatBeefTerms) -> "meat_beef"
        normalized.hasAny(shoppingMeatPorkTerms) -> "meat_pork"
        normalized.hasAny(shoppingMeatGenericTerms) -> "meat"
        normalized.hasAny("queso", "quesos") -> "cheese"
        normalized.hasAny("huevo", "huevos") -> "eggs"
        normalized.hasAny("macarron", "macarrones", "espagueti", "espaguetis", "spaghetti", "pasta", "fideo", "fideos") -> "pasta"
        normalized.hasAny("arroz") -> "rice"
        normalized.hasAny("lenteja", "lentejas", "garbanzo", "garbanzos", "alubia", "alubias", "legumbre", "legumbres") -> "legumes"
        normalized.hasAny("congelado", "congelados", "helado", "helados") -> "frozen"
        normalized.hasAny("agua") -> "water"
        normalized.hasAny("refresco", "refrescos", "gaseosa") || normalized.hasToken("cola") -> "soft_drinks"
        normalized.hasAny(shoppingCleaningVinegarTerms) -> "cleaning"
        normalized.hasAny("vinagre") -> "vinegar"
        normalized.hasAny("vino", "vinos") -> "wine"
        normalized.hasAny("cerveza", "cervezas") -> "beer"
        normalized.hasAny("cafe", "coffee") -> "coffee"
        normalized.hasToken("te", "tea", "infusion") -> "tea"
        normalized.hasAny(shoppingSnackNutsTerms) -> "snack_nuts"
        normalized.hasAny(shoppingSnackCrackersTerms) -> "snack_crackers"
        normalized.hasAny("snack", "snacks", "aperitivo", "aperitivos") -> "snacks"
        normalized.hasAny(shoppingSweetsChocolateTerms) -> "sweets_chocolate"
        normalized.hasAny(shoppingSweetsCandyTerms) -> "sweets_candy"
        normalized.hasAny(shoppingSweetsBonbonsTerms) -> "sweets_bonbons"
        normalized.hasAny(shoppingSweetsCookiesTerms) -> "sweets_cookies"
        normalized.hasAny(shoppingSweetsPastryTerms) -> "sweets_pastry"
        normalized.hasAny("dulce", "dulces") -> "sweets"
        normalized.hasAny("aceite") -> "oil"
        normalized.hasAny("plato listo", "platos listos", "preparado", "preparados", "caldo", "sopa") -> "ready_meals"
        normalized.hasAny(shoppingCleaningLaundryTerms) -> "cleaning_laundry"
        normalized.hasAny(shoppingCleaningDishSoapTerms) -> "cleaning_dish_soap"
        normalized.hasAny(shoppingCleaningHomeTerms) -> "cleaning"
        normalized.hasAny(shoppingHygieneShampooTerms) -> "hygiene_shampoo"
        normalized.hasAny(shoppingHygieneGenericTerms) -> "hygiene"
        normalized.hasAny(shoppingPetLitterTerms) -> "pet_litter"
        normalized.hasAny(shoppingPetTreatTerms) -> "pet_treats"
        normalized.hasPetFood("perro", "perros") -> "pet_dog_food"
        normalized.hasPetFood("gato", "gatos") -> "pet_cat_food"
        normalized.hasAny("mascota", "mascotas", "perro", "gato", "pienso") -> "pets"
        else -> null
    }
}

private val shoppingBreadBaguetteTerms = listOf("pan de barra", "barra de pan", "baguette", "baguettes")
private val shoppingBreadSlicedTerms = listOf("pan de molde", "molde")
private val shoppingBreadBurgerTerms = listOf("pan de hamburguesa", "pan hamburguesa", "bollos de hamburguesa")
private val shoppingBreadHotdogTerms = listOf("pan de perrito", "pan perrito", "pan de salchicha", "pan salchicha", "pan de hot dog", "hot dog bun")
private val shoppingBreadGenericTerms = listOf("pan", "tostada", "tostadas")

private val shoppingMilkLactoseFreeTerms = listOf("leche sin lactosa")
private val shoppingMilkWholeTerms = listOf("leche entera")
private val shoppingMilkSemiTerms = listOf("leche semi", "semidesnatada", "semi desnatada")
private val shoppingMilkSkimmedTerms = listOf("leche desnatada", "desnatada")

private val shoppingCannedFoodTerms = listOf("conserva", "conservas", "lata", "latas", "enlatado", "enlatados")

private val shoppingFruitTomatoTerms = listOf("tomate", "tomates")
private val shoppingFruitAppleTerms = listOf("manzana", "manzanas")
private val shoppingFruitPearTerms = listOf("pera", "peras")
private val shoppingFruitBananaTerms = listOf("platano", "platanos", "banana", "bananas")
private val shoppingFruitOrangeTerms = listOf("naranja", "naranjas")
private val shoppingFruitMandarinTerms = listOf("mandarina", "mandarinas", "clementina", "clementinas")
private val shoppingFruitLemonTerms = listOf("limon", "limones")
private val shoppingFruitLimeTerms = listOf("lima", "limas")
private val shoppingFruitGrapefruitTerms = listOf("pomelo", "pomelos")
private val shoppingFruitStrawberryTerms = listOf("fresa", "fresas", "freson", "fresones")
private val shoppingFruitRaspberryTerms = listOf("frambuesa", "frambuesas")
private val shoppingFruitBlackberryTerms = listOf("mora", "moras")
private val shoppingFruitBlueberryTerms = listOf("arandano", "arandanos")
private val shoppingFruitGrapeTerms = listOf("uva", "uvas")
private val shoppingFruitKiwiTerms = listOf("kiwi", "kiwis")
private val shoppingFruitMelonTerms = listOf("melon", "melones")
private val shoppingFruitWatermelonTerms = listOf("sandia", "sandias")
private val shoppingFruitPeachTerms = listOf("melocoton", "melocotones", "paraguayo", "paraguayos", "nectarina", "nectarinas", "albaricoque", "albaricoques")
private val shoppingFruitPlumTerms = listOf("ciruela", "ciruelas")
private val shoppingFruitCherryTerms = listOf("cereza", "cerezas", "picota", "picotas")
private val shoppingFruitPomegranateTerms = listOf("granada", "granadas")
private val shoppingFruitPersimmonTerms = listOf("caqui", "caquis", "kaki", "kakis")
private val shoppingFruitLoquatTerms = listOf("nispero", "nisperos")
private val shoppingFruitFigTerms = listOf("higo", "higos", "breva", "brevas")
private val shoppingFruitDateTerms = listOf("datil", "datiles")
private val shoppingFruitCoconutTerms = listOf("coco", "cocos")
private val shoppingFruitCherimoyaTerms = listOf("chirimoya", "chirimoyas")
private val shoppingFruitMangoTerms = listOf("mango", "mangos")
private val shoppingFruitPapayaTerms = listOf("papaya", "papayas", "papayon", "papayones")
private val shoppingFruitPineappleTerms = listOf("pina", "pinas")
private val shoppingFruitAvocadoTerms = listOf("aguacate", "aguacates")

private val shoppingFruitTerms = listOf(
    "fruta",
    "frutas",
    "tomate",
    "tomates",
    "manzana",
    "manzanas",
    "pera",
    "peras",
    "platano",
    "platanos",
    "banana",
    "bananas",
    "naranja",
    "naranjas",
    "mandarina",
    "mandarinas",
    "clementina",
    "clementinas",
    "limon",
    "limones",
    "lima",
    "limas",
    "pomelo",
    "pomelos",
    "fresa",
    "fresas",
    "freson",
    "fresones",
    "frambuesa",
    "frambuesas",
    "mora",
    "moras",
    "arandano",
    "arandanos",
    "grosella",
    "grosellas",
    "uva",
    "uvas",
    "kiwi",
    "kiwis",
    "melon",
    "melones",
    "sandia",
    "sandias",
    "melocoton",
    "melocotones",
    "paraguayo",
    "paraguayos",
    "nectarina",
    "nectarinas",
    "albaricoque",
    "albaricoques",
    "ciruela",
    "ciruelas",
    "cereza",
    "cerezas",
    "picota",
    "picotas",
    "mango",
    "mangos",
    "pina",
    "pinas",
    "papaya",
    "papayas",
    "aguacate",
    "aguacates",
    "chirimoya",
    "chirimoyas",
    "granada",
    "granadas",
    "caqui",
    "caquis",
    "kaki",
    "kakis",
    "nispero",
    "nisperos",
    "higo",
    "higos",
    "breva",
    "brevas",
    "datil",
    "datiles",
    "coco",
    "cocos",
    "lichi",
    "lichis",
    "maracuya",
    "maracuyas",
    "fruta de la pasion",
    "guayaba",
    "guayabas",
    "carambola",
    "carambolas",
    "tamarindo",
    "tamarindos",
    "membrillo",
    "membrillos",
    "endrina",
    "endrinas",
    "acerola",
    "acerolas",
    "pitaya",
    "pitayas",
    "papayon",
    "papayones",
    "physalis",
    "rambutan",
    "rambutanes",
    "mangostan",
    "mangostanes",
    "pasas",
    "frutos rojos",
    "frutas del bosque",
)

private val shoppingVegetablePotatoTerms = listOf("patata", "patatas", "papa", "papas")
private val shoppingVegetableOnionTerms = listOf("cebolla", "cebollas", "cebolleta", "cebolletas")
private val shoppingVegetableCarrotTerms = listOf("zanahoria", "zanahorias")
private val shoppingVegetablePepperTerms = listOf("pimiento", "pimientos", "pimiento rojo", "pimiento verde")
private val shoppingVegetableZucchiniTerms = listOf("calabacin", "calabacines")
private val shoppingVegetableCucumberTerms = listOf("pepino", "pepinos")
private val shoppingVegetableLettuceTerms = listOf("lechuga", "lechugas")
private val shoppingVegetableSpinachTerms = listOf("espinaca", "espinacas", "acelga", "acelgas")
private val shoppingVegetableGarlicTerms = listOf("ajo", "ajos")
private val shoppingVegetablePeasTerms = listOf("guisante", "guisantes")
private val shoppingVegetableGreenBeansTerms = listOf("judia verde", "judias verdes", "vainas")
private val shoppingVegetableEggplantTerms = listOf("berenjena", "berenjenas")
private val shoppingVegetablePumpkinTerms = listOf("calabaza", "calabazas")
private val shoppingVegetableArtichokeTerms = listOf("alcachofa", "alcachofas")
private val shoppingVegetableLeekTerms = listOf("puerro", "puerros")
private val shoppingVegetableCauliflowerTerms = listOf("coliflor", "coliflores", "brocoli", "brocolis")
private val shoppingVegetableGenericTerms = listOf("verdura", "verduras", "vegetal", "vegetales")

private val shoppingFishHakeTerms = listOf("merluza", "merluzas", "pescadilla", "pescadillas")
private val shoppingFishCodTerms = listOf("bacalao", "bacalaos")
private val shoppingFishSeabassTerms = listOf("lubina", "lubinas", "dorada", "doradas")
private val shoppingFishSalmonTerms = listOf("salmon", "salmones")
private val shoppingFishTunaTerms = listOf("atun", "atunes", "bonito", "bonitos")
private val shoppingFishSardineTerms = listOf("sardina", "sardinas", "boqueron", "boquerones", "anchoa", "anchoas")
private val shoppingFishTroutTerms = listOf("trucha", "truchas")
private val shoppingFishGenericTerms = listOf("pescado", "pescados", "filete de pescado", "filetes de pescado")

private val shoppingMeatSausageTerms = listOf(
    "chorizo",
    "chorizos",
    "morcilla",
    "morcillas",
    "salchicha",
    "salchichas",
    "butifarra",
    "butifarras",
    "longaniza",
    "longanizas",
    "fuet",
    "salchichon",
    "salchichones",
    "lomo embuchado",
    "jamon",
    "jamon serrano",
    "jamon cocido",
    "jamon york",
    "paleta",
    "cecina",
    "sobrasada",
    "bacon",
    "beicon",
    "torrezno",
    "torreznos",
)

private val shoppingMeatRabbitTerms = listOf(
    "conejo",
    "conejos",
    "conejo troceado",
    "lomo de conejo",
    "muslo de conejo",
    "muslos de conejo",
)

private val shoppingMeatTurkeyTerms = listOf(
    "pavo",
    "pavos",
    "pechuga de pavo",
    "pechugas de pavo",
    "filete de pavo",
    "filetes de pavo",
    "muslo de pavo",
    "muslos de pavo",
    "pavo picado",
)

private val shoppingMeatChickenTerms = listOf(
    "pollo",
    "pollos",
    "pechuga",
    "pechugas",
    "pechuga de pollo",
    "pechugas de pollo",
    "muslo de pollo",
    "muslos de pollo",
    "contramuslo",
    "contramuslos",
    "alita",
    "alitas",
    "ala de pollo",
    "alas de pollo",
    "jamoncito",
    "jamoncitos",
    "pollo entero",
    "pollo troceado",
    "pollo campero",
)

private val shoppingMeatLambTerms = listOf(
    "cordero",
    "corderos",
    "lechazo",
    "ternasco",
    "oveja",
    "chuleta de cordero",
    "chuletas de cordero",
    "paletilla de cordero",
    "pierna de cordero",
    "falda de cordero",
    "costillar de cordero",
    "cuello de cordero",
    "cabrito",
    "cabritos",
    "cabra",
    "chivo",
    "paletilla de cabrito",
    "pierna de cabrito",
)

private val shoppingMeatBeefTerms = listOf(
    "ternera",
    "vaca",
    "buey",
    "anojo",
    "novillo",
    "filete de ternera",
    "filetes de ternera",
    "entrecot",
    "entrecots",
    "chuleton",
    "chuletones",
    "solomillo de ternera",
    "lomo alto",
    "lomo bajo",
    "babilla",
    "tapa",
    "contra",
    "redondo",
    "aguja",
    "espaldilla",
    "falda de ternera",
    "morcillo",
    "jarrete",
    "osobuco",
    "rabillo",
    "cadera",
    "rabo de ternera",
    "carrillera",
    "carrilleras",
    "entrana",
    "vacio",
    "churrasco",
)

private val shoppingMeatPorkTerms = listOf(
    "cerdo",
    "cochinillo",
    "lomo de cerdo",
    "lomo",
    "cinta de lomo",
    "solomillo de cerdo",
    "secreto",
    "presa",
    "pluma",
    "abanico",
    "costilla de cerdo",
    "costillas de cerdo",
    "chuleta de cerdo",
    "chuletas de cerdo",
    "panceta",
    "papada",
    "careta",
    "codillo",
    "manitas",
    "magro",
)

private val shoppingMeatGenericTerms = listOf(
    "carne",
    "carnes",
    "filete",
    "filetes",
    "chuleta",
    "chuletas",
    "costilla",
    "costillas",
    "solomillo",
    "hamburguesa",
    "hamburguesas",
    "carne picada",
    "picada",
    "albondiga",
    "albondigas",
    "brocheta",
    "brochetas",
    "pincho",
    "pinchos",
    "milanesa",
    "milanesas",
    "escalope",
    "escalopes",
    "higado",
    "rinon",
    "rinones",
    "callos",
    "lengua",
    "corazon",
    "molleja",
    "mollejas",
    "sesos",
    "sangre",
    "criadilla",
    "criadillas",
)

private val shoppingSweetsChocolateTerms = listOf("chocolate", "chocolates", "tableta de chocolate", "cacao")
private val shoppingSweetsCandyTerms = listOf(
    "chucheria",
    "chucherias",
    "gominola",
    "gominolas",
    "caramelo",
    "caramelos",
    "regaliz",
    "nube",
    "nubes",
)
private val shoppingSweetsBonbonsTerms = listOf("bombon", "bombones", "trufa", "trufas")
private val shoppingSweetsCookiesTerms = listOf("galleta", "galletas", "cookie", "cookies")
private val shoppingSweetsPastryTerms = listOf(
    "bolleria",
    "bollo",
    "bollos",
    "croissant",
    "cruasan",
    "magdalena",
    "magdalenas",
    "napolitana",
    "napolitanas",
    "palmera",
    "palmeras",
)

private val shoppingSnackChipsTerms = listOf("patatas fritas", "chips", "bolsa de patatas")
private val shoppingSnackNutsTerms = listOf("frutos secos", "almendra", "almendras", "nuez", "nueces", "cacahuete", "cacahuetes", "pistacho", "pistachos")
private val shoppingSnackCrackersTerms = listOf("cracker", "crackers", "colines", "picos", "reganas", "rosquilletas")

private val shoppingCleaningVinegarTerms = listOf(
    "vinagre de limpieza",
    "vinagre limpieza",
    "vinagre limpiador",
)
private val shoppingCleaningLaundryTerms = listOf(
    "detergente",
    "suavizante",
    "lavadora",
    "detergente lavadora",
    "detergente ropa",
    "quitamanchas",
    "percarbonato",
    "lejia ropa",
    "lejia para ropa",
    "ropa",
)
private val shoppingCleaningDishSoapTerms = listOf(
    "lavavajillas",
    "lavaplatos",
    "jabon lavavajillas",
    "detergente lavavajillas",
    "pastilla lavavajillas",
    "pastillas lavavajillas",
    "sal lavavajillas",
    "abrillantador lavavajillas",
    "fairy",
    "estropajo",
    "esponja",
)
private val shoppingCleaningHomeTerms = listOf(
    "limpieza",
    "limpieza hogar",
    "limpiador",
    "limpiadores",
    "limpia",
    "limpiacristales",
    "limpia cristales",
    "cristales",
    "lejia",
    "amoniaco",
    "desinfectante",
    "multiusos",
    "quitagrasas",
    "limpiasuelos",
    "limpia suelos",
    "limpia vitro",
    "limpia vitros",
    "limpiavitro",
    "limpiavitros",
    "vitro",
    "vitroceramica",
    "wc",
)
private val shoppingHygieneShampooTerms = listOf(
    "champu",
    "gel",
    "gel de ducha",
    "gel de bano",
    "gel bano",
    "gel de baño",
    "gel baño",
    "acondicionador",
    "mascarilla pelo",
)
private val shoppingHygieneDentalTerms = listOf(
    "pasta de dientes",
    "pasta dientes",
    "dentifrico",
    "cepillo de dientes",
    "cepillo dientes",
    "cepillo dental",
    "hilo dental",
    "enjuague bucal",
    "colutorio",
)
private val shoppingHygieneGenericTerms = listOf(
    "higiene",
    "jabon",
    "jabon manos",
    "papel higienico",
    "colonia",
    "perfume",
    "desodorante",
    "toallita",
    "toallitas",
    "toallitas humedas",
    "compresa",
    "compresas",
    "tampon",
    "tampones",
    "maquinilla",
    "maquinillas",
    "cuchilla",
    "cuchillas",
)
private val shoppingPetLitterTerms = listOf("arena de gato", "arena gato", "arena para gato", "arenero")
private val shoppingPetTreatTerms = listOf("premio perro", "premios perro", "premio gato", "premios gato", "snack perro", "snack gato", "chuche perro", "chuche gato")

internal fun shoppingCategoryLabelForIconKey(iconKey: String): String {
    return when (shoppingCategoryIconKeyForProductIcon(iconKey)) {
        "fruits" -> "Frutas"
        "vegetables" -> "Verduras"
        "meat" -> "Carne"
        "fish" -> "Pescado"
        "seafood" -> "Marisco"
        "bread" -> "Pan"
        "milk" -> "Leche"
        "yogurts" -> "Yogures"
        "cheese" -> "Queso"
        "eggs" -> "Huevos"
        "pasta" -> "Pasta"
        "rice" -> "Arroz"
        "legumes" -> "Legumbres"
        "canned_food" -> "Conservas"
        "frozen" -> "Congelados"
        "water" -> "Agua"
        "soft_drinks" -> "Refrescos"
        "juice" -> "Zumo"
        "wine" -> "Vino"
        "beer" -> "Cerveza"
        "coffee" -> "Café"
        "tea" -> "Té"
        "snacks" -> "Snacks"
        "sweets" -> "Dulces"
        "sauces" -> "Salsas"
        "oil" -> "Aceite"
        "vinegar" -> "Vinagre"
        "ready_meals" -> "Platos listos"
        "cleaning" -> "Limpieza"
        "hygiene" -> "Higiene"
        "pets" -> "Mascotas"
        else -> "Otros"
    }
}

internal fun shoppingCategoryIconKeyForProductIcon(iconKey: String): String {
    return when (iconKey) {
        "meat_beef",
        "meat_pork",
        "meat_chicken",
        "meat_turkey",
        "meat_lamb",
        "meat_rabbit",
        "meat_sausage"
        -> "meat"
        "fish_hake",
        "fish_cod",
        "fish_seabass",
        "fish_salmon",
        "fish_tuna",
        "fish_sardine",
        "fish_trout"
        -> "fish"
        "vegetable_potato",
        "vegetable_onion",
        "vegetable_carrot",
        "vegetable_pepper",
        "vegetable_zucchini",
        "vegetable_cucumber",
        "vegetable_lettuce",
        "vegetable_spinach",
        "vegetable_garlic",
        "vegetable_peas",
        "vegetable_green_beans",
        "vegetable_eggplant",
        "vegetable_pumpkin",
        "vegetable_artichoke",
        "vegetable_leek",
        "vegetable_cauliflower"
        -> "vegetables"
        "bread_baguette",
        "bread_sliced",
        "bread_burger",
        "bread_hotdog"
        -> "bread"
        "milk_whole",
        "milk_semi",
        "milk_lactose_free",
        "milk_skimmed"
        -> "milk"
        "juice_orange",
        "juice_pineapple",
        "juice_peach",
        "juice_blueberry",
        "juice_multifruit",
        "juice_apple",
        "juice_tomato",
        "juice_grape"
        -> "juice"
        "sweets_chocolate",
        "sweets_candy",
        "sweets_bonbons",
        "sweets_cookies",
        "sweets_pastry"
        -> "sweets"
        "snack_chips",
        "snack_nuts",
        "snack_crackers"
        -> "snacks"
        "pet_dog_food",
        "pet_cat_food",
        "pet_litter",
        "pet_treats"
        -> "pets"
        "fruit_tomato",
        "fruit_apple",
        "fruit_pear",
        "fruit_banana",
        "fruit_orange",
        "fruit_mandarin",
        "fruit_lemon",
        "fruit_lime",
        "fruit_grapefruit",
        "fruit_strawberry",
        "fruit_raspberry",
        "fruit_blackberry",
        "fruit_blueberry",
        "fruit_grape",
        "fruit_kiwi",
        "fruit_melon",
        "fruit_watermelon",
        "fruit_peach",
        "fruit_plum",
        "fruit_cherry",
        "fruit_pomegranate",
        "fruit_persimmon",
        "fruit_loquat",
        "fruit_fig",
        "fruit_date",
        "fruit_coconut",
        "fruit_cherimoya",
        "fruit_mango",
        "fruit_papaya",
        "fruit_pineapple",
        "fruit_avocado"
        -> "fruits"
        "cleaning_laundry",
        "cleaning_dish_soap"
        -> "cleaning"
        "hygiene_shampoo",
        "hygiene_dental"
        -> "hygiene"
        else -> iconKey
    }
}

internal fun shoppingCategoryIconKey(label: String): String {
    return when (label.normalizeShoppingCategoryText()) {
        "frutas" -> "fruits"
        "verduras" -> "vegetables"
        "carne" -> "meat"
        "pescado" -> "fish"
        "marisco" -> "seafood"
        "pan" -> "bread"
        "leche" -> "milk"
        "yogures" -> "yogurts"
        "queso" -> "cheese"
        "huevos" -> "eggs"
        "pasta" -> "pasta"
        "arroz" -> "rice"
        "legumbres" -> "legumes"
        "pasta arroz" -> "rice"
        "conservas" -> "canned_food"
        "congelados" -> "frozen"
        "agua" -> "water"
        "refrescos" -> "soft_drinks"
        "zumo" -> "juice"
        "vino" -> "wine"
        "cerveza" -> "beer"
        "cafe" -> "coffee"
        "te" -> "tea"
        "cafe te" -> "coffee"
        "snacks" -> "snacks"
        "dulces" -> "sweets"
        "salsas" -> "sauces"
        "aceite" -> "oil"
        "vinagre" -> "vinegar"
        "aceite vinagre" -> "oil"
        "platos listos" -> "ready_meals"
        "limpieza" -> "cleaning"
        "higiene" -> "hygiene"
        "mascotas" -> "pets"
        else -> "other"
    }
}

internal fun shoppingDestinationForIconKey(iconKey: String): String {
    return when (shoppingCategoryIconKeyForProductIcon(iconKey)) {
        "fish", "meat", "eggs", "milk", "yogurts", "cheese", "vegetables", "fruits" -> "fridge"
        "frozen" -> "freezer"
        else -> "pantry"
    }
}

internal fun String.normalizeShoppingCategoryText(): String {
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
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}

private fun String.hasAny(vararg values: String): Boolean {
    return values.any { it in this }
}

private fun String.hasAny(values: Iterable<String>): Boolean {
    val tokens = split(' ').filter { it.isNotBlank() }
    return values.any { value ->
        if (value.contains(' ')) {
            value in this
        } else {
            tokens.any { token ->
                token == value ||
                    (token.length >= MIN_PREFIX_MATCH_LENGTH && value.startsWith(token)) ||
                    (value.length >= MIN_PREFIX_MATCH_LENGTH && token.startsWith(value))
            }
        }
    }
}

private fun String.hasToken(vararg values: String): Boolean {
    val tokens = split(' ').filter { it.isNotBlank() }.toSet()
    return values.any { it in tokens }
}

private fun String.hasJuiceFlavor(vararg flavors: String): Boolean {
    return hasAny("zumo", "zumos", "jugo", "jugos") && hasAny(flavors.asIterable())
}

private fun String.hasPetFood(vararg animals: String): Boolean {
    return hasAny("pienso", "comida", "alimento") && hasAny(animals.asIterable())
}

private const val MIN_PREFIX_MATCH_LENGTH = 4
