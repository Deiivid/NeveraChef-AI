package es.neverachefai.feature.shopping.ui

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.neverachefai.core.ads.NeveraChefBannerAd
import es.neverachefai.feature.shopping.domain.model.ShoppingListItem
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_beer
import neverachefai.shared.generated.resources.ic_cat_bread
import neverachefai.shared.generated.resources.ic_cat_canned_food
import neverachefai.shared.generated.resources.ic_cat_cheese
import neverachefai.shared.generated.resources.ic_cat_cleaning
import neverachefai.shared.generated.resources.ic_cat_coffee
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_frozen
import neverachefai.shared.generated.resources.ic_cat_fruits
import neverachefai.shared.generated.resources.ic_cat_hygiene
import neverachefai.shared.generated.resources.ic_cat_juice
import neverachefai.shared.generated.resources.ic_cat_legumes
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_milk
import neverachefai.shared.generated.resources.ic_cat_oil
import neverachefai.shared.generated.resources.ic_cat_other
import neverachefai.shared.generated.resources.ic_cat_pasta
import neverachefai.shared.generated.resources.ic_cat_pets
import neverachefai.shared.generated.resources.ic_cat_ready_meals
import neverachefai.shared.generated.resources.ic_cat_rice
import neverachefai.shared.generated.resources.ic_cat_sauces
import neverachefai.shared.generated.resources.ic_cat_seafood
import neverachefai.shared.generated.resources.ic_cat_snacks
import neverachefai.shared.generated.resources.ic_cat_soft_drinks
import neverachefai.shared.generated.resources.ic_cat_sweets
import neverachefai.shared.generated.resources.ic_cat_tea
import neverachefai.shared.generated.resources.ic_cat_vegetables
import neverachefai.shared.generated.resources.ic_cat_vinegar
import neverachefai.shared.generated.resources.ic_cat_water_bottle
import neverachefai.shared.generated.resources.ic_cat_wine
import neverachefai.shared.generated.resources.ic_cat_yogurts
import neverachefai.shared.generated.resources.ic_bread_baguette
import neverachefai.shared.generated.resources.ic_bread_burger
import neverachefai.shared.generated.resources.ic_bread_hotdog
import neverachefai.shared.generated.resources.ic_bread_sliced
import neverachefai.shared.generated.resources.ic_cleaning_dish_soap
import neverachefai.shared.generated.resources.ic_cleaning_laundry
import neverachefai.shared.generated.resources.ic_fish_cod
import neverachefai.shared.generated.resources.ic_fish_hake
import neverachefai.shared.generated.resources.ic_fish_salmon
import neverachefai.shared.generated.resources.ic_fish_sardine
import neverachefai.shared.generated.resources.ic_fish_seabass
import neverachefai.shared.generated.resources.ic_fish_trout
import neverachefai.shared.generated.resources.ic_fish_tuna
import neverachefai.shared.generated.resources.ic_fruit_apple
import neverachefai.shared.generated.resources.ic_fruit_avocado
import neverachefai.shared.generated.resources.ic_fruit_banana
import neverachefai.shared.generated.resources.ic_fruit_blackberry
import neverachefai.shared.generated.resources.ic_fruit_blueberry
import neverachefai.shared.generated.resources.ic_fruit_cherry
import neverachefai.shared.generated.resources.ic_fruit_cherimoya
import neverachefai.shared.generated.resources.ic_fruit_coconut
import neverachefai.shared.generated.resources.ic_fruit_date
import neverachefai.shared.generated.resources.ic_fruit_fig
import neverachefai.shared.generated.resources.ic_fruit_grape
import neverachefai.shared.generated.resources.ic_fruit_grapefruit
import neverachefai.shared.generated.resources.ic_fruit_kiwi
import neverachefai.shared.generated.resources.ic_fruit_lemon
import neverachefai.shared.generated.resources.ic_fruit_lime
import neverachefai.shared.generated.resources.ic_fruit_loquat
import neverachefai.shared.generated.resources.ic_fruit_mandarin
import neverachefai.shared.generated.resources.ic_fruit_mango
import neverachefai.shared.generated.resources.ic_fruit_melon
import neverachefai.shared.generated.resources.ic_fruit_orange
import neverachefai.shared.generated.resources.ic_fruit_papaya
import neverachefai.shared.generated.resources.ic_fruit_peach
import neverachefai.shared.generated.resources.ic_fruit_pear
import neverachefai.shared.generated.resources.ic_fruit_persimmon
import neverachefai.shared.generated.resources.ic_fruit_pineapple
import neverachefai.shared.generated.resources.ic_fruit_plum
import neverachefai.shared.generated.resources.ic_fruit_pomegranate
import neverachefai.shared.generated.resources.ic_fruit_raspberry
import neverachefai.shared.generated.resources.ic_fruit_strawberry
import neverachefai.shared.generated.resources.ic_fruit_tomato
import neverachefai.shared.generated.resources.ic_fruit_watermelon
import neverachefai.shared.generated.resources.ic_hygiene_dental
import neverachefai.shared.generated.resources.ic_hygiene_shampoo
import neverachefai.shared.generated.resources.ic_juice_apple
import neverachefai.shared.generated.resources.ic_juice_blueberry
import neverachefai.shared.generated.resources.ic_juice_grape
import neverachefai.shared.generated.resources.ic_juice_multifruit
import neverachefai.shared.generated.resources.ic_juice_orange
import neverachefai.shared.generated.resources.ic_juice_peach
import neverachefai.shared.generated.resources.ic_juice_pineapple
import neverachefai.shared.generated.resources.ic_juice_tomato
import neverachefai.shared.generated.resources.ic_meat_beef
import neverachefai.shared.generated.resources.ic_meat_chicken
import neverachefai.shared.generated.resources.ic_meat_lamb
import neverachefai.shared.generated.resources.ic_meat_pork
import neverachefai.shared.generated.resources.ic_meat_rabbit
import neverachefai.shared.generated.resources.ic_meat_sausage
import neverachefai.shared.generated.resources.ic_meat_turkey
import neverachefai.shared.generated.resources.ic_milk_lactose_free
import neverachefai.shared.generated.resources.ic_milk_semi
import neverachefai.shared.generated.resources.ic_milk_skimmed
import neverachefai.shared.generated.resources.ic_milk_whole
import neverachefai.shared.generated.resources.ic_pet_cat_food
import neverachefai.shared.generated.resources.ic_pet_dog_food
import neverachefai.shared.generated.resources.ic_pet_litter
import neverachefai.shared.generated.resources.ic_pet_treats
import neverachefai.shared.generated.resources.img_purchase_basket
import neverachefai.shared.generated.resources.img_purchase_check
import neverachefai.shared.generated.resources.img_purchase_inventory
import neverachefai.shared.generated.resources.ic_snack_chips
import neverachefai.shared.generated.resources.ic_snack_crackers
import neverachefai.shared.generated.resources.ic_snack_nuts
import neverachefai.shared.generated.resources.ic_sweets_bonbons
import neverachefai.shared.generated.resources.ic_sweets_candy
import neverachefai.shared.generated.resources.ic_sweets_chocolate
import neverachefai.shared.generated.resources.ic_sweets_cookies
import neverachefai.shared.generated.resources.ic_sweets_pastry
import neverachefai.shared.generated.resources.ic_vegetable_artichoke
import neverachefai.shared.generated.resources.ic_vegetable_carrot
import neverachefai.shared.generated.resources.ic_vegetable_cauliflower
import neverachefai.shared.generated.resources.ic_vegetable_cucumber
import neverachefai.shared.generated.resources.ic_vegetable_eggplant
import neverachefai.shared.generated.resources.ic_vegetable_garlic
import neverachefai.shared.generated.resources.ic_vegetable_green_beans
import neverachefai.shared.generated.resources.ic_vegetable_leek
import neverachefai.shared.generated.resources.ic_vegetable_lettuce
import neverachefai.shared.generated.resources.ic_vegetable_onion
import neverachefai.shared.generated.resources.ic_vegetable_peas
import neverachefai.shared.generated.resources.ic_vegetable_pepper
import neverachefai.shared.generated.resources.ic_vegetable_potato
import neverachefai.shared.generated.resources.ic_vegetable_pumpkin
import neverachefai.shared.generated.resources.ic_vegetable_spinach
import neverachefai.shared.generated.resources.ic_vegetable_zucchini
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_metric_basket_premium
import neverachefai.shared.generated.resources.ic_nc_metric_check_premium
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
    var showShoppingInfo by remember { mutableStateOf(false) }
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
    val finalizeActionHeight = 42.dp
    val fabSize = 44.dp
    val actionBottomMargin = 10.dp
    val actionSpacer = if (showFinalizePurchase) finalizeActionHeight else fabSize
    val bannerHeight = 50.dp
    val bottomControlsGap = 8.dp
    val bottomOverlayHeight = if (showFinalizePurchase) {
        bannerHeight + bottomControlsGap + finalizeActionHeight + actionBottomMargin
    } else {
        bannerHeight + actionBottomMargin
    }
    val bannerBottomPadding by animateDpAsState(
        targetValue = if (showFinalizePurchase) {
            actionBottomMargin + finalizeActionHeight + bottomControlsGap
        } else {
            actionBottomMargin
        },
        label = "shopping-banner-bottom",
    )
    val bannerEndPadding by animateDpAsState(
        targetValue = if (showFinalizePurchase) 0.dp else fabSize + 10.dp,
        label = "shopping-banner-end",
    )
    val onToggleDeleteMode = {
        if (deleteMode) {
            deleteMode = false
            selectedItemIds = emptySet()
        } else {
            deleteMode = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 4.dp, end = 4.dp, top = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    fixedTopContentHeightPx = coordinates.size.height
                },
        ) {
            ShoppingHeader(
                onInfoClick = { showShoppingInfo = true },
            )
            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                MetricChip(
                    icon = Res.drawable.ic_nc_metric_basket_premium,
                    text = "$addedCount añadidos",
                    modifier = Modifier.weight(1f),
                )
                MetricChip(
                    icon = Res.drawable.ic_nc_metric_check_premium,
                    text = "$markedCount marcado${if (markedCount == 1) "" else "s"}",
                    modifier = Modifier.weight(1f),
                )
                ShoppingDeleteButton(
                    deleteMode = deleteMode,
                    onClick = onToggleDeleteMode,
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

            item { Spacer(modifier = Modifier.height(bottomOverlayHeight)) }
        }

        NeveraChefBannerAd(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(end = bannerEndPadding, bottom = bannerBottomPadding)
                .height(bannerHeight),
        )

        if (showFinalizePurchase) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 20.dp, end = 20.dp, bottom = actionBottomMargin)
                    .fillMaxWidth()
                    .height(finalizeActionHeight),
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
                        .height(finalizeActionHeight),
                )
                AddProductFab(
                    onClick = onAddProductClick,
                    modifier = Modifier.size(fabSize),
                )
            }
        } else {
            AddProductFab(
                onClick = onAddProductClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = actionBottomMargin)
                    .size(fabSize),
            )
        }
    }

    if (showShoppingInfo) {
        ShoppingInfoDialog(onDismiss = { showShoppingInfo = false })
    }
}

