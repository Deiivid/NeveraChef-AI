package es.neverachefai.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors

@Composable
fun LoadingState(message: String = "Cargando...") {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(message, color = NeveraChefColors.Muted, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    ctaLabel: String? = null,
    onCtaClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(description, color = NeveraChefColors.Muted, style = MaterialTheme.typography.bodyMedium)
        if (ctaLabel != null && onCtaClick != null) {
            Button(onClick = onCtaClick) { Text(ctaLabel) }
        }
    }
}

@Composable
fun ErrorState(
    title: String = "Algo ha fallado",
    description: String = "No se ha podido completar la acción.",
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(description, color = NeveraChefColors.Muted, style = MaterialTheme.typography.bodyMedium)
        if (onRetry != null) {
            Button(onClick = onRetry) { Text("Reintentar") }
        }
    }
}
