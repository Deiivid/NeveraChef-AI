package es.neverachefai.feature.shopping.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.PantryFoodRecord
import es.neverachefai.core.persistence.ShoppingItemRecord
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
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_trash
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private data class ShoppingListItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val iconRes: DrawableResource,
    val iconKey: String,
    val destinationKey: String,
    val checked: Boolean,
)

@Composable
fun ShoppingListScreen(
    onAddProductClick: () -> Unit = {},
) {
    val items = remember {
        mutableStateListOf<ShoppingListItemUi>().apply {
            addAll(LocalAppContentStore.loadShoppingItems().map { it.toUi() })
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            ShoppingSummaryCard(items = items)
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Lista",
                color = Color(0xFF1D1B20),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp,
            )

            Spacer(modifier = Modifier.height(12.dp))

            items.forEachIndexed { index, item ->
                SwipeableShoppingListRow(
                    item = item,
                    onCheckedChange = { checked ->
                        if (checked) {
                            moveItemToPantry(item)
                            items.removeAt(index)
                            LocalAppContentStore.saveShoppingItems(items.map { it.toRecord() })
                        } else {
                            items[index] = item.copy(checked = false)
                        }
                    },
                    onRemove = {
                        items.removeAt(index)
                        LocalAppContentStore.saveShoppingItems(items.map { it.toRecord() })
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(96.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF004BCA))
                .clickable(onClick = onAddProductClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_plus),
                contentDescription = "Añadir producto",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableShoppingListRow(
    item: ShoppingListItemUi,
    onCheckedChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()

    if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
        LaunchedEffect(dismissState.currentValue) {
            onCheckedChange(!item.checked)
            dismissState.snapTo(SwipeToDismissBoxValue.Settled)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = true,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFDEE2ED))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    text = "Marcar",
                    color = Color(0xFF004BCA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
    ) {
        ShoppingListRow(
            item = item,
            onCheckedChange = onCheckedChange,
            onRemove = onRemove,
        )
    }
}

@Composable
private fun ShoppingSummaryCard(items: List<ShoppingListItemUi>) {
    val added = items.size
    val marked = items.count { it.checked }
    val pending = added - marked

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFEF3C7))
            .padding(12.dp),
    ) {
        Text(
            text = "Lista de Compra",
            color = Color(0xFF1D1B20),
            fontSize = 28.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MetricChip(
                value = added,
                label = "AÑADIDOS",
                background = Color.White,
                valueColor = Color(0xFF1D1B20),
                labelColor = Color(0xFF92400E),
                modifier = Modifier.weight(1f),
            )
            MetricChip(
                value = pending,
                label = "PENDIENTES",
                background = Color(0xFFE0F2FE),
                valueColor = Color(0xFF0284C7),
                labelColor = Color(0xFF0369A1),
                modifier = Modifier.weight(1f),
            )
            MetricChip(
                value = marked,
                label = "MARCADOS",
                background = Color(0xFFDCFCE7),
                valueColor = Color(0xFF15803D),
                labelColor = Color(0xFF166534),
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MetricChip(
    value: Int,
    label: String,
    background: Color,
    valueColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = value.toString(),
            color = valueColor,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = label,
            color = labelColor,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ShoppingListRow(
    item: ShoppingListItemUi,
    onCheckedChange: (Boolean) -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (item.checked) Color(0xFFF8F2F9) else Color.White)
            .border(1.dp, Color(0xFFE6E1E8), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(24.dp),
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF004BCA),
                checkmarkColor = Color.White,
                uncheckedColor = Color(0xFF737687),
            ),
        )

        Spacer(modifier = Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = item.name,
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = if (item.checked) Color(0xFF737687) else Color(0xFF1D1B20),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
            )
            Text(
                text = item.quantity,
                color = Color(0xFF424656),
                fontSize = 14.sp,
                lineHeight = 20.sp,
            )
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(28.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_trash),
                contentDescription = "Eliminar ${item.name}",
                tint = Color(0xFF737687),
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

private fun ShoppingItemRecord.toUi(): ShoppingListItemUi {
    return ShoppingListItemUi(
        id = id,
        name = name,
        quantity = quantity,
        iconRes = iconKey.toCategoryIconResource(),
        iconKey = iconKey,
        destinationKey = destinationKey,
        checked = false,
    )
}

private fun ShoppingListItemUi.toRecord(): ShoppingItemRecord {
    return ShoppingItemRecord(
        id = id,
        name = name,
        quantity = quantity,
        category = iconKey,
        destinationKey = destinationKey,
        iconKey = iconKey,
    )
}

private fun moveItemToPantry(item: ShoppingListItemUi) {
    val pantryFoods = LocalAppContentStore.loadPantryFoods().toMutableList()
    pantryFoods += PantryFoodRecord(
        id = "pantry_${item.id}_${pantryFoods.size + 1}",
        name = item.name,
        quantity = item.quantity,
        category = item.iconKey,
        locationKey = item.destinationKey,
        expiryLabel = null,
        iconKey = item.iconKey,
    )
    LocalAppContentStore.savePantryFoods(pantryFoods)
}

private fun String.toCategoryIconResource(): DrawableResource {
    return when (this) {
        "fruits" -> Res.drawable.ic_cat_fruits
        "vegetables" -> Res.drawable.ic_cat_vegetables
        "meat" -> Res.drawable.ic_cat_meat
        "fish" -> Res.drawable.ic_cat_fish
        "seafood" -> Res.drawable.ic_cat_seafood
        "bread" -> Res.drawable.ic_cat_bread
        "milk" -> Res.drawable.ic_cat_milk
        "yogurts" -> Res.drawable.ic_cat_yogurts
        "cheese" -> Res.drawable.ic_cat_cheese
        "eggs" -> Res.drawable.ic_cat_eggs
        "grains" -> Res.drawable.ic_cat_pasta_rice_legumes
        "canned_food" -> Res.drawable.ic_cat_canned_food
        "frozen" -> Res.drawable.ic_cat_frozen
        "water" -> Res.drawable.ic_cat_water_bottle
        "soft_drinks" -> Res.drawable.ic_cat_soft_drinks
        "juice" -> Res.drawable.ic_cat_juice
        "wine" -> Res.drawable.ic_cat_wine
        "beer" -> Res.drawable.ic_cat_beer
        "coffee_tea" -> Res.drawable.ic_cat_coffee_tea
        "snacks" -> Res.drawable.ic_cat_snacks
        "sweets" -> Res.drawable.ic_cat_sweets
        "sauces" -> Res.drawable.ic_cat_sauces
        "oil_vinegar" -> Res.drawable.ic_cat_oil_vinegar
        "ready_meals" -> Res.drawable.ic_cat_ready_meals
        "cleaning" -> Res.drawable.ic_cat_cleaning
        "hygiene" -> Res.drawable.ic_cat_hygiene
        "pets" -> Res.drawable.ic_cat_pets
        "other" -> Res.drawable.ic_cat_other
        else -> Res.drawable.ic_cat_other
    }
}