@Composable
private fun ShoppingInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp)
                .shadow(18.dp, RoundedCornerShape(30.dp), clip = false)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFFFFFBF4))
                .border(1.dp, Color(0xFFEADDCB), RoundedCornerShape(30.dp))
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.img_purchase_basket),
                contentDescription = null,
                modifier = Modifier.size(76.dp),
            )

            Text(
                text = "Finalizar compra",
                color = Ink,
                fontSize = 25.sp,
                lineHeight = 27.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Toca un producto para marcarlo como comprado. Al finalizar:",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                ShoppingInfoActionRow(
                    leadingIcon = Res.drawable.img_purchase_check,
                    trailingIcon = Res.drawable.img_purchase_check,
                    title = "Marca lo comprado",
                )
                ShoppingInfoActionRow(
                    leadingIcon = Res.drawable.img_purchase_inventory,
                    trailingIcon = Res.drawable.img_purchase_check,
                    title = "Pasa al inventario",
                )
            }

            Text(
                text = "Lo pendiente se queda en compra.",
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(Green)
                    .clickable(onClick = onDismiss),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Entendido",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun ShoppingInfoActionRow(
    leadingIcon: DrawableResource,
    trailingIcon: DrawableResource,
    title: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE9DED3), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color(0xFFE8F3E8)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(leadingIcon),
                contentDescription = null,
                modifier = Modifier.size(21.dp),
            )
        }
        Text(
            text = title,
            color = Ink,
            fontSize = 15.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F7F1)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun ShoppingInfoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE9DED3), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "i",
            color = Green,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ShoppingDeleteButton(
    deleteMode: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .shadow(3.dp, CircleShape, clip = false)
            .clip(CircleShape)
            .background(if (deleteMode) Color(0xFFFFE2E2) else Color.White)
            .border(
                1.dp,
                if (deleteMode) Color(0xFFFFB8B8) else Color(0xFFE9DED3),
                CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_trash),
            contentDescription = if (deleteMode) "Cancelar borrado" else "Seleccionar para borrar",
            tint = Color(0xFFE82222),
            modifier = Modifier.size(20.dp),
        )
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
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Finalizar compra",
            color = Color.White,
            fontSize = 15.sp,
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
            modifier = Modifier.size(23.dp),
        )
    }
}

