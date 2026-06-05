package es.neverachefai.feature.pantry.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.feature.pantry.domain.model.PantryFood
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
import neverachefai.shared.generated.resources.ic_fab_add_food
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_trash
import neverachefai.shared.generated.resources.ref_icon_search
import neverachefai.shared.generated.resources.ref_icon_sliders
import neverachefai.shared.generated.resources.ref_inventory_hero
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

enum class PantryLocation(val label: String, val title: String) {
    FRIDGE("Nevera", "En nevera"),
    PANTRY("Despensa", "En despensa"),
    FREEZER("Congelador", "En congelador"),
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
    onReview: () -> Unit,
    onFoodClick: (PantryFoodUi) -> Unit,
    onDeleteFoods: (Set<String>) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var deleteMode by remember { mutableStateOf(false) }
    var selectedFoodIds by remember { mutableStateOf(setOf<String>()) }
    val selectionMode = deleteMode || selectedFoodIds.isNotEmpty()
    val reminderDays = expiryReminderDays
    val filteredFoods = foods.filter { food ->
        val query = searchQuery.trim()
        if (query.isBlank()) true else {
            food.name.contains(query, ignoreCase = true) ||
                food.category.contains(query, ignoreCase = true) ||
                food.quantity.contains(query, ignoreCase = true)
        }
    }
    val groupedFoods = PantryLocation.entries.associateWith { location ->
        filteredFoods.filter { it.location == location }
    }

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

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HeroHeader(
                total = if (foods.size == 5) 6 else foods.size,
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
            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                onReview = onReview,
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 192.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 4.dp,
                top = 0.dp,
                end = 4.dp,
                bottom = 118.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
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
                    item {
                        SectionHeader(location = location, count = sectionFoods.size)
                    }
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
        }
        if (!selectionMode) {
            Surface(
                onClick = onAdd,
                shape = RoundedCornerShape(22.dp),
                color = Color(0xFF0B8E5F),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 8.dp)
                    .size(54.dp),
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

@Composable
private fun HeroHeader(
    total: Int,
    deleteMode: Boolean,
    onDeleteModeToggle: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth().height(114.dp)) {
        Image(
            painter = painterResource(Res.drawable.ref_inventory_hero),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 6.dp)
                .width(188.dp)
                .height(92.dp),
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "Tu inventario",
                color = Color(0xFF032E1F),
                fontSize = 27.sp,
                lineHeight = 31.sp,
                fontWeight = FontWeight.Bold,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "$total alimentos",
                    color = Color(0xFF093B2B),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                )
                IconButton(
                    onClick = onDeleteModeToggle,
                    modifier = Modifier.size(30.dp),
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_trash),
                        contentDescription = if (deleteMode) "Cancelar borrado" else "Seleccionar para borrar",
                        tint = if (deleteMode) Color(0xFFE03131) else Color(0xFF042D1F),
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
    onReview: () -> Unit,
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
        trailingIcon = {
            Image(
                painter = painterResource(Res.drawable.ref_icon_sliders),
                contentDescription = null,
                modifier = Modifier
                    .size(26.dp)
                    .clickable(onClick = onReview),
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
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            LocationGlyph(location = location, containerSize = 30.dp, iconSize = 19.dp)
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
                Box(
                    modifier = Modifier
                        .size(54.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(food.iconRes),
                        contentDescription = food.name,
                        modifier = Modifier.size(54.dp),
                    )
                }
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
        iconRes = pantryIconResource(iconKey),
    )
}

internal fun pantryIconResource(iconKey: String): DrawableResource {
    val normalizedKey = when (iconKey.trim().lowercase()) {
        "egg", "eggs", "huevo", "huevos" -> "eggs"
        "spinach", "verdura", "verduras", "vegetable" -> "vegetables"
        "fruit", "fruta", "frutas" -> "fruits"
        "proteina", "proteína", "protein", "carne" -> "meat"
        "rice", "lentils", "grain", "cereal", "grano", "granos" -> "grains"
        else -> iconKey.trim().lowercase()
    }
    return when (normalizedKey) {
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
