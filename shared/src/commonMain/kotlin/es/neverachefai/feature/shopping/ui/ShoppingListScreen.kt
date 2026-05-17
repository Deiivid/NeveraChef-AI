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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import neverachefai.shared.generated.resources.ic_nc_square
import neverachefai.shared.generated.resources.ic_nc_trash
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

private enum class ShoppingDestination { Fridge, Pantry }

private data class ShoppingVisualItemUi(
    val id: String,
    val name: String,
    val quantity: String,
    val category: String,
    val destination: ShoppingDestination,
    val selected: Boolean,
    val iconRes: DrawableResource,
)

@Composable
fun ShoppingListScreen() {
    var selectedDestination by remember { mutableStateOf(ShoppingDestination.Fridge) }
    val items = remember {
        mutableStateListOf(
            ShoppingVisualItemUi("yogurt", "Yogur natural", "4 uds", "lacteos", ShoppingDestination.Fridge, false, Res.drawable.ic_food_yogurt),
            ShoppingVisualItemUi("tomato", "Tomates cherry", "500 g", "verdura", ShoppingDestination.Fridge, true, Res.drawable.ic_food_tomato),
            ShoppingVisualItemUi("broth", "Caldo de verduras", "1 brick", "receta sopa", ShoppingDestination.Pantry, false, Res.drawable.ic_food_soup),
            ShoppingVisualItemUi("rice", "Arroz redondo", "1 kg", "basico", ShoppingDestination.Pantry, false, Res.drawable.ic_food_rice),
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
    val selectedCount = items.count { it.selected }

    fun updateItem(id: String, transform: (ShoppingVisualItemUi) -> ShoppingVisualItemUi) {
        val index = items.indexOfFirst { it.id == id }
        if (index >= 0) items[index] = transform(items[index])
    }

    fun moveItem(id: String, destination: ShoppingDestination) {
        updateItem(id) { it.copy(destination = destination) }
    }

    fun deleteItem(id: String) {
        items.removeAll { it.id == id }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        ShoppingVisualHeader(onAddProductClick = {})
        ShoppingHero(fridgeCount, pantryCount, items.size, selectedCount)
        AddProductPanel(selectedDestination, { selectedDestination = it }, onAddProductClick = {})
        DestinationFilters(items.size, fridgeCount, pantryCount)
        Text(
            "Tip: toca o manten pulsado para editar/eliminar. Arrastra arriba/abajo para mover entre nevera y despensa.",
            style = MaterialTheme.typography.bodySmall,
            color = Muted,
        )

        SectionHeader(title = "Para la nevera")
        items.filter { it.destination == ShoppingDestination.Fridge }.forEach { item ->
            ShoppingVisualItemRow(
                item = item,
                onToggleSelected = { updateItem(item.id) { it.copy(selected = !it.selected) } },
                onOpenActions = { selectedItemId = item.id },
                onMoveByDrag = { dragDown ->
                    if (dragDown) moveItem(item.id, ShoppingDestination.Pantry)
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SectionHeader(title = "Para despensa")
            Text(
                text = "Limpiar despensa",
                color = Muted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        items.filter { it.destination == ShoppingDestination.Pantry }.forEach { item ->
            ShoppingVisualItemRow(
                item = item,
                onToggleSelected = { updateItem(item.id) { it.copy(selected = !it.selected) } },
                onOpenActions = { selectedItemId = item.id },
                onMoveByDrag = { dragDown ->
                    if (!dragDown) moveItem(item.id, ShoppingDestination.Fridge)
                },
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ClearDestinationButton(
                text = "Limpiar nevera",
                icon = Res.drawable.ic_nc_trash,
                tint = Accent,
                background = AccentSoft,
                onClick = { items.removeAll { it.destination == ShoppingDestination.Fridge } },
                modifier = Modifier.weight(1f),
            )
            ClearDestinationButton(
                text = "Limpiar despensa",
                icon = Res.drawable.ic_nc_trash,
                tint = Muted,
                background = Soft,
                border = true,
                onClick = { items.removeAll { it.destination == ShoppingDestination.Pantry } },
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (selectedItem != null) {
        ItemActionsSheet(
            item = selectedItem,
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
            onMove = {
                moveItem(
                    selectedItem.id,
                    if (selectedItem.destination == ShoppingDestination.Fridge) ShoppingDestination.Pantry else ShoppingDestination.Fridge,
                )
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
private fun ShoppingHero(fridgeCount: Int, pantryCount: Int, totalCount: Int, selectedCount: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink, RoundedCornerShape(22.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text("Hoy compras $totalCount cosas", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("$fridgeCount para nevera · $pantryCount para despensa · $selectedCount marcadas", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HeroMetric("Nevera", fridgeCount, Modifier.weight(1f))
            HeroMetric("Despensa", pantryCount, Modifier.weight(1f))
            HeroMetric("Total", totalCount, Modifier.weight(1f), highlighted = true)
        }
    }
}

@Composable
private fun HeroMetric(label: String, value: Int, modifier: Modifier = Modifier, highlighted: Boolean = false) {
    Column(
        modifier = modifier
            .background(if (highlighted) Accent else Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(value.toString(), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(label.uppercase(), color = Color.White.copy(alpha = if (highlighted) 1f else 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
            .border(1.dp, Line, RoundedCornerShape(18.dp))
            .background(Soft, RoundedCornerShape(18.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
                Text("SUMAR", color = Accent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DestinationPill("Nevera", Res.drawable.ic_nc_fridge, selectedDestination == ShoppingDestination.Fridge, { onDestinationSelected(ShoppingDestination.Fridge) }, Modifier.weight(1f))
            DestinationPill("Despensa", Res.drawable.ic_nc_pantry, selectedDestination == ShoppingDestination.Pantry, { onDestinationSelected(ShoppingDestination.Pantry) }, Modifier.weight(1f))
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
            Icon(painter = painterResource(icon), contentDescription = null, tint = if (selected) Color.White else Muted, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.size(6.dp))
            Text(text, color = if (selected) Color.White else Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun DestinationFilters(totalCount: Int, fridgeCount: Int, pantryCount: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChipText("Todos $totalCount", selected = true)
        FilterChipText("Nevera $fridgeCount", selected = false, accent = true)
        FilterChipText("Despensa $pantryCount", selected = false)
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
private fun SectionHeader(title: String) {
    Text(title, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.Bold)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShoppingVisualItemRow(
    item: ShoppingVisualItemUi,
    onToggleSelected: () -> Unit,
    onOpenActions: () -> Unit,
    onMoveByDrag: (dragDown: Boolean) -> Unit,
) {
    val density = LocalDensity.current
    val dragThresholdPx = with(density) { 76.dp.toPx() }
    var dragOffsetY by remember(item.id) { mutableStateOf(0f) }
    val isFridge = item.destination == ShoppingDestination.Fridge
    val background = if (isFridge && !item.selected) AccentSoft else Color.White

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
            .height(62.dp)
            .border(
                width = if (background == Color.White) 1.dp else 0.dp,
                color = Line,
                shape = RoundedCornerShape(15.dp),
            )
            .background(background, RoundedCornerShape(15.dp))
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            onClick = onToggleSelected,
            color = Color.Transparent,
        ) {
            Icon(
                painter = painterResource(if (item.selected) Res.drawable.ic_nc_check_square else Res.drawable.ic_nc_square),
                contentDescription = if (item.selected) "Producto seleccionado" else "Producto sin seleccionar",
                tint = Accent,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(item.name, color = if (item.selected) Muted else Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("${item.quantity} · ${item.category}", color = Muted, fontSize = 11.sp)
        }
        DestinationTag(destination = item.destination)
    }
}

@Composable
private fun DestinationTag(destination: ShoppingDestination) {
    val isFridge = destination == ShoppingDestination.Fridge
    val icon = if (isFridge) Res.drawable.ic_nc_fridge else Res.drawable.ic_nc_pantry
    val text = if (isFridge) "NEVERA" else "DESPENSA"
    Row(
        modifier = Modifier
            .background(if (isFridge) Color.White else Soft, RoundedCornerShape(999.dp))
            .then(if (isFridge) Modifier else Modifier.border(1.dp, Line, RoundedCornerShape(999.dp)))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(painter = painterResource(icon), contentDescription = null, tint = if (isFridge) Accent else Muted, modifier = Modifier.size(12.dp))
        Text(text, color = if (isFridge) Accent else Muted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
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
private fun FoodIconBubble(
    iconRes: DrawableResource,
    tintColor: Color,
    backgroundColor: Color,
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(backgroundColor, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tintColor,
            modifier = Modifier.size(22.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemActionsSheet(
    item: ShoppingVisualItemUi,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMove: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 42.dp, height = 5.dp)
                    .background(Color(0xFFD8DCE3), RoundedCornerShape(999.dp)),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Soft, RoundedCornerShape(18.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                FoodIconBubble(iconRes = item.iconRes, tintColor = NeveraChefColors.Blue, backgroundColor = AccentSoft)
                Column(modifier = Modifier.weight(1f)) {
                    Text(item.name, color = Ink, fontSize = 34.sp / 2, fontWeight = FontWeight.Bold)
                    Text("${item.quantity} · ${item.category} · Producto seleccionado", color = Muted, fontSize = 12.sp)
                }
            }

            Text(
                text = "¿Qué quieres hacer con este producto?",
                color = Ink,
                fontSize = 32.sp / 2,
                fontWeight = FontWeight.Bold,
            )

            ActionSheetRow(
                icon = if (item.destination == ShoppingDestination.Fridge) Res.drawable.ic_nc_pantry else Res.drawable.ic_nc_fridge,
                title = if (item.destination == ShoppingDestination.Fridge) "Mover a despensa" else "Mover a nevera",
                body = if (item.destination == ShoppingDestination.Fridge) "Guardar como básico o producto seco" else "Marcar como fresco al terminar la compra",
                background = if (item.destination == ShoppingDestination.Fridge) NeveraChefColors.SuccessSoft else AccentSoft,
                contentColor = if (item.destination == ShoppingDestination.Fridge) Color(0xFF218A54) else Accent,
                onClick = onMove,
            )
            ActionSheetRow(
                icon = Res.drawable.ic_nc_plus,
                title = "Modificar cantidad o nombre",
                body = "Editar antes de guardarlo",
                background = Color.White,
                contentColor = Ink,
                bordered = true,
                onClick = onEdit,
            )
            ActionSheetRow(
                icon = Res.drawable.ic_nc_trash,
                title = "Eliminar de la lista",
                body = "No borra alimentos ya guardados",
                background = Color(0xFFFFE7E2),
                contentColor = Color(0xFFC93A2F),
                onClick = onDelete,
            )
        }
    }
}

@Composable
private fun ActionSheetRow(
    icon: DrawableResource,
    title: String,
    body: String,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit,
    bordered: Boolean = false,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = background,
        border = if (bordered) BorderStroke(1.dp, Line) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp),
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, color = contentColor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(body, color = Muted, style = MaterialTheme.typography.bodySmall)
            }
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
