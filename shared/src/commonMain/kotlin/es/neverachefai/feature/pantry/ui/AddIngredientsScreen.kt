package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun AddIngredientsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Añadir alimentos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        ScreenCard(
            title = "Entrada manual (MVP)",
            description = "Tomates cherry · 250g · Nevera",
            backgroundColor = NeveraChefColors.AccentSoft,
        )
        ScreenCard(title = "Voz", description = "Pendiente Sprint 5")
        Button(modifier = Modifier.fillMaxWidth(), onClick = onBack) { Text("Volver") }
    }
}
