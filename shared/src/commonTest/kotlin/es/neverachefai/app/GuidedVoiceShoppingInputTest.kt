package es.neverachefai.app

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GuidedVoiceShoppingInputTest {

    @Test
    fun `quantity step accepts mode words without a number`() {
        parseSpokenShoppingInput("peso").also { parsed ->
            assertEquals("100", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("cantidad").also { parsed ->
            assertEquals("1", parsed.quantity)
            assertEquals("Unidades", parsed.quantityMode)
            assertNull(parsed.productName)
        }
    }

    @Test
    fun `quantity step accepts common spoken weight and unit phrases`() {
        parseSpokenShoppingInput("tres").also { parsed ->
            assertEquals("3", parsed.quantity)
            assertEquals("Unidades", parsed.quantityMode)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("veintitrés").also { parsed ->
            assertEquals("23", parsed.quantity)
            assertEquals("Unidades", parsed.quantityMode)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("treinta y cinco").also { parsed ->
            assertEquals("35", parsed.quantity)
            assertEquals("Unidades", parsed.quantityMode)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("quinientos gramos").also { parsed ->
            assertEquals("500", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("100G").also { parsed ->
            assertEquals("100", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("100 KG").also { parsed ->
            assertEquals("100000", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("1,5 kg").also { parsed ->
            assertEquals("1500", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("dos kilos").also { parsed ->
            assertEquals("2000", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("dos kilos y medio").also { parsed ->
            assertEquals("2500", parsed.quantity)
            assertEquals("Peso", parsed.quantityMode)
        }

        parseSpokenShoppingInput("cantidad tres").also { parsed ->
            assertEquals("3", parsed.quantity)
            assertEquals("Unidades", parsed.quantityMode)
        }
    }

    @Test
    fun `location step accepts storage words without changing product name`() {
        parseSpokenShoppingInput("despensa").also { parsed ->
            assertEquals("despensa", parsed.location)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("en el congelador").also { parsed ->
            assertEquals("congelador", parsed.location)
            assertNull(parsed.productName)
        }

        parseSpokenShoppingInput("a la nevera").also { parsed ->
            assertEquals("nevera", parsed.location)
            assertNull(parsed.productName)
        }
    }

    @Test
    fun `full sentence keeps product and extracts mode and location`() {
        val parsed = parseSpokenShoppingInput("cacahuetes peso despensa")

        assertEquals("Cacahuetes", parsed.productName)
        assertEquals("100", parsed.quantity)
        assertEquals("Peso", parsed.quantityMode)
        assertEquals("despensa", parsed.location)
    }
}
