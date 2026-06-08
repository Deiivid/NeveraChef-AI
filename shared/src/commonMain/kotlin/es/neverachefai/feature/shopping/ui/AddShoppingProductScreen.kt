package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
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
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_microphone
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_settings
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ref_shopping_hero_exact
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class AddShoppingMode(
    val label: String,
    val iconRes: DrawableResource,
) {
    Manual("Manual", Res.drawable.ic_nc_shopping_basket),
    Voice("Voz", Res.drawable.ic_nc_microphone),
}

enum class AddProductTarget {
    ShoppingList,
    Inventory,
}

data class AddShoppingProductUiState(
    val selectedMode: AddShoppingMode = AddShoppingMode.Manual,
    val productName: String = "",
    val quantity: String = "1",
    val quantityMode: String = "Unidades",
    val destination: String = "Frutas",
    val location: String = "nevera",
    val previewProducts: List<String> = emptyList(),
)

private val NcGreen = Color(0xFF00563C)
private val NcGreenStrong = Color(0xFF006C4D)
private val NcSurface = Color(0xFFFFFFFF)
private val NcCard = Color(0xFFFFFFFF)
private val NcWarmPill = Color(0xFFF7F2EA)
private val NcText = Color(0xFF424A5B)
private val NcSubtitle = Color(0xFF4D5565)
private val NcWarmBorder = Color(0xFFE8E0D6)
private val NcMutedBg = Color(0xFFF1F4F2)

@Composable
fun AddShoppingProductScreen(
    state: AddShoppingProductUiState,
    target: AddProductTarget = AddProductTarget.ShoppingList,
    onModeSelected: (AddShoppingMode) -> Unit,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onQuantityModeChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onCameraClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddToShoppingListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val quantityMode = state.quantityMode
    val location = state.location
    val numericValue = state.quantity.toIntOrNull() ?: 0
    val quantity = numericValue.coerceAtLeast(1)
    val weightValue = numericValue.coerceAtLeast(0)
    val isWeightMode = quantityMode == "Peso"
    val showKgInField = isWeightMode && weightValue >= 1000
    val quantityFieldValue = when {
        isWeightMode && showKgInField -> "${gramsToKgInput(weightValue)} kg"
        isWeightMode -> "$weightValue gr"
        else -> quantity.toString()
    }

    Scaffold(
        modifier = modifier,
        containerColor = NcSurface,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(NcSurface),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 8.dp,
                bottom = 4.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            item {
                HeaderSection(target = target, onBackClick = onBackClick)
            }
            item {
                ProductNameField(
                    productName = state.productName,
                    onProductNameChange = onProductNameChange,
                    onVoiceClick = onVoiceClick,
                )
            }
            item {
                CategorySection(
                    selectedCategory = state.destination,
                    onDestinationChange = onDestinationChange,
                )
            }
            item {
                QuantitySelectorCard(
                    quantityMode = quantityMode,
                    quantityFieldValue = quantityFieldValue,
                    isWeightMode = isWeightMode,
                    showKgInField = showKgInField,
                    onQuantityChange = onQuantityChange,
                    onQuantityModeChange = onQuantityModeChange,
                    onDecrease = {
                        if (quantityMode == "Peso") {
                            onQuantityChange((weightValue - 100).coerceAtLeast(0).toString())
                        } else {
                            onQuantityChange((quantity - 1).coerceAtLeast(1).toString())
                        }
                    },
                    onIncrease = {
                        if (quantityMode == "Peso") {
                            onQuantityChange((weightValue + 100).toString())
                        } else {
                            onQuantityChange((quantity + 1).toString())
                        }
                    },
                )
            }
            item {
                LocationSelectorRow(
                    location = location,
                    onLocationChange = onLocationChange,
                )
            }
            item {
                VoiceEntryCard(
                    onClick = {
                        onModeSelected(AddShoppingMode.Voice)
                        onVoiceClick()
                    },
                )
            }
            item {
                AddToListCta(
                    label = if (target == AddProductTarget.Inventory) "Añadir a tu inventario" else "Añadir a la lista",
                    onAddToShoppingListClick = onAddToShoppingListClick,
                )
            }
        }
    }
}

