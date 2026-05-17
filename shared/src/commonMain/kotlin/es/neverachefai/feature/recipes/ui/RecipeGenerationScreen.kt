package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.ui.components.ScreenCard

@Composable
fun RecipeGenerationScreen(onGenerate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Generar recetas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Ajusta preferencias y deja que la IA combine lo que tienes.", color = NeveraChefColors.Muted)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NeveraChefColors.AccentSoft),
            shape = RoundedCornerShape(14.dp),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text("Analizando combinaciones", fontWeight = FontWeight.SemiBold)
                Text("Estado de carga · 12 ingredientes evaluados", style = MaterialTheme.typography.bodySmall)
            }
        }

        Text("Objetivo", style = MaterialTheme.typography.labelLarge, color = NeveraChefColors.Muted)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            GoalChip("Rápido", true)
            GoalChip("Saludable", false)
            GoalChip("Ahorro", false)
        }

        ScreenCard(title = "Máximo 25 min", description = "ON")
        ScreenCard(title = "Sin gluten", description = "OFF")
        ScreenCard(title = "Estado vacío", description = "Añade al menos 3 alimentos para obtener recetas útiles.")

        Button(modifier = Modifier.fillMaxWidth(), onClick = onGenerate) {
            Text("Generar 5 recetas")
        }
    }
}

@Composable
private fun GoalChip(text: String, selected: Boolean) {
    val background = if (selected) NeveraChefColors.Blue else NeveraChefColors.Soft
    val foreground = if (selected) androidx.compose.ui.graphics.Color.White else NeveraChefColors.Ink
    Text(
        text = text,
        color = foreground,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(background, RoundedCornerShape(22.dp))
            .padding(horizontal = 14.dp, vertical = 9.dp),
    )
}
