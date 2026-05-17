package es.neverachefai.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object NeveraChefColors {
    val Blue = Color(0xFF0066FF)
    val Ink = Color(0xFF1A1A1A)
    val Muted = Color(0xFF666666)
    val Line = Color(0xFFE6E8EC)
    val Soft = Color(0xFFF6F8FB)
    val AccentSoft = Color(0xFFEAF2FF)
    val WarningSoft = Color(0xFFFFF4D6)
    val ErrorSoft = Color(0xFFFFE7E2)
    val SuccessSoft = Color(0xFFEAF7EF)
}

private val LightScheme = lightColorScheme(
    primary = NeveraChefColors.Blue,
    onPrimary = Color.White,
    background = Color.White,
    surface = NeveraChefColors.Soft,
    onSurface = NeveraChefColors.Ink,
    outline = NeveraChefColors.Line,
)

private val DarkScheme = darkColorScheme(
    primary = NeveraChefColors.Blue,
    onPrimary = Color.White,
    background = Color(0xFF0F1115),
    surface = Color(0xFF171A20),
    onSurface = Color(0xFFECEFF4),
    outline = Color(0xFF303541),
)

@Composable
fun NeveraChefTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = Typography(),
        content = content,
    )
}
