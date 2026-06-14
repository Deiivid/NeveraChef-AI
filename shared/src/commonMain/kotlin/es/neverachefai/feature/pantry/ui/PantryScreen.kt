package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.ads.NeveraChefBannerAd
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.feature.pantry.domain.model.PantryFood
import es.neverachefai.feature.shopping.ui.inferShoppingIconKey
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
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_fab_add_food
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ic_nc_trash
import neverachefai.shared.generated.resources.ref_icon_search
import neverachefai.shared.generated.resources.ref_inventory_hero_premium
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class PantryLocation(val label: String, val title: String) {
    FRIDGE("Nevera", "En nevera"),
    PANTRY("Despensa", "En despensa"),
    FREEZER("Congelador", "En congelador"),
}

private enum class InventoryFilter(val label: String) {
    ALL("Todos"),
    FRIDGE("Nevera"),
    PANTRY("Despensa"),
    FREEZER("Congelador"),
}

data class PantryFoodUi(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val location: PantryLocation,
    val expiryLabel: String?,
    val expiryDateIso: String?,
    val addedDateIso: String?,
    val iconKey: String,
    val iconRes: DrawableResource,
)

@Composable
fun PantryScreen(
    foods: List<PantryFoodUi>,
    expiryReminderDays: Int,
    onAdd: () -> Unit,
    onFoodClick: (PantryFoodUi) -> Unit,
    onDeleteFoods: (Set<String>) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var deleteMode by remember { mutableStateOf(false) }
    var selectedFoodIds by remember { mutableStateOf(setOf<String>()) }
    var selectedFilter by remember { mutableStateOf(InventoryFilter.ALL) }
    var fixedTopContentHeightPx by remember { mutableIntStateOf(0) }
    val selectionMode = deleteMode || selectedFoodIds.isNotEmpty()
    val reminderDays = expiryReminderDays
    val searchedFoods = foods.filter { food ->
        val query = searchQuery.trim()
        if (query.isBlank()) true else {
            food.name.contains(query, ignoreCase = true) ||
                food.category.contains(query, ignoreCase = true) ||
                food.quantity.contains(query, ignoreCase = true)
        }
    }
    val filteredFoods = searchedFoods.filter { food ->
        when (selectedFilter) {
            InventoryFilter.ALL -> true
            InventoryFilter.FRIDGE -> food.location == PantryLocation.FRIDGE
            InventoryFilter.PANTRY -> food.location == PantryLocation.PANTRY
            InventoryFilter.FREEZER -> food.location == PantryLocation.FREEZER
        }
    }
    val groupedFoods = PantryLocation.entries.associateWith { location ->
        filteredFoods.filter { it.location == location }
    }
    val locationCounts = PantryLocation.entries.associateWith { location ->
        foods.count { it.location == location }
    }
    val density = LocalDensity.current
    val listTopPadding = with(density) { fixedTopContentHeightPx.toDp() }

    val deleteSelected: () -> Unit = {
        val ids = selectedFoodIds
        if (ids.isNotEmpty()) {
            onDeleteFoods(ids)
            selectedFoodIds = emptySet()
        }
        deleteMode = false
    }

    val toggleSelection: (String) -> Unit = { id ->
        selectedFoodIds = if (id in selectedFoodIds) {
            selectedFoodIds - id
        } else {
            selectedFoodIds + id
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val compactWidth = maxWidth < 380.dp
        val horizontalPadding = if (compactWidth) 2.dp else 4.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .onGloballyPositioned { coordinates ->
                    fixedTopContentHeightPx = coordinates.size.height
                },
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HeroHeader(
                deleteMode = deleteMode,
                onDeleteModeToggle = {
                    if (deleteMode) {
                        deleteMode = false
                        selectedFoodIds = emptySet()
                    } else {
                        deleteMode = true
                    }
                },
            )
            InventoryFilterRow(
                selectedFilter = selectedFilter,
                total = foods.size,
                fridgeCount = locationCounts[PantryLocation.FRIDGE].orZero(),
                pantryCount = locationCounts[PantryLocation.PANTRY].orZero(),
                freezerCount = locationCounts[PantryLocation.FREEZER].orZero(),
                compact = compactWidth,
                onSelect = { selectedFilter = it },
            )
            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = listTopPadding),
            contentPadding = PaddingValues(
                start = horizontalPadding,
                top = 12.dp,
                end = horizontalPadding,
                bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (selectionMode) {
                item {
                    SelectionBar(
                        count = selectedFoodIds.size,
                        onDelete = deleteSelected,
                        onCancel = {
                            deleteMode = false
                            selectedFoodIds = emptySet()
                        },
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
            PantryLocation.entries.forEach { location ->
                val sectionFoods = groupedFoods[location].orEmpty()
                if (sectionFoods.isNotEmpty()) {
                    items(sectionFoods.size) { index ->
                        val food = sectionFoods[index]
                        PantryItemCard(
                            food = food,
                            selectionMode = selectionMode,
                            selected = food.id in selectedFoodIds,
                            reminderDays = reminderDays,
                            onClick = {
                                if (selectionMode) {
                                    toggleSelection(food.id)
                                } else {
                                    onFoodClick(food)
                                }
                            },
                            onLongClick = {
                                if (!selectionMode) {
                                    deleteMode = true
                                    selectedFoodIds = setOf(food.id)
                                } else {
                                    toggleSelection(food.id)
                                }
                            },
                            onSelectedChange = { checked ->
                                if (checked) {
                                    selectedFoodIds = selectedFoodIds + food.id
                                } else {
                                    selectedFoodIds = selectedFoodIds - food.id
                                }
                            },
                        )
                    }
                }
            }
            if (filteredFoods.isEmpty()) {
                item {
                    EmptyInventoryState(
                        filter = selectedFilter,
                        hasSearch = searchQuery.isNotBlank(),
                    )
                }
            }
        }
        if (!selectionMode) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = horizontalPadding, end = horizontalPadding, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                InventoryBannerAd(modifier = Modifier.weight(1f))
                Surface(
                    onClick = onAdd,
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFF0B8E5F),
                    modifier = Modifier.size(54.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_fab_add_food),
                            contentDescription = "Añadir alimento",
                            tint = Color.White,
                            modifier = Modifier.size(42.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InventoryBannerAd(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        contentAlignment = Alignment.Center,
    ) {
        NeveraChefBannerAd(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
        )
    }
}

@Composable
private fun HeroHeader(
    deleteMode: Boolean,
    onDeleteModeToggle: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth().height(124.dp)) {
        Image(
            painter = painterResource(Res.drawable.ref_inventory_hero_premium),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 2.dp, end = 2.dp)
                .width(204.dp)
                .height(112.dp),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Tu inventario",
                    color = Color(0xFF032E1F),
                    fontSize = 28.sp,
                    lineHeight = 31.sp,
                    fontWeight = FontWeight.Bold,
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            if (deleteMode) Color(0xFFFFE2E2) else Color.White,
                            CircleShape,
                        )
                        .border(
                            1.dp,
                            if (deleteMode) Color(0xFFFFB8B8) else Color(0xFFF1DCD5),
                            CircleShape,
                        )
                        .clickable(onClick = onDeleteModeToggle),
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
        }
    }
}

@Composable
private fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Image(
                painter = painterResource(Res.drawable.ref_icon_search),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        },
        placeholder = {
            Text("Buscar alimento", color = Color(0xFF7C7F8E), fontSize = 14.sp)
        },
        shape = RoundedCornerShape(26.dp),
        modifier = Modifier.fillMaxWidth().height(58.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFD2D7E2),
            unfocusedBorderColor = Color(0xFFD2D7E2),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
    )
}

