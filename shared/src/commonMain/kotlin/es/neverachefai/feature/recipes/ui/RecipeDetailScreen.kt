package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.ui.components.ScreenCard

@Composable
fun RecipeDetailScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        TextButton(onClick = onBack) { Text("← Volver") }

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.AccentSoft, RoundedCornerShape(18.dp))
                .padding(20.dp),
        ) {
            Text("IA · 94% match", fontWeight = FontWeight.SemiBold)
        }

        Text("Tortilla verde con tomate", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetaPill("18 min")
            MetaPill("2 raciones")
            MetaPill("0 faltan")
        }

        ScreenCard(title = "Ingredientes detectados", description = "Espinacas · Huevos · Tomate")
        ScreenCard(title = "Paso 1", description = "Saltea espinacas y tomate 4 minutos.")
        ScreenCard(title = "Paso 2", description = "Añade huevos batidos y cocina a fuego bajo.")
        ScreenCard(title = "Sin sustituciones pendientes", description = "Receta lista para cocinar.")

        Button(modifier = Modifier.fillMaxWidth(), onClick = onBack) {
            Text("Empezar cocina guiada")
        }
    }
}

@Composable
private fun MetaPill(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .background(NeveraChefColors.Soft, RoundedCornerShape(22.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = NeveraChefColors.Ink,
    )
}
