package es.neverachefai.feature.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.feature.onboarding.ui.InitialPreferencesUi
import es.neverachefai.feature.onboarding.ui.MaxCookingTime
import es.neverachefai.feature.onboarding.ui.RecipeGoal

@Composable
fun SettingsRecipePreferencesScreen(
    state: InitialPreferencesUi,
    onPeopleMinusClick: () -> Unit,
    onPeoplePlusClick: () -> Unit,
    onChildrenChanged: (Boolean) -> Unit,
    onGoalSelected: (RecipeGoal) -> Unit,
    onRestrictionsClick: () -> Unit,
    onTimeSelected: (MaxCookingTime) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteAllDataClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Ajustes", color = NeveraChefColors.Ink, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text(
            "Cambia tus preferencias de recetas y controla los datos locales guardados en este móvil.",
            color = NeveraChefColors.Muted,
            fontSize = 13.sp,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.Soft, RoundedCornerShape(24.dp))
                .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(24.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Personas en casa", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier
                    .height(36.dp)
                    .background(Color.White, RoundedCornerShape(18.dp))
                    .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(18.dp))
                    .padding(horizontal = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(onClick = onPeopleMinusClick, color = Color.Transparent) { Text("-", fontWeight = FontWeight.Bold) }
                Text(state.peopleCount.toString(), fontWeight = FontWeight.Bold)
                Surface(onClick = onPeoplePlusClick, color = Color.Transparent) { Text("+", color = NeveraChefColors.Blue, fontWeight = FontWeight.Bold) }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Hay niños en casa", fontWeight = FontWeight.Bold)
            Switch(checked = state.hasChildren, onCheckedChange = onChildrenChanged)
        }

        Text("Objetivo principal", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            RecipeGoal.entries.take(2).forEach { goal ->
                FilterChip(
                    selected = goal == state.selectedGoal,
                    onClick = { onGoalSelected(goal) },
                    label = { Text(goal.label) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            RecipeGoal.entries.drop(2).forEach { goal ->
                FilterChip(
                    selected = goal == state.selectedGoal,
                    onClick = { onGoalSelected(goal) },
                    label = { Text(goal.label) },
                    modifier = Modifier.weight(1f),
                )
            }
        }

        Surface(
            onClick = onRestrictionsClick,
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, NeveraChefColors.Line),
        ) {
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                Text("RESTRICCIONES O ALERGIAS", color = NeveraChefColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(state.restrictions.ifBlank { "Sin restricciones" })
            }
        }

        Text("Tiempo habitual", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            MaxCookingTime.entries.forEach { time ->
                Surface(
                    onClick = { onTimeSelected(time) },
                    modifier = Modifier.weight(1f).height(38.dp),
                    color = if (state.selectedTime == time) NeveraChefColors.Blue else Color.White,
                    shape = RoundedCornerShape(19.dp),
                    border = if (state.selectedTime == time) null else BorderStroke(1.dp, NeveraChefColors.Line),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(time.label, color = if (state.selectedTime == time) Color.White else NeveraChefColors.Muted, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        Button(onClick = onDeleteAllDataClick, colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.ErrorSoft), modifier = Modifier.fillMaxWidth().height(46.dp)) {
            Text("Borrar todos mis datos", color = Color(0xFFC93A2F), fontWeight = FontWeight.Bold)
        }
        Button(onClick = onSaveClick, colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue), modifier = Modifier.fillMaxWidth().height(52.dp)) {
            Text("Guardar preferencias", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}
