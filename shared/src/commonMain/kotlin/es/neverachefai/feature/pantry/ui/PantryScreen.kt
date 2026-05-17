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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_food_egg
import neverachefai.shared.generated.resources.ic_food_fish
import neverachefai.shared.generated.resources.ic_food_lentils
import neverachefai.shared.generated.resources.ic_food_rice
import neverachefai.shared.generated.resources.ic_food_spinach
import neverachefai.shared.generated.resources.ic_food_soup
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_scan
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class PantryLocation(val label: String, val subtitle: String) {
    FRIDGE("Nevera", "Frescos"),
    PANTRY("Despensa", "Secos"),
    FREEZER("Congelador", "Reserva"),
}

data class PantryFoodUi(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val location: PantryLocation,
    val expiryLabel: String?,
    val iconRes: DrawableResource,
)

@Composable
fun PantryScreen(
    onAdd: () -> Unit,
    onReview: () -> Unit,
    onFoodClick: (PantryFoodUi) -> Unit,
) {
    var selectedLocation by remember { mutableStateOf(PantryLocation.FRIDGE) }
    var searchQuery by remember { mutableStateOf("") }

    val foods = remember {
        listOf(
            PantryFoodUi("1", "Espinacas", "1 bolsa", "Verdura", PantryLocation.FRIDGE, "mañana", Res.drawable.ic_food_spinach),
            PantryFoodUi("2", "Huevos", "6 uds", "Proteína", PantryLocation.FRIDGE, "2 días", Res.drawable.ic_food_egg),
            PantryFoodUi("3", "Arroz integral", "1 kg", "Cereal", PantryLocation.PANTRY, "bajo", Res.drawable.ic_food_rice),
            PantryFoodUi("4", "Lentejas", "500 g", "Legumbre", PantryLocation.PANTRY, "ok", Res.drawable.ic_food_lentils),
            PantryFoodUi("5", "Filetes de pescado", "2 uds", "Proteína", PantryLocation.FREEZER, "20 días", Res.drawable.ic_food_fish),
            PantryFoodUi("6", "Caldo casero", "1 litro", "Preparado", PantryLocation.FREEZER, "1 mes", Res.drawable.ic_food_soup),
        )
    }

    val filteredFoods = foods.filter {
        it.location == selectedLocation && it.name.contains(searchQuery, ignoreCase = true)
    }

    val sectionTitle = when (selectedLocation) {
        PantryLocation.FRIDGE -> "En nevera"
        PantryLocation.PANTRY -> "En despensa"
        PantryLocation.FREEZER -> "En congelador"
    }

    val bannerTitle = when (selectedLocation) {
        PantryLocation.FRIDGE -> "2 alimentos caducan pronto"
        PantryLocation.PANTRY -> "3 básicos con cantidad baja"
        PantryLocation.FREEZER -> "Sin caducidades urgentes"
    }

    val bannerBody = when (selectedLocation) {
        PantryLocation.FRIDGE -> "Huevos y espinacas · genera una receta rápida"
        PantryLocation.PANTRY -> "Arroz, lentejas y aceite · añade a compra"
        PantryLocation.FREEZER -> "Todo está controlado en congelador"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = when (selectedLocation) {
                        PantryLocation.FRIDGE -> "Tu nevera"
                        PantryLocation.PANTRY -> "Tu despensa"
                        PantryLocation.FREEZER -> "Tu congelador"
                    },
                    color = NeveraChefColors.Ink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = when (selectedLocation) {
                        PantryLocation.FRIDGE -> "Inventario local de lo que tienes en casa"
                        PantryLocation.PANTRY -> "Básicos, secos y reservas de cocina"
                        PantryLocation.FREEZER -> "Congelados y preparaciones guardadas"
                    },
                    color = NeveraChefColors.Muted,
                    fontSize = 12.sp,
                )
            }
            Surface(onClick = onAdd, color = NeveraChefColors.Blue, shape = CircleShape, modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_plus),
                        contentDescription = "Añadir alimento",
                        tint = Color.White,
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.Ink, RoundedCornerShape(26.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = when (selectedLocation) {
                    PantryLocation.FRIDGE -> "28 alimentos guardados"
                    PantryLocation.PANTRY -> "16 alimentos de despensa"
                    PantryLocation.FREEZER -> "4 alimentos congelados"
                },
                color = Color.White,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = when (selectedLocation) {
                    PantryLocation.FRIDGE -> "Solo en este dispositivo · sin login"
                    PantryLocation.PANTRY -> "Arroz, pasta, legumbres y conservas"
                    PantryLocation.FREEZER -> "Reserva para cocinar cualquier día"
                },
                color = Color.White.copy(alpha = 0.72f),
                fontSize = 12.sp,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryItem("8", "Nevera", Modifier.weight(1f))
                SummaryItem("16", "Despensa", Modifier.weight(1f))
                SummaryItem("4", "Congelador", Modifier.weight(1f))
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_scan),
                    contentDescription = null,
                    tint = NeveraChefColors.Muted,
                )
            },
            placeholder = { Text("Buscar alimento guardado", color = NeveraChefColors.Muted) },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            PantryLocation.entries.forEach { location ->
                val selected = selectedLocation == location
                Surface(
                    onClick = { selectedLocation = location },
                    modifier = Modifier
                        .weight(1f)
                        .height(94.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = if (selected) NeveraChefColors.AccentSoft else Color.White,
                    border = BorderStroke(1.dp, NeveraChefColors.Line),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            painter = painterResource(
                                when (location) {
                                    PantryLocation.FRIDGE -> Res.drawable.ic_nc_fridge
                                    PantryLocation.PANTRY -> Res.drawable.ic_nc_pantry
                                    PantryLocation.FREEZER -> Res.drawable.ic_nc_scan
                                },
                            ),
                            contentDescription = null,
                            tint = if (selected) NeveraChefColors.Blue else NeveraChefColors.Muted,
                        )
                        Text(
                            location.label,
                            color = NeveraChefColors.Ink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(location.subtitle, color = NeveraChefColors.Muted, fontSize = 12.sp)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NeveraChefColors.WarningSoft, RoundedCornerShape(18.dp))
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_scan),
                contentDescription = null,
                tint = Color(0xFF9B6A00),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(bannerTitle, color = NeveraChefColors.Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(bannerBody, color = NeveraChefColors.Muted, fontSize = 12.sp)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(sectionTitle, color = NeveraChefColors.Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text("Ver todo", color = NeveraChefColors.Blue, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        filteredFoods.forEach { food ->
            Surface(
                onClick = { onFoodClick(food) },
                color = Color.White,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, NeveraChefColors.Line),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                when (food.location) {
                                    PantryLocation.FRIDGE -> NeveraChefColors.SuccessSoft
                                    PantryLocation.PANTRY -> NeveraChefColors.WarningSoft
                                    PantryLocation.FREEZER -> NeveraChefColors.AccentSoft
                                },
                                RoundedCornerShape(16.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(food.iconRes),
                            contentDescription = food.name,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(food.name, color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(
                            "${food.quantity} · ${food.category} · ${food.location.label}",
                            color = NeveraChefColors.Muted,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    food.expiryLabel?.let { label ->
                        Text(
                            label,
                            color = Color(0xFF9B6A00),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .background(NeveraChefColors.WarningSoft, RoundedCornerShape(999.dp))
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onReview,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
        ) {
            Text("Cocinar con lo que tengo", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SummaryItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(10.dp),
    ) {
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.72f), fontSize = 10.sp)
    }
}
