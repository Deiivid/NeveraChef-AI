package es.neverachefai.feature.pantry.ui

enum class ExpirationPriority {
    EXPIRED,
    SOON,
    NORMAL,
    UNKNOWN,
}

data class SimpleDate(val year: Int, val month: Int, val day: Int)

fun getExpirationText(
    expiryDateIso: String?,
    todayIso: String = platformTodayIsoDate(),
): String? {
    val expirationDate = parseIsoDate(expiryDateIso) ?: return null
    val today = parseIsoDate(todayIso) ?: return null
    val days = daysBetween(today, expirationDate)

    return when {
        days < -1 -> "Caducado hace ${-days} días"
        days == -1 -> "Caducado ayer"
        days == 0 -> "Caduca hoy"
        days == 1 -> "Caduca mañana"
        else -> "Caduca en $days días"
    }
}

fun expirationPriority(
    expiryDateIso: String?,
    todayIso: String = platformTodayIsoDate(),
): ExpirationPriority {
    val expirationDate = parseIsoDate(expiryDateIso) ?: return ExpirationPriority.UNKNOWN
    val today = parseIsoDate(todayIso) ?: return ExpirationPriority.UNKNOWN
    val days = daysBetween(today, expirationDate)
    return when {
        days < 0 -> ExpirationPriority.EXPIRED
        days <= 1 -> ExpirationPriority.SOON
        else -> ExpirationPriority.NORMAL
    }
}

fun parseIsoDate(value: String?): SimpleDate? {
    val raw = value?.trim().orEmpty()
    if (raw.length != 10 || raw[4] != '-' || raw[7] != '-') return null
    val year = raw.substring(0, 4).toIntOrNull() ?: return null
    val month = raw.substring(5, 7).toIntOrNull() ?: return null
    val day = raw.substring(8, 10).toIntOrNull() ?: return null
    if (!isValidDate(year, month, day)) return null
    return SimpleDate(year, month, day)
}

fun formatToIsoDate(date: SimpleDate): String {
    return "${date.year.toString().padStart(4, '0')}-${date.month.toString().padStart(2, '0')}-${date.day.toString().padStart(2, '0')}"
}

fun formatDisplayDate(isoDate: String?): String? {
    val parsed = parseIsoDate(isoDate) ?: return null
    return "${parsed.day.toString().padStart(2, '0')}/${parsed.month.toString().padStart(2, '0')}/${parsed.year}"
}

fun isoDateToUtcMillis(isoDate: String?): Long? {
    val parsed = parseIsoDate(isoDate) ?: return null
    return toEpochDay(parsed) * MILLIS_PER_DAY
}

fun utcMillisToIsoDate(utcMillis: Long): String {
    val epochDay = floorDiv(utcMillis, MILLIS_PER_DAY)
    return formatToIsoDate(civilFromDays(epochDay))
}

fun isValidDate(year: Int, month: Int, day: Int): Boolean {
    if (month !in 1..12) return false
    if (day < 1) return false
    val daysInMonth = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> return false
    }
    return day <= daysInMonth
}

fun daysBetween(start: SimpleDate, end: SimpleDate): Int {
    return (toEpochDay(end) - toEpochDay(start)).toInt()
}

private fun isLeapYear(year: Int): Boolean {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

private fun toEpochDay(date: SimpleDate): Long {
    return daysFromCivil(date.year, date.month, date.day)
}

private fun daysFromCivil(year: Int, month: Int, day: Int): Long {
    var y = year.toLong()
    var m = month.toLong()
    val d = day.toLong()
    y -= if (m <= 2L) 1 else 0
    val era = if (y >= 0) y / 400 else (y - 399) / 400
    val yoe = y - era * 400
    m = m + if (m > 2) -3 else 9
    val doy = (153 * m + 2) / 5 + d - 1
    val doe = yoe * 365 + yoe / 4 - yoe / 100 + doy
    return era * 146097 + doe - 719468
}

private fun civilFromDays(zInput: Long): SimpleDate {
    val z = zInput + 719468
    val era = if (z >= 0) z / 146097 else (z - 146096) / 146097
    val doe = z - era * 146097
    val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
    var y = yoe + era * 400
    val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
    val mp = (5 * doy + 2) / 153
    val d = doy - (153 * mp + 2) / 5 + 1
    val m = mp + if (mp < 10) 3 else -9
    y += if (m <= 2) 1 else 0
    return SimpleDate(y.toInt(), m.toInt(), d.toInt())
}

private fun floorDiv(value: Long, divisor: Long): Long {
    val quotient = value / divisor
    val remainder = value % divisor
    return if (remainder != 0L && (remainder > 0) != (divisor > 0)) quotient - 1 else quotient
}

private const val MILLIS_PER_DAY = 86_400_000L

expect fun platformTodayIsoDate(): String
