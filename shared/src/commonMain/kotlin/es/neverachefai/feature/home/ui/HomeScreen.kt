package es.neverachefai.feature.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.ui.components.ScreenCard

@Composable
fun HomeScreen(
    onGoPantry: () -> Unit,
    onGoRecipes: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Hola", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "Tu panel rápido para cocinar con lo que tienes hoy.",
            color = NeveraChefColors.Muted,
            style = MaterialTheme.typography.bodyMedium,
        )
        ScreenCard(
            title = "Nunca más \"no sé qué cocinar\"",
            description = "Añade ingredientes y deja que NeveraChef-AI proponga recetas realistas.",
            backgroundColor = NeveraChefColors.AccentSoft,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(modifier = Modifier.weight(1f), onClick = onGoPantry) { Text("Ir a Nevera") }
            Button(modifier = Modifier.weight(1f), onClick = onGoRecipes) { Text("Ir a Recetas") }
        }
        ScreenCard(
            title = "Sugerencia de hoy",
            description = "Tortilla con tomate · 18 min · fácil",
            backgroundColor = NeveraChefColors.Soft,
        )
    }
}
