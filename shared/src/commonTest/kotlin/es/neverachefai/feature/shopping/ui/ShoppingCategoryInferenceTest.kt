package es.neverachefai.feature.shopping.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ShoppingCategoryInferenceTest {

    @Test
    fun `common fruit names select specific product icons under fruit category`() {
        val cases = mapOf(
            "tomate" to "fruit_tomato",
            "pera conferencia" to "fruit_pear",
            "plátano" to "fruit_banana",
            "fresas" to "fruit_strawberry",
            "sandía" to "fruit_watermelon",
            "melocotones" to "fruit_peach",
            "nectarina" to "fruit_peach",
            "arándanos" to "fruit_blueberry",
            "manzana" to "fruit_apple",
            "naranja" to "fruit_orange",
            "mandarina" to "fruit_mandarin",
            "uva" to "fruit_grape",
            "aguacate" to "fruit_avocado",
            "pomelo" to "fruit_grapefruit",
            "granada" to "fruit_pomegranate",
            "caqui" to "fruit_persimmon",
            "níspero" to "fruit_loquat",
            "higos" to "fruit_fig",
            "dátiles" to "fruit_date",
            "coco" to "fruit_coconut",
            "chirimoya" to "fruit_cherimoya",
            "papaya" to "fruit_papaya",
        )

        cases.forEach { (productName, expectedIconKey) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expectedIconKey, inference?.productIconKey)
            assertEquals("fruits", inference?.categoryIconKey)
            assertEquals("Frutas", inference?.categoryLabel)
        }

        assertEquals("fruits", inferShoppingIconKey("fruta de la pasión"))
        assertEquals("fruits", inferShoppingIconKey("frutas del bosque"))
    }

    @Test
    fun `partial fruit names select specific fruit product icons`() {
        assertEquals("fruit_apple", inferShoppingIconKey("manzan"))
        assertEquals("fruit_mandarin", inferShoppingIconKey("mandarin"))
        assertEquals("fruit_blueberry", inferShoppingIconKey("arandan"))
        assertEquals("fruit_peach", inferShoppingIconKey("nectarin"))
    }

    @Test
    fun `tomate frito stays as sauce`() {
        assertEquals("sauces", inferShoppingIconKey("tomate frito"))
    }

    @Test
    fun `fruit terms do not match inside unrelated words`() {
        assertNull(inferShoppingIconKey("emperador"))
    }

    @Test
    fun `meat names select specific product icons under meat category`() {
        val cases = mapOf(
            "pechuga de pollo" to "meat_chicken",
            "filetes de ternera" to "meat_beef",
            "lomo de cerdo" to "meat_pork",
            "pavo" to "meat_turkey",
            "cordero" to "meat_lamb",
            "conejo troceado" to "meat_rabbit",
            "chorizo" to "meat_sausage",
            "carne picada" to "meat",
        )

        cases.forEach { (productName, expectedIconKey) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expectedIconKey, inference?.productIconKey)
            assertEquals("meat", inference?.categoryIconKey)
            assertEquals("Carne", inference?.categoryLabel)
        }
    }

    @Test
    fun `specific meat terms win over generic meat terms`() {
        assertEquals("meat_beef", inferShoppingIconKey("lomo alto"))
        assertEquals("meat_sausage", inferShoppingIconKey("lomo embuchado"))
    }

    @Test
    fun `specific meat icons keep fridge destination`() {
        assertEquals("fridge", shoppingDestinationForIconKey("meat_chicken"))
    }

    @Test
    fun `fish names select specific product icons under fish category`() {
        val cases = mapOf(
            "merluza" to "fish_hake",
            "bacalao" to "fish_cod",
            "lubina" to "fish_seabass",
            "salmón" to "fish_salmon",
            "atún" to "fish_tuna",
            "sardinas" to "fish_sardine",
            "trucha" to "fish_trout",
        )

        cases.forEach { (productName, expectedIconKey) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expectedIconKey, inference?.productIconKey)
            assertEquals("fish", inference?.categoryIconKey)
            assertEquals("Pescado", inference?.categoryLabel)
        }
    }

    @Test
    fun `vegetable names select specific product icons under vegetable category`() {
        val cases = mapOf(
            "patatas" to "vegetable_potato",
            "cebolla" to "vegetable_onion",
            "zanahoria" to "vegetable_carrot",
            "pimiento rojo" to "vegetable_pepper",
            "calabacín" to "vegetable_zucchini",
            "pepino" to "vegetable_cucumber",
            "lechuga" to "vegetable_lettuce",
            "espinacas" to "vegetable_spinach",
            "ajo" to "vegetable_garlic",
            "guisantes" to "vegetable_peas",
            "judías verdes" to "vegetable_green_beans",
            "berenjena" to "vegetable_eggplant",
            "calabaza" to "vegetable_pumpkin",
            "alcachofa" to "vegetable_artichoke",
            "puerro" to "vegetable_leek",
            "coliflor" to "vegetable_cauliflower",
        )

        cases.forEach { (productName, expectedIconKey) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expectedIconKey, inference?.productIconKey)
            assertEquals("vegetables", inference?.categoryIconKey)
            assertEquals("Verduras", inference?.categoryLabel)
        }
    }

    @Test
    fun `bread milk and juice names select specific product icons under their categories`() {
        val cases = mapOf(
            "pan de barra" to Triple("bread_baguette", "bread", "Pan"),
            "pan de molde" to Triple("bread_sliced", "bread", "Pan"),
            "pan de hamburguesa" to Triple("bread_burger", "bread", "Pan"),
            "pan de perrito" to Triple("bread_hotdog", "bread", "Pan"),
            "leche entera" to Triple("milk_whole", "milk", "Leche"),
            "leche semidesnatada" to Triple("milk_semi", "milk", "Leche"),
            "leche sin lactosa" to Triple("milk_lactose_free", "milk", "Leche"),
            "leche desnatada" to Triple("milk_skimmed", "milk", "Leche"),
            "zumo de naranja" to Triple("juice_orange", "juice", "Zumo"),
            "zumo de piña" to Triple("juice_pineapple", "juice", "Zumo"),
            "zumo multifrutas" to Triple("juice_multifruit", "juice", "Zumo"),
            "zumo de arándanos" to Triple("juice_blueberry", "juice", "Zumo"),
            "zumo de tomate" to Triple("juice_tomato", "juice", "Zumo"),
        )

        cases.forEach { (productName, expected) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expected.first, inference?.productIconKey)
            assertEquals(expected.second, inference?.categoryIconKey)
            assertEquals(expected.third, inference?.categoryLabel)
        }
    }

    @Test
    fun `sweets snacks pets cleaning and hygiene names select specific product icons`() {
        val cases = mapOf(
            "chocolate negro" to Triple("sweets_chocolate", "sweets", "Dulces"),
            "chucherías" to Triple("sweets_candy", "sweets", "Dulces"),
            "bombones" to Triple("sweets_bonbons", "sweets", "Dulces"),
            "galletas" to Triple("sweets_cookies", "sweets", "Dulces"),
            "bollería" to Triple("sweets_pastry", "sweets", "Dulces"),
            "patatas fritas" to Triple("snack_chips", "snacks", "Snacks"),
            "frutos secos" to Triple("snack_nuts", "snacks", "Snacks"),
            "crackers" to Triple("snack_crackers", "snacks", "Snacks"),
            "pienso de perro" to Triple("pet_dog_food", "pets", "Mascotas"),
            "comida de gato" to Triple("pet_cat_food", "pets", "Mascotas"),
            "arena de gato" to Triple("pet_litter", "pets", "Mascotas"),
            "premios perro" to Triple("pet_treats", "pets", "Mascotas"),
            "detergente lavadora" to Triple("cleaning_laundry", "cleaning", "Limpieza"),
            "lavavajillas" to Triple("cleaning_dish_soap", "cleaning", "Limpieza"),
            "lejía" to Triple("cleaning", "cleaning", "Limpieza"),
            "limpia cristales" to Triple("cleaning", "cleaning", "Limpieza"),
            "limpia vitros" to Triple("cleaning", "cleaning", "Limpieza"),
            "fairy" to Triple("cleaning_dish_soap", "cleaning", "Limpieza"),
            "pastillas lavavajillas" to Triple("cleaning_dish_soap", "cleaning", "Limpieza"),
            "sal lavavajillas" to Triple("cleaning_dish_soap", "cleaning", "Limpieza"),
            "champú" to Triple("hygiene_shampoo", "hygiene", "Higiene"),
            "gel de baño" to Triple("hygiene_shampoo", "hygiene", "Higiene"),
            "pasta de dientes" to Triple("hygiene_dental", "hygiene", "Higiene"),
            "cepillo dientes" to Triple("hygiene_dental", "hygiene", "Higiene"),
            "hilo dental" to Triple("hygiene_dental", "hygiene", "Higiene"),
            "papel higiénico" to Triple("hygiene", "hygiene", "Higiene"),
            "colonia" to Triple("hygiene", "hygiene", "Higiene"),
            "perfume" to Triple("hygiene", "hygiene", "Higiene"),
            "desodorante" to Triple("hygiene", "hygiene", "Higiene"),
            "toallitas" to Triple("hygiene", "hygiene", "Higiene"),
        )

        cases.forEach { (productName, expected) ->
            val inference = inferShoppingProduct(productName)
            assertEquals(expected.first, inference?.productIconKey)
            assertEquals(expected.second, inference?.categoryIconKey)
            assertEquals(expected.third, inference?.categoryLabel)
        }
    }

    @Test
    fun `cleaning vinegar does not override food vinegar or oil`() {
        assertEquals("cleaning", inferShoppingIconKey("vinagre de limpieza"))
        assertEquals("vinegar", inferShoppingIconKey("vinagre de vino"))
        assertEquals("vinegar", inferShoppingIconKey("vinagre balsámico"))
        assertEquals("oil", inferShoppingIconKey("aceite de oliva"))
        assertEquals("oil", inferShoppingIconKey("aceite de girasol"))
    }

    @Test
    fun `specific icon names keep expected destinations`() {
        assertEquals("pantry", shoppingDestinationForIconKey("bread_burger"))
        assertEquals("fridge", shoppingDestinationForIconKey("milk_lactose_free"))
        assertEquals("pantry", shoppingDestinationForIconKey("juice_orange"))
        assertEquals("pantry", shoppingDestinationForIconKey("pet_dog_food"))
    }
}
