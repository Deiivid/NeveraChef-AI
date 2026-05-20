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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                SearchInput(state.productName, onProductNameChange)
                AddNewProductButton { onModeSelected(AddShoppingMode.Manual) }
                CategoryStrip(onDestinationChange)
                QuantitySelector(
                    quantity = quantity,
                    onDecrease = { onQuantityChange((quantity - 1).coerceAtLeast(1).toString()) },
                    onIncrease = { onQuantityChange((quantity + 1).toString()) },
                )
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
                        "Anadir a la lista",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
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
        Text("←", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(start = 16.dp))
        Text(
            text = "Anadir Producto",
            color = Color.White,
            fontSize = 30.sp,
            lineHeight = 40.sp,
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
            "¿Que necesitas comprar?",
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
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Primary.copy(alpha = 0.35f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
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
                    "Anadir como nuevo producto",
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
private fun CategoryStrip(onCategorySelected: (String) -> Unit) {
    val categories = listOf(
        CategoryUi("Frutas", Res.drawable.ic_cat_fruits),
        CategoryUi("Verduras", Res.drawable.ic_cat_vegetables),
        CategoryUi("Carne", Res.drawable.ic_cat_meat),
        CategoryUi("Pescado", Res.drawable.ic_cat_fish),
        CategoryUi("Marisco", Res.drawable.ic_cat_seafood),
        CategoryUi("Pan", Res.drawable.ic_cat_bread),
        CategoryUi("Leche", Res.drawable.ic_cat_milk),
        CategoryUi("Yogures", Res.drawable.ic_cat_yogurts),
        CategoryUi("Queso", Res.drawable.ic_cat_cheese),
        CategoryUi("Huevos", Res.drawable.ic_cat_eggs),
        CategoryUi("Pasta/Arroz", Res.drawable.ic_cat_pasta_rice_legumes),
        CategoryUi("Conservas", Res.drawable.ic_cat_canned_food),
        CategoryUi("Congelados", Res.drawable.ic_cat_frozen),
        CategoryUi("Agua", Res.drawable.ic_cat_water_bottle),
        CategoryUi("Zumo", Res.drawable.ic_cat_juice),
        CategoryUi("Vino", Res.drawable.ic_cat_wine),
        CategoryUi("Cerveza", Res.drawable.ic_cat_beer),
        CategoryUi("Cafe/Te", Res.drawable.ic_cat_coffee_tea),
        CategoryUi("Snacks", Res.drawable.ic_cat_snacks),
        CategoryUi("Dulces", Res.drawable.ic_cat_sweets),
        CategoryUi("Salsas", Res.drawable.ic_cat_sauces),
        CategoryUi("Aceite/Vinagre", Res.drawable.ic_cat_oil_vinegar),
        CategoryUi("Platos listos", Res.drawable.ic_cat_ready_meals),
        CategoryUi("Limpieza", Res.drawable.ic_cat_cleaning),
        CategoryUi("Higiene", Res.drawable.ic_cat_hygiene),
        CategoryUi("Mascotas", Res.drawable.ic_cat_pets),
        CategoryUi("Otros", Res.drawable.ic_cat_other),
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "Categorias",
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
                Surface(
                    onClick = { onCategorySelected(category.label) },
                    color = Color.Transparent
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp))
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
                            color = Ink,
                            fontSize = 11.sp,
                            lineHeight = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
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
)
