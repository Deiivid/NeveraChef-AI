package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem
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
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ic_nc_trash
import neverachefai.shared.generated.resources.ref_shopping_list_hero_premium
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val Ink = Color(0xFF03291E)
private val Muted = Color(0xFF4E5561)
private val SoftLine = Color(0xFFDDE2DE)
private val CardLine = Color(0xFFE6E8EB)
private val Green = Color(0xFF0A7A4B)
private val GreenSoft = Color(0xFFE8F3E8)
private val FabGreen = Color(0xFF037646)

private data class ShoppingListItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val iconRes: DrawableResource,
    val iconKey: String,
    val destinationKey: String,
    val quantityValue: String,
    val quantityUnit: String,
    val checked: Boolean,
)

@Composable
fun ShoppingListScreen(
    items: List<ShoppingListItem>,
    onItemsChange: (List<ShoppingListItem>) -> Unit,
    onFinalizePurchase: (List<ShoppingListItem>) -> List<ShoppingListItem>,
    onAddProductClick: () -> Unit = {},
) {
    val visibleItems = remember { mutableStateListOf<ShoppingListItemUi>() }
    var deleteMode by remember { mutableStateOf(false) }
    var selectedItemIds by remember { mutableStateOf(setOf<String>()) }
    var fixedTopContentHeightPx by remember { mutableIntStateOf(0) }
    val selectionMode = deleteMode || selectedItemIds.isNotEmpty()
    val density = LocalDensity.current
    val listTopPadding = with(density) { fixedTopContentHeightPx.toDp() }

    LaunchedEffect(items) {
        visibleItems.clear()
        visibleItems.addAll(items.map { it.toUi() })
    }

    val addedCount = visibleItems.size
    val markedCount = visibleItems.count { it.checked }
    val showFinalizePurchase = !selectionMode && visibleItems.any { it.checked }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    fixedTopContentHeightPx = coordinates.size.height
                },
        ) {
            ShoppingHeader(
                addedCount = addedCount,
                deleteMode = deleteMode,
                onDeleteClick = {
                    if (deleteMode) {
                        deleteMode = false
                        selectedItemIds = emptySet()
                    } else {
                        deleteMode = true
                    }
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(17.dp), clip = false)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFEEF7F2))
                    .border(1.dp, Color(0xFFD7EBDD), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFDCEFE5)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_check_square),
                        contentDescription = null,
                        tint = Green,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Text(
                    text = "Marca lo que ya tienes en tu cesta y finaliza la compra",
                    color = Ink,
                    fontSize = 13.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                MetricChip(
                    icon = Res.drawable.ic_nc_shopping_basket,
                    text = "$addedCount añadidos",
                    modifier = Modifier.weight(1f),
                )
                MetricChip(
                    icon = Res.drawable.ic_nc_check_square,
                    text = "$markedCount marcado${if (markedCount == 1) "" else "s"}",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = listTopPadding),
        ) {
            item { Spacer(modifier = Modifier.height(18.dp)) }

            if (selectionMode) {
                item {
                    SelectionBar(
                        count = selectedItemIds.size,
                        onDelete = {
                            if (selectedItemIds.isNotEmpty()) {
                                val kept = visibleItems.filterNot { it.id in selectedItemIds }
                                visibleItems.clear()
                                visibleItems.addAll(kept)
                                onItemsChange(visibleItems.map { it.toDomain() })
                            }
                            selectedItemIds = emptySet()
                            deleteMode = false
                        },
                        onCancel = {
                            selectedItemIds = emptySet()
                            deleteMode = false
                        },
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            itemsIndexed(visibleItems) { index, item ->
                ShoppingListRow(
                    item = item,
                    selectionMode = selectionMode,
                    deleteSelected = item.id in selectedItemIds,
                    onDeleteSelectedChange = { checked ->
                        selectedItemIds = if (checked) selectedItemIds + item.id else selectedItemIds - item.id
                    },
                    onLongSelect = {
                        if (!selectionMode) {
                            deleteMode = true
                            selectedItemIds = setOf(item.id)
                        }
                    },
                    onCheckedChange = { checked ->
                        if (!selectionMode) {
                            visibleItems[index] = item.copy(checked = checked)
                            onItemsChange(visibleItems.map { it.toDomain() })
                        }
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item { Spacer(modifier = Modifier.height(if (showFinalizePurchase) 100.dp else 120.dp)) }
        }

        if (showFinalizePurchase) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 20.dp)
                    .fillMaxWidth()
                    .height(62.dp)
                    .background(Color.White),
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 20.dp)
                    .padding(start = 0.dp, end = 0.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .height(54.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FinalizePurchaseButton(
                    onClick = {
                        val remaining = onFinalizePurchase(visibleItems.map { it.toDomain() })
                        visibleItems.clear()
                        visibleItems.addAll(remaining.map { it.toUi() })
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                )
                AddProductFab(
                    onClick = onAddProductClick,
                    modifier = Modifier.size(58.dp),
                )
            }
        } else {
            AddProductFab(
                onClick = onAddProductClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = 20.dp)
                    .padding(end = 22.dp, bottom = 12.dp)
                    .size(54.dp),
            )
        }
    }
}

@Composable
private fun FinalizePurchaseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(Green)
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Finalizar compra",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun AddProductFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .shadow(8.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(FabGreen)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_plus),
            contentDescription = "Añadir producto",
            tint = Color.White,
            modifier = Modifier.size(28.dp),
        )
    }
}

