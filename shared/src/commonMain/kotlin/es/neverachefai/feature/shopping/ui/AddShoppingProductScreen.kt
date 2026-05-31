package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import neverachefai.shared.generated.resources.ic_nc_plus
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
    val destination: String = "Frutas",
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
    onDestinationChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onCameraClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddToShoppingListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var quantityMode by remember { mutableStateOf("Unidades") }
    var location by remember { mutableStateOf("nevera") }
    val quantity = state.quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1

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
                    .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 10.dp),
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 0.dp,
                    bottom = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp),
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
                                color = Color.Black,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
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
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                        ),
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
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
                                    modifier = Modifier.size(84.dp),
                                ) { onDestinationChange(category.label) }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .border(1.dp, NcBorder, RoundedCornerShape(14.dp)),
                        ) {
                            SegmentedTab(
                                "Unidades",
                                quantityMode == "Unidades",
                                Modifier.weight(1f)
                            ) {
                                quantityMode = "Unidades"
                            }
                            SegmentedTab("Peso", quantityMode == "Peso", Modifier.weight(1f)) {
                                quantityMode = "Peso"
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            CircleControlButton(label = "−", filled = false) {
                                onQuantityChange((quantity - 1).coerceAtLeast(1).toString())
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = state.quantity,
                                onValueChange = { raw ->
                                    val filtered = if (quantityMode == "Peso") {
                                        raw.filter { it.isDigit() || it == '.' || it == ',' }
                                    } else {
                                        raw.filter(Char::isDigit)
                                    }
                                    onQuantityChange(filtered)
                                },
                                modifier = Modifier.width(62.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (quantityMode == "Peso") KeyboardType.Decimal else KeyboardType.Number,
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = Color(0xFF0C3E2F),
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
                                onQuantityChange((quantity + 1).toString())
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                            LocationChip(
                                label = "Nevera",
                                icon = Res.drawable.ic_nc_fridge,
                                selected = location == "nevera",
                                tint = NcGreen,
                                modifier = Modifier.weight(1f),
                            ) { location = "nevera" }
                            LocationChip(
                                label = "Despensa",
                                icon = Res.drawable.ic_nc_pantry,
                                selected = location == "despensa",
                                tint = NcText,
                                modifier = Modifier.weight(1f),
                            ) { location = "despensa" }
                            LocationChip(
                                label = "Congelador",
                                icon = Res.drawable.ic_nc_freezer,
                                selected = location == "congelador",
                                tint = Color(0xFF144FA6),
                                modifier = Modifier.weight(1f),
                        ) { location = "congelador" }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(NcMutedBg)
                            .clickable {
                                onModeSelected(AddShoppingMode.Voice)
                                onVoiceClick()
                            }
                            .padding(horizontal = 14.dp, vertical = 13.dp),
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
            }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            AddToListCta(onAddToShoppingListClick)
        }
    }

    if (false) onCameraClick()
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFEAF3ED))
                        .clickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_arrow_back),
                        contentDescription = null,
                        tint = NcGreen,
                        modifier = Modifier.size(17.dp),
                    )
                }
                Text(
                    text = "Añadir producto",
                    color = NcGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Text(
                text = "Añade un producto a tu lista\nde la compra",
                color = NcSubtitle,
                fontSize = 13.sp,
                modifier = Modifier.padding(start = 34.dp, end = 6.dp),
            )
        }
        Box(
            modifier = Modifier.size(150.dp, 112.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ref_shopping_hero_basket),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun AddToListCta(onAddToShoppingListClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NcSurface)
            .padding(start = 16.dp, end = 16.dp, top = 2.dp, bottom = 0.dp),
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
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_plus),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
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
private fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NcCard)
            .border(1.dp, Color(0xFFF0F2F4), RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        content = content,
    )
}

@Composable
private fun SectionTitle(
    text: String,
    fontSize: androidx.compose.ui.unit.TextUnit = 17.sp,
) {
    Text(
        text = text,
        color = Color(0xFF0D3E2F),
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
    )
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
            modifier = Modifier.size(48.dp),
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
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) Color(0xFFE9F2EC) else Color(0xFFF4F5F7))
            .border(1.dp, if (selected) NcGreen else Color(0xFFE2E5E9), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            color = if (selected) NcGreen else NcText,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Clip,
        )
    }
}

private data class CategoryUi(
    val label: String,
    val icon: DrawableResource,
    val bg: Color,
    val tint: Color,
)
