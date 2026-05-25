package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_beer
import neverachefai.shared.generated.resources.ic_cat_bread
import neverachefai.shared.generated.resources.ic_cat_canned_food
import neverachefai.shared.generated.resources.ic_cat_cheese
import neverachefai.shared.generated.resources.ic_cat_cleaning
import neverachefai.shared.generated.resources.ic_cat_coffee_tea
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_frozen
import neverachefai.shared.generated.resources.ic_cat_fruits
import neverachefai.shared.generated.resources.ic_cat_hygiene
import neverachefai.shared.generated.resources.ic_cat_juice
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_milk
import neverachefai.shared.generated.resources.ic_cat_oil_vinegar
import neverachefai.shared.generated.resources.ic_cat_other
import neverachefai.shared.generated.resources.ic_cat_pasta_rice_legumes
import neverachefai.shared.generated.resources.ic_cat_pets
import neverachefai.shared.generated.resources.ic_cat_ready_meals
import neverachefai.shared.generated.resources.ic_cat_sauces
import neverachefai.shared.generated.resources.ic_cat_seafood
import neverachefai.shared.generated.resources.ic_cat_snacks
import neverachefai.shared.generated.resources.ic_cat_soft_drinks
import neverachefai.shared.generated.resources.ic_cat_sweets
import neverachefai.shared.generated.resources.ic_cat_vegetables
import neverachefai.shared.generated.resources.ic_cat_water_bottle
import neverachefai.shared.generated.resources.ic_cat_wine
import neverachefai.shared.generated.resources.ic_cat_yogurts
import neverachefai.shared.generated.resources.ic_expiry
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_pencil
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

private data class EditableProductUiState(
    val name: String,
    val location: PantryLocation,
    val quantity: String,
    val category: String,
    val expirationDateIso: String?,
)

@Composable
fun FoodDetailScreen(
    food: PantryFoodUi?,
    onBack: () -> Unit,
    onGenerateRecipe: () -> Unit,
    onSaveEditedFood: (PantryFoodUi) -> Unit,
) {
    if (food == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "No hay alimento seleccionado",
                color = NeveraChefColors.Ink,
                fontWeight = FontWeight.Bold
            )
            Button(onClick = onBack) { Text("Volver") }
        }
        return
    }

    var isEditing by remember(food.id) { mutableStateOf(false) }
    var draft by remember(food.id) {
        mutableStateOf(
            EditableProductUiState(
                name = food.name,
                location = food.location,
                quantity = food.quantity,
                category = normalizeCategoryKey(food.category),
                expirationDateIso = food.expiryDateIso,
            ),
        )
    }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerInitialMillis =
        remember(draft.expirationDateIso) { isoDateToUtcMillis(draft.expirationDateIso) }
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = datePickerInitialMillis)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeroCard(
            iconRes = if (isEditing) categoryIconResByKey(draft.category) else food.iconRes,
            name = if (isEditing) draft.name else food.name,
            isEditing = isEditing,
            onNameChange = { draft = draft.copy(name = it) },
            selectedCategory = draft.category,
            onCategoryChange = { draft = draft.copy(category = it) },
            onBack = onBack,
            onEdit = { isEditing = true },
            onCancelEdit = {
                draft = EditableProductUiState(
                    name = food.name,
                    location = food.location,
                    quantity = food.quantity,
                    category = normalizeCategoryKey(food.category),
                    expirationDateIso = food.expiryDateIso,
                )
                isEditing = false
            },
            subtitle = heroSubtitle(
                quantity = if (isEditing) draft.quantity else food.quantity,
                category = if (isEditing) labelForCategoryKey(draft.category) else food.category,
                location = if (isEditing) draft.location else food.location,
            ),
        )

        val expirationText = getExpirationText(
            expiryDateIso = if (isEditing) draft.expirationDateIso else food.expiryDateIso,
        )
        val fallbackExpirationText = if (isEditing) null else food.expiryLabel
        ExpirationCard(
            expirationText = expirationText,
            fallbackText = fallbackExpirationText,
            priority = expirationPriority(if (isEditing) draft.expirationDateIso else food.expiryDateIso),
            expirationDateIso = if (isEditing) draft.expirationDateIso else food.expiryDateIso,
            isEditing = isEditing,
            onSelectDate = { showDatePicker = true },
        )

        if (isEditing) {
            EditForm(
                draft = draft,
                onDraftChange = { draft = it },
            )
        } else {
            InfoGrid(food = food)
        }

        ActionRow(
            isEditing = isEditing,
            onEdit = { isEditing = true },
            onSave = {
                val updated = food.copy(
                    name = draft.name.trim().ifBlank { food.name },
                    location = draft.location,
                    quantity = draft.quantity.trim().ifBlank { food.quantity },
                    category = labelForCategoryKey(draft.category).ifBlank { food.category },
                    expiryDateIso = draft.expirationDateIso,
                    expiryLabel = food.expiryLabel,
                )
                onSaveEditedFood(updated)
                isEditing = false
            },
            onGenerateRecipe = onGenerateRecipe,
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selected = datePickerState.selectedDateMillis
                        if (selected != null) {
                            draft = draft.copy(expirationDateIso = utcMillisToIsoDate(selected))
                        }
                        showDatePicker = false
                    },
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) { Text("Cancelar") }
            },
        ) { DatePicker(state = datePickerState) }
    }
}

