package es.neverachefai.feature.settings.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_camera
import neverachefai.shared.generated.resources.ic_nc_microphone
import neverachefai.shared.generated.resources.ic_nc_trash
import neverachefai.shared.generated.resources.ref_settings_calendar_clock
import neverachefai.shared.generated.resources.ref_settings_bell
import neverachefai.shared.generated.resources.ref_settings_header_premium_subject
import neverachefai.shared.generated.resources.ref_settings_privacy_lock
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

@Composable
fun SettingsScreen(
    cameraPermissionGranted: Boolean,
    microphonePermissionGranted: Boolean,
    expiryReminderDays: Int,
    onExpiryReminderDaysChange: (Int) -> Unit,
    onRequestCameraPermission: () -> Unit,
    onRequestMicrophonePermission: () -> Unit,
    onReset: () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val reminderOptions = remember { (2..10).toList() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsColors.Background)
            .statusBarsPadding()
            .padding(horizontal = 16.dp),
    ) {
        HeaderSection()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            PrivacyCard()
            Spacer(Modifier.height(10.dp))
            PermissionsCard(
                cameraPermissionGranted = cameraPermissionGranted,
                microphonePermissionGranted = microphonePermissionGranted,
                onRequestCameraPermission = onRequestCameraPermission,
                onRequestMicrophonePermission = onRequestMicrophonePermission,
            )
            Spacer(Modifier.height(10.dp))

            ExpiryReminderCard(
                selectedDays = expiryReminderDays,
                daysOptions = reminderOptions,
                onDaysSelected = onExpiryReminderDaysChange,
            )
            Spacer(Modifier.height(10.dp))

            DataCard(onDeleteClick = { showDeleteDialog = true })

            Spacer(modifier = Modifier.height(18.dp))
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
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp),
    ) {
        SettingsHeroIllustration(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 18.dp, y = (-14).dp)
                .size(width = 260.dp, height = 158.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 4.dp, top = 8.dp)
                .width(195.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "Ajustes",
                color = SettingsColors.GreenDark,
                fontSize = 36.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.8).sp,
            )
            PrivacyPill()
        }
    }
}