@Composable
private fun ProductNameField(
    productName: String,
    onProductNameChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
) {
    OutlinedTextField(
        value = productName,
        onValueChange = onProductNameChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(3.dp, RoundedCornerShape(14.dp), clip = false),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        placeholder = {
            Text(
                "Introduce el nombre del producto",
                color = Color(0xFF7A808B),
                fontSize = 13.sp,
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_microphone),
                contentDescription = null,
                tint = NcGreen,
                modifier = Modifier
                    .size(22.dp)
                    .clickable(onClick = onVoiceClick),
            )
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            color = NcText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = NcGreen,
            unfocusedBorderColor = NcGreen,
            focusedTextColor = NcText,
            unfocusedTextColor = NcText,
            cursorColor = NcGreen,
        ),
    )
}

@Composable
private fun HeaderSection(
    target: AddProductTarget,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(268.dp),
    ) {
        Image(
            painter = painterResource(Res.drawable.ref_shopping_hero_exact),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-12).dp)
                .size(width = maxWidth + 42.dp, height = 222.dp),
            contentScale = ContentScale.Fit,
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 20.dp)
                .size(46.dp)
                .shadow(4.dp, RoundedCornerShape(23.dp), clip = false)
                .clip(RoundedCornerShape(23.dp))
                .background(Color.White)
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_arrow_back),
                contentDescription = null,
                tint = NcGreen,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(y = 188.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = if (target == AddProductTarget.Inventory) "Añadir alimento" else "Añadir producto",
                color = NcGreen,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
            )
            Text(
                text = if (target == AddProductTarget.Inventory) {
                    "Añade lo que ya tienes en tu nevera, despensa o congelador"
                } else {
                    "Añade el producto que buscas a tu lista de la compra"
                },
                color = NcSubtitle,
                fontSize = 15.sp,
                lineHeight = 21.sp,
            )
        }
    }
}

@Composable
private fun AddToListCta(
    label: String,
    onAddToShoppingListClick: () -> Unit,
) {
    Surface(
        onClick = onAddToShoppingListClick,
        shape = RoundedCornerShape(30.dp),
        color = NcGreenStrong,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(5.dp, RoundedCornerShape(30.dp), clip = false),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_shopping_basket),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(21.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: String,
    onDestinationChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 8.dp),
        ) {
            items(shoppingCategories()) { category ->
                val selected = selectedCategory == category.label
                CategoryCard(
                    category = category,
                    selected = selected,
                    modifier = Modifier
                        .width(if (selected) 78.dp else 68.dp)
                        .height(82.dp),
                ) { onDestinationChange(category.label) }
            }
        }
    }
}

@Composable
private fun QuantitySelectorCard(
    quantityMode: String,
    quantityFieldValue: String,
    isWeightMode: Boolean,
    showKgInField: Boolean,
    onQuantityChange: (String) -> Unit,
    onQuantityModeChange: (String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .background(NcCard)
            .border(1.dp, NcWarmBorder.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(NcWarmPill)
                .padding(3.dp),
        ) {
            SegmentedTab(
                label = "Cantidad",
                selected = quantityMode == "Unidades",
                modifier = Modifier.weight(1f),
            ) {
                if (quantityMode != "Unidades") {
                    onQuantityChange("1")
                    onQuantityModeChange("Unidades")
                }
            }
            SegmentedTab(
                label = "Peso",
                selected = quantityMode == "Peso",
                modifier = Modifier.weight(1f),
            ) {
                if (quantityMode != "Peso") {
                    onQuantityChange("100")
                    onQuantityModeChange("Peso")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleControlButton(label = "−", filled = false, onClick = onDecrease)
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = quantityFieldValue,
                onValueChange = { raw ->
                    if (isWeightMode && showKgInField) {
                        val normalized = raw.replace(',', '.').filter { it.isDigit() || it == '.' }
                        val kg = normalized.toDoubleOrNull() ?: 0.0
                        val grams = (kg * 1000.0).roundToInt().coerceAtLeast(0)
                        onQuantityChange(grams.toString())
                    } else if (isWeightMode) {
                        onQuantityChange(raw.filter(Char::isDigit))
                    } else {
                        val sanitized = raw.filter(Char::isDigit)
                        val units = sanitized.toIntOrNull()?.coerceAtLeast(1) ?: 1
                        onQuantityChange(units.toString())
                    }
                },
                modifier = Modifier
                    .width(if (isWeightMode) 118.dp else 92.dp)
                    .height(54.dp),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = if (showKgInField) KeyboardType.Decimal else KeyboardType.Number,
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = NcGreen,
                    fontSize = if (isWeightMode) 19.sp else 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = NcWarmBorder,
                    unfocusedBorderColor = NcWarmBorder,
                    focusedTextColor = NcGreen,
                    unfocusedTextColor = NcGreen,
                    cursorColor = NcGreen,
                ),
            )
            Spacer(modifier = Modifier.width(12.dp))
            CircleControlButton(label = "+", filled = true, onClick = onIncrease)
        }
    }
}