@Composable
private fun ShoppingHeader(
    addedCount: Int,
    deleteMode: Boolean,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(158.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.width(166.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Lista de\ncompra",
                    color = Ink,
                    fontSize = 30.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "$addedCount añadidos",
                        color = Ink,
                        fontSize = 18.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (deleteMode) Color(0xFFFFE2E2) else Color(0xFFFFF5F2))
                            .border(
                                1.dp,
                                if (deleteMode) Color(0xFFFFB8B8) else Color(0xFFF1DCD5),
                                RoundedCornerShape(10.dp),
                            )
                            .clickable(onClick = onDeleteClick),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_nc_trash),
                            contentDescription = if (deleteMode) "Cancelar borrado" else "Seleccionar para borrar",
                            tint = Color(0xFFE82222),
                            modifier = Modifier.size(23.dp),
                        )
                    }
                }
            }

            Image(
                painter = painterResource(Res.drawable.ref_shopping_list_hero_premium),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 0.dp)
                    .size(width = 220.dp, height = 158.dp),
            )
        }

    }
}

@Composable
private fun MetricChip(
    icon: DrawableResource?,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(42.dp)
            .shadow(3.dp, RoundedCornerShape(21.dp), clip = false)
            .clip(RoundedCornerShape(21.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE9DED3), RoundedCornerShape(21.dp))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Ink,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = text,
            color = Ink,
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun ShoppingListRow(
    item: ShoppingListItemUi,
    selectionMode: Boolean,
    deleteSelected: Boolean,
    onDeleteSelectedChange: (Boolean) -> Unit,
    onLongSelect: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(17.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(if (item.checked || (selectionMode && deleteSelected)) Color(0xFFF6FBF6) else Color.White)
            .border(
                1.dp,
                if (item.checked || (selectionMode && deleteSelected)) Green.copy(alpha = 0.45f) else CardLine,
                RoundedCornerShape(16.dp),
            )
            .combinedClickable(
                onClick = {
                    if (selectionMode) {
                        onDeleteSelectedChange(!deleteSelected)
                    } else {
                        onCheckedChange(!item.checked)
                    }
                },
                onLongClick = onLongSelect,
            )
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PremiumCheckControl(
            checked = if (selectionMode) deleteSelected else item.checked,
            danger = selectionMode,
            onClick = {
                if (selectionMode) {
                    onDeleteSelectedChange(!deleteSelected)
                } else {
                    onCheckedChange(!item.checked)
                }
            },
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = Ink,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = item.quantity,
                color = Muted,
                fontSize = 12.sp,
                lineHeight = 15.sp,
            )
            Spacer(modifier = Modifier.height(3.dp))
            LocationPill(destinationKey = item.destinationKey, checked = item.checked)
        }

        Image(
            painter = painterResource(item.iconRes),
            contentDescription = item.name,
            modifier = Modifier.size(60.dp),
        )
    }
}