@Composable
private fun PrivacyPill() {
    Row(
        modifier = Modifier
            .background(SettingsColors.SuccessSoft, RoundedCornerShape(18.dp))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ShieldCheckIcon(
            modifier = Modifier.size(16.dp),
            color = SettingsColors.GreenTrack,
        )
        Text(
            text = "Privacidad primero",
            color = SettingsColors.GreenDark,
            fontSize = 11.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun PrivacyCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = SettingsColors.Card,
        border = androidx.compose.foundation.BorderStroke(1.dp, SettingsColors.Line),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(SettingsColors.IconTile, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                SettingsIconImage(
                    drawable = Res.drawable.ref_settings_privacy_lock,
                    modifier = Modifier.size(38.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "Privacidad",
                    color = SettingsColors.GreenDark,
                    fontSize = 18.sp,
                    lineHeight = 21.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = "Tus datos nunca salen de este móvil",
                    color = SettingsColors.Subtitle,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun PermissionsCard(
    cameraPermissionGranted: Boolean,
    microphonePermissionGranted: Boolean,
    onRequestCameraPermission: () -> Unit,
    onRequestMicrophonePermission: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = SettingsColors.SuccessPanel,
        border = androidx.compose.foundation.BorderStroke(1.dp, SettingsColors.Line),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Permisos",
                        color = SettingsColors.GreenDark,
                        fontSize = 18.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Cámara y micrófono",
                        color = SettingsColors.Subtitle,
                        fontSize = 11.sp,
                        lineHeight = 13.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(SettingsColors.SuccessSoft, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    ShieldCheckIcon(
                        modifier = Modifier.size(16.dp),
                        color = SettingsColors.GreenTrack,
                    )
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = SettingsColors.Card,
            ) {
                Column {
                    PermissionRow(
                        title = "Permiso de cámara",
                        description = "Para escanear alimentos",
                        granted = cameraPermissionGranted,
                        iconRes = Res.drawable.ic_nc_camera,
                        onClick = onRequestCameraPermission,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .height(1.dp)
                            .background(SettingsColors.Divider),
                    )
                    PermissionRow(
                        title = "Permiso de micrófono",
                        description = "Para añadir por voz",
                        granted = microphonePermissionGranted,
                        iconRes = Res.drawable.ic_nc_microphone,
                        onClick = onRequestMicrophonePermission,
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    description: String,
    granted: Boolean,
    iconRes: DrawableResource,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(SettingsColors.IconTile, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = SettingsColors.GreenDark,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    color = SettingsColors.GreenDark,
                    fontSize = 14.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
                Text(
                    text = description,
                    color = SettingsColors.Subtitle,
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    maxLines = 1,
                )
            }

            Switch(
                checked = granted,
                onCheckedChange = { onClick() },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = SettingsColors.GreenTrack,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = SettingsColors.SwitchOffTrack,
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun ExpiryReminderCard(
    selectedDays: Int,
    daysOptions: List<Int>,
    onDaysSelected: (Int) -> Unit,
) {
    val minDays = daysOptions.first()
    val maxDays = daysOptions.last()
    val clampedDays = selectedDays.coerceIn(minDays, maxDays)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = SettingsColors.ExpiryPanel,
        border = androidx.compose.foundation.BorderStroke(1.dp, SettingsColors.ExpiryLine),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SettingsColors.WarningSoft, RoundedCornerShape(15.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    SettingsIconImage(
                        drawable = Res.drawable.ref_settings_bell,
                        modifier = Modifier.size(26.dp),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "Caducidad",
                        color = SettingsColors.GreenDark,
                        fontSize = 18.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Aviso $clampedDays días antes de caducar",
                        color = Color.White,
                        fontSize = 11.sp,
                        lineHeight = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        modifier = Modifier
                            .background(SettingsColors.GreenTrack, RoundedCornerShape(16.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }

                SettingsIconImage(
                    drawable = Res.drawable.ref_settings_calendar_clock,
                    modifier = Modifier.size(width = 68.dp, height = 54.dp),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StepButton(
                    text = "-",
                    enabled = clampedDays > minDays,
                    onClick = { onDaysSelected((clampedDays - 1).coerceAtLeast(minDays)) },
                )
                ReminderSlider(
                    selectedDays = clampedDays,
                    minDays = minDays,
                    maxDays = maxDays,
                    onDaysSelected = onDaysSelected,
                    modifier = Modifier.weight(1f),
                )
                StepButton(
                    text = "+",
                    enabled = clampedDays < maxDays,
                    onClick = { onDaysSelected((clampedDays + 1).coerceAtMost(maxDays)) },
                )
            }
        }
    }
}

@Composable
private fun ReminderSlider(
    selectedDays: Int,
    minDays: Int,
    maxDays: Int,
    onDaysSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val range = (maxDays - minDays).coerceAtLeast(1)

    fun dayForX(x: Float, width: Float): Int {
        val fraction = (x / width.coerceAtLeast(1f)).coerceIn(0f, 1f)
        return (minDays + (fraction * range).roundToInt()).coerceIn(minDays, maxDays)
    }

    Box(
        modifier = modifier
            .height(36.dp)
            .pointerInput(minDays, maxDays) {
                detectDragGestures(
                    onDragStart = { offset ->
                        onDaysSelected(dayForX(offset.x, size.width.toFloat()))
                    },
                    onDrag = { change, _ ->
                        onDaysSelected(dayForX(change.position.x, size.width.toFloat()))
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val trackHeight = 20.dp.toPx()
            val thumbWidth = 4.dp.toPx()
            val thumbHeight = 30.dp.toPx()
            val centerY = size.height / 2f
            val fraction = (selectedDays - minDays).toFloat() / range.toFloat()
            val thumbX = size.width * fraction
            val corner = CornerRadius(trackHeight / 2f, trackHeight / 2f)

            drawRoundRect(
                color = SettingsColors.WarningSoft,
                topLeft = Offset(0f, centerY - trackHeight / 2f),
                size = Size(size.width, trackHeight),
                cornerRadius = corner,
            )
            drawRoundRect(
                color = SettingsColors.GreenTrack,
                topLeft = Offset(0f, centerY - trackHeight / 2f),
                size = Size(thumbX.coerceAtLeast(trackHeight), trackHeight),
                cornerRadius = corner,
            )
            for (step in 1 until range) {
                val tickX = size.width * step / range.toFloat()
                drawCircle(
                    color = if (tickX <= thumbX) SettingsColors.GreenDark.copy(alpha = 0.25f) else SettingsColors.ExpiryLine,
                    radius = 2.dp.toPx(),
                    center = Offset(tickX, centerY),
                )
            }
            drawRoundRect(
                color = SettingsColors.GreenTrack,
                topLeft = Offset(thumbX - thumbWidth / 2f, centerY - thumbHeight / 2f),
                size = Size(thumbWidth, thumbHeight),
                cornerRadius = CornerRadius(thumbWidth / 2f, thumbWidth / 2f),
            )
        }
        Text(
            text = "$minDays",
            color = if (selectedDays > minDays) Color.White else SettingsColors.GreenDark,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 12.dp),
        )
        Text(
            text = "$maxDays",
            color = if (selectedDays == maxDays) Color.White else SettingsColors.GreenDark,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 10.dp),
        )
    }
}

@Composable
private fun StepButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(32.dp),
        shape = CircleShape,
        color = if (enabled) SettingsColors.GreenTrack else SettingsColors.SuccessSoft,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (enabled) Color.White else SettingsColors.GreenDark.copy(alpha = 0.35f),
                fontSize = 18.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DataCard(
    onDeleteClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = SettingsColors.DangerPanel,
        border = androidx.compose.foundation.BorderStroke(1.dp, SettingsColors.DangerLine),
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SettingsColors.ErrorSoft, RoundedCornerShape(17.dp)),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(Res.drawable.ic_nc_trash),
                    contentDescription = null,
                    tint = SettingsColors.Error,
                    modifier = Modifier.size(22.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "Datos locales",
                    color = SettingsColors.GreenDark,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = "Borrar datos locales",
                    color = SettingsColors.Error,
                    fontSize = 13.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                )
            }

            Surface(
                onClick = onDeleteClick,
                shape = RoundedCornerShape(18.dp),
                color = SettingsColors.Error,
                modifier = Modifier.size(width = 84.dp, height = 44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Borrar",
                        color = Color.White,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteDataDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(30.dp),
            color = SettingsColors.Card,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .background(SettingsColors.ErrorSoft, RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(Res.drawable.ic_nc_trash),
                        contentDescription = null,
                        tint = SettingsColors.Error,
                        modifier = Modifier.size(28.dp),
                    )
                }
                Text(
                    text = "¿Borrar datos locales?",
                    color = SettingsColors.GreenDark,
                    fontSize = 22.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Se eliminarán alimentos, preferencias y listas guardadas en este dispositivo. Esta acción no se puede deshacer.",
                    color = SettingsColors.Subtitle,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                )

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SettingsColors.Error,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Sí, borrar todo", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SettingsHeroIllustration(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.ref_settings_header_premium_subject),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun SettingsIconImage(
    drawable: DrawableResource,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(drawable),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

@Composable
private fun MiniBellIcon(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = w * 0.085f
        drawArc(
            color = color,
            startAngle = 205f,
            sweepAngle = 130f,
            useCenter = false,
            topLeft = Offset(w * 0.23f, h * 0.18f),
            size = androidx.compose.ui.geometry.Size(w * 0.54f, h * 0.48f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round),
        )
        drawLine(color, Offset(w * 0.26f, h * 0.55f), Offset(w * 0.26f, h * 0.75f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.74f, h * 0.55f), Offset(w * 0.74f, h * 0.75f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.21f, h * 0.75f), Offset(w * 0.79f, h * 0.75f), stroke, StrokeCap.Round)
        drawCircle(color, radius = stroke * 0.55f, center = Offset(w * 0.5f, h * 0.86f))
        drawLine(color, Offset(w * 0.23f, h * 0.18f), Offset(w * 0.13f, h * 0.08f), stroke, StrokeCap.Round)
        drawLine(color, Offset(w * 0.77f, h * 0.18f), Offset(w * 0.87f, h * 0.08f), stroke, StrokeCap.Round)
    }
}

@Composable
private fun ShieldCheckIcon(
    modifier: Modifier = Modifier,
    color: Color,
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val shield = Path().apply {
            moveTo(w * 0.5f, h * 0.06f)
            cubicTo(w * 0.72f, h * 0.15f, w * 0.86f, h * 0.17f, w * 0.88f, h * 0.18f)
            cubicTo(w * 0.9f, h * 0.58f, w * 0.72f, h * 0.82f, w * 0.5f, h * 0.94f)
            cubicTo(w * 0.28f, h * 0.82f, w * 0.1f, h * 0.58f, w * 0.12f, h * 0.18f)
            cubicTo(w * 0.14f, h * 0.17f, w * 0.28f, h * 0.15f, w * 0.5f, h * 0.06f)
            close()
        }
        drawPath(shield, color)
        drawLine(
            color = Color.White,
            start = Offset(w * 0.33f, h * 0.52f),
            end = Offset(w * 0.45f, h * 0.64f),
            strokeWidth = w * 0.09f,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = Color.White,
            start = Offset(w * 0.45f, h * 0.64f),
            end = Offset(w * 0.68f, h * 0.4f),
            strokeWidth = w * 0.09f,
            cap = StrokeCap.Round,
        )
    }
}

private object SettingsColors {
    val Background = Color.White
    val Card = Color(0xFFFFFFFF)
    val SuccessPanel = Color(0xFFF4FAF5)
    val ExpiryPanel = Color(0xFFFFFBF0)
    val DangerPanel = Color(0xFFFFF7F7)
    val GreenDark = Color(0xFF0B4F3F)
    val Subtitle = Color(0xFF59606E)
    val Line = Color(0xFFE8E3D8)
    val ExpiryLine = Color(0xFFF2DEB0)
    val DangerLine = Color(0xFFF2D0CD)
    val Divider = Color(0xFFF1F2EE)
    val IconTile = Color(0xFFEAF3E8)
    val SuccessSoft = Color(0xFFEAF3E8)
    val SuccessSoftStrong = Color(0xFFCFE4D1)
    val WarningSoft = Color(0xFFFFECB8)
    val ErrorSoft = Color(0xFFFCE8E5)
    val Error = Color(0xFFD91F1F)
    val GreenTrack = Color(0xFF1E7157)
    val SwitchOffTrack = Color(0xFFD8D8D8)
}
