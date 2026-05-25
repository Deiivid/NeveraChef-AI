package es.neverachefai.feature.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.preferences.AppPreferences

private const val KEY_EXPIRY_REMINDER_DAYS = "settings.expiry_reminder_days"

@Composable
fun SettingsScreen(
    cameraPermissionGranted: Boolean,
    microphonePermissionGranted: Boolean,
    onRequestCameraPermission: () -> Unit,
    onRequestMicrophonePermission: () -> Unit,
    onReset: () -> Unit,
) {
    var expiryReminderDays by remember { mutableStateOf(loadExpiryReminderDays()) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NeveraChefColors.Surface)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 22.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            PrivacyCard()

            SettingsSection(title = "Permisos") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PermissionCard(
                        title = "Permiso de cámara",
                        description = "Para escanear alimentos",
                        checked = cameraPermissionGranted,
                        onCheckedChange = { enabled ->
                            if (enabled && !cameraPermissionGranted) onRequestCameraPermission()
                        },
                    )
                    PermissionCard(
                        title = "Permiso de micrófono",
                        description = "Para añadir por voz",
                        checked = microphonePermissionGranted,
                        onCheckedChange = { enabled ->
                            if (enabled && !microphonePermissionGranted) onRequestMicrophonePermission()
                        },
                    )
                }
            }

            SettingsSection(title = "Avisos de caducidad") {
                ReminderCard(
                    selectedDays = expiryReminderDays,
                    onDaysSelected = {
                        expiryReminderDays = it
                        AppPreferences.setString(KEY_EXPIRY_REMINDER_DAYS, it.toString())
                    },
                )
            }

            SettingsSection(title = "Gestión de datos") {
                DataCard(onDeleteClick = { showDeleteDialog = true })
            }

            Spacer(modifier = Modifier.height(64.dp))
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
private fun PrivacyCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NeveraChefColors.PrimaryContainer, RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(NeveraChefColors.Primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("🛡", color = Color.White, fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Ajustes",
                color = Color.White,
                fontSize = 26.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Tus datos nunca salen de este móvil. Privacidad total por diseño.",
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 16.sp,
                lineHeight = 24.sp,
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .background(Color.White.copy(alpha = 0.38f), RoundedCornerShape(999.dp))
                .padding(horizontal = 13.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text("ON", color = NeveraChefColors.Primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SectionTitle(title)
        Box(modifier = Modifier.fillMaxWidth(), content = content)
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NeveraChefColors.SurfaceContainerLow, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = NeveraChefColors.OnSurface,
                fontSize = 16.sp,
                lineHeight = 20.sp,
            )
            Text(
                text = description,
                color = NeveraChefColors.OnSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = NeveraChefColors.Primary,
                uncheckedThumbColor = NeveraChefColors.Outline,
                uncheckedTrackColor = NeveraChefColors.SurfaceVariant,
                uncheckedBorderColor = Color.Transparent,
            ),
        )
    }
}

@Composable
private fun ReminderCard(
    selectedDays: Int,
    onDaysSelected: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NeveraChefColors.SurfaceContainerLow, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Días antes para recordar",
            color = NeveraChefColors.OnSurfaceVariant,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, NeveraChefColors.Outline, RoundedCornerShape(999.dp))
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOf(2, 3, 4, 5).forEachIndexed { index, days ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            color = if (selectedDays == days) NeveraChefColors.SecondaryContainer else Color.Transparent,
                        )
                        .clickable { onDaysSelected(days) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = days.toString(),
                        color = NeveraChefColors.OnSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (index < 3) {
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(NeveraChefColors.Outline),
                    )
                }
            }
        }
    }
}

@Composable
private fun DataCard(
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NeveraChefColors.SurfaceContainerLow, RoundedCornerShape(12.dp))
            .border(1.dp, NeveraChefColors.Error.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(NeveraChefColors.Error.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text("⌫", color = NeveraChefColors.Error, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "Borrar datos locales",
                color = NeveraChefColors.OnSurface,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Elimina permanentemente tus datos de este dispositivo. Esta acción no se puede deshacer.",
                color = NeveraChefColors.OnSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }
        TextButton(
            onClick = onDeleteClick,
            modifier = Modifier.defaultMinSize(minWidth = 0.dp, minHeight = 40.dp),
        ) {
            Text("Borrar", color = NeveraChefColors.Error, fontWeight = FontWeight.Bold)
        }
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
            Text(
                "Borrar todos los datos",
                color = NeveraChefColors.Ink,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Se eliminarán alimentos, preferencias y listas guardadas. Esta acción no se puede deshacer.",
                color = NeveraChefColors.Muted,
            )

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111216)),
            ) {
                Text("Borrar definitivamente", color = Color.White)
            }
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = NeveraChefColors.Primary,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        modifier = Modifier.padding(horizontal = 4.dp),
    )
}

private fun loadExpiryReminderDays(): Int {
    return AppPreferences.getString(KEY_EXPIRY_REMINDER_DAYS)
        ?.toIntOrNull()
        ?.coerceIn(2, 5)
        ?: 2
}

private val NeveraChefColors.Surface: Color
    get() = Color(0xFFFEF7FF)

private val NeveraChefColors.SurfaceContainerLow: Color
    get() = Color(0xFFF8F2F9)

private val NeveraChefColors.SurfaceVariant: Color
    get() = Color(0xFFE6E1E8)

private val NeveraChefColors.OnSurface: Color
    get() = Color(0xFF1D1B20)

private val NeveraChefColors.OnSurfaceVariant: Color
    get() = Color(0xFF424656)

private val NeveraChefColors.Primary: Color
    get() = Color(0xFF004BCA)

private val NeveraChefColors.PrimaryContainer: Color
    get() = Color(0xFF0061FF)

private val NeveraChefColors.SecondaryContainer: Color
    get() = Color(0xFFDEE2ED)

private val NeveraChefColors.Outline: Color
    get() = Color(0xFF737687)

private val NeveraChefColors.Error: Color
    get() = Color(0xFFBA1A1A)
