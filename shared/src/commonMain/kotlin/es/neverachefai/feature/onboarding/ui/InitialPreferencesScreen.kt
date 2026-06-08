package es.neverachefai.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_plus
import org.jetbrains.compose.resources.painterResource

enum class RecipeGoal(val label: String) {
    HEALTHY("Saludable"),
    SAVE_MONEY("Ahorrar"),
    FAST("Rapidas"),
    PROTEIN("Proteina"),
}

enum class MaxCookingTime(val label: String) {
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY_FIVE("45"),
    NO_LIMIT("Sin"),
}

data class InitialPreferencesUi(
    val peopleCount: Int,
    val hasChildren: Boolean,
    val selectedGoal: RecipeGoal,
    val restrictions: String,
    val selectedTime: MaxCookingTime,
)

@Composable
fun InitialPreferencesScreen(onSave: () -> Unit) {
    var state by remember {
        mutableStateOf(
            InitialPreferencesUi(
                peopleCount = 2,
                hasChildren = true,
                selectedGoal = RecipeGoal.HEALTHY,
                restrictions = "",
                selectedTime = MaxCookingTime.THIRTY,
            ),
        )
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val compactHeight = maxHeight < 760.dp
        val horizontalPadding = if (maxWidth < 380.dp) 18.dp else 20.dp
        val spacing = if (compactHeight) 10.dp else 13.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = horizontalPadding, vertical = if (compactHeight) 14.dp else 18.dp),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) {
                Text(
                    text = "Configura NeveraChef",
                    color = NeveraChefColors.Ink,
                    fontSize = if (compactHeight) 26.sp else 28.sp,
                    lineHeight = if (compactHeight) 28.sp else 31.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Solo necesitamos preferencias básicas para proponer recetas mejores. Puedes cambiarlo luego en Ajustes.",
                    color = NeveraChefColors.Muted,
                    fontSize = if (compactHeight) 13.sp else 14.sp,
                    lineHeight = if (compactHeight) 17.sp else 19.sp,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeveraChefColors.SuccessSoft, RoundedCornerShape(22.dp))
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_check_square),
                        contentDescription = null,
                        tint = Color(0xFF2F8F5B),
                    )
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text("IA bajo tu control", color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "La IA sugiere, pero tú revisas antes de guardar. Tus datos se quedan en este móvil.",
                            color = NeveraChefColors.Muted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeveraChefColors.Soft, RoundedCornerShape(24.dp))
                        .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(24.dp))
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                ) {
                    HouseholdRow(
                        peopleCount = state.peopleCount,
                        onMinus = { if (state.peopleCount > 1) state = state.copy(peopleCount = state.peopleCount - 1) },
                        onPlus = { state = state.copy(peopleCount = (state.peopleCount + 1).coerceAtMost(12)) },
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("Hay niños en casa", color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Recetas más familiares", color = NeveraChefColors.Muted, fontSize = 13.sp)
                        }
                        Switch(
                            checked = state.hasChildren,
                            onCheckedChange = { state = state.copy(hasChildren = it) },
                        )
                    }
                }

                Text("Objetivo principal", color = NeveraChefColors.Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Column(verticalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
                    RecipeGoal.entries.chunked(2).forEach { row ->
                        Row(horizontalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
                            row.forEach { goal ->
                                PreferenceChip(
                                    text = goal.label,
                                    selected = state.selectedGoal == goal,
                                    modifier = Modifier.weight(1f),
                                    onClick = { state = state.copy(selectedGoal = goal) },
                                )
                            }
                        }
                    }
                }

                RestrictionsCard(text = state.restrictions.ifBlank { "Sin restricciones" })

                Text("Tiempo habitual", color = NeveraChefColors.Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    MaxCookingTime.entries.forEach { time ->
                        TimeChip(
                            label = time.label,
                            selected = state.selectedTime == time,
                            modifier = Modifier.weight(1f),
                            onClick = { state = state.copy(selectedTime = time) },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(if (compactHeight) 12.dp else 16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
            ) {
                Text("Guardar y continuar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun HouseholdRow(
    peopleCount: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text("Personas en casa", color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("Para ajustar raciones", color = NeveraChefColors.Muted, fontSize = 13.sp)
        }
        Row(
            modifier = Modifier
                .height(44.dp)
                .background(Color.White, RoundedCornerShape(22.dp))
                .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(22.dp))
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CounterButton(text = "-", color = NeveraChefColors.Muted, onClick = onMinus)
            Text(peopleCount.toString(), color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            CounterButton(text = "+", color = NeveraChefColors.Blue, onClick = onPlus)
        }
    }
}

@Composable
private fun CounterButton(
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = CircleShape,
        modifier = Modifier.size(28.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PreferenceChip(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(41.dp),
        color = if (selected) NeveraChefColors.AccentSoft else Color.White,
        shape = RoundedCornerShape(10.dp),
        border = if (selected) null else BorderStroke(1.dp, NeveraChefColors.Line),
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = text,
                color = NeveraChefColors.Ink,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun RestrictionsCard(text: String) {
    Surface(
        onClick = {},
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, NeveraChefColors.Line),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
                Text("RESTRICCIONES O ALERGIAS", color = NeveraChefColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(text, color = NeveraChefColors.Ink, fontSize = 15.sp)
            }
            Icon(
                painter = painterResource(Res.drawable.ic_nc_plus),
                contentDescription = null,
                tint = NeveraChefColors.Muted,
                modifier = Modifier.size(17.dp),
            )
        }
    }
}

@Composable
private fun TimeChip(
    label: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        color = if (selected) NeveraChefColors.Blue else Color.White,
        shape = RoundedCornerShape(22.dp),
        border = if (selected) null else BorderStroke(1.dp, NeveraChefColors.Line),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = if (selected) Color.White else NeveraChefColors.Muted,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
