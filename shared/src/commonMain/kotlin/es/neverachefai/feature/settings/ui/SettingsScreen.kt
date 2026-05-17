package es.neverachefai.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.neverachefai.core.designsystem.NeveraChefColors

@Composable
fun SettingsScreen(
    cameraPermissionGranted: Boolean,
    microphonePermissionGranted: Boolean,
    onRequestCameraPermission: () -> Unit,
    onRequestMicrophonePermission: () -> Unit,
    onOpenOnboarding: () -> Unit,
    onReset: () -> Unit,
) {
    var expiryReminderEnabled by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Ajustes", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "Privacidad, permisos y datos guardados en este dispositivo.",
            color = NeveraChefColors.Muted,
            style = MaterialTheme.typography.bodyMedium,
        )

        LocalProcessingCard()

        SectionTitle("Permisos")
        PermissionRow(
            title = "Permiso de camara",
            description = "Para detectar alimentos por foto",
            checked = cameraPermissionGranted,
            onCheckedChange = { enabled ->
                if (enabled) onRequestCameraPermission()
            },
        )
        PermissionRow(
            title = "Permiso de microfono",
            description = "Para anadir alimentos hablando",
            checked = microphonePermissionGranted,
            onCheckedChange = { enabled ->
                if (enabled) onRequestMicrophonePermission()
            },
        )

        SectionTitle("Avisos")
        PermissionRow(
            title = "Avisos de caducidad",
            description = "Recordar 2 dias antes",
            checked = expiryReminderEnabled,
            onCheckedChange = { expiryReminderEnabled = it },
            backgroundColor = NeveraChefColors.WarningSoft,
            bordered = false,
        )

        SectionTitle("Flujo")
        NavigationRow(
            title = "Ver onboarding",
            description = "Revisar pantalla de bienvenida y explicacion inicial",
            action = "Abrir",
            onClick = onOpenOnboarding,
        )

        SectionTitle("Zona sensible")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = NeveraChefColors.ErrorSoft),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text("Borrar datos de NeveraChef", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("Elimina alimentos, recetas guardadas y preferencias.", style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
                }
                TextButton(onClick = { showDeleteDialog = true }) {
                    Text("Borrar")
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteDataDialog(
            onCancel = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                onReset()
            },
        )
    }
}

@Composable
private fun DeleteDataDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(28.dp))
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(NeveraChefColors.Soft, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text("🗑")
            }

            Text(
                "Borrar todos tus datos?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Se eliminaran alimentos, preferencias, avisos de caducidad y lista de compra guardados en este dispositivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = NeveraChefColors.Muted,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = onCancel,
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Text("Cancelar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onConfirm,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111216)),
                ) {
                    Text("Borrar datos", color = Color.White, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun LocalProcessingCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NeveraChefColors.AccentSoft),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "NC",
                modifier = Modifier
                    .background(NeveraChefColors.Blue, CircleShape)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text("Procesar en dispositivo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Nevera, preferencias y lista se guardan localmente.", style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
            }
            Text("ON", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = NeveraChefColors.Blue)
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    backgroundColor: Color = Color.White,
    bordered: Boolean = true,
) {
    val rowModifier = if (bordered) {
        Modifier
            .fillMaxWidth()
            .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(14.dp))
            .background(backgroundColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    }

    Row(
        modifier = rowModifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(description, style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeveraChefColors.Blue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = NeveraChefColors.Soft,
                uncheckedBorderColor = NeveraChefColors.Line,
            ),
        )
    }
}

@Composable
private fun NavigationRow(
    title: String,
    description: String,
    action: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(description, style = MaterialTheme.typography.bodySmall, color = NeveraChefColors.Muted)
            }
            TextButton(onClick = onClick) { Text(action) }
        }
    }
}
