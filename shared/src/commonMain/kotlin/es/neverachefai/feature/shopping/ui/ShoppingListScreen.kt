package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.neverachefai.core.persistence.LocalAppContentStore
import es.neverachefai.core.persistence.ShoppingItemRecord
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_scan
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ic_nc_trash
import neverachefai.shared.generated.resources.ic_food_fish
import neverachefai.shared.generated.resources.ic_food_tomato
import neverachefai.shared.generated.resources.ic_food_rice
import neverachefai.shared.generated.resources.ic_food_yogurt
import neverachefai.shared.generated.resources.ic_food_soup
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val Ink = Color(0xFF1A1A1A)
private val Muted = Color(0xFF666666)
private val Accent = Color(0xFF0066FF)
private val AccentSoft = Color(0xFFEAF2FF)
private val Soft = Color(0xFFF6F8FB)
private val Line = Color(0xFFE6E8EC)

internal enum class ShoppingDestination { Fridge, Pantry, Freezer }

private val ShoppingDestinationOrder = listOf(
    ShoppingDestination.Fridge,
    ShoppingDestination.Pantry,
    ShoppingDestination.Freezer,
)

private data class ShoppingVisualItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val destination: ShoppingDestination,
    val iconKey: String,
    val iconRes: DrawableResource,
)

@Composable
fun ShoppingListScreen() {
    var selectedDestination by remember { mutableStateOf(ShoppingDestination.Fridge) }
    val items = remember {
        mutableStateListOf(
            *LocalAppContentStore.loadShoppingItems().map { it.toUi() }.toTypedArray(),
        )
    }
    var selectedItemId by remember { mutableStateOf<String?>(null) }
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var editName by remember { mutableStateOf("") }
    var editQty by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }

    val selectedItem = items.firstOrNull { it.id == selectedItemId }
    val editingItem = items.firstOrNull { it.id == editingItemId }

    val fridgeCount = items.count { it.destination == ShoppingDestination.Fridge }
    val pantryCount = items.count { it.destination == ShoppingDestination.Pantry }
    val freezerCount = items.count { it.destination == ShoppingDestination.Freezer }

    fun persistItems() {
        LocalAppContentStore.saveShoppingItems(items.map { it.toRecord() })
    }

    fun updateItem(id: String, transform: (ShoppingVisualItemUi) -> ShoppingVisualItemUi) {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) {
            items[index] = transform(items[index])
            persistItems()
        }
    }

    fun moveItem(id: String, destination: ShoppingDestination) {
        updateItem(id) { it.copy(destination = destination) }
    }

    fun moveItemByDrag(id: String, currentDestination: ShoppingDestination, dragDown: Boolean) {
        val currentIndex = ShoppingDestinationOrder.indexOf(currentDestination)
        val nextIndex = (currentIndex + if (dragDown) 1 else -1).coerceIn(ShoppingDestinationOrder.indices)
        moveItem(id, ShoppingDestinationOrder[nextIndex])
    }

    fun deleteItem(id: String) {
        if (items.removeAll { it.id == id }) {
            persistItems()
        }
    }

    fun clearDestination(destination: ShoppingDestination) {
        if (items.removeAll { it.destination == destination }) {
            persistItems()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        ShoppingVisualHeader(onAddProductClick = {})
        ShoppingHero(fridgeCount, pantryCount, freezerCount, items.size)
        AddProductPanel(selectedDestination, { selectedDestination = it }, onAddProductClick = {})
        DestinationFilters(items.size, fridgeCount, pantryCount, freezerCount)
        Text(
            "Tip: toca o manten pulsado para editar/eliminar. Arrastra arriba/abajo para mover entre nevera, despensa y congelador.",
            style = MaterialTheme.typography.bodySmall,
            color = Muted,
        )

        ShoppingDestinationSection(
            title = "Para la nevera",
            clearLabel = "Limpiar nevera",
            items = items.filter { it.destination == ShoppingDestination.Fridge },
            onClear = { clearDestination(ShoppingDestination.Fridge) },
            onOpenActions = { itemId -> selectedItemId = itemId },
            onMoveByDrag = { itemId, dragDown -> moveItemByDrag(itemId, ShoppingDestination.Fridge, dragDown) },
        )
        ShoppingDestinationSection(
            title = "Para despensa",
            clearLabel = "Limpiar despensa",
            items = items.filter { it.destination == ShoppingDestination.Pantry },
            onClear = { clearDestination(ShoppingDestination.Pantry) },
            onOpenActions = { itemId -> selectedItemId = itemId },
            onMoveByDrag = { itemId, dragDown -> moveItemByDrag(itemId, ShoppingDestination.Pantry, dragDown) },
        )
        ShoppingDestinationSection(
            title = "Para congelador",
            clearLabel = "Limpiar congelador",
            items = items.filter { it.destination == ShoppingDestination.Freezer },
            onClear = { clearDestination(ShoppingDestination.Freezer) },
            onOpenActions = { itemId -> selectedItemId = itemId },
            onMoveByDrag = { itemId, dragDown -> moveItemByDrag(itemId, ShoppingDestination.Freezer, dragDown) },
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (selectedItem != null) {
        ShoppingListActionSheet(
            itemName = selectedItem.name,
            quantity = selectedItem.quantity,
            category = selectedItem.category,
            iconRes = selectedItem.iconRes,
            currentDestination = selectedItem.destination,
            onDismiss = { selectedItemId = null },
            onEdit = {
                selectedItemId = null
                editingItemId = selectedItem.id
                editName = selectedItem.name
                editQty = selectedItem.quantity
                editCategory = selectedItem.category
            },
            onDelete = {
                deleteItem(selectedItem.id)
                selectedItemId = null
            },
            onMoveToFridge = {
                moveItem(selectedItem.id, ShoppingDestination.Fridge)
                selectedItemId = null
            },
            onMoveToPantry = {
                moveItem(selectedItem.id, ShoppingDestination.Pantry)
                selectedItemId = null
            },
            onMoveToFreezer = {
                moveItem(selectedItem.id, ShoppingDestination.Freezer)
                selectedItemId = null
            },
        )
    }

    if (editingItem != null) {
        EditItemDialog(
            name = editName,
            qty = editQty,
            category = editCategory,
            onNameChange = { editName = it },
            onQtyChange = { editQty = it },
            onCategoryChange = { editCategory = it },
            onDismiss = { editingItemId = null },
            onSave = {
                updateItem(editingItem.id) {
                    it.copy(name = editName.trim(), quantity = editQty.trim(), category = editCategory.trim())
                }
                editingItemId = null
            },
        )
    }
}

@Composable
private fun ShoppingVisualHeader(onAddProductClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Text("Lista de compra", color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Elige destino y suma productos a tu nevera o despensa", color = Muted, fontSize = 12.sp, lineHeight = 15.sp)
        }
        Surface(
            onClick = onAddProductClick,
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Accent,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_plus),
                    contentDescription = "Anadir producto",
                    tint = Color.White,
                    modifier = Modifier.size(21.dp),
                )
            }
        }
    }
}