@Composable
private fun LocationSelectorRow(
    location: String,
    onLocationChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LocationChip(
            label = "Nevera",
            icon = Res.drawable.ic_nc_fridge,
            selected = location == "nevera",
            accent = NcGreen,
            background = Color(0xFFEFF6EF),
            modifier = Modifier.weight(1f),
        ) { onLocationChange("nevera") }
        LocationChip(
            label = "Despensa",
            icon = Res.drawable.ic_nc_pantry,
            selected = location == "despensa",
            accent = Color(0xFFC46B00),
            background = Color(0xFFFFF7EA),
            modifier = Modifier.weight(1f),
        ) { onLocationChange("despensa") }
        LocationChip(
            label = "Congelador",
            icon = Res.drawable.ic_nc_freezer,
            selected = location == "congelador",
            accent = Color(0xFF1160C8),
            background = Color(0xFFF0F7FF),
            modifier = Modifier.weight(1f),
        ) { onLocationChange("congelador") }
    }
}

@Composable
private fun VoiceEntryCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(NcMutedBg)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE0EDE5)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_microphone),
                contentDescription = null,
                tint = NcGreen,
                modifier = Modifier.size(19.dp),
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
        ) {
            Text(
                text = "Añadir por voz",
                color = Color(0xFF113E2E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Nombre, cantidad, categoría y ubicación",
                color = NcSubtitle,
                fontSize = 12.sp,
            )
        }
        Text(text = "›", color = NcGreen, fontSize = 26.sp)
    }
}

