package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.ui.components.ScreenCard

@Composable
fun IngredientReviewScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Revisar alimentos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("Confirma los cambios antes de cocinar.", color = NeveraChefColors.Muted)

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = NeveraChefColors.WarningSoft,
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                "2 elementos necesitan revisión manual",
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Chip("Revisados 8", active = true)
            Chip("Pendientes 2", active = false)
        }

        IngredientRow(
            title = "Espinacas",
            subtitle = "Confirmado · 180 g",
            tone = NeveraChefColors.Soft,
        )
        IngredientRow(
            title = "Tomate o pimiento",
            subtitle = "Revisa el nombre antes de guardar",
            tone = NeveraChefColors.ErrorSoft,
        )
        IngredientRow(
            title = "Huevos",
            subtitle = "6 unidades · caducan pronto",
            tone = NeveraChefColors.Soft,
        )

        ScreenCard(
            title = "Estado vacío",
            description = "Añade alimentos manualmente o por voz.",
        )

        Spacer(modifier = Modifier.height(4.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = onBack) {
            Text("Confirmar y generar recetas")
        }
    }
}

@Composable
private fun Chip(text: String, active: Boolean) {
    val background = if (active) NeveraChefColors.Blue else NeveraChefColors.Soft
    val foreground = if (active) androidx.compose.ui.graphics.Color.White else NeveraChefColors.Ink
    Text(
        text = text,
        color = foreground,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(background, RoundedCornerShape(24.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

@Composable
private fun IngredientRow(
    title: String,
    subtitle: String,
    tone: androidx.compose.ui.graphics.Color,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = tone),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
        }
    }
}