@Composable
private fun InventoryFilterRow(
    selectedFilter: InventoryFilter,
    total: Int,
    fridgeCount: Int,
    pantryCount: Int,
    freezerCount: Int,
    compact: Boolean,
    onSelect: (InventoryFilter) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InventoryFilterChip(
            filter = InventoryFilter.ALL,
            selected = selectedFilter == InventoryFilter.ALL,
            count = total,
            enabled = true,
            compact = compact,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(InventoryFilter.ALL) },
        )
        InventoryFilterChip(
            filter = InventoryFilter.FRIDGE,
            selected = selectedFilter == InventoryFilter.FRIDGE,
            count = fridgeCount,
            enabled = fridgeCount > 0,
            compact = compact,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(InventoryFilter.FRIDGE) },
        )
        InventoryFilterChip(
            filter = InventoryFilter.PANTRY,
            selected = selectedFilter == InventoryFilter.PANTRY,
            count = pantryCount,
            enabled = pantryCount > 0,
            compact = compact,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(InventoryFilter.PANTRY) },
        )
        InventoryFilterChip(
            filter = InventoryFilter.FREEZER,
            selected = selectedFilter == InventoryFilter.FREEZER,
            count = freezerCount,
            enabled = freezerCount > 0,
            compact = compact,
            modifier = Modifier.weight(1f),
            onClick = { onSelect(InventoryFilter.FREEZER) },
        )
    }
}

