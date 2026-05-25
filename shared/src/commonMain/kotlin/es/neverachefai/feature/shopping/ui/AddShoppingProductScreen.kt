package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
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
import neverachefai.shared.generated.resources.ic_cat_pasta_rice_legumes
import neverachefai.shared.generated.resources.ic_cat_hygiene
import neverachefai.shared.generated.resources.ic_cat_juice
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_milk
import neverachefai.shared.generated.resources.ic_cat_oil_vinegar
import neverachefai.shared.generated.resources.ic_cat_other
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
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_square
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val Ink = Color(0xFF1D1B20)
private val Muted = Color(0xFF424656)
private val Primary = Color(0xFF004BCA)
private val PrimaryContainer = Color(0xFF0061FF)
private val Surface = Color(0xFFFEF7FF)
private val SurfaceContainer = Color(0xFFF2ECF3)
private val SurfaceLow = Color(0xFFF8F2F9)
private val SurfaceVariant = Color(0xFFE6E1E8)
private val OutlineVariant = Color(0xFFC2C6D9)

enum class AddShoppingMode(
    val label: String,
    val iconRes: DrawableResource,
) {
    Manual("Manual", Res.drawable.ic_nc_square),
    Voice("Voz", Res.drawable.ic_nc_square),
    Camera("Camara", Res.drawable.ic_nc_square),
}

data class AddShoppingProductUiState(
    val selectedMode: AddShoppingMode = AddShoppingMode.Manual,
    val productName: String = "",
    val quantity: String = "1",
    val destination: String = "",
    val previewProducts: List<String> = emptyList(),
)

@Composable
fun AddShoppingProductScreen(
    state: AddShoppingProductUiState,
    onModeSelected: (AddShoppingMode) -> Unit,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onCameraClick: () -> Unit,
    onAddToShoppingListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val quantity = state.quantity.toIntOrNull()?.coerceAtLeast(1) ?: 1
    var showNewProductModal by remember { mutableStateOf(false) }
    val selectedCategory = state.destination
    val isWeightCategory = isWeightCategory(selectedCategory)

    Scaffold(
        modifier = modifier.background(Surface),
        containerColor = Surface,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SurfaceLow),
        ) {
            TopBar()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                SearchInput(state.productName, onProductNameChange)
                AddNewProductButton { showNewProductModal = true }
                CategoryStrip(
                    selectedCategory = selectedCategory,
                    onCategorySelected = onDestinationChange,
                )
                if (isWeightCategory) {
                    WeightInputSection(
                        productName = state.productName,
                        quantityText = state.quantity,
                        onProductNameChange = onProductNameChange,
                        onQuantityChange = onQuantityChange,
                    )
                } else {
                    QuantitySelector(
                        quantity = quantity,
                        onDecrease = { onQuantityChange((quantity - 1).coerceAtLeast(1).toString()) },
                        onIncrease = { onQuantityChange((quantity + 1).toString()) },
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onAddToShoppingListClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_plus),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Añadir a la lista",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        if (showNewProductModal) {
            AddShoppingProductScreenModal(
                productName = state.productName,
                quantity = quantity,
                selectedUnit = quantityToUnit(state.quantity),
                onProductNameChange = onProductNameChange,
                onDecrease = { onQuantityChange((quantity - 1).coerceAtLeast(1).toString()) },
                onIncrease = { onQuantityChange((quantity + 1).toString()) },
                onUnitChange = { unit ->
                    val updated = when (unit) {
                        "g" -> "500 g"
                        "1 kg", "2 kg", "3 kg" -> unit
                        else -> quantity.toString()
                    }
                    onQuantityChange(updated)
                },
                onConfirm = {
                    showNewProductModal = false
                    onAddToShoppingListClick()
                },
                onDismiss = { showNewProductModal = false },
            )
        }
    }

    if (false) {
        onVoiceClick()
        onCameraClick()
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp).background(PrimaryContainer),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("←", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "Añadir Producto",
            color = Color.White,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f).padding(end = 36.dp),
        )
    }
}