@Composable
private fun HeaderRow(
    isEditing: Boolean,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onCancelEdit: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            onClick = onBack,
            color = Color.Transparent,
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.size(48.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_arrow_back),
                    contentDescription = "Volver",
                    tint = NeveraChefColors.Ink,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Text(
            text = "Detalle de producto",
            color = NeveraChefColors.Ink,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )

        if (!isEditing) {
            Surface(
                onClick = onEdit,
                color = Color(0xFFEAF2FF),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0x33C2C6D9)),
                modifier = Modifier.height(40.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_nc_pencil),
                            contentDescription = "Editar",
                            tint = NeveraChefColors.Blue,
                            modifier = Modifier.size(18.dp),
                        )
                    }
                }
            }
        } else {
            Surface(
                onClick = onCancelEdit,
                color = Color(0xFFF8F2F9),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0x33C2C6D9)),
                modifier = Modifier.size(40.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "✕",
                        color = NeveraChefColors.Muted,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun HeroCard(
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    name: String,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    subtitle: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE6E3D4), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0x33C2C6D9), RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HeaderRow(
            isEditing = isEditing,
            onBack = onBack,
            onEdit = onEdit,
            onCancelEdit = onCancelEdit,
        )
        Spacer(Modifier.height(12.dp))
        if (isEditing) {
            val selectedIndex =
                allCategoryOptions.indexOfFirst { it.key == selectedCategory }.coerceAtLeast(0)
            val sliderState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
            LaunchedEffect(selectedCategory) {
                val idx = allCategoryOptions.indexOfFirst { it.key == selectedCategory }
                if (idx >= 0) sliderState.animateScrollToItem(idx)
            }
            LaunchedEffect(sliderState) {
                snapshotFlow {
                    val info = sliderState.layoutInfo
                    if (info.visibleItemsInfo.isEmpty()) null
                    else {
                        val center = (info.viewportStartOffset + info.viewportEndOffset) / 2
                        info.visibleItemsInfo.minByOrNull { item ->
                            abs((item.offset + item.size / 2) - center)
                        }?.index
                    }
                }.filterNotNull().distinctUntilChanged().collect { centeredIndex ->
                    val centeredKey =
                        allCategoryOptions.getOrNull(centeredIndex)?.key ?: return@collect
                    if (centeredKey != selectedCategory) onCategoryChange(centeredKey)
                }
            }
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val itemWidth = 80.dp
                val centerPadding = ((maxWidth - itemWidth) / 2).coerceAtLeast(0.dp)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(104.dp),
                    state = sliderState,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = centerPadding),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(allCategoryOptions) { option ->
                        HeroCategoryCarouselItem(
                            selected = selectedCategory == option.key,
                            iconRes = option.iconRes,
                            onClick = { onCategoryChange(option.key) },
                        )
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0x33C2C6D9), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = name,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp),
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Nombre del producto") },
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors(),
            )
        } else {
            Text(
                name,
                color = NeveraChefColors.Ink,
                fontSize = 24.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(subtitle, color = NeveraChefColors.Muted, fontSize = 12.sp)
    }
}

