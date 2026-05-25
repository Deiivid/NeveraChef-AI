package es.neverachefai.feature.pantry.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ExpirationDateHelperTest {

    @Test
    fun `returns caduca hoy when same day`() {
        val text = getExpirationText("2026-05-21", todayIso = "2026-05-21")
        assertEquals("Caduca hoy", text)
    }

    @Test
    fun `returns caduca manana when next day`() {
        val text = getExpirationText("2026-05-22", todayIso = "2026-05-21")
        assertEquals("Caduca mañana", text)
    }

    @Test
    fun `returns caduca en x dias`() {
        val text = getExpirationText("2026-05-23", todayIso = "2026-05-21")
        assertEquals("Caduca en 2 días", text)
    }

    @Test
    fun `returns caducado ayer`() {
        val text = getExpirationText("2026-05-20", todayIso = "2026-05-21")
        assertEquals("Caducado ayer", text)
    }

    @Test
    fun `returns caducado hace x dias`() {
        val text = getExpirationText("2026-05-18", todayIso = "2026-05-21")
        assertEquals("Caducado hace 3 días", text)
    }

    @Test
    fun `returns null for null date`() {
        assertNull(getExpirationText(null, todayIso = "2026-05-21"))
    }

    @Test
    fun `returns null for invalid date`() {
        assertNull(getExpirationText("2026-13-99", todayIso = "2026-05-21"))
    }
}
