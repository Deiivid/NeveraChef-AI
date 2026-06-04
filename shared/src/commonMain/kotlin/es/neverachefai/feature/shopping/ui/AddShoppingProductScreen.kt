package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import neverachefai.shared.generated.resources.ref_shopping_hero_basket
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class AddShoppingMode(
    val label: String,
    val iconRes: DrawableResource,
) {
    Manual("Manual", Res.drawable.ic_nc_shopping_basket),
    Voice("Voz", Res.drawable.ic_nc_microphone),
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
private val NcText = Color(0xFF424A5B)
private val NcSubtitle = Color(0xFF4D5565)
private val NcBorder = Color(0xFFE2E5E8)
private val NcMutedBg = Color(0xFFF1F4F2)

@Composable
fun AddShoppingProductScreen(
    state: AddShoppingProductUiState,
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
        isWeightMode && showKgInField -> gramsToKgInput(weightValue)
        isWeightMode -> weightValue.toString()
        else -> quantity.toString()
    }

    Scaffold(
        modifier = modifier,
        containerColor = NcSurface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NcSurface)
                .padding(innerPadding),
        ) {
            HeaderSection(
                onBackClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 0.dp),

            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                item {
                    OutlinedTextField(
                        value = state.productName,
                        onValueChange = onProductNameChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        label = {
                            Text(
                                "Introduce el nombre del producto",
                                color = NcGreen,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(Res.drawable.ic_nc_microphone),
                                contentDescription = null,
                                tint = NcGreen,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(onClick = onVoiceClick),
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = NcGreen,
                            unfocusedBorderColor = NcGreen,
                            focusedTextColor = NcGreen,
                            unfocusedTextColor = NcGreen,
                            focusedLabelColor = NcGreen,
                            unfocusedLabelColor = NcGreen,
                            cursorColor = NcGreen,
                        ),
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            val categories = listOf(
                                CategoryUi(
                                    "Frutas",
                                    Res.drawable.ic_cat_fruits,
                                    Color(0xFFE9F3ED),
                                    NcGreen
                                ),
                                CategoryUi(
                                    "Carne",
                                    Res.drawable.ic_cat_meat,
                                    Color(0xFFFBF8F1),
                                    Color(0xFFBF6A00)
                                ),
                                CategoryUi(
                                    "Verduras",
                                    Res.drawable.ic_cat_vegetables,
                                    Color(0xFFEFF5EC),
                                    Color(0xFF4A8D35)
                                ),
                                CategoryUi(
                                    "Pescado",
                                    Res.drawable.ic_cat_fish,
                                    Color(0xFFF1F5FB),
                                    Color(0xFF1D53D8)
                                ),
                                CategoryUi(
                                    "Marisco",
                                    Res.drawable.ic_cat_seafood,
                                    Color(0xFFEFF7FA),
                                    Color(0xFF207C8F)
                                ),
                                CategoryUi(
                                    "Pan",
                                    Res.drawable.ic_cat_bread,
                                    Color(0xFFFDF5E8),
                                    Color(0xFFB07A2B)
                                ),
                                CategoryUi(
                                    "Leche",
                                    Res.drawable.ic_cat_milk,
                                    Color(0xFFEFF6FE),
                                    Color(0xFF3A6EA5)
                                ),
                                CategoryUi(
                                    "Yogures",
                                    Res.drawable.ic_cat_yogurts,
                                    Color(0xFFEFF6FE),
                                    Color(0xFF3A6EA5)
                                ),
                                CategoryUi(
                                    "Queso",
                                    Res.drawable.ic_cat_cheese,
                                    Color(0xFFFFF8DD),
                                    Color(0xFFAF8B00)
                                ),
                                CategoryUi(
                                    "Huevos",
                                    Res.drawable.ic_cat_eggs,
                                    Color(0xFFFBF1E7),
                                    Color(0xFF9E6A2A)
                                ),
                                CategoryUi(
                                    "Pasta/Arroz",
                                    Res.drawable.ic_cat_pasta_rice_legumes,
                                    Color(0xFFF9F3E3),
                                    Color(0xFF9A7B31)
                                ),
                                CategoryUi(
                                    "Conservas",
                                    Res.drawable.ic_cat_canned_food,
                                    Color(0xFFF3EFEA),
                                    Color(0xFF7D6A56)
                                ),
                                CategoryUi(
                                    "Congelados",
                                    Res.drawable.ic_cat_frozen,
                                    Color(0xFFEAF3FC),
                                    Color(0xFF2F6FA6)
                                ),
                                CategoryUi(
                                    "Agua",
                                    Res.drawable.ic_cat_water_bottle,
                                    Color(0xFFEAF3FC),
                                    Color(0xFF2F6FA6)
                                ),
                                CategoryUi(
                                    "Refrescos",
                                    Res.drawable.ic_cat_soft_drinks,
                                    Color(0xFFF0F7FF),
                                    Color(0xFF2F6FA6)
                                ),
                                CategoryUi(
                                    "Zumo",
                                    Res.drawable.ic_cat_juice,
                                    Color(0xFFFFF3E8),
                                    Color(0xFFB56A25)
                                ),
                                CategoryUi(
                                    "Vino",
                                    Res.drawable.ic_cat_wine,
                                    Color(0xFFFAEEF2),
                                    Color(0xFF91506A)
                                ),
                                CategoryUi(
                                    "Cerveza",
                                    Res.drawable.ic_cat_beer,
                                    Color(0xFFFFF7E1),
                                    Color(0xFFA7801B)
                                ),
                                CategoryUi(
                                    "Cafe/Te",
                                    Res.drawable.ic_cat_coffee_tea,
                                    Color(0xFFF5F0EA),
                                    Color(0xFF7A5D3B)
                                ),
                                CategoryUi(
                                    "Snacks",
                                    Res.drawable.ic_cat_snacks,
                                    Color(0xFFFFF7E7),
                                    Color(0xFFA67A1F)
                                ),
                                CategoryUi(
                                    "Dulces",
                                    Res.drawable.ic_cat_sweets,
                                    Color(0xFFFDEDF0),
                                    Color(0xFFB65D73)
                                ),
                                CategoryUi(
                                    "Salsas",
                                    Res.drawable.ic_cat_sauces,
                                    Color(0xFFFDEDED),
                                    Color(0xFFA35353)
                                ),
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
                                CategoryUi(
                                    "Limpieza",
                                    Res.drawable.ic_cat_cleaning,
                                    Color(0xFFEAF4FD),
                                    Color(0xFF3A7FB2)
                                ),
                                CategoryUi(
                                    "Higiene",
                                    Res.drawable.ic_cat_hygiene,
                                    Color(0xFFEAF7F6),
                                    Color(0xFF2E817E)
                                ),
                                CategoryUi(
                                    "Mascotas",
                                    Res.drawable.ic_cat_pets,
                                    Color(0xFFF6F1EA),
                                    Color(0xFF7A6550)
                                ),
                                CategoryUi(
                                    "Otros",
                                    Res.drawable.ic_cat_other,
                                    Color(0xFFF5F3EE),
                                    Color(0xFF6E6A60)
                                ),
                            )
                            items(categories) { category ->
                                val selected = state.destination == category.label
                                CategoryCard(
                                    category = category,
                                    selected = selected,
                                    modifier = Modifier.size(74.dp),
                                ) { onDestinationChange(category.label) }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, NcBorder, RoundedCornerShape(14.dp)),
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
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircleControlButton(label = "−", filled = false) {
                                if (quantityMode == "Peso") {
                                    onQuantityChange((weightValue - 100).coerceAtLeast(0).toString())
                                } else {
                                    onQuantityChange((quantity - 1).coerceAtLeast(1).toString())
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = quantityFieldValue,
                                onValueChange = { raw ->
                                    if (isWeightMode && showKgInField) {
                                        val normalized = raw.replace(',', '.')
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
                                modifier = Modifier.width(if (isWeightMode) 108.dp else 84.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (showKgInField) {
                                        KeyboardType.Decimal
                                    } else {
                                        KeyboardType.Number
                                    },
                                ),
                                trailingIcon = if (isWeightMode) {
                                    {
                                        Text(
                                            text = if (showKgInField) "kg" else "gr",
                                            color = Color(0xFF0C3E2F),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                    }
                                } else {
                                    null
                                },
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = NcGreen,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = NcBorder,
                                    unfocusedBorderColor = NcBorder,
                                ),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            CircleControlButton(label = "+", filled = true) {
                                if (quantityMode == "Peso") {
                                    onQuantityChange((weightValue + 100).toString())
                                } else {
                                    onQuantityChange((quantity + 1).toString())
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                            LocationChip(
                                label = "Nevera",
                                icon = Res.drawable.ic_nc_fridge,
                                selected = location == "nevera",
                                accent = NcGreen,
                                modifier = Modifier.weight(1f),
                            ) { onLocationChange("nevera") }
                            LocationChip(
                                label = "Despensa",
                                icon = Res.drawable.ic_nc_pantry,
                                selected = location == "despensa",
                                accent = Color(0xFFB06B12),
                                modifier = Modifier.weight(1f),
                            ) { onLocationChange("despensa") }
                            LocationChip(
                                label = "Congelador",
                                icon = Res.drawable.ic_nc_freezer,
                                selected = location == "congelador",
                                accent = Color(0xFF144FA6),
                                modifier = Modifier.weight(1f),
                        ) { onLocationChange("congelador") }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(NcMutedBg)
                                .clickable {
                                    onModeSelected(AddShoppingMode.Voice)
                                    onVoiceClick()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFFE3ECE6)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_nc_microphone),
                                    contentDescription = null,
                                    tint = NcGreen,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .weight(1f),
                            ) {
                                Text(
                                    text = "Añadir por voz",
                                    color = Color(0xFF113E2E),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = "Dicta el nombre, cantidad y categoría",
                                    color = NcSubtitle,
                                    fontSize = 13.sp,
                                )
                            }
                            Text(text = "›", color = NcGreen, fontSize = 24.sp)
                        }
                            Spacer(modifier = Modifier.height(12.dp))
                        AddToListCta(onAddToShoppingListClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(34.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color(0xFFEAF3ED))
                .clickable(onClick = onBackClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_arrow_back),
                contentDescription = null,
                tint = NcGreen,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 14.dp, end = 6.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Añadir producto",
                color = NcGreen,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "Añade el producto que buscas a tu lista de la compra",
                color = NcSubtitle,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        }
        Image(
            painter = painterResource(Res.drawable.ref_shopping_hero_basket),
            contentDescription = null,
            modifier = Modifier.size(width = 122.dp, height = 74.dp),
            contentScale = ContentScale.Fit,
        )
    }
}
@Composable
private fun AddToListCta(onAddToShoppingListClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp),
    ) {
        Surface(
            onClick = onAddToShoppingListClick,
            shape = RoundedCornerShape(30.dp),
            color = NcGreenStrong,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Añadir a la lista",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: CategoryUi,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(category.bg)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) category.tint else category.bg,
                shape = RoundedCornerShape(14.dp),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Icon(
            painter = painterResource(category.icon),
            contentDescription = category.label,
            tint = Color.Unspecified,
            modifier = Modifier.size(40.dp),
        )
        Text(
            text = category.label,
            color = if (selected) NcGreen else NcText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
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
            color = if (selected) NcGreen else NcText,
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
            .background(if (filled) NcGreenStrong else Color(0xFFE4EEE7))
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

@Composable
private fun LocationChip(
    label: String,
    icon: DrawableResource,
    selected: Boolean,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) accent.copy(alpha = 0.12f) else Color(0xFFF4F5F7))
            .border(1.dp, if (selected) accent else Color(0xFFE2E5E9), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = accent,
            modifier = Modifier.size(18.dp),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = if (selected) accent else NcText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
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

private fun gramsToKgLabel(grams: Int): String {
    val kg = grams / 1000.0
    return if (kg % 1.0 == 0.0) {
        "${kg.toInt()} kg"
    } else {
        "${kg.toString().replace('.', ',')} kg"
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