@Composable
private fun PremiumCheckControl(
    checked: Boolean,
    danger: Boolean,
    onClick: () -> Unit,
) {
    val accent = if (danger) Color(0xFFE03131) else Green
    val bg = if (checked) accent else Color.White
    val border = if (checked) accent else Color(0xFFD9E1DB)

    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.5.dp, border, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 22.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun LocationPill(destinationKey: String, checked: Boolean = false) {
    val normalized = normalizeDestinationKey(destinationKey)
    val icon = when (normalized) {
        "fridge" -> Res.drawable.ic_nc_fridge
        "freezer" -> Res.drawable.ic_nc_freezer
        else -> Res.drawable.ic_nc_pantry
    }
    val label = when (normalized) {
        "fridge" -> "Nevera"
        "freezer" -> "Congelador"
        else -> "Despensa"
    }
    val tint = when (normalized) {
        "fridge" -> Color(0xFF006C4D)
        "freezer" -> Color(0xFF185CC4)
        else -> Color(0xFFB06B12)
    }
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(tint.copy(alpha = 0.14f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = label,
            color = tint,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SelectionBar(
    count: Int,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_trash),
                contentDescription = null,
                tint = Color(0xFFE03131),
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = "$count seleccionados",
                color = Color(0xFF1D1B20),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
            Surface(
                onClick = onDelete,
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFCE6E6),
            ) {
                Text(
                    text = "Eliminar",
                    color = Color(0xFFE03131),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                )
            }
        }
    }
}

private fun ShoppingListItem.toUi(): ShoppingListItemUi {
    val normalizedIconKey = normalizeIconKey(iconKey, category, name)
    val quantityLabel = if (quantity.isNotBlank()) quantity else listOf(quantityValue, quantityUnit).filter { it.isNotBlank() }.joinToString(" ")
    return ShoppingListItemUi(
        id = id,
        name = name,
        quantity = quantityLabel,
        iconRes = normalizedIconKey.toCategoryIconResource(),
        iconKey = normalizedIconKey,
        destinationKey = destinationKey,
        quantityValue = quantityValue,
        quantityUnit = quantityUnit,
        checked = checked,
    )
}

private fun ShoppingListItemUi.toDomain(): ShoppingListItem {
    return ShoppingListItem(
        id = id,
        name = name,
        quantity = quantity,
        quantityValue = quantityValue,
        quantityUnit = quantityUnit,
        checked = checked,
        category = iconKey,
        destinationKey = destinationKey,
        iconKey = iconKey,
    )
}

private fun normalizeDestinationKey(value: String): String {
    return when (value.trim().lowercase()) {
        "fridge", "nevera" -> "fridge"
        "freezer", "congelador" -> "freezer"
        "pantry", "despensa" -> "pantry"
        else -> "pantry"
    }
}

private fun normalizeIconKey(iconKey: String, category: String, name: String): String {
    val candidates = listOf(iconKey, category, name.lowercase())
    for (candidate in candidates) {
        val key = when (candidate) {
            "fruits" -> "fruits"
            "vegetables" -> "vegetables"
            "meat" -> "meat"
            "fish" -> "fish"
            "seafood" -> "seafood"
            "bread" -> "bread"
            "milk" -> "milk"
            "yogurts" -> "yogurts"
            "cheese" -> "cheese"
            "eggs" -> "eggs"
            "grains" -> "grains"
            "canned_food" -> "canned_food"
            "frozen" -> "frozen"
            "water" -> "water"
            "soft_drinks" -> "soft_drinks"
            "juice" -> "juice"
            "wine" -> "wine"
            "beer" -> "beer"
            "coffee_tea" -> "coffee_tea"
            "snacks" -> "snacks"
            "sweets" -> "sweets"
            "sauces" -> "sauces"
            "oil_vinegar" -> "oil_vinegar"
            "ready_meals" -> "ready_meals"
            "cleaning" -> "cleaning"
            "hygiene" -> "hygiene"
            "pets" -> "pets"
            "other" -> "other"
            else -> ""
        }
        if (key.isNotBlank()) return key
    }
    return "other"
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