@Composable
private fun ShoppingHero(fridgeCount: Int, pantryCount: Int, freezerCount: Int, totalCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink, RoundedCornerShape(22.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Llevas $totalCount productos", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(
            "$fridgeCount para nevera · $pantryCount para despensa · $freezerCount para congelador",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HeroMetric("Nevera", fridgeCount, Modifier.weight(1f))
            HeroMetric("Despensa", pantryCount, Modifier.weight(1f))
            HeroMetric("Congelador", freezerCount, Modifier.weight(1f))
            HeroMetric("Total", totalCount, Modifier.weight(1f), highlighted = true)
        }
    }
}

@Composable
private fun HeroMetric(label: String, value: Int, modifier: Modifier = Modifier, highlighted: Boolean = false) {
    Column(
        modifier = modifier
            .background(if (highlighted) Accent else Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(horizontal = 8.dp, vertical = 9.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(value.toString(), color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
        Text(label.uppercase(), color = Color.White.copy(alpha = if (highlighted) 1f else 0.8f), fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AddProductPanel(
    selectedDestination: ShoppingDestination,
    onDestinationSelected: (ShoppingDestination) -> Unit,
    onAddProductClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Line, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(onClick = onAddProductClick, color = Color.Transparent) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(painter = painterResource(Res.drawable.ic_nc_shopping_basket), contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Anadir producto", color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Nombre, cantidad y destino", color = Muted, fontSize = 11.sp)
                }
                Box(
                    modifier = Modifier
                        .background(AccentSoft, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("SUMAR", color = Accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DestinationPill(
                "Nevera",
                Res.drawable.ic_nc_fridge,
                selectedDestination == ShoppingDestination.Fridge,
                { onDestinationSelected(ShoppingDestination.Fridge) },
                Modifier.weight(1f),
            )
            DestinationPill(
                "Despensa",
                Res.drawable.ic_nc_pantry,
                selectedDestination == ShoppingDestination.Pantry,
                { onDestinationSelected(ShoppingDestination.Pantry) },
                Modifier.weight(1f),
            )
            DestinationPill(
                "Congelador",
                Res.drawable.ic_nc_scan,
                selectedDestination == ShoppingDestination.Freezer,
                { onDestinationSelected(ShoppingDestination.Freezer) },
                Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DestinationPill(
    text: String,
    icon: DrawableResource,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(36.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Accent else Color.White,
        border = if (selected) null else BorderStroke(1.dp, Line),
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = if (selected) Color.White else Muted,
                modifier = Modifier.size(15.dp),
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = text,
                color = if (selected) Color.White else Muted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun DestinationFilters(totalCount: Int, fridgeCount: Int, pantryCount: Int, freezerCount: Int) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChipText("Todos $totalCount", selected = true)
        FilterChipText("Nevera $fridgeCount", selected = false, accent = true)
        FilterChipText("Despensa $pantryCount", selected = false)
        FilterChipText("Congelador $freezerCount", selected = false)
    }
}

@Composable
private fun FilterChipText(text: String, selected: Boolean, accent: Boolean = false) {
    val bg = when { selected -> Accent; accent -> AccentSoft; else -> Soft }
    val fg = when { selected -> Color.White; accent -> Accent; else -> Muted }
    val shape = RoundedCornerShape(999.dp)
    Box(
        modifier = Modifier
            .background(bg, shape)
            .then(if (selected || accent) Modifier else Modifier.border(1.dp, Line, shape))
            .padding(horizontal = 11.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = fg, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ShoppingDestinationSection(
    title: String,
    clearLabel: String,
    items: List<ShoppingVisualItemUi>,
    onClear: () -> Unit,
    onOpenActions: (String) -> Unit,
    onMoveByDrag: (String, Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        TextButton(onClick = onClear) {
            Text(clearLabel, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
    if (items.isEmpty()) {
        Text(
            text = "Sin artículos en esta sección",
            color = Muted,
            fontSize = 11.sp,
        )
    } else {
        items.forEach { item ->
            ShoppingVisualItemRow(
                item = item,
                onOpenActions = { onOpenActions(item.id) },
                onMoveByDrag = { dragDown -> onMoveByDrag(item.id, dragDown) },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShoppingVisualItemRow(
    item: ShoppingVisualItemUi,
    onOpenActions: () -> Unit,
    onMoveByDrag: (dragDown: Boolean) -> Unit,
) {
    val density = LocalDensity.current
    val dragThresholdPx = with(density) { 76.dp.toPx() }
    var dragOffsetY by remember(item.id) { mutableStateOf(0f) }
    val background = when {
        item.destination == ShoppingDestination.Fridge -> AccentSoft
        item.destination == ShoppingDestination.Freezer -> Color(0xFFEAF2FF)
        else -> Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, dragOffsetY.toInt()) }
            .pointerInput(item.id, item.destination) {
                detectVerticalDragGestures(
                    onDragEnd = { dragOffsetY = 0f },
                    onDragCancel = { dragOffsetY = 0f },
                ) { change, dragAmount ->
                    change.consume()
                    dragOffsetY += dragAmount
                    if (dragOffsetY > dragThresholdPx) {
                        onMoveByDrag(true)
                        dragOffsetY = 0f
                    } else if (dragOffsetY < -dragThresholdPx) {
                        onMoveByDrag(false)
                        dragOffsetY = 0f
                    }
                }
            }
            .combinedClickable(
                onClick = onOpenActions,
                onLongClick = onOpenActions,
            )
            .height(72.dp)
            .border(
                width = if (background == Color.White) 1.dp else 0.dp,
                color = Line,
                shape = RoundedCornerShape(15.dp),
            )
            .background(background, RoundedCornerShape(15.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.72f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(item.iconRes),
                contentDescription = null,
                tint = Accent,
                modifier = Modifier.size(18.dp),
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(item.name, color = Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("${item.quantity} · ${item.category}", color = Muted, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        DestinationTag(destination = item.destination)
    }
}

@Composable
private fun DestinationTag(destination: ShoppingDestination) {
    val isFridge = destination == ShoppingDestination.Fridge
    val icon = when (destination) {
        ShoppingDestination.Fridge -> Res.drawable.ic_nc_fridge
        ShoppingDestination.Pantry -> Res.drawable.ic_nc_pantry
        ShoppingDestination.Freezer -> Res.drawable.ic_nc_scan
    }
    val text = when (destination) {
        ShoppingDestination.Fridge -> "NEVERA"
        ShoppingDestination.Pantry -> "DESPENSA"
        ShoppingDestination.Freezer -> "CONGELADOR"
    }
    Row(
        modifier = Modifier
            .background(
                when (destination) {
                    ShoppingDestination.Fridge -> Color.White
                    ShoppingDestination.Pantry -> Soft
                    ShoppingDestination.Freezer -> Color(0xFFEAF2FF)
                },
                RoundedCornerShape(999.dp),
            )
            .then(if (isFridge) Modifier else Modifier.border(1.dp, Line, RoundedCornerShape(999.dp)))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = when (destination) {
                ShoppingDestination.Fridge -> Accent
                ShoppingDestination.Pantry -> Muted
                ShoppingDestination.Freezer -> Accent
            },
            modifier = Modifier.size(12.dp),
        )
        Text(
            text = text,
            color = when (destination) {
                ShoppingDestination.Fridge -> Accent
                ShoppingDestination.Pantry -> Muted
                ShoppingDestination.Freezer -> Accent
            },
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ClearDestinationButton(
    text: String,
    icon: DrawableResource,
    tint: Color,
    background: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    border: Boolean = false,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = background,
        border = if (border) BorderStroke(1.dp, Line) else null,
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(icon), contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(text, color = tint, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun EditItemDialog(
    name: String,
    qty: String,
    category: String,
    onNameChange: (String) -> Unit,
    onQtyChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(22.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text("Modificar producto", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                OutlinedTextField(value = name, onValueChange = onNameChange, modifier = Modifier.fillMaxWidth(), label = { Text("Nombre") })
                OutlinedTextField(
                    value = qty,
                    onValueChange = onQtyChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                )
                OutlinedTextField(value = category, onValueChange = onCategoryChange, modifier = Modifier.fillMaxWidth(), label = { Text("Categoria") })
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) { Text("Cancelar") }
                    Button(onClick = onSave, modifier = Modifier.weight(1f)) { Text("Guardar") }
                }
            }
        }
    }
}

private fun ShoppingItemRecord.toUi(): ShoppingVisualItemUi {
    return ShoppingVisualItemUi(
        id = id,
        name = name,
        quantity = quantity,
        category = category,
        destination = when (destinationKey) {
            "freezer" -> ShoppingDestination.Freezer
            "pantry" -> ShoppingDestination.Pantry
            else -> ShoppingDestination.Fridge
        },
        iconKey = iconKey,
        iconRes = shoppingIconResource(iconKey),
    )
}

private fun ShoppingVisualItemUi.toRecord(): ShoppingItemRecord {
    return ShoppingItemRecord(
        id = id,
        name = name,
        quantity = quantity,
        category = category,
        destinationKey = when (destination) {
            ShoppingDestination.Freezer -> "freezer"
            ShoppingDestination.Pantry -> "pantry"
            ShoppingDestination.Fridge -> "fridge"
        },
        iconKey = iconKey,
    )
}

private fun shoppingIconResource(iconKey: String): DrawableResource {
    return when (iconKey) {
        "fish" -> Res.drawable.ic_food_fish
        "rice" -> Res.drawable.ic_food_rice
        "tomato" -> Res.drawable.ic_food_tomato
        "yogurt" -> Res.drawable.ic_food_yogurt
        else -> Res.drawable.ic_food_soup
    }
}
