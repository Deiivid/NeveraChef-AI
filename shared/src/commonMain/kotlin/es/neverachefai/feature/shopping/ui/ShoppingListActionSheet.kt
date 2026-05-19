package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_plus
import neverachefai.shared.generated.resources.ic_nc_scan
import neverachefai.shared.generated.resources.ic_nc_trash
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val SheetInk = Color(0xFF1A1A1A)
private val SheetMuted = Color(0xFF666666)
private val SheetBorder = Color(0xFFE6E8EC)
private val SheetAccent = Color(0xFF0066FF)
private val SheetAccentSoft = Color(0xFFEAF2FF)
private val SheetSuccessSoft = Color(0xFFEAF8EE)
private val SheetDangerSoft = Color(0xFFFFE7E2)
private val SheetSummarySoft = Color(0xFFFFF7E6)
private val SheetNeutralSoft = Color(0xFFF6F8FB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShoppingListActionSheet(
    itemName: String,
    quantity: String,
    category: String,
    iconRes: DrawableResource,
    currentDestination: ShoppingDestination,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveToFridge: () -> Unit,
    onMoveToPantry: () -> Unit,
    onMoveToFreezer: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, end = 18.dp, bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 2.dp, bottom = 6.dp)
                    .size(width = 38.dp, height = 4.dp)
                    .background(Color(0xFFD5D9E1), RoundedCornerShape(999.dp)),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SheetSummarySoft, RoundedCornerShape(22.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ProductBadge(iconRes = iconRes, tint = SheetAccent, background = Color.White)
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    TextLine(
                        text = itemName,
                        color = SheetInk,
                        size = 18.sp,
                        bold = true,
                    )
                    TextLine(
                        text = "$quantity · $category · ${destinationLabel(currentDestination)}",
                        color = SheetMuted,
                        size = 12.sp,
                    )
                    DestinationPill(
                        text = destinationLabel(currentDestination),
                        icon = destinationIcon(currentDestination),
                        tint = destinationTint(currentDestination),
                        background = destinationBackground(currentDestination),
                    )
                }
            }

            TextLine(
                text = "¿Dónde quieres guardar este producto?",
                color = SheetInk,
                size = 16.sp,
                bold = true,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                when (currentDestination) {
                    ShoppingDestination.Fridge -> {
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_pantry,
                            title = "Mover a despensa",
                            body = "Se queda como seco o básico",
                            background = SheetSuccessSoft,
                            contentColor = Color(0xFF208A54),
                            onClick = onMoveToPantry,
                        )
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_scan,
                            title = "Mover a congelador",
                            body = "Guardar como reserva",
                            background = SheetNeutralSoft,
                            contentColor = SheetMuted,
                            onClick = onMoveToFreezer,
                        )
                    }
                    ShoppingDestination.Pantry -> {
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_fridge,
                            title = "Mover a nevera",
                            body = "Se queda como fresco",
                            background = SheetAccentSoft,
                            contentColor = SheetAccent,
                            onClick = onMoveToFridge,
                        )
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_scan,
                            title = "Mover a congelador",
                            body = "Guardar para más tarde",
                            background = SheetNeutralSoft,
                            contentColor = SheetMuted,
                            onClick = onMoveToFreezer,
                        )
                    }
                    ShoppingDestination.Freezer -> {
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_fridge,
                            title = "Mover a nevera",
                            body = "Pasa a fresco",
                            background = SheetAccentSoft,
                            contentColor = SheetAccent,
                            onClick = onMoveToFridge,
                        )
                        MoveCard(
                            modifier = Modifier.weight(1f),
                            icon = Res.drawable.ic_nc_pantry,
                            title = "Mover a despensa",
                            body = "Pasa a básico o seco",
                            background = SheetSuccessSoft,
                            contentColor = Color(0xFF208A54),
                            onClick = onMoveToPantry,
                        )
                    }
                }
            }

            ActionCardRow(
                icon = Res.drawable.ic_nc_plus,
                title = "Modificar producto",
                body = "Editar antes de guardarlo",
                background = Color.White,
                contentColor = SheetInk,
                border = true,
                onClick = onEdit,
            )
            ActionCardRow(
                icon = Res.drawable.ic_nc_trash,
                title = "Eliminar de la lista",
                body = "No borra alimentos ya guardados",
                background = SheetDangerSoft,
                contentColor = Color(0xFFC93A2F),
                onClick = onDelete,
            )
        }
    }
}

@Composable
private fun ProductBadge(
    iconRes: DrawableResource,
    tint: Color,
    background: Color,
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(background, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(22.dp),
        )
    }
}

@Composable
private fun DestinationPill(
    text: String,
    icon: DrawableResource,
    tint: Color,
    background: Color,
) {
    Row(
        modifier = Modifier
            .background(background, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(12.dp),
        )
        TextLine(text = text, color = tint, size = 10.sp, bold = true)
    }
}

@Composable
private fun MoveCard(
    modifier: Modifier,
    icon: DrawableResource,
    title: String,
    body: String,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(94.dp),
        shape = RoundedCornerShape(20.dp),
        color = background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                TextLine(text = title, color = contentColor, size = 14.sp, bold = true)
                TextLine(text = body, color = SheetMuted, size = 12.sp)
            }
        }
    }
}

@Composable
private fun ActionCardRow(
    icon: DrawableResource,
    title: String,
    body: String,
    background: Color,
    contentColor: Color,
    onClick: () -> Unit,
    border: Boolean = false,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = background,
        border = if (border) BorderStroke(1.dp, SheetBorder) else null,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(Color.White, RoundedCornerShape(11.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                TextLine(
                    text = title,
                    color = contentColor,
                    size = 14.sp,
                    bold = true,
                )
                TextLine(
                    text = body,
                    color = SheetMuted,
                    size = 12.sp,
                )
            }
        }
    }
}

private fun destinationLabel(destination: ShoppingDestination): String = when (destination) {
    ShoppingDestination.Fridge -> "Nevera"
    ShoppingDestination.Pantry -> "Despensa"
    ShoppingDestination.Freezer -> "Congelador"
}

private fun destinationIcon(destination: ShoppingDestination): DrawableResource = when (destination) {
    ShoppingDestination.Fridge -> Res.drawable.ic_nc_fridge
    ShoppingDestination.Pantry -> Res.drawable.ic_nc_pantry
    ShoppingDestination.Freezer -> Res.drawable.ic_nc_scan
}

private fun destinationTint(destination: ShoppingDestination): Color = when (destination) {
    ShoppingDestination.Fridge -> SheetAccent
    ShoppingDestination.Pantry -> Color(0xFF208A54)
    ShoppingDestination.Freezer -> SheetMuted
}

private fun destinationBackground(destination: ShoppingDestination): Color = when (destination) {
    ShoppingDestination.Fridge -> SheetAccentSoft
    ShoppingDestination.Pantry -> SheetSuccessSoft
    ShoppingDestination.Freezer -> SheetNeutralSoft
}

@Composable
private fun TextLine(
    text: String,
    color: Color,
    size: androidx.compose.ui.unit.TextUnit,
    bold: Boolean = false,
) {
    androidx.compose.material3.Text(
        text = text,
        color = color,
        fontSize = size,
        fontWeight = if (bold) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal,
        style = MaterialTheme.typography.bodyMedium,
    )
}