@Composable
private fun ShoppingHeader(
    onInfoClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(118.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.width(166.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Lista de",
                    color = Ink,
                    fontSize = 29.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "compra",
                        color = Ink,
                        fontSize = 29.sp,
                        lineHeight = 31.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    ShoppingInfoButton(onClick = onInfoClick)
                }
            }

            Image(
                painter = painterResource(Res.drawable.ref_shopping_list_hero_premium),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 0.dp)
                    .size(width = 190.dp, height = 118.dp),
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
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(21.dp),
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
    val rowMarked = !selectionMode && item.checked
    val rowDeleteSelected = selectionMode && deleteSelected

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(17.dp), clip = false)
            .clip(RoundedCornerShape(16.dp))
            .background(
                when {
                    rowDeleteSelected -> Color(0xFFFFF7F7)
                    rowMarked -> Color(0xFFF6FBF6)
                    else -> Color.White
                },
            )
            .border(
                1.dp,
                when {
                    rowDeleteSelected -> Color(0xFFE03131).copy(alpha = 0.55f)
                    rowMarked -> Green.copy(alpha = 0.45f)
                    else -> CardLine
                },
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
            .padding(horizontal = 12.dp, vertical = 6.dp),
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

        Spacer(modifier = Modifier.width(9.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                color = Ink,
                fontSize = 15.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.quantity,
                    color = Muted,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                )
                LocationPill(destinationKey = item.destinationKey, checked = item.checked)
            }
        }

        Image(
            painter = painterResource(item.iconRes),
            contentDescription = item.name,
            modifier = Modifier.size(54.dp),
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
            .size(32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.5.dp, border, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Text(
                text = "✓",
                color = Color.White,
                fontSize = 19.sp,
                lineHeight = 19.sp,
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
        category = shoppingCategoryIconKeyForProductIcon(iconKey),
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
    val joined = listOf(iconKey, category, name).joinToString(" ").normalizeCategoryText()
    inferShoppingIconKey(joined)?.let { return it }
    when {
        joined.containsAny("macarron", "espagueti", "spaghetti", "pasta", "fideo") -> return "pasta"
        joined.containsAny("arroz", "rice", "cereal") -> return "rice"
        joined.containsAny("lenteja", "garbanzo", "alubia", "legumbre") -> return "legumes"
        joined.containsAny("cafe", "coffee") -> return "coffee"
        joined.containsToken("te", "tea", "infusion") -> return "tea"
        joined.containsAny("vinagre", "vinegar") -> return "vinegar"
        joined.containsAny("aceite", "oil") -> return "oil"
    }
    val candidates = listOf(iconKey, category, name.lowercase())
    for (candidate in candidates) {
        val key = when (candidate) {
            "fruits" -> "fruits"
            "fruit_tomato" -> "fruit_tomato"
            "fruit_apple" -> "fruit_apple"
            "fruit_pear" -> "fruit_pear"
            "fruit_banana" -> "fruit_banana"
            "fruit_orange" -> "fruit_orange"
            "fruit_mandarin" -> "fruit_mandarin"
            "fruit_lemon" -> "fruit_lemon"
            "fruit_lime" -> "fruit_lime"
            "fruit_grapefruit" -> "fruit_grapefruit"
            "fruit_strawberry" -> "fruit_strawberry"
            "fruit_raspberry" -> "fruit_raspberry"
            "fruit_blackberry" -> "fruit_blackberry"
            "fruit_blueberry" -> "fruit_blueberry"
            "fruit_grape" -> "fruit_grape"
            "fruit_kiwi" -> "fruit_kiwi"
            "fruit_melon" -> "fruit_melon"
            "fruit_watermelon" -> "fruit_watermelon"
            "fruit_peach" -> "fruit_peach"
            "fruit_plum" -> "fruit_plum"
            "fruit_cherry" -> "fruit_cherry"
            "fruit_pomegranate" -> "fruit_pomegranate"
            "fruit_persimmon" -> "fruit_persimmon"
            "fruit_loquat" -> "fruit_loquat"
            "fruit_fig" -> "fruit_fig"
            "fruit_date" -> "fruit_date"
            "fruit_coconut" -> "fruit_coconut"
            "fruit_cherimoya" -> "fruit_cherimoya"
            "fruit_mango" -> "fruit_mango"
            "fruit_papaya" -> "fruit_papaya"
            "fruit_pineapple" -> "fruit_pineapple"
            "fruit_avocado" -> "fruit_avocado"
            "vegetables" -> "vegetables"
            "meat" -> "meat"
            "meat_beef" -> "meat_beef"
            "meat_pork" -> "meat_pork"
            "meat_chicken" -> "meat_chicken"
            "meat_turkey" -> "meat_turkey"
            "meat_lamb" -> "meat_lamb"
            "meat_rabbit" -> "meat_rabbit"
            "meat_sausage" -> "meat_sausage"
            "fish" -> "fish"
            "fish_hake" -> "fish_hake"
            "fish_cod" -> "fish_cod"
            "fish_seabass" -> "fish_seabass"
            "fish_salmon" -> "fish_salmon"
            "fish_tuna" -> "fish_tuna"
            "fish_sardine" -> "fish_sardine"
            "fish_trout" -> "fish_trout"
            "seafood" -> "seafood"
            "vegetable_potato" -> "vegetable_potato"
            "vegetable_onion" -> "vegetable_onion"
            "vegetable_carrot" -> "vegetable_carrot"
            "vegetable_pepper" -> "vegetable_pepper"
            "vegetable_zucchini" -> "vegetable_zucchini"
            "vegetable_cucumber" -> "vegetable_cucumber"
            "vegetable_lettuce" -> "vegetable_lettuce"
            "vegetable_spinach" -> "vegetable_spinach"
            "vegetable_garlic" -> "vegetable_garlic"
            "vegetable_peas" -> "vegetable_peas"
            "vegetable_green_beans" -> "vegetable_green_beans"
            "vegetable_eggplant" -> "vegetable_eggplant"
            "vegetable_pumpkin" -> "vegetable_pumpkin"
            "vegetable_artichoke" -> "vegetable_artichoke"
            "vegetable_leek" -> "vegetable_leek"
            "vegetable_cauliflower" -> "vegetable_cauliflower"
            "bread_baguette" -> "bread_baguette"
            "bread_sliced" -> "bread_sliced"
            "bread_burger" -> "bread_burger"
            "bread_hotdog" -> "bread_hotdog"
            "bread" -> "bread"
            "milk_whole" -> "milk_whole"
            "milk_semi" -> "milk_semi"
            "milk_lactose_free" -> "milk_lactose_free"
            "milk_skimmed" -> "milk_skimmed"
            "milk" -> "milk"
            "yogurts" -> "yogurts"
            "cheese" -> "cheese"
            "eggs" -> "eggs"
            "grains" -> "rice"
            "pasta" -> "pasta"
            "rice" -> "rice"
            "legumes" -> "legumes"
            "canned_food" -> "canned_food"
            "frozen" -> "frozen"
            "water" -> "water"
            "soft_drinks" -> "soft_drinks"
            "juice_orange" -> "juice_orange"
            "juice_pineapple" -> "juice_pineapple"
            "juice_peach" -> "juice_peach"
            "juice_blueberry" -> "juice_blueberry"
            "juice_multifruit" -> "juice_multifruit"
            "juice_apple" -> "juice_apple"
            "juice_tomato" -> "juice_tomato"
            "juice_grape" -> "juice_grape"
            "juice" -> "juice"
            "wine" -> "wine"
            "beer" -> "beer"
            "coffee_tea" -> "coffee"
            "coffee" -> "coffee"
            "tea" -> "tea"
            "snack_chips" -> "snack_chips"
            "snack_nuts" -> "snack_nuts"
            "snack_crackers" -> "snack_crackers"
            "snacks" -> "snacks"
            "sweets_chocolate" -> "sweets_chocolate"
            "sweets_candy" -> "sweets_candy"
            "sweets_bonbons" -> "sweets_bonbons"
            "sweets_cookies" -> "sweets_cookies"
            "sweets_pastry" -> "sweets_pastry"
            "sweets" -> "sweets"
            "sauces" -> "sauces"
            "oil_vinegar" -> "oil"
            "oil" -> "oil"
            "vinegar" -> "vinegar"
            "ready_meals" -> "ready_meals"
            "cleaning_laundry" -> "cleaning_laundry"
            "cleaning_dish_soap" -> "cleaning_dish_soap"
            "cleaning" -> "cleaning"
            "hygiene_shampoo" -> "hygiene_shampoo"
            "hygiene_dental" -> "hygiene_dental"
            "hygiene" -> "hygiene"
            "pet_dog_food" -> "pet_dog_food"
            "pet_cat_food" -> "pet_cat_food"
            "pet_litter" -> "pet_litter"
            "pet_treats" -> "pet_treats"
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
        "fruit_tomato" -> Res.drawable.ic_fruit_tomato
        "fruit_apple" -> Res.drawable.ic_fruit_apple
        "fruit_pear" -> Res.drawable.ic_fruit_pear
        "fruit_banana" -> Res.drawable.ic_fruit_banana
        "fruit_orange" -> Res.drawable.ic_fruit_orange
        "fruit_mandarin" -> Res.drawable.ic_fruit_mandarin
        "fruit_lemon" -> Res.drawable.ic_fruit_lemon
        "fruit_lime" -> Res.drawable.ic_fruit_lime
        "fruit_grapefruit" -> Res.drawable.ic_fruit_grapefruit
        "fruit_strawberry" -> Res.drawable.ic_fruit_strawberry
        "fruit_raspberry" -> Res.drawable.ic_fruit_raspberry
        "fruit_blackberry" -> Res.drawable.ic_fruit_blackberry
        "fruit_blueberry" -> Res.drawable.ic_fruit_blueberry
        "fruit_grape" -> Res.drawable.ic_fruit_grape
        "fruit_kiwi" -> Res.drawable.ic_fruit_kiwi
        "fruit_melon" -> Res.drawable.ic_fruit_melon
        "fruit_watermelon" -> Res.drawable.ic_fruit_watermelon
        "fruit_peach" -> Res.drawable.ic_fruit_peach
        "fruit_plum" -> Res.drawable.ic_fruit_plum
        "fruit_cherry" -> Res.drawable.ic_fruit_cherry
        "fruit_pomegranate" -> Res.drawable.ic_fruit_pomegranate
        "fruit_persimmon" -> Res.drawable.ic_fruit_persimmon
        "fruit_loquat" -> Res.drawable.ic_fruit_loquat
        "fruit_fig" -> Res.drawable.ic_fruit_fig
        "fruit_date" -> Res.drawable.ic_fruit_date
        "fruit_coconut" -> Res.drawable.ic_fruit_coconut
        "fruit_cherimoya" -> Res.drawable.ic_fruit_cherimoya
        "fruit_mango" -> Res.drawable.ic_fruit_mango
        "fruit_papaya" -> Res.drawable.ic_fruit_papaya
        "fruit_pineapple" -> Res.drawable.ic_fruit_pineapple
        "fruit_avocado" -> Res.drawable.ic_fruit_avocado
        "vegetables" -> Res.drawable.ic_cat_vegetables
        "meat" -> Res.drawable.ic_cat_meat
        "meat_beef" -> Res.drawable.ic_meat_beef
        "meat_pork" -> Res.drawable.ic_meat_pork
        "meat_chicken" -> Res.drawable.ic_meat_chicken
        "meat_turkey" -> Res.drawable.ic_meat_turkey
        "meat_lamb" -> Res.drawable.ic_meat_lamb
        "meat_rabbit" -> Res.drawable.ic_meat_rabbit
        "meat_sausage" -> Res.drawable.ic_meat_sausage
        "fish" -> Res.drawable.ic_cat_fish
        "fish_hake" -> Res.drawable.ic_fish_hake
        "fish_cod" -> Res.drawable.ic_fish_cod
        "fish_seabass" -> Res.drawable.ic_fish_seabass
        "fish_salmon" -> Res.drawable.ic_fish_salmon
        "fish_tuna" -> Res.drawable.ic_fish_tuna
        "fish_sardine" -> Res.drawable.ic_fish_sardine
        "fish_trout" -> Res.drawable.ic_fish_trout
        "seafood" -> Res.drawable.ic_cat_seafood
        "vegetable_potato" -> Res.drawable.ic_vegetable_potato
        "vegetable_onion" -> Res.drawable.ic_vegetable_onion
        "vegetable_carrot" -> Res.drawable.ic_vegetable_carrot
        "vegetable_pepper" -> Res.drawable.ic_vegetable_pepper
        "vegetable_zucchini" -> Res.drawable.ic_vegetable_zucchini
        "vegetable_cucumber" -> Res.drawable.ic_vegetable_cucumber
        "vegetable_lettuce" -> Res.drawable.ic_vegetable_lettuce
        "vegetable_spinach" -> Res.drawable.ic_vegetable_spinach
        "vegetable_garlic" -> Res.drawable.ic_vegetable_garlic
        "vegetable_peas" -> Res.drawable.ic_vegetable_peas
        "vegetable_green_beans" -> Res.drawable.ic_vegetable_green_beans
        "vegetable_eggplant" -> Res.drawable.ic_vegetable_eggplant
        "vegetable_pumpkin" -> Res.drawable.ic_vegetable_pumpkin
        "vegetable_artichoke" -> Res.drawable.ic_vegetable_artichoke
        "vegetable_leek" -> Res.drawable.ic_vegetable_leek
        "vegetable_cauliflower" -> Res.drawable.ic_vegetable_cauliflower
        "bread" -> Res.drawable.ic_cat_bread
        "bread_baguette" -> Res.drawable.ic_bread_baguette
        "bread_sliced" -> Res.drawable.ic_bread_sliced
        "bread_burger" -> Res.drawable.ic_bread_burger
        "bread_hotdog" -> Res.drawable.ic_bread_hotdog
        "milk" -> Res.drawable.ic_cat_milk
        "milk_whole" -> Res.drawable.ic_milk_whole
        "milk_semi" -> Res.drawable.ic_milk_semi
        "milk_lactose_free" -> Res.drawable.ic_milk_lactose_free
        "milk_skimmed" -> Res.drawable.ic_milk_skimmed
        "yogurts" -> Res.drawable.ic_cat_yogurts
        "cheese" -> Res.drawable.ic_cat_cheese
        "eggs" -> Res.drawable.ic_cat_eggs
        "pasta" -> Res.drawable.ic_cat_pasta
        "rice" -> Res.drawable.ic_cat_rice
        "legumes" -> Res.drawable.ic_cat_legumes
        "canned_food" -> Res.drawable.ic_cat_canned_food
        "frozen" -> Res.drawable.ic_cat_frozen
        "water" -> Res.drawable.ic_cat_water_bottle
        "soft_drinks" -> Res.drawable.ic_cat_soft_drinks
        "juice" -> Res.drawable.ic_cat_juice
        "juice_orange" -> Res.drawable.ic_juice_orange
        "juice_pineapple" -> Res.drawable.ic_juice_pineapple
        "juice_peach" -> Res.drawable.ic_juice_peach
        "juice_blueberry" -> Res.drawable.ic_juice_blueberry
        "juice_multifruit" -> Res.drawable.ic_juice_multifruit
        "juice_apple" -> Res.drawable.ic_juice_apple
        "juice_tomato" -> Res.drawable.ic_juice_tomato
        "juice_grape" -> Res.drawable.ic_juice_grape
        "wine" -> Res.drawable.ic_cat_wine
        "beer" -> Res.drawable.ic_cat_beer
        "coffee" -> Res.drawable.ic_cat_coffee
        "tea" -> Res.drawable.ic_cat_tea
        "snacks" -> Res.drawable.ic_cat_snacks
        "snack_chips" -> Res.drawable.ic_snack_chips
        "snack_nuts" -> Res.drawable.ic_snack_nuts
        "snack_crackers" -> Res.drawable.ic_snack_crackers
        "sweets" -> Res.drawable.ic_cat_sweets
        "sweets_chocolate" -> Res.drawable.ic_sweets_chocolate
        "sweets_candy" -> Res.drawable.ic_sweets_candy
        "sweets_bonbons" -> Res.drawable.ic_sweets_bonbons
        "sweets_cookies" -> Res.drawable.ic_sweets_cookies
        "sweets_pastry" -> Res.drawable.ic_sweets_pastry
        "sauces" -> Res.drawable.ic_cat_sauces
        "oil" -> Res.drawable.ic_cat_oil
        "vinegar" -> Res.drawable.ic_cat_vinegar
        "ready_meals" -> Res.drawable.ic_cat_ready_meals
        "cleaning" -> Res.drawable.ic_cat_cleaning
        "cleaning_laundry" -> Res.drawable.ic_cleaning_laundry
        "cleaning_dish_soap" -> Res.drawable.ic_cleaning_dish_soap
        "hygiene" -> Res.drawable.ic_cat_hygiene
        "hygiene_shampoo" -> Res.drawable.ic_hygiene_shampoo
        "hygiene_dental" -> Res.drawable.ic_hygiene_dental
        "pets" -> Res.drawable.ic_cat_pets
        "pet_dog_food" -> Res.drawable.ic_pet_dog_food
        "pet_cat_food" -> Res.drawable.ic_pet_cat_food
        "pet_litter" -> Res.drawable.ic_pet_litter
        "pet_treats" -> Res.drawable.ic_pet_treats
        "other" -> Res.drawable.ic_cat_other
        else -> Res.drawable.ic_cat_other
    }
}

private fun String.normalizeCategoryText(): String {
    return lowercase()
        .map { char ->
            when (char) {
                'á', 'à', 'ä', 'â' -> 'a'
                'é', 'è', 'ë', 'ê' -> 'e'
                'í', 'ì', 'ï', 'î' -> 'i'
                'ó', 'ò', 'ö', 'ô' -> 'o'
                'ú', 'ù', 'ü', 'û' -> 'u'
                else -> char
            }
        }
        .joinToString("")
}

private fun String.containsAny(vararg values: String): Boolean {
    return values.any { it in this }
}

private fun String.containsToken(vararg values: String): Boolean {
    val tokens = split(Regex("[^a-z0-9]+")).filter { it.isNotBlank() }.toSet()
    return values.any { it in tokens }
}