@Composable
private fun HeroCategoryCarouselItem(
    selected: Boolean,
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = if (selected) Color.White else Color.Transparent,
        shape = RoundedCornerShape(if (selected) 22.dp else 12.dp),
        border = BorderStroke(
            if (selected) 1.dp else 0.dp,
            if (selected) Color(0x33C2C6D9) else Color.Transparent
        ),
        modifier = Modifier.size(85.dp),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(if (selected) 80.dp else 50.dp),
            )
        }
    }
}

@Composable
private fun ExpirationCard(
    expirationText: String?,
    fallbackText: String?,
    priority: ExpirationPriority,
    expirationDateIso: String?,
    isEditing: Boolean,
    onSelectDate: () -> Unit,
) {
    val visibleText = expirationText ?: fallbackText
    if (visibleText.isNullOrBlank()) return

    val titleColor = when (priority) {
        ExpirationPriority.EXPIRED -> Color(0xFFB42318)
        ExpirationPriority.SOON -> Color(0xFFF57F17)
        ExpirationPriority.NORMAL -> Color(0xFFB4531E)
        ExpirationPriority.UNKNOWN -> Color(0xFFB4531E)
    }
    val borderColor = when (priority) {
        ExpirationPriority.EXPIRED -> Color(0xFFFECACA)
        ExpirationPriority.SOON -> Color(0xFFFFE082)
        ExpirationPriority.NORMAL -> Color(0xFFFFE082)
        ExpirationPriority.UNKNOWN -> Color(0xFFFFE082)
    }
    val backgroundColor = when (priority) {
        ExpirationPriority.EXPIRED -> Color(0xFFFFF1F2)
        else -> Color(0xFFFFF8E1)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_expiry),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "CADUCIDAD",
                color = titleColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    visibleText,
                    color = NeveraChefColors.Ink,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f),
                )
                if (isEditing) {
                    val dateText = formatDisplayDate(expirationDateIso) ?: "Seleccionar fecha"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable { onSelectDate() },
                    ) {
                        Text(
                            text = dateText,
                            color = NeveraChefColors.Blue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        Icon(
                            painter = painterResource(Res.drawable.ic_nc_pencil),
                            contentDescription = null,
                            tint = NeveraChefColors.Blue,
                            modifier = Modifier.size(12.dp),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditForm(
    draft: EditableProductUiState,
    onDraftChange: (EditableProductUiState) -> Unit,
) {
    var amountMode by remember {
        mutableStateOf(if (isWeightQuantity(draft.quantity)) "Peso" else "Unidades")
    }
    var unitsCount by remember {
        mutableStateOf(
            if (isWeightQuantity(draft.quantity)) 1 else parseUnitCount(draft.quantity).coerceIn(1, 999)
        )
    }
    var weightAmount by remember {
        mutableStateOf(parseWeightQuantity(draft.quantity).amount.ifBlank { "500" })
    }
    var weightUnit by remember {
        mutableStateOf(parseWeightQuantity(draft.quantity).unit.ifBlank { "g" })
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text(
            "Cantidad",
            color = NeveraChefColors.Muted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SelectChip(
                selected = amountMode == "Unidades",
                text = "Unidades",
                modifier = Modifier.weight(1f),
                onClick = {
                    amountMode = "Unidades"
                    onDraftChange(draft.copy(quantity = unitsCount.toString()))
                },
            )
            SelectChip(
                selected = amountMode == "Peso",
                text = "Peso",
                modifier = Modifier.weight(1f),
                onClick = {
                    amountMode = "Peso"
                    onDraftChange(draft.copy(quantity = formatWeightQuantity(weightAmount, weightUnit)))
                },
            )
        }
        if (amountMode == "Unidades") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F2F9), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0x33C2C6D9), RoundedCornerShape(12.dp))
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(
                    onClick = {
                        val nextValue = (unitsCount - 1).coerceAtLeast(1)
                        unitsCount = nextValue
                        onDraftChange(draft.copy(quantity = nextValue.toString()))
                    },
                    color = Color(0xFFECE6ED),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(34.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "−",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    "$unitsCount",
                    color = NeveraChefColors.Ink,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    onClick = {
                        val nextValue = (unitsCount + 1).coerceAtMost(999)
                        unitsCount = nextValue
                        onDraftChange(draft.copy(quantity = nextValue.toString()))
                    },
                    color = NeveraChefColors.Blue,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(34.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "+",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = weightAmount,
                    onValueChange = { typed ->
                        val nextAmount = typed.filter(Char::isDigit).take(4)
                        weightAmount = nextAmount
                        onDraftChange(
                            draft.copy(
                                quantity = formatWeightQuantity(
                                    nextAmount,
                                    weightUnit,
                                ),
                            ),
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("Peso") },
                    placeholder = { Text("250") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedFieldColors(),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.width(78.dp)
                ) {
                    listOf("g", "kg").forEach { unit ->
                        val selected = weightUnit == unit
                        SelectChip(
                            selected = selected,
                            text = unit,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                weightUnit = unit
                                val nextAmount = weightAmount.ifBlank { if (unit == "kg") "1" else "500" }
                                onDraftChange(
                                    draft.copy(
                                        quantity = formatWeightQuantity(
                                            nextAmount,
                                            unit,
                                        ),
                                    ),
                                )
                            },
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    PantryLocation.FRIDGE,
                    PantryLocation.PANTRY,
                    PantryLocation.FREEZER
                ).forEach { location ->
                    LocationSelectChip(
                        selected = draft.location == location,
                        iconRes = locationIconRes(location),
                        text = location.label,
                        modifier = Modifier.weight(1f),
                        onClick = { onDraftChange(draft.copy(location = location)) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val quickWeights = listOf("250g", "500g", "1kg", "2kg")
                quickWeights.chunked(2).forEach { rowWeights ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowWeights.forEach { quick ->
                            SelectChip(
                                selected = quick == "$weightAmount$weightUnit",
                                text = quick,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    val nextAmount = when (quick) {
                                        "250g" -> "250"
                                        "500g" -> "500"
                                        "1kg" -> "1"
                                        "2kg" -> "2"
                                        else -> weightAmount
                                    }
                                    val nextUnit = when (quick) {
                                        "1kg", "2kg" -> "kg"
                                        else -> "g"
                                    }
                                    weightAmount = nextAmount
                                    weightUnit = nextUnit
                                    amountMode = "Peso"
                                    onDraftChange(
                                        draft.copy(quantity = formatWeightQuantity(nextAmount, nextUnit))
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionRow(
    isEditing: Boolean,
    onEdit: () -> Unit,
    onSave: () -> Unit,
    onGenerateRecipe: () -> Unit,
) {
    if (isEditing) {
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004BCA)),
        ) {
            Text("Guardar", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                onClick = onGenerateRecipe,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                color = Color(0xFF004BCA),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(Res.drawable.ic_nc_chef_hat),
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        "  Receta rápida",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoGrid(food: PantryFoodUi) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard("Ubicación", food.location.label, Modifier.weight(1f))
            InfoCard("Cantidad", food.quantity, Modifier.weight(1f))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoCard("Categoría", food.category, Modifier.weight(1f))
            InfoCard(
                "Caducidad",
                formatDisplayDate(food.expiryDateIso) ?: (food.expiryLabel ?: "Sin fecha"),
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SelectChip(
    selected: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (selected) NeveraChefColors.Blue else Color(0x33C2C6D9)),
        color = if (selected) Color(0xFFEAF2FF) else Color(0xFFF8F2F9),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (selected) NeveraChefColors.Blue else NeveraChefColors.Ink,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun LocationSelectChip(
    selected: Boolean,
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (selected) NeveraChefColors.Blue else Color(0x33C2C6D9)),
        color = if (selected) Color(0xFFEAF2FF) else Color(0xFFF8F2F9),
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = if (iconRes == Res.drawable.ic_cat_frozen) Color.Unspecified else if (selected) NeveraChefColors.Blue else NeveraChefColors.Ink,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = " $text",
                color = if (selected) NeveraChefColors.Blue else NeveraChefColors.Ink,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(90.dp)
            .background(Color(0xFFF8F2F9), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0x33C2C6D9), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            label,
            color = NeveraChefColors.Muted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Text(value, color = NeveraChefColors.Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategorySelectCard(
    selected: Boolean,
    text: String,
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            if (selected) 1.6.dp else 0.dp,
            if (selected) NeveraChefColors.Blue else Color.Transparent
        ),
        modifier = modifier,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.fillMaxSize().padding(if (selected) 4.dp else 2.dp),
            )
        }
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = NeveraChefColors.Blue,
    unfocusedBorderColor = Color(0x33C2C6D9),
    focusedContainerColor = Color(0xFFF8F2F9),
    unfocusedContainerColor = Color(0xFFF8F2F9),
)

private fun parseUnitCount(quantity: String): Int {
    val digits = quantity.trim().takeWhile { it.isDigit() }
    return digits.toIntOrNull() ?: 1
}

private data class WeightQuantity(
    val amount: String,
    val unit: String,
)

private fun parseWeightQuantity(quantity: String): WeightQuantity {
    val normalized = quantity.trim().lowercase().replace(" ", "")
    return when {
        normalized.endsWith("kg") -> WeightQuantity(
            amount = normalized.removeSuffix("kg").ifBlank { "1" },
            unit = "kg",
        )

        normalized.endsWith("g") -> WeightQuantity(
            amount = normalized.removeSuffix("g").ifBlank { "500" },
            unit = "g",
        )

        else -> WeightQuantity(amount = "500", unit = "g")
    }
}

private fun formatWeightQuantity(amount: String, unit: String): String {
    val safeUnit = if (unit == "kg") "kg" else "g"
    val safeAmount = amount.ifBlank { if (safeUnit == "kg") "1" else "500" }
    return "$safeAmount $safeUnit"
}

private fun isWeightQuantity(quantity: String): Boolean {
    val q = quantity.trim().lowercase()
    return q.endsWith("g") || q.endsWith("kg")
}

private fun categoryIconRes(category: String) = when (category.lowercase()) {
    "verdura", "verduras", "vegetables" -> Res.drawable.ic_cat_vegetables
    "fruta", "frutas", "fruits" -> Res.drawable.ic_cat_fruits
    "proteína", "proteina", "meat", "protein" -> Res.drawable.ic_cat_meat
    "cereal", "grano", "granos", "grain", "grains" -> Res.drawable.ic_cat_pasta_rice_legumes
    else -> Res.drawable.ic_cat_vegetables
}

private data class CategoryOption(
    val key: String,
    val label: String,
    val iconRes: org.jetbrains.compose.resources.DrawableResource,
)

private val allCategoryOptions = listOf(
    CategoryOption("fruits", "Fruta", Res.drawable.ic_cat_fruits),
    CategoryOption("vegetables", "Verdura", Res.drawable.ic_cat_vegetables),
    CategoryOption("meat", "Carne", Res.drawable.ic_cat_meat),
    CategoryOption("fish", "Pescado", Res.drawable.ic_cat_fish),
    CategoryOption("seafood", "Marisco", Res.drawable.ic_cat_seafood),
    CategoryOption("bread", "Pan", Res.drawable.ic_cat_bread),
    CategoryOption("milk", "Leche", Res.drawable.ic_cat_milk),
    CategoryOption("yogurts", "Yogur", Res.drawable.ic_cat_yogurts),
    CategoryOption("cheese", "Queso", Res.drawable.ic_cat_cheese),
    CategoryOption("eggs", "Huevos", Res.drawable.ic_cat_eggs),
    CategoryOption("grains", "Cereal", Res.drawable.ic_cat_pasta_rice_legumes),
    CategoryOption("canned_food", "Conserva", Res.drawable.ic_cat_canned_food),
    CategoryOption("frozen", "Congelado", Res.drawable.ic_cat_frozen),
    CategoryOption("water", "Agua", Res.drawable.ic_cat_water_bottle),
    CategoryOption("soft_drinks", "Refresco", Res.drawable.ic_cat_soft_drinks),
    CategoryOption("juice", "Zumo", Res.drawable.ic_cat_juice),
    CategoryOption("wine", "Vino", Res.drawable.ic_cat_wine),
    CategoryOption("beer", "Cerveza", Res.drawable.ic_cat_beer),
    CategoryOption("coffee_tea", "Café/Té", Res.drawable.ic_cat_coffee_tea),
    CategoryOption("snacks", "Snacks", Res.drawable.ic_cat_snacks),
    CategoryOption("sweets", "Dulces", Res.drawable.ic_cat_sweets),
    CategoryOption("sauces", "Salsas", Res.drawable.ic_cat_sauces),
    CategoryOption("oil_vinegar", "Aceite", Res.drawable.ic_cat_oil_vinegar),
    CategoryOption("ready_meals", "Preparado", Res.drawable.ic_cat_ready_meals),
    CategoryOption("cleaning", "Limpieza", Res.drawable.ic_cat_cleaning),
    CategoryOption("hygiene", "Higiene", Res.drawable.ic_cat_hygiene),
    CategoryOption("pets", "Mascotas", Res.drawable.ic_cat_pets),
    CategoryOption("other", "Otro", Res.drawable.ic_cat_other),
)

private fun normalizeCategoryKey(category: String): String {
    val raw = category.trim().lowercase()
    return when (raw) {
        "verdura", "verduras", "vegetable", "vegetables" -> "vegetables"
        "fruta", "frutas", "fruit", "fruits" -> "fruits"
        "proteína", "proteina", "protein", "meat", "carne" -> "meat"
        "cereal", "grano", "granos", "grain", "grains", "rice", "lentils" -> "grains"
        else -> allCategoryOptions.firstOrNull { it.key == raw }?.key ?: "vegetables"
    }
}

private fun labelForCategoryKey(key: String): String =
    allCategoryOptions.firstOrNull { it.key == key }?.label ?: key.replace("_", " ")
        .replaceFirstChar { it.uppercase() }

private fun categoryIconResByKey(key: String) =
    allCategoryOptions.firstOrNull { it.key == key }?.iconRes ?: Res.drawable.ic_cat_vegetables

private fun locationIconRes(location: PantryLocation) = when (location) {
    PantryLocation.FRIDGE -> Res.drawable.ic_nc_fridge
    PantryLocation.PANTRY -> Res.drawable.ic_nc_pantry
    PantryLocation.FREEZER -> Res.drawable.ic_nc_freezer
}

private fun heroSubtitle(quantity: String, category: String, location: PantryLocation): String {
    return listOf(quantity.trim(), category.trim(), location.label.trim())
        .filter { it.isNotBlank() }
        .joinToString(" · ")
}
