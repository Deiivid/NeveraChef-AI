package es.neverachefai.feature.pantry.ui

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_scan
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodDetailScreen(
    food: PantryFoodUi?,
    onBack: () -> Unit,
    onGenerateRecipe: () -> Unit,
) {
    if (food == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("No hay alimento seleccionado", color = NeveraChefColors.Ink, fontWeight = FontWeight.Bold)
            Button(onClick = onBack) { Text("Volver") }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                onClick = onBack,
                color = NeveraChefColors.Soft,
                shape = CircleShape,
                border = BorderStroke(1.dp, NeveraChefColors.Line),
                modifier = Modifier.size(42.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("‹", color = NeveraChefColors.Ink, fontSize = 28.sp, fontWeight = FontWeight.Normal)
                }
            }
            Surface(
                onClick = {},
                color = NeveraChefColors.AccentSoft,
                shape = CircleShape,
                modifier = Modifier.size(42.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_plus),
                        contentDescription = "Editar",
                        tint = NeveraChefColors.Blue,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(216.dp)
                .background(NeveraChefColors.WarningSoft, RoundedCornerShape(28.dp))
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .background(Color.White, RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(food.iconRes),
                    contentDescription = food.name,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp),
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(food.name, color = NeveraChefColors.Ink, fontSize = 27.sp, lineHeight = 31.sp, fontWeight = FontWeight.Bold)
            Text(
                "${food.quantity} · ${food.category} · ${food.location.label}",
                color = NeveraChefColors.Muted,
                fontSize = 13.sp,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.WarningSoft, RoundedCornerShape(24.dp))
                .border(1.dp, Color(0xFFE8AF45), RoundedCornerShape(24.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color.White, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_scan),
                    contentDescription = null,
                    tint = Color(0xFFB4531E),
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Caducidad", color = Color(0xFFB4531E), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(food.expiryLabel ?: "Sin fecha", color = NeveraChefColors.Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Quedan 2 días · úsalo primero", color = NeveraChefColors.Muted, fontSize = 12.sp)
            }
            Text(
                text = food.expiryLabel?.substringAfterLast(" ") ?: "ok",
                color = Color(0xFFB4531E),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(999.dp))
                    .border(1.dp, Color(0xFFE8AF45), RoundedCornerShape(999.dp))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            InfoCard("Ubicación", food.location.label, Modifier.weight(1f))
            InfoCard("Cantidad", food.quantity, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            InfoCard("Categoría", food.category, Modifier.weight(1f))
            InfoCard("Añadido", "14 mayo", Modifier.weight(1f))
        }

        Surface(
            onClick = onGenerateRecipe,
            color = NeveraChefColors.Soft,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, NeveraChefColors.Line),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(13.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painterResource(Res.drawable.ic_nc_chef_hat), contentDescription = null, tint = NeveraChefColors.Blue)
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Idea rápida para hoy", color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Tortilla con espinacas en menos de 15 min", color = NeveraChefColors.Muted, fontSize = 12.sp)
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Surface(
                onClick = onBack,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                color = Color.White,
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, NeveraChefColors.Line),
            ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(Res.drawable.ic_nc_pantry), contentDescription = null, tint = NeveraChefColors.Muted)
                    Text("  Mover", color = NeveraChefColors.Muted, fontWeight = FontWeight.Bold)
                }
            }

            Surface(
                onClick = onGenerateRecipe,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                color = NeveraChefColors.AccentSoft,
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, Color(0xFFBBD5FF)),
            ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(Res.drawable.ic_nc_chef_hat), contentDescription = null, tint = NeveraChefColors.Blue)
                    Text("  Receta rápida", color = NeveraChefColors.Blue, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(82.dp)
            .background(NeveraChefColors.Soft, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(label, color = NeveraChefColors.Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
