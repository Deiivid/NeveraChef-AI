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

@Composable
fun RecipeResultsScreen(onOpenDetail: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Recetas listas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("5 ideas usando tus alimentos confirmados.", color = NeveraChefColors.Muted)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip("Mejor match", true)
            FilterChip("< 25 min", false)
            FilterChip("Sin horno", false)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NeveraChefColors.Soft),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeveraChefColors.AccentSoft, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                ) {
                    Text("94% match", fontWeight = FontWeight.SemiBold)
                }
                Text("Tortilla verde con tomate", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("18 min · 7 ingredientes · usa huevos y espinacas", color = NeveraChefColors.Muted)
            }
        }

        SecondaryRecipeRow("Sopa rápida de verduras", "22 min · falta caldo")
        SecondaryRecipeRow("Ensalada templada", "12 min · sin compras")

        Text(
            "Error recuperable: conexión interrumpida. Reintentar",
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.ErrorSoft, RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = onOpenDetail) {
            Text("Ver detalle")
        }
    }
}

@Composable
private fun FilterChip(text: String, selected: Boolean) {
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

@Composable
private fun SecondaryRecipeRow(
    title: String,
    subtitle: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
        }
    }
}
