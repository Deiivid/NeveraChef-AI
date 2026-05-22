package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.PantryFoodRecord
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_frozen
import neverachefai.shared.generated.resources.ic_cat_fruits
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_milk
import neverachefai.shared.generated.resources.ic_cat_pasta_rice_legumes
import neverachefai.shared.generated.resources.ic_cat_vegetables
import neverachefai.shared.generated.resources.ic_caducidad
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
    val expiryDateIso: String?,
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
    var showAddModal by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newAmount by remember { mutableStateOf("1") }
    var newMeasureType by remember { mutableStateOf("Unidades") }
    var newWeightAmount by remember { mutableStateOf("") }
    var newWeightUnit by remember { mutableStateOf("") }
    var showWeightOptions by remember { mutableStateOf(false) }
    var newLocation by remember { mutableStateOf(PantryLocation.FRIDGE) }
    var newCategory by remember { mutableStateOf("verduras") }

    val foods = remember {
        mutableStateListOf<PantryFoodUi>().apply {
            addAll(LocalAppContentStore.loadPantryFoods().map { it.toUi() })
        }
    }

    val filteredFoods = foods.filter { food ->
        food.name.contains(searchQuery, ignoreCase = true) ||
            food.category.contains(searchQuery, ignoreCase = true) ||
            food.quantity.contains(searchQuery, ignoreCase = true)
    }

    val groupedFoods = PantryLocation.entries.associateWith { location ->
        filteredFoods.filter { it.location == location }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 6.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Tu inventario",
                    color = NeveraChefColors.Ink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7), RoundedCornerShape(26.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Tienes ${foods.size} alimentos",
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
                        iconRes = Res.drawable.ic_cat_frozen,
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
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeveraChefColors.Blue,
                    unfocusedBorderColor = NeveraChefColors.Line,
                ),
            )

            PantryLocation.entries.forEach { location ->
                val sectionFoods = groupedFoods[location].orEmpty()
                if (sectionFoods.isNotEmpty()) {
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
                                    modifier = Modifier.size(52.dp),
                                    contentAlignment = Alignment.Center,
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
                                ExpirationInfo(
                                    expiryDateIso = food.expiryDateIso,
                                    expiryLabel = food.expiryLabel,
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .size(56.dp)
                .background(NeveraChefColors.Blue, RoundedCornerShape(16.dp))
                .clickable { showAddModal = true },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_plus),
                contentDescription = "Añadir alimento",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }

        if (showAddModal) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66000000))
                    .clickable { showAddModal = false },
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0xFFFFFFFF), RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .clickable(enabled = false) {}
                        .padding(top = 12.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(32.dp)
                            .height(4.dp)
                            .background(NeveraChefColors.Line, RoundedCornerShape(99.dp)),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Añadir producto", color = NeveraChefColors.Ink, fontSize = 32.sp, lineHeight = 46.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(NeveraChefColors.Soft, CircleShape)
                                .clickable { showAddModal = false },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("×", color = NeveraChefColors.Muted, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Nombre del producto") },
                        placeholder = { Text("Ej. Manzanas Fuji") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeveraChefColors.Blue,
                            unfocusedBorderColor = NeveraChefColors.Line,
                            focusedContainerColor = Color(0xFFF8F2F9),
                            unfocusedContainerColor = Color(0xFFF8F2F9),
                        ),
                    )

                    Text("Categoría", color = NeveraChefColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        listOf("frutas", "verduras", "lacteos", "proteina", "granos").forEach { category ->
                            val selected = newCategory == category
                            Surface(
                                onClick = { newCategory = category },
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) Color(0xFFEAF2FF) else Color.White,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (selected) 2.dp else 1.dp,
                                    if (selected) NeveraChefColors.Blue else NeveraChefColors.Line,
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(104.dp),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 2.dp, vertical = 2.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            when (category) {
                                                "verduras" -> Res.drawable.ic_cat_vegetables
                                                "frutas" -> Res.drawable.ic_cat_fruits
                                                "lacteos" -> Res.drawable.ic_cat_milk
                                                "proteina" -> Res.drawable.ic_cat_meat
                                                else -> Res.drawable.ic_cat_pasta_rice_legumes
                                            }
                                        ),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier
                                            .size(68.dp)
                                            .padding(top = 2.dp),
                                    )
                                    Text(
                                        text = when (category) {
                                            "verduras" -> "Verdura"
                                            "frutas" -> "Fruta"
                                            "lacteos" -> "Lácteo"
                                            "proteina" -> "Proteína"
                                            else -> "Grano"
                                        },
                                        color = if (selected) NeveraChefColors.Blue else NeveraChefColors.Ink,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 2.dp),
                                    )
                                }
                            }
                        }
                    }

                    Text("Ubicación", color = NeveraChefColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        PantryLocation.entries.forEach { location ->
                            val selected = newLocation == location
                            val locationIcon = when (location) {
                                PantryLocation.FRIDGE -> Res.drawable.ic_nc_fridge
                                PantryLocation.PANTRY -> Res.drawable.ic_nc_pantry
                                PantryLocation.FREEZER -> Res.drawable.ic_cat_frozen
                            }
                            Surface(
                                onClick = { newLocation = location },
                                shape = RoundedCornerShape(14.dp),
                                color = if (selected) Color(0xFFEAF2FF) else Color(0xFFF8F2F9),
                                border = androidx.compose.foundation.BorderStroke(
                                    if (selected) 2.dp else 1.dp,
                                    if (selected) NeveraChefColors.Blue else NeveraChefColors.Line,
                                ),
                                modifier = Modifier.weight(1f).height(62.dp),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(locationIcon),
                                        contentDescription = location.label,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(28.dp),
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = location.label,
                                        color = if (selected) NeveraChefColors.Blue else NeveraChefColors.Muted,
                                        fontSize = 13.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Cantidad", color = NeveraChefColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                                    .background(Color(0xFFF8F2F9), RoundedCornerShape(12.dp))
                                    .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(12.dp))
                                    .padding(horizontal = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = {
                                    val value = (newAmount.toIntOrNull() ?: 1)
                                    newAmount = (value - 1).coerceAtLeast(1).toString()
                                }) { Text("−", color = NeveraChefColors.Muted, fontSize = 18.sp) }
                                OutlinedTextField(
                                    value = newAmount,
                                    onValueChange = { typed ->
                                        newAmount = typed.filter(Char::isDigit).take(3).ifBlank { "1" }
                                    },
                                    singleLine = true,
                                    modifier = Modifier.width(72.dp),
                                    textStyle = androidx.compose.ui.text.TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                    ),
                                )
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(NeveraChefColors.Blue, RoundedCornerShape(10.dp))
                                        .clickable {
                                            val value = (newAmount.toIntOrNull() ?: 1)
                                            newAmount = (value + 1).coerceAtMost(999).toString()
                                        },
                                    contentAlignment = Alignment.Center,
                                ) { Text("+", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                            }
                        }
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Peso", color = NeveraChefColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Surface(
                                onClick = {
                                    showWeightOptions = !showWeightOptions
                                },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8F2F9),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeveraChefColors.Line),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = if (newMeasureType == "Peso" && newWeightAmount.isNotBlank()) "$newWeightAmount $newWeightUnit" else "Peso",
                                            color = if (newMeasureType == "Peso" && newWeightAmount.isNotBlank()) NeveraChefColors.Ink else NeveraChefColors.Muted,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (showWeightOptions) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            listOf("150g", "250g", "500g", "750g", "1kg", "2kg").forEach { quick ->
                                val selected = when (quick) {
                                    "150g" -> newWeightUnit == "g" && newWeightAmount == "150"
                                    "250g" -> newWeightUnit == "g" && newWeightAmount == "250"
                                    "500g" -> newWeightUnit == "g" && newWeightAmount == "500"
                                    "750g" -> newWeightUnit == "g" && newWeightAmount == "750"
                                    "1kg" -> newWeightUnit == "kg" && newWeightAmount == "1"
                                    else -> newWeightUnit == "kg" && newWeightAmount == "2"
                                }
                                Surface(
                                    onClick = {
                                        when (quick) {
                                            "150g" -> {
                                                newWeightUnit = "g"
                                                newWeightAmount = "150"
                                            }
                                            "250g" -> {
                                                newWeightUnit = "g"
                                                newWeightAmount = "250"
                                            }
                                            "500g" -> {
                                                newWeightUnit = "g"
                                                newWeightAmount = "500"
                                            }
                                            "750g" -> {
                                                newWeightUnit = "g"
                                                newWeightAmount = "750"
                                            }
                                            "1kg" -> {
                                                newWeightUnit = "kg"
                                                newWeightAmount = "1"
                                            }
                                            "2kg" -> {
                                                newWeightUnit = "kg"
                                                newWeightAmount = "2"
                                            }
                                        }
                                        newMeasureType = "Peso"
                                        showWeightOptions = false
                                    },
                                    shape = RoundedCornerShape(999.dp),
                                    color = if (selected) Color(0xFFEAF2FF) else Color(0xFFF2ECF3),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeveraChefColors.Line),
                                    modifier = Modifier.weight(1f).height(38.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = quick,
                                            color = if (selected) NeveraChefColors.Blue else NeveraChefColors.Muted,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val trimmed = newName.trim()
                            if (trimmed.isNotEmpty()) {
                                val current = LocalAppContentStore.loadPantryFoods().toMutableList()
                                val quantityLabel = if (newMeasureType == "Peso" && newWeightAmount.isNotBlank()) {
                                    "$newWeightAmount $newWeightUnit"
                                } else {
                                    "${newAmount.ifBlank { "1" }} uds"
                                }
                                val quantityValue = if (newMeasureType == "Peso" && newWeightAmount.isNotBlank()) {
                                    newWeightAmount
                                } else {
                                    newAmount.ifBlank { "1" }
                                }
                                val quantityUnit = if (newMeasureType == "Peso" && newWeightAmount.isNotBlank()) {
                                    newWeightUnit
                                } else {
                                    "uds"
                                }
                                val iconKey = when (newCategory) {
                                    "frutas" -> "fruits"
                                    "lacteos" -> "milk"
                                    "proteina" -> "meat"
                                    "granos" -> "rice"
                                    else -> "spinach"
                                }
                                val locationKey = when (newLocation) {
                                    PantryLocation.FRIDGE -> "fridge"
                                    PantryLocation.PANTRY -> "pantry"
                                    PantryLocation.FREEZER -> "freezer"
                                }
                                val newRecord = PantryFoodRecord(
                                    id = "pantry_${current.size + 1}_${trimmed.lowercase().replace(" ", "_")}",
                                    name = trimmed,
                                    quantity = quantityLabel,
                                    quantityValue = quantityValue,
                                    quantityUnit = quantityUnit,
                                    category = newCategory,
                                    locationKey = locationKey,
                                    expiryLabel = null,
                                    expiryDateIso = null,
                                    iconKey = iconKey,
                                )
                                current += newRecord
                                LocalAppContentStore.savePantryFoods(current)
                                foods += newRecord.toUi()

                                newName = ""
                                newAmount = "1"
                                newMeasureType = "Unidades"
                                newWeightAmount = ""
                                newWeightUnit = ""
                                showWeightOptions = false
                                newLocation = PantryLocation.FRIDGE
                                newCategory = "verduras"
                                showAddModal = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
                    ) {
                        Text("Guardar alimento", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpirationInfo(
    expiryDateIso: String?,
    expiryLabel: String?,
) {
    val relative = getExpirationText(expiryDateIso)
    val fallback = expiryLabel?.trim().orEmpty().ifBlank { null }
    val subtitle = relative ?: fallback ?: return
    val priority = if (relative != null) expirationPriority(expiryDateIso) else ExpirationPriority.UNKNOWN

    val title = when (priority) {
        ExpirationPriority.EXPIRED -> "Caducado"
        ExpirationPriority.SOON -> "Caduca pronto"
        ExpirationPriority.NORMAL -> "Caduca"
        ExpirationPriority.UNKNOWN -> "Caduca"
    }
    val titleColor = when (priority) {
        ExpirationPriority.EXPIRED -> Color(0xFFB42318)
        ExpirationPriority.SOON -> Color(0xFFB54708)
        else -> NeveraChefColors.Muted
    }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
            if (priority == ExpirationPriority.SOON || priority == ExpirationPriority.EXPIRED) {
                Icon(
                    painter = painterResource(Res.drawable.ic_caducidad),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(13.dp),
                )
            }
            Text(
                text = title,
                color = titleColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
        Text(
            text = subtitle,
            color = if (priority == ExpirationPriority.NORMAL || priority == ExpirationPriority.UNKNOWN) NeveraChefColors.Muted else titleColor.copy(alpha = 0.9f),
            fontSize = 10.sp,
            maxLines = 1,
        )
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
                tint = if (iconRes == Res.drawable.ic_cat_frozen) Color.Unspecified else Color.Black.copy(alpha = 0.92f),
                modifier = Modifier.size(if (iconRes == Res.drawable.ic_cat_frozen) 18.dp else 16.dp),
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
    val quantityLabel = if (quantity.isNotBlank()) {
        quantity
    } else {
        listOf(quantityValue, quantityUnit).filter { it.isNotBlank() }.joinToString(" ")
    }
    return PantryFoodUi(
        id = id,
        name = name,
        quantity = quantityLabel,
        category = category,
        location = when (locationKey) {
            "fridge" -> PantryLocation.FRIDGE
            "freezer" -> PantryLocation.FREEZER
            else -> PantryLocation.PANTRY
        },
        expiryLabel = expiryLabel,
        expiryDateIso = expiryDateIso,
        iconKey = iconKey,
        iconRes = pantryIconResource(iconKey),
    )
}

private fun pantryIconResource(iconKey: String): DrawableResource {
    return when (iconKey) {
        "egg" -> Res.drawable.ic_cat_eggs
        "fish" -> Res.drawable.ic_cat_fish
        "fruits" -> Res.drawable.ic_cat_fruits
        "meat" -> Res.drawable.ic_cat_meat
        "milk" -> Res.drawable.ic_cat_milk
        "lentils" -> Res.drawable.ic_cat_pasta_rice_legumes
        "rice" -> Res.drawable.ic_cat_pasta_rice_legumes
        "spinach" -> Res.drawable.ic_cat_vegetables
        else -> Res.drawable.ic_cat_vegetables
    }
}