@Composable
private fun SearchInput(
    value: String,
    onValueChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "¿Qué necesitas comprar?",
            color = Muted,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    "Ej. Tomates fritos, Aguacate...",
                    color = Muted.copy(alpha = 0.5f),
                    fontSize = 16.sp
                )
            },
            leadingIcon = { Text("⌕", color = Muted, fontSize = 18.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineVariant,
                focusedTextColor = Ink,
                unfocusedTextColor = Ink,
            ),
        )
    }
}

@Composable
private fun AddNewProductButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = PrimaryContainer.copy(alpha = 0.08f),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Primary.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(999.dp))
                    .background(Primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_plus),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(16.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Añadir como nuevo producto",
                    color = Primary,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Si no encuentras lo que buscas",
                    color = Muted,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            Text("›", color = Primary, fontSize = 20.sp)
        }
    }
}

@Composable
private fun CategoryStrip(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
) {
    val categories = listOf(
        CategoryUi("Frutas", Res.drawable.ic_cat_fruits, Color(0xFFF8E4A8)),
        CategoryUi("Verduras", Res.drawable.ic_cat_vegetables, Color(0xFFC6E9AC)),
        CategoryUi("Carne", Res.drawable.ic_cat_meat, Color(0xFFF0C5C8)),
        CategoryUi("Pescado", Res.drawable.ic_cat_fish, Color(0xFFBFD4E8)),
        CategoryUi("Marisco", Res.drawable.ic_cat_seafood, Color(0xFFC9E0E8)),
        CategoryUi("Pan", Res.drawable.ic_cat_bread, Color(0xFFF6D8A7)),
        CategoryUi("Leche", Res.drawable.ic_cat_milk, Color(0xFFDDEAF6)),
        CategoryUi("Yogures", Res.drawable.ic_cat_yogurts, Color(0xFFCBE3F4)),
        CategoryUi("Queso", Res.drawable.ic_cat_cheese, Color(0xFFFCE99B)),
        CategoryUi("Huevos", Res.drawable.ic_cat_eggs, Color(0xFFE9D6BE)),
        CategoryUi("Pasta/Arroz", Res.drawable.ic_cat_pasta_rice_legumes, Color(0xFFE9D9A7)),
        CategoryUi("Conservas", Res.drawable.ic_cat_canned_food, Color(0xFFD9D0C4)),
        CategoryUi("Congelados", Res.drawable.ic_cat_frozen, Color(0xFFCCE1F4)),
        CategoryUi("Agua", Res.drawable.ic_cat_water_bottle, Color(0xFFBBDAF0)),
        CategoryUi("Zumo", Res.drawable.ic_cat_juice, Color(0xFFF2D1A8)),
        CategoryUi("Vino", Res.drawable.ic_cat_wine, Color(0xFFE6C4CF)),
        CategoryUi("Cerveza", Res.drawable.ic_cat_beer, Color(0xFFEFD89E)),
        CategoryUi("Café/Te", Res.drawable.ic_cat_coffee_tea, Color(0xFFE5DACC)),
        CategoryUi("Snacks", Res.drawable.ic_cat_snacks, Color(0xFFEFDFA4)),
        CategoryUi("Dulces", Res.drawable.ic_cat_sweets, Color(0xFFF1C0C5)),
        CategoryUi("Salsas", Res.drawable.ic_cat_sauces, Color(0xFFF1B7B7)),
        CategoryUi("Aceite/Vinagre", Res.drawable.ic_cat_oil_vinegar, Color(0xFFE5E2AC)),
        CategoryUi("Platos listos", Res.drawable.ic_cat_ready_meals, Color(0xFFEED4B8)),
        CategoryUi("Limpieza", Res.drawable.ic_cat_cleaning, Color(0xFFBFDDF1)),
        CategoryUi("Higiene", Res.drawable.ic_cat_hygiene, Color(0xFFC7E6E4)),
        CategoryUi("Mascotas", Res.drawable.ic_cat_pets, Color(0xFFE4D8C8)),
        CategoryUi("Otros", Res.drawable.ic_cat_other, Color(0xFFE5D7BF)),
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Categorías",
            color = Ink,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            categories.forEach { category ->
                val selected = selectedCategory == category.label
                Surface(
                    onClick = { onCategorySelected(category.label) },
                    color = Color.Transparent,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp))
                                .background(if (selected) category.bg.copy(alpha = 0.95f) else category.bg.copy(alpha = 0.55f))
                                .border(
                                    width = if (selected) 1.5.dp else 0.dp,
                                    color = if (selected) Primary else Color.Transparent,
                                    shape = RoundedCornerShape(14.dp),
                                )
                            ,contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = painterResource(category.icon),
                                contentDescription = category.label,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(34.dp),
                            )
                        }
                        Text(
                            category.label,
                            color = if (selected) Primary else Ink,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightInputSection(
    productName: String,
    quantityText: String,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
) {
    var selectedWeightMode by remember(quantityText) {
        mutableStateOf(
            when {
                quantityText.endsWith("kg") -> quantityText
                quantityText.endsWith("g") -> "g"
                else -> "g"
            },
        )
    }
    var gramsText by remember(quantityText) {
        mutableStateOf(quantityText.removeSuffix(" g").trim().takeIf { quantityText.endsWith("g") } ?: "500")
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Nombre del producto",
            color = Muted,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
        OutlinedTextField(
            value = productName,
            onValueChange = onProductNameChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text("Ej. Arroz redondo", color = Muted.copy(alpha = 0.55f), fontSize = 16.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineVariant,
                focusedTextColor = Ink,
                unfocusedTextColor = Ink,
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainer)
                .border(1.dp, SurfaceVariant, RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(999.dp)).background(SurfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("#", color = Muted, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                }
                Text("Cantidad", color = Ink, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(
                modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Color.White).padding(3.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WeightChip(
                    selected = selectedWeightMode == "g",
                    label = "g",
                    onClick = {
                        selectedWeightMode = "g"
                        onQuantityChange("${gramsText.ifBlank { "500" }} g")
                    },
                )
                listOf("1 kg", "2 kg", "3 kg").forEach { option ->
                    WeightChip(
                        selected = selectedWeightMode == option,
                        label = option,
                        onClick = {
                            selectedWeightMode = option
                            onQuantityChange(option)
                        },
                    )
                }
            }
        }
        if (selectedWeightMode == "g") {
            OutlinedTextField(
                value = gramsText,
                onValueChange = {
                    val filtered = it.filter(Char::isDigit).take(4)
                    gramsText = filtered
                    if (filtered.isNotBlank()) onQuantityChange("$filtered g")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Cantidad en gramos", color = Muted.copy(alpha = 0.55f), fontSize = 14.sp) },
                suffix = { Text("g", color = Muted, fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = OutlineVariant,
                    focusedTextColor = Ink,
                    unfocusedTextColor = Ink,
                ),
            )
        }
        HorizontalDivider(color = OutlineVariant.copy(alpha = 0.5f))
    }
}

@Composable
private fun WeightChip(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        color = if (selected) Primary else Color.Transparent,
        shape = RoundedCornerShape(999.dp),
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Ink,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun QuantitySelector(
    quantity: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainer)
            .border(1.dp, SurfaceVariant, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(999.dp))
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text("#", color = Muted, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(
                "Cantidad",
                color = Ink,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(Surface).padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            IconButton(onClick = onDecrease, modifier = Modifier.size(32.dp)) {
                Text("−", color = Muted, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = quantity.toString(),
                color = Ink,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(24.dp),
            )
            Box(
                modifier = Modifier.size(32.dp).clip(RoundedCornerShape(999.dp))
                    .background(PrimaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                IconButton(onClick = onIncrease, modifier = Modifier.size(32.dp)) {
                    Text(
                        "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private data class CategoryUi(
    val label: String,
    val icon: DrawableResource,
    val bg: Color,
)

private fun isWeightCategory(category: String): Boolean = category in setOf(
    "Pasta/Arroz",
    "Carne",
    "Pescado",
    "Marisco",
    "Frutas",
    "Verduras",
    "Queso",
)

private fun quantityToUnit(quantityText: String): String {
    return when {
        quantityText.endsWith(" g") -> "g"
        quantityText == "1 kg" || quantityText == "2 kg" || quantityText == "3 kg" -> quantityText
        else -> "ud"
    }
}
