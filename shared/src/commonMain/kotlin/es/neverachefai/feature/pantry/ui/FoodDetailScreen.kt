package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.withStyle
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_detail_quantity
import neverachefai.shared.generated.resources.ic_detail_weight
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_alert_triangle
import neverachefai.shared.generated.resources.ic_nc_calendar
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_microphone
import neverachefai.shared.generated.resources.ic_nc_pencil
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

private data class Palette(val background: Color, val tint: Color)

private enum class QuantityMode { UNITS, WEIGHT }

private enum class DateTarget { EXPIRY, ADDED }

private data class CategoryOption(
    val label: String,
    val iconKey: String,
)

private data class QuantityPresentation(
    val title: String,
    val valueLabel: String,
    val icon: DrawableResource,
    val tint: Color,
    val background: Color,
)

@Composable
fun FoodDetailScreen(
    food: PantryFoodUi?,
    onBack: () -> Unit,
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
                text = "No hay alimento seleccionado",
                color = NeveraChefColors.Ink,
                fontWeight = FontWeight.Bold,
            )
            Surface(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE7EFE8),
            ) {
                Text(
                    text = "Volver",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    color = NeveraChefColors.Ink,
                )
            }
        }
        return
    }

    var isEditing by remember(food.id) { mutableStateOf(false) }
    var selectedCategory by remember(food.id, food.category, food.iconKey) { mutableStateOf(categoryOptionFor(food)) }
    var editedName by remember(food.id, food.name) { mutableStateOf(food.name) }
    var selectedLocation by remember(food.id, food.location) { mutableStateOf(food.location) }
    var quantityMode by remember(food.id, food.quantity) { mutableStateOf(parseQuantityMode(food.quantity)) }
    var quantityValue by remember(food.id, food.quantity) { mutableStateOf(parseQuantityValue(food.quantity)) }
    var weightUnit by remember(food.id, food.quantity) { mutableStateOf(parseWeightUnit(food.quantity)) }
    var expiryDateIso by remember(food.id, food.expiryDateIso) { mutableStateOf(food.expiryDateIso) }
    var addedDateIso by remember(food.id, food.addedDateIso) { mutableStateOf(food.addedDateIso) }
    var dateTarget by remember { mutableStateOf<DateTarget?>(null) }

    val resolvedWeightUnit = if (quantityMode == QuantityMode.WEIGHT && (quantityValue.toIntOrNull() ?: 0) >= 1000) {
        "kg"
    } else {
        "gr"
    }
    val quantityLabel = formatQuantityLabel(quantityValue, quantityMode, resolvedWeightUnit)
    val previewFood = food.copy(
        name = editedName,
        quantity = quantityLabel,
        category = selectedCategory.label,
        location = selectedLocation,
        expiryDateIso = expiryDateIso,
        addedDateIso = addedDateIso,
        iconKey = selectedCategory.iconKey,
        iconRes = pantryIconResource(selectedCategory.iconKey),
    )

    val expiryText = formatDisplayDate(previewFood.expiryDateIso) ?: previewFood.expiryLabel ?: "Sin fecha"
    val expiryBadge = when (expirationPriority(previewFood.expiryDateIso)) {
        ExpirationPriority.EXPIRED -> "Caducado"
        ExpirationPriority.SOON -> "Próximo a caducar"
        ExpirationPriority.NORMAL -> "En buen estado"
        ExpirationPriority.UNKNOWN -> "Añadir fecha"
    }
    val quantityInfo = quantityPresentation(previewFood.quantity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        Header(onBack = onBack)
        ProductCard(
            selectedCategory = selectedCategory,
            productName = previewFood.name,
            isEditing = isEditing,
            onProductNameChange = { editedName = it },
            onCategoryChange = { selectedCategory = it },
        )
        QuantityFieldCard(
            title = quantityInfo.title,
            value = quantityInfo.valueLabel,
            icon = quantityInfo.icon,
            isEditing = isEditing,
            quantityMode = quantityMode,
            quantityValue = quantityValue,
            onQuantityValueChange = { quantityValue = it },
            weightUnit = weightUnit,
            onWeightUnitChange = { weightUnit = it },
        )
        LocationFieldCard(
            title = "Ubicación",
            value = selectedLocation.label,
            icon = locationIcon(selectedLocation),
            isEditing = isEditing,
            selectedLocation = selectedLocation,
            onLocationChange = { selectedLocation = it },
        )
        ExpiryCard(
            expiryText = expiryText,
            badgeText = if (isEditing) expiryBadge else null,
            onBadgeClick = { dateTarget = DateTarget.EXPIRY },
            onClick = if (isEditing) { { dateTarget = DateTarget.EXPIRY } } else null,
        )
        AddedCard(
            addedText = addedDateText(previewFood.addedDateIso),
            onClick = { if (isEditing) dateTarget = DateTarget.ADDED },
        )
        if (!isEditing) {
            EditButton(
                onClick = { isEditing = true },
            )
        }
        if (isEditing) {
            EditPanel(
                category = selectedCategory,
                onCancel = { isEditing = false },
                onSave = {
                    onSaveEditedFood(previewFood)
                    isEditing = false
                },
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
    }

    if (dateTarget != null) {
        val initialMillis = when (dateTarget) {
            DateTarget.EXPIRY -> isoDateToUtcMillis(expiryDateIso) ?: isoDateToUtcMillis(platformTodayIsoDate())
            DateTarget.ADDED -> isoDateToUtcMillis(addedDateIso) ?: isoDateToUtcMillis(platformTodayIsoDate())
            null -> isoDateToUtcMillis(platformTodayIsoDate())
        }
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)

        DatePickerDialog(
            onDismissRequest = { dateTarget = null },
            confirmButton = {
                TextButton(onClick = {
                    val selectedIso = pickerState.selectedDateMillis?.let(::utcMillisToIsoDate)
                    when (dateTarget) {
                        DateTarget.EXPIRY -> expiryDateIso = selectedIso
                        DateTarget.ADDED -> addedDateIso = selectedIso
                        null -> Unit
                    }
                    dateTarget = null
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { dateTarget = null }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

@Composable
private fun Header(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(34.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFEAF3ED))
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_arrow_back),
                contentDescription = "Volver",
                tint = Color(0xFF0A5A3A),
                modifier = Modifier.size(18.dp),
            )
        }

        Text(
            text = "Detalle de producto",
            color = Color(0xFF063D29),
            fontSize = 23.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ProductCard(
    selectedCategory: CategoryOption,
    productName: String,
    isEditing: Boolean,
    onProductNameChange: (String) -> Unit,
    onCategoryChange: (CategoryOption) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val categories = detailCategoryOptions()
        val selectedIndex = categories.indexOfFirst { it.iconKey == selectedCategory.iconKey }.coerceAtLeast(0)
        val heroIconSize = if (maxWidth < 360.dp) 84.dp else 92.dp
        val heroCardWidth = if (maxWidth < 360.dp) 214.dp else 220.dp
        val heroCardHeight = if (maxWidth < 360.dp) 226.dp else 232.dp
        val selectedTheme = categoryTheme(selectedCategory.iconKey)
        if (!isEditing) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    modifier = Modifier
                        .width(heroCardWidth)
                        .height(heroCardHeight),
                    shape = RoundedCornerShape(30.dp),
                    color = selectedTheme.background,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, selectedTheme.tint),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 22.dp, bottom = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Image(
                            painter = painterResource(pantryIconResource(selectedCategory.iconKey)),
                            contentDescription = selectedCategory.label,
                            modifier = Modifier.size(heroIconSize),
                        )
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, selectedTheme.tint.copy(alpha = 0.3f)),
                            ) {
                                Text(
                                text = selectedCategory.label,
                                color = Color(0xFF0A5A3A),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            )
                        }
                        Text(
                            text = productName,
                            color = Color(0xFF063D29),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 18.dp),
                        )
                    }
                }
            }
        } else {
            val cardWidth = heroCardWidth
            val cardHeight = heroCardHeight
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
            val sidePadding = (((maxWidth - cardWidth) / 2f).coerceAtLeast(12.dp))

            LaunchedEffect(selectedCategory.iconKey) {
                if (selectedIndex >= 0) {
                    listState.animateScrollToItem(selectedIndex)
                }
            }
            LaunchedEffect(listState, categories) {
                snapshotFlow {
                    Triple(
                        listState.isScrollInProgress,
                        listState.layoutInfo.visibleItemsInfo.map { item ->
                            item.index to (item.offset + item.size / 2)
                        },
                        listState.layoutInfo.viewportEndOffset,
                    )
                }.collect { (scrolling, items, viewportEnd) ->
                    if (!scrolling && items.isNotEmpty()) {
                        val viewportCenter = viewportEnd / 2
                        val nearestIndex = items.minByOrNull { abs(it.second - viewportCenter) }?.first ?: selectedIndex
                        val nearest = categories.getOrNull(nearestIndex) ?: return@collect
                        if (nearest.iconKey != selectedCategory.iconKey) {
                            onCategoryChange(nearest)
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight),
                ) {
                    LazyRow(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = sidePadding),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        items(categories) { option ->
                            val optionIndex = categories.indexOf(option)
                            FoodCarouselItem(
                                option = option,
                                selected = optionIndex == selectedIndex,
                                itemWidth = cardWidth,
                                itemHeight = cardHeight,
                                productName = productName,
                                onProductNameChange = onProductNameChange,
                                onClick = null,
                            )
                        }
                    }

                    if (selectedIndex > 0) {
                        CarouselArrow(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp),
                            direction = ArrowDirection.Left,
                            onClick = {
                                val previous = categories.previousFrom(selectedCategory)
                                onCategoryChange(previous)
                            },
                        )
                    }
                    if (selectedIndex in 0 until categories.lastIndex) {
                        CarouselArrow(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp),
                            direction = ArrowDirection.Right,
                            onClick = {
                                val next = categories.nextFrom(selectedCategory)
                                onCategoryChange(next)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FoodCarouselItem(
    option: CategoryOption,
    selected: Boolean,
    itemWidth: Dp,
    itemHeight: Dp,
    productName: String,
    onProductNameChange: (String) -> Unit,
    onClick: (() -> Unit)?,
) {
    val theme = categoryTheme(option.iconKey)
    val surfaceColor = if (selected) theme.background else theme.background.copy(alpha = 0.72f)
    val borderColor = if (selected) theme.tint else theme.tint.copy(alpha = 0.22f)
    val cardWidth = if (selected) itemWidth else itemWidth * 0.58f
    val cardHeight = if (selected) itemHeight else itemHeight * 0.72f
    val scale = if (selected) 1.0f else 0.78f
    val alpha = if (selected) 1f else 0.56f
    val iconSize = if (selected) 92.dp else 38.dp

    Column(
        modifier = Modifier
            .width(cardWidth)
            .height(cardHeight)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .clip(RoundedCornerShape(28.dp))
            .background(surfaceColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(28.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = if (selected) 18.dp else 14.dp, vertical = if (selected) 16.dp else 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (selected) Arrangement.SpaceBetween else Arrangement.Center,
    ) {
        Image(
            painter = painterResource(pantryIconResource(option.iconKey)),
            contentDescription = option.label,
            modifier = Modifier.size(iconSize),
        )

        Surface(
            shape = RoundedCornerShape(18.dp),
            color = Color.White.copy(alpha = if (selected) 0.98f else 0.82f),
            border = androidx.compose.foundation.BorderStroke(1.dp, borderColor.copy(alpha = if (selected) 0.35f else 0.14f)),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = if (selected) 14.dp else 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = option.label,
                    color = if (selected) Color(0xFF0A5A3A) else Color(0xFF6A707D),
                    fontSize = if (selected) 13.sp else 10.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        if (selected) {
            EditableProductNamePill(
                value = productName,
                onValueChange = onProductNameChange,
                borderColor = borderColor,
            )
        }
    }
}

@Composable
private fun EditableProductNamePill(
    value: String,
    onValueChange: (String) -> Unit,
    borderColor: Color,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor.copy(alpha = 0.4f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF063D29),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ),
                cursorBrush = androidx.compose.ui.graphics.SolidColor(Color(0xFF0A5A3A)),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private enum class ArrowDirection { Left, Right }

@Composable
private fun CarouselArrow(
    modifier: Modifier,
    direction: ArrowDirection,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .size(34.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE4DEC9), RoundedCornerShape(17.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (direction == ArrowDirection.Left) "‹" else "›",
            color = Color(0xFF0A5A3A),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun List<CategoryOption>.previousFrom(current: CategoryOption): CategoryOption {
    val index = indexOfFirst { it.iconKey == current.iconKey }
    return if (index <= 0) last() else get(index - 1)
}

private fun List<CategoryOption>.nextFrom(current: CategoryOption): CategoryOption {
    val index = indexOfFirst { it.iconKey == current.iconKey }
    return if (index < 0 || index >= lastIndex) first() else get(index + 1)
}

@Composable
private fun ProductNameSection(
    name: String,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "Nombre del producto",
            color = Color(0xFF566172),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )

        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF063D29),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE4DEC9),
                    unfocusedBorderColor = Color(0xFFE4DEC9),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF0A5A3A),
                ),
            )
        } else {
            Text(
                text = name,
                color = Color(0xFF063D29),
                fontSize = 20.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun LocationFieldCard(
    title: String,
    value: String,
    icon: DrawableResource,
    isEditing: Boolean,
    selectedLocation: PantryLocation,
    onLocationChange: (PantryLocation) -> Unit,
) {
    val neutralBorder = Color(0xFFE8E8E8)
    val locationAccent = locationTint(selectedLocation)
    val locationIconBackground = locationBackground(selectedLocation)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, neutralBorder),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(25.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(locationIconBackground, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = locationAccent,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(if (isEditing) 10.dp else 2.dp),
                ) {
                    Text(
                        text = title,
                        color = Color(0xFF4E5662),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start,
                    )
                    if (!isEditing) {
                        Text(
                            text = value,
                            color = Color(0xFF063D29),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Start,
                        )
                    }
                    if (isEditing) {
                        LocationSelector(
                            selected = selectedLocation,
                            onSelect = onLocationChange,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuantityFieldCard(
    title: String,
    value: String,
    icon: DrawableResource,
    isEditing: Boolean,
    quantityMode: QuantityMode,
    quantityValue: String,
    onQuantityValueChange: (String) -> Unit,
    weightUnit: String,
    onWeightUnitChange: (String) -> Unit,
) {
    val neutralBorder = Color(0xFFE8E8E8)
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, neutralBorder),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFEAF3ED), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFF0A5A3A),
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = title,
                    color = Color(0xFF4E5662),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                )
                if (!isEditing) {
                    Text(
                        text = value,
                        color = Color(0xFF063D29),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            if (isEditing) {
                QuantityStepper(
                    mode = quantityMode,
                    value = quantityValue,
                    onValueChange = onQuantityValueChange,
                    weightUnit = weightUnit,
                    onWeightUnitChange = onWeightUnitChange,
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(
    text: String,
    color: Color,
    textAlign: TextAlign = TextAlign.Start,
) {
    Text(
        text = text,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        textAlign = textAlign,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ExpiryCard(
    expiryText: String,
    badgeText: String?,
    onBadgeClick: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    if (badgeText == null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 78.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFE7E1CF), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFE7BE), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_alert_triangle),
                    contentDescription = null,
                    tint = Color(0xFFE58A00),
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "Caducidad",
                    color = Color(0xFFAB6700),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = expiryText,
                    color = Color(0xFFCC7600),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 78.dp)
                .background(Color(0xFFFFF7EA), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFF5CF8A), RoundedCornerShape(20.dp))
                .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFE7BE), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_alert_triangle),
                    contentDescription = null,
                    tint = Color(0xFFE58A00),
                    modifier = Modifier.size(20.dp),
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "Caducidad",
                    color = Color(0xFFAB6700),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start,
                )
                Text(
                    text = expiryText,
                    color = Color(0xFFCC7600),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
            }

            Text(
                text = badgeText,
                color = Color(0xFFE38A00),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .background(Color(0xFFFFE9C8), RoundedCornerShape(11.dp))
                    .clickable(enabled = onBadgeClick != null, onClick = { onBadgeClick?.invoke() })
                    .padding(horizontal = 7.dp, vertical = 4.dp),
            )
        }
    }
}

@Composable
private fun AddedCard(
    addedText: String,
    onClick: (() -> Unit)? = null,
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 78.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(20.dp))
                .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp),
        ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(Color(0xFFEAF3ED), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_calendar),
                contentDescription = null,
                tint = Color(0xFF0A5A3A),
                modifier = Modifier.size(20.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = "Producto añadido el día",
                color = Color(0xFF566172),
                fontSize = 13.sp,
                textAlign = TextAlign.Start,
            )
            Text(
                text = addedText,
                color = Color(0xFF063D29),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
private fun EditButton(
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF0A5A3A),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0A5A3A)),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_pencil),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Editar",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun EditPanel(
    category: CategoryOption,
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(15.dp), modifier = Modifier.fillMaxWidth()) {
            Surface(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE3E8DF)),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Cancelar",
                        color = Color(0xFF063D29),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
            Surface(
                onClick = onSave,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color(0xFF0A7A4B),
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Guardar cambios",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun QuantityStepper(
    mode: QuantityMode,
    value: String,
    onValueChange: (String) -> Unit,
    weightUnit: String,
    onWeightUnitChange: (String) -> Unit,
) {
    val numericValue = value.toIntOrNull() ?: 0
    val quantity = numericValue.coerceAtLeast(1)
    val weightValue = numericValue.coerceAtLeast(0)
    val isWeightMode = mode == QuantityMode.WEIGHT
    val showKgInField = isWeightMode && weightValue >= 1000
    val quantityFieldValue = when {
        isWeightMode && showKgInField -> gramsToKgInput(weightValue)
        isWeightMode -> weightValue.toString()
        else -> quantity.toString()
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF6F4EC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD9DED3)),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 44.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                    .background(Color(0xFFE8EEE4))
                    .clickable {
                        if (isWeightMode) {
                            onValueChange((weightValue - 100).coerceAtLeast(0).toString())
                        } else {
                            onValueChange((quantity - 1).coerceAtLeast(1).toString())
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "−",
                    color = Color(0xFF1D4D3A),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 68.dp, height = 44.dp)
                    .background(Color.White)
                    .border(1.dp, Color(0xFFD9DED3)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = quantityFieldValue,
                    color = Color(0xFF0A5A3A),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            }

            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 44.dp)
                    .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                    .background(Color(0xFF0A7A4B))
                    .clickable {
                        if (isWeightMode) {
                            onValueChange((weightValue + 100).toString())
                        } else {
                            onValueChange((quantity + 1).toString())
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

@Composable
private fun LocationSelector(
    selected: PantryLocation,
    onSelect: (PantryLocation) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            LocationChoiceChip(
                modifier = Modifier.weight(1f),
                title = "Nevera",
                icon = Res.drawable.ic_nc_fridge,
                selected = selected == PantryLocation.FRIDGE,
                onClick = { onSelect(PantryLocation.FRIDGE) },
            )
            LocationChoiceChip(
                modifier = Modifier.weight(1f),
                title = "Despensa",
                icon = Res.drawable.ic_nc_pantry,
                selected = selected == PantryLocation.PANTRY,
                onClick = { onSelect(PantryLocation.PANTRY) },
            )
            LocationChoiceChip(
                modifier = Modifier.weight(1f),
                title = "Congelador",
                icon = Res.drawable.ic_nc_freezer,
                selected = selected == PantryLocation.FREEZER,
                onClick = { onSelect(PantryLocation.FREEZER) },
            )
        }
    }
}

