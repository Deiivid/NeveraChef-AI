package es.neverachefai.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors

@Composable
fun WelcomeScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeveraChefColors.AccentSoft)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("🥬 🍅 🥚", style = MaterialTheme.typography.headlineLarge)
            Text("NeveraChef-AI", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                "Cocina mejor con lo que ya tienes en casa. Sin login, local-first y pensada para familias.",
                style = MaterialTheme.typography.bodyLarge,
                color = NeveraChefColors.Muted,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onContinue,
            ) {
                Text("Empezar")
            }
            Text("Tus datos se guardan localmente en este dispositivo.", color = NeveraChefColors.Muted)
        }
    }
}
