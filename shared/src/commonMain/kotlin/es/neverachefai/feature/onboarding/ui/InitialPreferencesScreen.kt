package es.neverachefai.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Configura NeveraChef",
            color = NeveraChefColors.Ink,
            fontSize = 27.sp,
            lineHeight = 29.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Solo necesitamos preferencias basicas para proponer recetas mejores. Puedes cambiarlo luego en Ajustes.",
            color = NeveraChefColors.Muted,
            fontSize = 13.sp,
            lineHeight = 17.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.SuccessSoft, RoundedCornerShape(20.dp))
                .padding(13.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(Res.drawable.ic_nc_check_square),
                contentDescription = null,
                tint = Color(0xFF2F8F5B),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("IA bajo tu control", color = NeveraChefColors.Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(
                    "La IA sugiere, pero tu revisas antes de guardar. Tus datos se quedan en este movil.",
                    color = NeveraChefColors.Muted,
                    fontSize = 12.sp,
                    lineHeight = 15.sp,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.Soft, RoundedCornerShape(24.dp))
                .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(24.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Personas en casa", color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Para ajustar raciones", color = NeveraChefColors.Muted, fontSize = 12.sp)
                }
                Row(
                    modifier = Modifier
                        .height(38.dp)
                        .background(Color.White, RoundedCornerShape(19.dp))
                        .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(19.dp))
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(onClick = { if (state.peopleCount > 1) state = state.copy(peopleCount = state.peopleCount - 1) }, color = Color.Transparent) {
                        Text("-", color = NeveraChefColors.Muted, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(state.peopleCount.toString(), color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Surface(onClick = { state = state.copy(peopleCount = state.peopleCount + 1) }, color = Color.Transparent) {
                        Text("+", color = NeveraChefColors.Blue, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Hay ninos en casa", color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text("Recetas mas familiares", color = NeveraChefColors.Muted, fontSize = 12.sp)
                }
                Switch(
                    checked = state.hasChildren,
                    onCheckedChange = { state = state.copy(hasChildren = it) },
                )
            }
        }

        Text("Objetivo principal", color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            RecipeGoal.entries.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { goal ->
                        FilterChip(
                            selected = state.selectedGoal == goal,
                            onClick = { state = state.copy(selectedGoal = goal) },
                            label = { Text(goal.label) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }

        Surface(
            onClick = {},
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, NeveraChefColors.Line),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text("RESTRICCIONES O ALERGIAS", color = NeveraChefColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(state.restrictions.ifBlank { "Sin restricciones" }, color = NeveraChefColors.Ink, fontSize = 14.sp)
            }
        }

        Text("Tiempo habitual", color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            MaxCookingTime.entries.forEach { time ->
                Surface(
                    onClick = { state = state.copy(selectedTime = time) },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    color = if (state.selectedTime == time) NeveraChefColors.Blue else Color.White,
                    shape = RoundedCornerShape(20.dp),
                    border = if (state.selectedTime == time) null else BorderStroke(1.dp, NeveraChefColors.Line),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = time.label,
                            color = if (state.selectedTime == time) Color.White else NeveraChefColors.Muted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
        ) {
            Text("Guardar y continuar", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