@Composable
private fun LocationChoiceChip(
    modifier: Modifier,
    title: String,
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val location = when (title) {
        "Nevera" -> PantryLocation.FRIDGE
        "Despensa" -> PantryLocation.PANTRY
        "Congelador" -> PantryLocation.FREEZER
        else -> PantryLocation.FRIDGE
    }
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(14.dp),
        color = if (selected) locationBackground(location) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) locationTint(location).copy(alpha = 0.18f) else Color(0xFFE3E8DF),
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = when (selected) {
                    true -> locationTint(location)
                    false -> Color(0xFF4E5561)
                },
                modifier = Modifier.size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                color = if (selected) locationTint(location) else Color(0xFF4E5561),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DateEditorRow(
    title: String,
    value: String,
    actionText: String,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE3E8DF)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFEAF3ED), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_calendar),
                    contentDescription = null,
                    tint = Color(0xFF0A5A3A),
                    modifier = Modifier.size(20.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color(0xFF566172), fontSize = 14.sp)
                Text(
                    text = value,
                    color = Color(0xFF063D29),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            TextButton(onClick = onClick) {
                Text(actionText)
            }
        }
    }
}

@Composable
private fun SegmentedTab(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Color(0xFFEAF3ED) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) Color(0xFF0A5A3A) else Color(0xFF4E5561),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun CircleControlButton(
    label: String,
    filled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (filled) Color(0xFF0A7A4B) else Color(0xFFE4EEE7))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (filled) Color.White else Color(0xFF19503D),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun gramsToKgInput(grams: Int): String {
    val kg = grams / 1000.0
    return if (kg % 1.0 == 0.0) {
        kg.toInt().toString()
    } else {
        kg.toString().replace('.', ',')
    }
}

