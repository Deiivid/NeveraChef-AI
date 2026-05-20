package es.neverachefai.feature.pantry.ui

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.PantryFoodRecord
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_pasta_rice_legumes
import neverachefai.shared.generated.resources.ic_cat_vegetables
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
    val iconKey: String,
    val iconRes: DrawableResource,
)

@Composable
fun PantryScreen(
    onAdd: () -> Unit,
    onReview: () -> Unit,
    onFoodClick: (PantryFoodUi) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    val foods = remember {
        LocalAppContentStore.loadPantryFoods().map { it.toUi() }
    }

    val filteredFoods = foods.filter { food ->
        food.name.contains(searchQuery, ignoreCase = true) ||
            food.category.contains(searchQuery, ignoreCase = true) ||
            food.quantity.contains(searchQuery, ignoreCase = true)
    }

    val groupedFoods = PantryLocation.entries.associateWith { location ->
        filteredFoods.filter { it.location == location }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 6.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEF3C7), RoundedCornerShape(26.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Tienes ${foods.size} Alimentos",
                color = Color.Black,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                LocationSummaryItem(
                    iconRes = Res.drawable.ic_nc_fridge,
                    label = "Nevera",
                    value = groupedFoods[PantryLocation.FRIDGE]?.size?.toString().orEmpty(),
                    modifier = Modifier.weight(1f),
                )
                LocationSummaryItem(
                    iconRes = Res.drawable.ic_nc_pantry,
                    label = "Despensa",
                    value = groupedFoods[PantryLocation.PANTRY]?.size?.toString().orEmpty(),
                    modifier = Modifier.weight(1f),
                )
                LocationSummaryItem(
                    iconRes = Res.drawable.ic_nc_scan,
                    label = "Congelador",
                    value = groupedFoods[PantryLocation.FREEZER]?.size?.toString().orEmpty(),
                    modifier = Modifier.weight(1f),
                )
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

        PantryLocation.entries.forEach { location ->
            val sectionFoods = groupedFoods[location].orEmpty()
            if (sectionFoods.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = when (location) {
                            PantryLocation.FRIDGE -> "En nevera"
                            PantryLocation.PANTRY -> "En despensa"
                            PantryLocation.FREEZER -> "En congelador"
                        },
                        color = NeveraChefColors.Ink,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(NeveraChefColors.Line),
                )

                sectionFoods.forEach { food ->
                    Surface(
                        onClick = { onFoodClick(food) },
                        color = Color.White,
                        shape = RoundedCornerShape(18.dp),
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
                                    .size(52.dp) ,contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(food.iconRes),
                                    contentDescription = food.name,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(52.dp),
                                )
                            }
                            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    food.name,
                                    color = NeveraChefColors.Ink,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    food.quantity,
                                    color = NeveraChefColors.Muted,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationSummaryItem(
    iconRes: DrawableResource,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(Color.Black.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.92f),
                modifier = Modifier.size(16.dp),
            )
            Text(
                label,
                color = Color.Black.copy(alpha = 0.82f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Text(value, color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

private fun PantryFoodRecord.toUi(): PantryFoodUi {
    return PantryFoodUi(
        id = id,
        name = name,
        quantity = quantity,
        category = category,
        location = when (locationKey) {
            "fridge" -> PantryLocation.FRIDGE
            "freezer" -> PantryLocation.FREEZER
            else -> PantryLocation.PANTRY
        },
        expiryLabel = expiryLabel,
        iconKey = iconKey,
        iconRes = pantryIconResource(iconKey),
    )
}

private fun pantryIconResource(iconKey: String): DrawableResource {
    return when (iconKey) {
        "egg" -> Res.drawable.ic_cat_eggs
        "fish" -> Res.drawable.ic_cat_fish
        "lentils" -> Res.drawable.ic_cat_pasta_rice_legumes
        "rice" -> Res.drawable.ic_cat_pasta_rice_legumes
        "spinach" -> Res.drawable.ic_cat_vegetables
        else -> Res.drawable.ic_cat_vegetables
    }
}
