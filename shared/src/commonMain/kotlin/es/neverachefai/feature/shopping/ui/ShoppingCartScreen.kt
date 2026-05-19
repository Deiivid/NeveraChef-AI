@file:Suppress("FunctionName")

package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF1A1A1A)
private val Muted = Color(0xFF666666)
private val Accent = Color(0xFF0066FF)
private val AccentSoft = Color(0xFFEAF2FF)
private val Success = Color(0xFF2F8F5B)
private val SuccessSoft = Color(0xFFEAF7EF)
private val Soft = Color(0xFFF6F8FB)
private val Line = Color(0xFFE6E8EC)
private val CartWarm = Color(0xFFFFF4D6)
private val CartWarmText = Color(0xFF6B4E00)

private data class ShoppingCartItemUi(
    val id: String,
    val icon: String,
    val name: String,
    val quantity: String,
    val marked: Boolean,
)

@Composable
fun ShoppingCartScreen(modifier: Modifier = Modifier) {
    val items = remember {
        mutableStateListOf(*sampleShoppingCartItems().toTypedArray())
    }

    ShoppingCartContent(
        cartItems = items,
        onItemMarkedChange = { item, checked ->
            val index = items.indexOfFirst { it.id == item.id }
            if (index >= 0) {
                items[index] = item.copy(marked = checked)
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun ShoppingCartContent(
    cartItems: List<ShoppingCartItemUi>,
    onItemMarkedChange: (ShoppingCartItemUi, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val markedCount = cartItems.count { it.marked }
    val pendingCount = cartItems.size - markedCount

    Scaffold(
        modifier = modifier.background(Color.White),
        containerColor = Color.White,
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Carrito de compra",
                    color = Ink,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            item {
                ShoppingCartSummary(
                    addedCount = cartItems.size,
                    pendingCount = pendingCount,
                    markedCount = markedCount,
                )
            }
            item {
                Text(
                    text = "Lista",
                    color = Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            items(cartItems, key = { it.id }) { item ->
                ShoppingCartItemRow(
                    item = item,
                    onMarkedChange = { checked -> onItemMarkedChange(item, checked) },
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ShoppingCartSummary(
    addedCount: Int,
    pendingCount: Int,
    markedCount: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CartWarm)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "$addedCount añadidos",
            color = Ink,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "$pendingCount pendientes · $markedCount marcados",
            color = CartWarmText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CartMetric(
                value = addedCount,
                label = "AÑADIDOS",
                background = Color.White,
                valueColor = Ink,
                labelColor = CartWarmText,
                modifier = Modifier.weight(1f),
            )
            CartMetric(
                value = pendingCount,
                label = "PENDIENTES",
                background = AccentSoft,
                valueColor = Accent,
                labelColor = Accent,
                modifier = Modifier.weight(1f),
            )
            CartMetric(
                value = markedCount,
                label = "MARCADOS",
                background = SuccessSoft,
                valueColor = Success,
                labelColor = Success,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun CartMetric(
    value: Int,
    label: String,
    background: Color,
    valueColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp),
    ) {
        Text(
            text = value.toString(),
            color = valueColor,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            color = labelColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ShoppingCartItemRow(
    item: ShoppingCartItemUi,
    onMarkedChange: (Boolean) -> Unit,
) {
    val background = if (item.marked) Soft else Color.White
    val textColor = if (item.marked) Muted else Ink

    Surface(
        onClick = { onMarkedChange(!item.marked) },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(15.dp),
        color = background,
        border = BorderStroke(1.dp, Line),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = item.marked,
                onCheckedChange = onMarkedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Success,
                    uncheckedColor = Accent,
                ),
            )
            ProductIcon(icon = item.icon, marked = item.marked)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = item.name,
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = item.quantity,
                    color = Muted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
private fun ProductIcon(
    icon: String,
    marked: Boolean,
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (marked) Color.White else CartWarm),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private fun sampleShoppingCartItems(): List<ShoppingCartItemUi> =
    listOf(
        ShoppingCartItemUi("tomatoes", "🍅", "Tomates cherry", "500 g", marked = false),
        ShoppingCartItemUi("yogurt", "🥛", "Yogur natural", "4 uds", marked = false),
        ShoppingCartItemUi("lemons", "🍋", "Limones", "2 uds", marked = false),
        ShoppingCartItemUi("lettuce", "🥬", "Lechuga romana", "1 unidad", marked = true),
        ShoppingCartItemUi("onion", "🧅", "Cebolla dulce", "3 uds", marked = false),
        ShoppingCartItemUi("potatoes", "🥔", "Patatas", "1 kg", marked = false),
        ShoppingCartItemUi("garlic", "🧄", "Ajo", "1 cabeza", marked = true),
    )