@Composable
private fun EditCategoryChip(
    option: CategoryOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val theme = categoryTheme(option.iconKey)
    Surface(
        onClick = onClick,
        modifier = Modifier.height(82.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFEAF3ED) else Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Color(0xFF0A5A3A) else Color(0xFFE3E8DF)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(theme.background, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(pantryIconResource(option.iconKey)),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = option.label,
                color = if (selected) Color(0xFF0A5A3A) else Color(0xFF4E5561),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CategoryGrid(
    selected: CategoryOption,
    onSelect: (CategoryOption) -> Unit,
) {
    val categories = detailCategoryOptions()
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { option ->
                    EditCategoryChip(
                        option = option,
                        selected = selected.iconKey == option.iconKey,
                        onClick = { onSelect(option) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun EditCategoryChip(
    option: CategoryOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = categoryTheme(option.iconKey)
    Surface(
        onClick = onClick,
        modifier = modifier.height(82.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFFEAF3ED) else Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) Color(0xFF0A5A3A) else Color(0xFFE3E8DF)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(theme.background, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(pantryIconResource(option.iconKey)),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
            Text(
                text = option.label,
                color = if (selected) Color(0xFF0A5A3A) else Color(0xFF4E5561),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DetailInfoChip(
    title: String,
    value: String,
    icon: DrawableResource,
    iconSize: Dp = 22.dp,
) {
    val chipBackground = Color(0xFFF7F8F4)
    val chipBorder = Color(0xFFDDE5DA)
    val iconBackground = Color(0xFFEAF3ED)
    val labelColor = Color(0xFF5C6773)
    val valueColor = Color(0xFF0A5A3A)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(chipBackground)
            .border(1.dp, chipBorder, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(iconBackground, RoundedCornerShape(11.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = valueColor,
                modifier = Modifier.size(iconSize),
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = labelColor, fontWeight = FontWeight.Medium)) {
                    append("$title: ")
                }
                withStyle(SpanStyle(color = valueColor, fontWeight = FontWeight.SemiBold)) {
                    append(value)
                }
            },
            color = labelColor,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MetaChip(
    text: String,
    icon: DrawableResource,
    tint: Color,
    background: Color,
) {
    Row(
        modifier = Modifier
            .widthIn(min = 72.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (background == Color.Transparent) Color(0xFFF4F5F7) else background)
            .border(1.dp, tint, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            color = tint,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private fun quantityPresentation(quantity: String): QuantityPresentation {
    val mode = parseQuantityMode(quantity)
    val blueTint = Color(0xFF2F6FA6)
    val blueBackground = Color(0xFFEFF5FB)
    val valueLabel = when (mode) {
        QuantityMode.UNITS -> parseQuantityValue(quantity).toIntOrNull()?.coerceAtLeast(1)?.toString() ?: "1"
        QuantityMode.WEIGHT -> {
            val rawValue = parseQuantityValue(quantity).replace(',', '.').toDoubleOrNull() ?: 0.0
            if (rawValue >= 1000.0) {
                val kilograms = rawValue / 1000.0
                val formatted = if (kilograms % 1.0 == 0.0) {
                    kilograms.toInt().toString()
                } else {
                    kilograms.toString().replace('.', ',')
                }
                "$formatted kg"
            } else {
                "${rawValue.toInt()} gr"
            }
        }
    }

    return if (mode == QuantityMode.WEIGHT) {
        QuantityPresentation(
            title = "Peso",
            valueLabel = valueLabel,
            icon = Res.drawable.ic_detail_weight,
            tint = blueTint,
            background = blueBackground,
        )
    } else {
        QuantityPresentation(
            title = "Cantidad",
            valueLabel = valueLabel,
            icon = Res.drawable.ic_detail_quantity,
            tint = blueTint,
            background = blueBackground,
        )
    }
}

private fun parseQuantityMode(quantity: String): QuantityMode {
    val normalized = quantity.trim().lowercase()
    return if (normalized.contains("kg") || Regex("""\b(g|gr|gramo|gramos)\b""").containsMatchIn(normalized)) {
        QuantityMode.WEIGHT
    } else {
        QuantityMode.UNITS
    }
}

private fun parseQuantityValue(quantity: String): String {
    val match = Regex("""(\d+(?:[.,]\d+)?)""").find(quantity.trim()) ?: return "1"
    return match.groupValues[1]
}

private fun parseWeightUnit(quantity: String): String {
    val normalized = quantity.trim().lowercase()
    return when {
        normalized.contains("kg") -> "kg"
        normalized.contains("gr") || Regex("""\b(g|gramo|gramos)\b""").containsMatchIn(normalized) -> "gr"
        else -> "gr"
    }
}

private fun formatQuantityLabel(value: String, mode: QuantityMode, weightUnit: String): String {
    val cleanValue = value.trim().ifBlank { "1" }
    return when (mode) {
        QuantityMode.UNITS -> cleanValue.toIntOrNull()?.coerceAtLeast(1)?.toString() ?: "1"
        QuantityMode.WEIGHT -> "$cleanValue ${weightUnit.ifBlank { "gr" }}"
    }
}

private fun addedDateText(addedDateIso: String?): String {
    return formatDisplayDate(addedDateIso) ?: "Sin fecha"
}

private fun categoryOptionFor(food: PantryFoodUi): CategoryOption {
    val key = normalizeCategoryKey(food.iconKey, food.category)
    return detailCategoryOptions().firstOrNull { it.iconKey == key } ?: detailCategoryOptions().last()
}

private fun normalizeCategoryKey(iconKey: String, category: String): String {
    val candidates = listOf(iconKey.trim().lowercase(), category.trim().lowercase())
    for (candidate in candidates) {
        val normalized = when (candidate) {
            "fruits", "fruta", "frutas" -> "fruits"
            "vegetables", "verdura", "verduras" -> "vegetables"
            "meat", "carne" -> "meat"
            "fish", "pescado" -> "fish"
            "seafood", "marisco" -> "seafood"
            "bread", "pan" -> "bread"
            "milk", "leche" -> "milk"
            "yogurts", "yogures" -> "yogurts"
            "cheese", "queso" -> "cheese"
            "eggs", "huevos" -> "eggs"
            "grains", "pasta/arroz", "arroz", "pasta" -> "grains"
            "canned_food", "conservas" -> "canned_food"
            "frozen", "congelados" -> "frozen"
            "water", "agua" -> "water"
            "soft_drinks", "refrescos" -> "soft_drinks"
            "juice", "zumo" -> "juice"
            "wine", "vino" -> "wine"
            "beer", "cerveza" -> "beer"
            "coffee_tea", "cafe/te", "café/te" -> "coffee_tea"
            "snacks" -> "snacks"
            "sweets", "dulces" -> "sweets"
            "sauces", "salsas" -> "sauces"
            "oil_vinegar", "aceite/vinagre" -> "oil_vinegar"
            "ready_meals", "platos listos" -> "ready_meals"
            "cleaning", "limpieza" -> "cleaning"
            "hygiene", "higiene" -> "hygiene"
            "pets", "mascotas" -> "pets"
            else -> ""
        }
        if (normalized.isNotBlank()) return normalized
    }
    return "other"
}

private fun detailCategoryOptions(): List<CategoryOption> {
    return listOf(
        CategoryOption("Frutas", "fruits"),
        CategoryOption("Verduras", "vegetables"),
        CategoryOption("Carne", "meat"),
        CategoryOption("Pescado", "fish"),
        CategoryOption("Marisco", "seafood"),
        CategoryOption("Pan", "bread"),
        CategoryOption("Leche", "milk"),
        CategoryOption("Yogures", "yogurts"),
        CategoryOption("Queso", "cheese"),
        CategoryOption("Huevos", "eggs"),
        CategoryOption("Pasta/Arroz", "grains"),
        CategoryOption("Conservas", "canned_food"),
        CategoryOption("Congelados", "frozen"),
        CategoryOption("Agua", "water"),
        CategoryOption("Refrescos", "soft_drinks"),
        CategoryOption("Zumo", "juice"),
        CategoryOption("Vino", "wine"),
        CategoryOption("Cerveza", "beer"),
        CategoryOption("Café/Té", "coffee_tea"),
        CategoryOption("Snacks", "snacks"),
        CategoryOption("Dulces", "sweets"),
        CategoryOption("Salsas", "sauces"),
        CategoryOption("Aceite/Vinagre", "oil_vinegar"),
        CategoryOption("Platos listos", "ready_meals"),
        CategoryOption("Limpieza", "cleaning"),
        CategoryOption("Higiene", "hygiene"),
        CategoryOption("Mascotas", "pets"),
        CategoryOption("Otro", "other"),
    )
}

private fun categoryTheme(iconKey: String): Palette {
    return when (iconKey) {
        "fruits" -> Palette(Color(0xFFF3F7EA), Color(0xFF5C8A2D))
        "vegetables" -> Palette(Color(0xFFEFF5EC), Color(0xFF4A8D35))
        "meat" -> Palette(Color(0xFFFBF8F1), Color(0xFFBF6A00))
        "fish" -> Palette(Color(0xFFF1F5FB), Color(0xFF1D53D8))
        "seafood" -> Palette(Color(0xFFEFF7FA), Color(0xFF207C8F))
        "bread" -> Palette(Color(0xFFFDF5E8), Color(0xFFB07A2B))
        "milk" -> Palette(Color(0xFFEFF6FE), Color(0xFF3A6EA5))
        "yogurts" -> Palette(Color(0xFFEFF6FE), Color(0xFF3A6EA5))
        "cheese" -> Palette(Color(0xFFFFF8DD), Color(0xFFAF8B00))
        "eggs" -> Palette(Color(0xFFFBF1E7), Color(0xFF9E6A2A))
        "grains" -> Palette(Color(0xFFF9F3E3), Color(0xFF9A7B31))
        "canned_food" -> Palette(Color(0xFFF3EFEA), Color(0xFF7D6A56))
        "frozen" -> Palette(Color(0xFFEAF3FC), Color(0xFF2F6FA6))
        "water" -> Palette(Color(0xFFEAF3FC), Color(0xFF2F6FA6))
        "soft_drinks" -> Palette(Color(0xFFF0F7FF), Color(0xFF2F6FA6))
        "juice" -> Palette(Color(0xFFFFF3E8), Color(0xFFB56A25))
        "wine" -> Palette(Color(0xFFFAEEF2), Color(0xFF91506A))
        "beer" -> Palette(Color(0xFFFFF7E1), Color(0xFFA7801B))
        "coffee_tea" -> Palette(Color(0xFFF4EFE6), Color(0xFF8A6230))
        "snacks" -> Palette(Color(0xFFF8F0E8), Color(0xFFB06D35))
        "sweets" -> Palette(Color(0xFFFCEFEF), Color(0xFFB85A5A))
        "sauces" -> Palette(Color(0xFFF9F3E6), Color(0xFF9F6F27))
        "oil_vinegar" -> Palette(Color(0xFFF7F2E0), Color(0xFF8D6B26))
        "ready_meals" -> Palette(Color(0xFFF1F5EE), Color(0xFF5D8651))
        "cleaning" -> Palette(Color(0xFFF2F1F7), Color(0xFF6F5FA7))
        "hygiene" -> Palette(Color(0xFFF6F2F8), Color(0xFF9A6FAE))
        "pets" -> Palette(Color(0xFFF4F3EE), Color(0xFF8C7C5E))
        else -> Palette(Color(0xFFEAF3ED), Color(0xFF0A5A3A))
    }
}

private fun locationIcon(location: PantryLocation): DrawableResource {
    return when (location) {
        PantryLocation.FRIDGE -> Res.drawable.ic_nc_fridge
        PantryLocation.PANTRY -> Res.drawable.ic_nc_pantry
        PantryLocation.FREEZER -> Res.drawable.ic_nc_freezer
    }
}

private fun locationBackground(location: PantryLocation): Color {
    return when (location) {
        PantryLocation.FRIDGE -> Color(0xFFE8F4E8)
        PantryLocation.PANTRY -> Color(0xFFF9F0DE)
        PantryLocation.FREEZER -> Color(0xFFE5ECFB)
    }
}

private fun locationTint(location: PantryLocation): Color {
    return when (location) {
        PantryLocation.FRIDGE -> Color(0xFF007A53)
        PantryLocation.PANTRY -> Color(0xFFB17413)
        PantryLocation.FREEZER -> Color(0xFF1767C5)
    }
}

private fun PantryLocation.next(): PantryLocation {
    return when (this) {
        PantryLocation.FRIDGE -> PantryLocation.PANTRY
        PantryLocation.PANTRY -> PantryLocation.FREEZER
        PantryLocation.FREEZER -> PantryLocation.FRIDGE
    }
}
