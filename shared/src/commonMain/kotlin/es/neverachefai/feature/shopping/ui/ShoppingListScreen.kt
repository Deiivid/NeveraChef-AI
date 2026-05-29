package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ref_shopping_hero_basket
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
    onAddProductClick: () -> Unit = {},
) {
    val items = remember {
        mutableStateListOf<ShoppingListItemUi>().apply {
            addAll(LocalAppContentStore.loadShoppingItems().map { it.toUi() })
        }
    }

    val addedCount = items.size
    val markedCount = items.count { it.checked }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 4.dp, end = 4.dp, top = 26.dp, bottom = 8.dp),
        ) {
            ShoppingHeader(addedCount = addedCount)
            Spacer(modifier = Modifier.height(12.dp))

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
                    icon = null,
                    text = "$markedCount marcado${if (markedCount == 1) "" else "s"}",
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            items.forEachIndexed { index, item ->
                ShoppingListRow(
                    item = item,
                    onCheckedChange = { checked ->
                        items[index] = item.copy(checked = checked)
                        LocalAppContentStore.saveShoppingItems(items.map { it.toRecord() })
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (items.any { it.checked }) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFE9F7EF))
                        .clickable { finalizeCheckedItems(items) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "Finalizar compra",
                        color = Green,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 18.dp, bottom = 22.dp)
                .size(62.dp)
                .clip(CircleShape)
                .background(FabGreen)
                .clickable(onClick = onAddProductClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_plus),
                contentDescription = "Añadir producto",
                tint = Color.White,
                modifier = Modifier.size(30.dp),
            )
        }
    }
}

@Composable
private fun ShoppingHeader(addedCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(148.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Lista de compra",
                    color = Ink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$addedCount añadidos",
                    color = Ink,
                    fontSize = 17.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Image(
                painter = painterResource(Res.drawable.ref_shopping_hero_basket),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(width = 150.dp, height = 174.dp),
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
            .height(48.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, SoftLine, RoundedCornerShape(28.dp))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Ink,
                modifier = Modifier.size(18.dp),
            )
        }
        Text(
            text = text,
            color = Ink,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun ShoppingListRow(
    item: ShoppingListItemUi,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (item.checked) Color(0xFFF5FAF5) else Color.White)
            .border(1.dp, CardLine, RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.size(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (item.checked) Green else Color.White)
                    .border(1.dp, if (item.checked) Green else Color(0xFF5A616D), CircleShape)
                    .clickable { onCheckedChange(!item.checked) },
                contentAlignment = Alignment.Center,
            ) {
                if (item.checked) {
                    Text(
                        text = "✓",
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = if (item.checked) Color(0xFF5D8B6E) else Ink,
                fontSize = 15.sp,
                lineHeight = 19.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (item.checked) TextDecoration.LineThrough else TextDecoration.None,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.quantity,
                color = Muted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        }

        Image(
            painter = painterResource(item.iconRes),
            contentDescription = item.name,
            modifier = Modifier.size(52.dp),
        )
    }
}

private fun ShoppingItemRecord.toUi(): ShoppingListItemUi {
    val normalizedIconKey = normalizeIconKey(iconKey, category, name)
    val quantityLabel = if (quantity.isNotBlank()) quantity else listOf(
        quantityValue,
        quantityUnit
    ).filter { it.isNotBlank() }.joinToString(" ")
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

private fun ShoppingListItemUi.toRecord(): ShoppingItemRecord {
    return ShoppingItemRecord(
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

private fun finalizeCheckedItems(items: MutableList<ShoppingListItemUi>) {
    val checkedItems = items.filter { it.checked }
    if (checkedItems.isEmpty()) return

    val pantryFoods = LocalAppContentStore.loadPantryFoods().toMutableList()
    checkedItems.forEach { item ->
        pantryFoods += PantryFoodRecord(
            id = "pantry_${item.id}_${pantryFoods.size + 1}",
            name = item.name,
            quantity = item.quantity,
            quantityValue = item.quantityValue,
            quantityUnit = item.quantityUnit,
            category = item.iconKey,
            locationKey = item.destinationKey,
            expiryLabel = null,
            expiryDateIso = null,
            iconKey = item.iconKey,
        )
    }

    LocalAppContentStore.savePantryFoods(pantryFoods)
    items.removeAll { it.checked }
    LocalAppContentStore.saveShoppingItems(items.map { it.toRecord() })
}

private fun normalizeIconKey(iconKey: String, category: String, name: String): String {
    val candidates = listOf(iconKey, category, name.lowercase())
    for (candidate in candidates) {
        val key = when {
            candidate == "fruits" -> "fruits"
            candidate == "vegetables" -> "vegetables"
            candidate == "meat" -> "meat"
            candidate == "fish" -> "fish"
            candidate == "seafood" -> "seafood"
            candidate == "bread" -> "bread"
            candidate == "milk" -> "milk"
            candidate == "yogurts" -> "yogurts"
            candidate == "cheese" -> "cheese"
            candidate == "eggs" -> "eggs"
            candidate == "grains" -> "grains"
            candidate == "canned_food" -> "canned_food"
            candidate == "frozen" -> "frozen"
            candidate == "water" -> "water"
            candidate == "soft_drinks" -> "soft_drinks"
            candidate == "juice" -> "juice"
            candidate == "wine" -> "wine"
            candidate == "beer" -> "beer"
            candidate == "coffee_tea" -> "coffee_tea"
            candidate == "snacks" -> "snacks"
            candidate == "sweets" -> "sweets"
            candidate == "sauces" -> "sauces"
            candidate == "oil_vinegar" -> "oil_vinegar"
            candidate == "ready_meals" -> "ready_meals"
            candidate == "cleaning" -> "cleaning"
            candidate == "hygiene" -> "hygiene"
            candidate == "pets" -> "pets"
            candidate == "other" -> "other"
            "tomate" in candidate || "verdura" in candidate || "fruta" in candidate -> "vegetables"
            "leche" in candidate -> "milk"
            "yogur" in candidate -> "yogurts"
            "queso" in candidate -> "cheese"
            "huevo" in candidate -> "eggs"
            "arroz" in candidate || "pasta" in candidate -> "grains"
            "pesc" in candidate -> "fish"
            "pollo" in candidate || "carne" in candidate -> "meat"
            "pan" in candidate -> "bread"
            "caldo" in candidate || "sopa" in candidate -> "ready_meals"
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