@Composable
private fun InventoryFilterChip(
    filter: InventoryFilter,
    selected: Boolean,
    count: Int,
    enabled: Boolean,
    compact: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val tint = when (filter) {
        InventoryFilter.ALL -> Color(0xFF057A4F)
        InventoryFilter.FRIDGE -> locationTint(PantryLocation.FRIDGE)
        InventoryFilter.PANTRY -> locationTint(PantryLocation.PANTRY)
        InventoryFilter.FREEZER -> locationTint(PantryLocation.FREEZER)
    }
    val background = when {
        selected -> tint
        filter == InventoryFilter.ALL -> Color(0xFFE8F4E8)
        filter == InventoryFilter.FRIDGE -> locationBackground(PantryLocation.FRIDGE)
        filter == InventoryFilter.PANTRY -> locationBackground(PantryLocation.PANTRY)
        else -> locationBackground(PantryLocation.FREEZER)
    }
    val contentColor = if (selected) Color.White else tint
    val alpha = if (enabled || filter == InventoryFilter.ALL) 1f else 0.45f
    val icon = when (filter) {
        InventoryFilter.ALL -> Res.drawable.ic_nc_shopping_basket
        InventoryFilter.FRIDGE -> Res.drawable.ic_nc_fridge
        InventoryFilter.PANTRY -> Res.drawable.ic_nc_pantry
        InventoryFilter.FREEZER -> Res.drawable.ic_nc_freezer
    }

    Surface(
        onClick = {
            if (enabled || filter == InventoryFilter.ALL) onClick()
        },
        modifier = modifier.graphicsLayer { this.alpha = alpha },
        shape = RoundedCornerShape(20.dp),
        color = background,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) tint else tint.copy(alpha = 0.18f),
        ),
        shadowElevation = if (selected) 4.dp else 2.dp,
    ) {
        Column(
            modifier = Modifier
                .height(if (compact) 68.dp else 72.dp)
                .padding(horizontal = if (compact) 4.dp else 6.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(if (compact) 17.dp else 19.dp),
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = filter.label,
                color = contentColor,
                fontSize = if (compact) 9.sp else 10.sp,
                lineHeight = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = count.toString(),
                color = contentColor,
                fontSize = if (compact) 18.sp else 20.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun EmptyInventoryState(
    filter: InventoryFilter,
    hasSearch: Boolean,
) {
    val message = if (hasSearch) {
        "No hay alimentos con esa busqueda"
    } else {
        when (filter) {
            InventoryFilter.ALL -> "Todavia no hay alimentos"
            InventoryFilter.FRIDGE -> "No hay alimentos en nevera"
            InventoryFilter.PANTRY -> "No hay alimentos en despensa"
            InventoryFilter.FREEZER -> "No hay alimentos en congelador"
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp)
            .background(Color(0xFFF7FAF7), RoundedCornerShape(22.dp))
            .border(1.dp, Color(0xFFE0E7DF), RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = Color(0xFF4E5561),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )
    }
}

@Composable
private fun SelectionBar(
    count: Int,
    onDelete: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
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

@Composable
private fun SectionHeader(location: PantryLocation, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(9.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(22.dp)
                    .background(locationTint(location), RoundedCornerShape(999.dp)),
            )
            Text(
                text = location.title,
                color = Color(0xFF042D1F),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Box(
            modifier = Modifier
                .background(
                    when (location) {
                        PantryLocation.FRIDGE -> Color(0xFFE8F4E8)
                        PantryLocation.PANTRY -> Color(0xFFF9F0DE)
                        PantryLocation.FREEZER -> Color(0xFFE5ECFB)
                    },
                    RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 9.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = count.toString(),
                color = when (location) {
                    PantryLocation.FRIDGE -> Color(0xFF0D6B57)
                    PantryLocation.PANTRY -> Color(0xFF9A6310)
                    PantryLocation.FREEZER -> Color(0xFF2C6DC7)
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun LocationGlyph(
    location: PantryLocation,
    containerSize: Dp,
    iconSize: Dp,
) {
    Box(
        modifier = Modifier
            .size(containerSize)
            .background(locationBackground(location), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(locationIcon(location)),
            contentDescription = null,
            tint = locationTint(location),
            modifier = Modifier.size(iconSize),
        )
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

@Composable
private fun PantryItemCard(
    food: PantryFoodUi,
    selectionMode: Boolean,
    selected: Boolean,
    reminderDays: Int,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onSelectedChange: (Boolean) -> Unit,
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (selectionMode) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = onSelectedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF0B8E5F),
                        checkmarkColor = Color.White,
                        uncheckedColor = Color(0xFF9AA0AE),
                    ),
                    modifier = Modifier.size(24.dp),
                )
            } else {
                Image(
                    painter = painterResource(food.iconRes),
                    contentDescription = food.name,
                    modifier = Modifier.size(66.dp),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    food.name,
                    color = Color(0xFF141923),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    food.quantity,
                    color = Color(0xFF2A2F3D),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
            ExpiryPill(food = food, reminderDays = reminderDays)
            if (!selectionMode) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_arrow_back),
                    contentDescription = null,
                    tint = Color(0xFF808492),
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer { rotationZ = 180f },
                )
            }
        }
    }
}

@Composable
private fun ExpiryPill(
    food: PantryFoodUi,
    reminderDays: Int,
) {
    val days = daysUntilExpiry(food.expiryDateIso) ?: parseDaysFromLabel(food.expiryLabel) ?: return
    if (days > reminderDays) return

    val background = when {
        days < 0 -> Color(0xFFFCE6E6)
        days <= 1 -> Color(0xFFFCE6E6)
        else -> Color(0xFFFBEED8)
    }
    val textColor = when {
        days < 0 -> Color(0xFFE03131)
        days <= 1 -> Color(0xFFE03131)
        else -> Color(0xFFA15F08)
    }
    val text = when {
        days < 0 -> if (days == -1) "Caducado ayer" else "Caducado hace ${-days} días"
        days == 0 -> "Caduca hoy"
        days == 1 -> "Caduca mañana"
        else -> "Caduca en $days días"
    }

    Box(
        modifier = Modifier
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

private fun parseDaysFromLabel(expiryLabel: String?): Int? {
    val label = expiryLabel?.trim().orEmpty()
    if (label.isBlank()) return null
    if (!label.contains("día", ignoreCase = true)) return null
    return Regex("(\\d+)").find(label)?.groupValues?.getOrNull(1)?.toIntOrNull()
}

private fun Int?.orZero(): Int = this ?: 0

internal fun PantryFood.toPantryFoodUi(): PantryFoodUi {
    val quantityLabel = if (quantity.isNotBlank()) {
        quantity
    } else {
        listOf(quantityValue, quantityUnit).filter { it.isNotBlank() }.joinToString(" ")
    }
    return PantryFoodUi(
        id = id,
        name = name,
        quantity = quantityLabel,
        category = category,
        location = when (locationKey) {
            "fridge" -> PantryLocation.FRIDGE
            "freezer" -> PantryLocation.FREEZER
            else -> PantryLocation.PANTRY
        },
        expiryLabel = expiryLabel,
        expiryDateIso = expiryDateIso,
        addedDateIso = addedDateIso,
        iconKey = iconKey,
        iconRes = pantryIconResource(iconKey = iconKey, category = category, name = name),
    )
}

internal fun pantryIconResource(
    iconKey: String,
    category: String = "",
    name: String = "",
): DrawableResource {
    val normalizedText = listOf(iconKey, category, name).joinToString(" ").normalizeCategoryText()
    val rawIconKey = iconKey.trim().lowercase()
    val normalizedKey = when (rawIconKey) {
        "meat_beef", "meat_pork", "meat_chicken", "meat_turkey", "meat_lamb", "meat_rabbit", "meat_sausage" -> rawIconKey
        "fish_hake", "fish_cod", "fish_seabass", "fish_salmon", "fish_tuna", "fish_sardine", "fish_trout" -> rawIconKey
        "fruit_tomato", "fruit_apple", "fruit_pear", "fruit_banana", "fruit_orange", "fruit_mandarin",
        "fruit_lemon", "fruit_lime", "fruit_grapefruit", "fruit_strawberry", "fruit_raspberry", "fruit_blackberry",
        "fruit_blueberry", "fruit_grape", "fruit_kiwi", "fruit_melon", "fruit_watermelon",
        "fruit_peach", "fruit_plum", "fruit_cherry", "fruit_pomegranate", "fruit_persimmon",
        "fruit_loquat", "fruit_fig", "fruit_date", "fruit_coconut", "fruit_cherimoya",
        "fruit_mango", "fruit_papaya", "fruit_pineapple", "fruit_avocado" -> rawIconKey
        "vegetable_potato", "vegetable_onion", "vegetable_carrot", "vegetable_pepper", "vegetable_zucchini",
        "vegetable_cucumber", "vegetable_lettuce", "vegetable_spinach", "vegetable_garlic", "vegetable_peas",
        "vegetable_green_beans", "vegetable_eggplant", "vegetable_pumpkin", "vegetable_artichoke",
        "vegetable_leek", "vegetable_cauliflower" -> rawIconKey
        "bread_baguette", "bread_sliced", "bread_burger", "bread_hotdog",
        "milk_whole", "milk_semi", "milk_lactose_free", "milk_skimmed",
        "juice_orange", "juice_pineapple", "juice_peach", "juice_blueberry", "juice_multifruit", "juice_apple",
        "juice_tomato", "juice_grape",
        "sweets_chocolate", "sweets_candy", "sweets_bonbons", "sweets_cookies", "sweets_pastry",
        "snack_chips", "snack_nuts", "snack_crackers",
        "pet_dog_food", "pet_cat_food", "pet_litter", "pet_treats",
        "cleaning_laundry", "cleaning_dish_soap", "hygiene_shampoo", "hygiene_dental" -> rawIconKey
        else -> inferShoppingIconKey(normalizedText) ?: when {
            normalizedText.containsAny("macarron", "espagueti", "spaghetti", "pasta", "fideo") -> "pasta"
            normalizedText.containsAny("arroz", "rice", "cereal") -> "rice"
            normalizedText.containsAny("lenteja", "garbanzo", "alubia", "legumbre", "legumbres") -> "legumes"
            normalizedText.containsAny("cafe", "coffee") -> "coffee"
            normalizedText.containsToken("te", "infusion", "tea") -> "tea"
            normalizedText.containsAny("vinagre", "vinegar") -> "vinegar"
            normalizedText.containsAny("aceite", "oil") -> "oil"
            else -> when (iconKey.trim().lowercase()) {
                "egg", "eggs", "huevo", "huevos" -> "eggs"
                "spinach", "verdura", "verduras", "vegetable" -> "vegetables"
                "fruit", "fruta", "frutas" -> "fruits"
                "proteina", "proteína", "protein", "carne" -> "meat"
                "rice" -> "rice"
                "pasta" -> "pasta"
                "lentils", "legumes" -> "legumes"
                "grain", "cereal", "grano", "granos", "grains" -> "rice"
                "coffee_tea" -> "coffee"
                "oil_vinegar" -> "oil"
                else -> iconKey.trim().lowercase()
            }
        }
    }
    return when (normalizedKey) {
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