@Composable
private fun CategoryCard(
    category: CategoryUi,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .shadow(if (selected) 5.dp else 3.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(14.dp))
            .background(category.bg)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) category.tint else NcWarmBorder.copy(alpha = 0.72f),
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Image(
                painter = painterResource(category.icon),
                contentDescription = category.label,
                modifier = Modifier.size(if (selected) 44.dp else 40.dp),
            )
            Text(
                text = category.label,
                color = if (selected) NcGreen else NcText,
                fontSize = if (selected) 12.sp else 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(22.dp)
                    .clip(RoundedCornerShape(11.dp))
                    .background(category.tint),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✓",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
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
            .clip(RoundedCornerShape(21.dp))
            .background(if (selected) NcGreenStrong else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 7.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else NcText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
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
            .size(42.dp)
            .shadow(if (filled) 4.dp else 2.dp, RoundedCornerShape(21.dp), clip = false)
            .clip(RoundedCornerShape(21.dp))
            .background(if (filled) NcGreenStrong else Color(0xFFE4EEE7))
            .pointerInput(onClick) {
                coroutineScope {
                    detectTapGestures(
                        onPress = {
                            val repeatJob = launch {
                                onClick()
                                delay(360)
                                while (isActive) {
                                    onClick()
                                    delay(90)
                                }
                            }
                            tryAwaitRelease()
                            repeatJob.cancel()
                        },
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (filled) Color.White else Color(0xFF19503D),
            fontSize = 24.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun LocationChip(
    label: String,
    icon: DrawableResource,
    selected: Boolean,
    accent: Color,
    background: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .height(60.dp)
            .shadow(if (selected) 4.dp else 3.dp, RoundedCornerShape(16.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) background else Color.White)
            .border(
                if (selected) 2.dp else 1.dp,
                if (selected) accent else NcWarmBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = accent,
            modifier = Modifier.size(21.dp),
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            color = if (selected) accent else NcText,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

private data class CategoryUi(
    val label: String,
    val icon: DrawableResource,
    val bg: Color,
    val tint: Color,
)

private fun shoppingCategories() = listOf(
    CategoryUi("Frutas", Res.drawable.ic_cat_fruits, Color(0xFFEAF4EC), NcGreen),
    CategoryUi("Carne", Res.drawable.ic_cat_meat, Color(0xFFFBF6EF), Color(0xFF8A3F2A)),
    CategoryUi("Verduras", Res.drawable.ic_cat_vegetables, Color(0xFFEFF5EC), Color(0xFF2F7E42)),
    CategoryUi("Pescado", Res.drawable.ic_cat_fish, Color(0xFFF1F6FC), Color(0xFF1C597F)),
    CategoryUi("Marisco", Res.drawable.ic_cat_seafood, Color(0xFFFFF7EF), Color(0xFF9A4D2D)),
    CategoryUi("Pan", Res.drawable.ic_cat_bread, Color(0xFFFDF5E8), Color(0xFFB07A2B)),
    CategoryUi("Leche", Res.drawable.ic_cat_milk, Color(0xFFEFF6FE), Color(0xFF3A6EA5)),
    CategoryUi("Yogures", Res.drawable.ic_cat_yogurts, Color(0xFFEFF6FE), Color(0xFF3A6EA5)),
    CategoryUi("Queso", Res.drawable.ic_cat_cheese, Color(0xFFFFF8DD), Color(0xFFAF8B00)),
    CategoryUi("Huevos", Res.drawable.ic_cat_eggs, Color(0xFFFBF1E7), Color(0xFF9E6A2A)),
    CategoryUi(
        "Pasta/Arroz",
        Res.drawable.ic_cat_pasta_rice_legumes,
        Color(0xFFF9F3E3),
        Color(0xFF9A7B31)
    ),
    CategoryUi("Conservas", Res.drawable.ic_cat_canned_food, Color(0xFFF3EFEA), Color(0xFF7D6A56)),
    CategoryUi("Congelados", Res.drawable.ic_cat_frozen, Color(0xFFEAF3FC), Color(0xFF2F6FA6)),
    CategoryUi("Agua", Res.drawable.ic_cat_water_bottle, Color(0xFFEAF3FC), Color(0xFF2F6FA6)),
    CategoryUi("Refrescos", Res.drawable.ic_cat_soft_drinks, Color(0xFFF0F7FF), Color(0xFF2F6FA6)),
    CategoryUi("Zumo", Res.drawable.ic_cat_juice, Color(0xFFFFF3E8), Color(0xFFB56A25)),
    CategoryUi("Vino", Res.drawable.ic_cat_wine, Color(0xFFFAEEF2), Color(0xFF91506A)),
    CategoryUi("Cerveza", Res.drawable.ic_cat_beer, Color(0xFFFFF7E1), Color(0xFFA7801B)),
    CategoryUi("Cafe/Te", Res.drawable.ic_cat_coffee_tea, Color(0xFFF5F0EA), Color(0xFF7A5D3B)),
    CategoryUi("Snacks", Res.drawable.ic_cat_snacks, Color(0xFFFFF7E7), Color(0xFFA67A1F)),
    CategoryUi("Dulces", Res.drawable.ic_cat_sweets, Color(0xFFFDEDF0), Color(0xFFB65D73)),
    CategoryUi("Salsas", Res.drawable.ic_cat_sauces, Color(0xFFFDEDED), Color(0xFFA35353)),
    CategoryUi(
        "Aceite/Vinagre",
        Res.drawable.ic_cat_oil_vinegar,
        Color(0xFFF8F7E6),
        Color(0xFF8A8331)
    ),
    CategoryUi(
        "Platos listos",
        Res.drawable.ic_cat_ready_meals,
        Color(0xFFFBF2E7),
        Color(0xFFA36A39)
    ),
    CategoryUi("Limpieza", Res.drawable.ic_cat_cleaning, Color(0xFFEAF4FD), Color(0xFF3A7FB2)),
    CategoryUi("Higiene", Res.drawable.ic_cat_hygiene, Color(0xFFEAF7F6), Color(0xFF2E817E)),
    CategoryUi("Mascotas", Res.drawable.ic_cat_pets, Color(0xFFF6F1EA), Color(0xFF7A6550)),
    CategoryUi("Otros", Res.drawable.ic_cat_other, Color(0xFFF5F3EE), Color(0xFF6E6A60)),
)

private fun gramsToKgInput(grams: Int): String {
    val kg = grams / 1000.0
    return if (kg % 1.0 == 0.0) {
        kg.toInt().toString()
    } else {
        kg.toString().replace('.', ',')
    }
}
