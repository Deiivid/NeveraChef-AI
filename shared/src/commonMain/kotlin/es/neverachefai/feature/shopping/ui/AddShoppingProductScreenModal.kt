package es.neverachefai.feature.shopping.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_plus
import org.jetbrains.compose.resources.painterResource

private val ModalInk = Color(0xFF1D1B20)
private val ModalMuted = Color(0xFF424656)
private val ModalPrimary = Color(0xFF004BCA)
private val ModalSurface = Color(0xFFFEF7FF)
private val ModalSurfaceLow = Color(0xFFF8F2F9)
private val ModalOutline = Color(0xFF737687)
private val ModalOutlineVariant = Color(0xFFC2C6D9)

@Composable
fun AddShoppingProductScreenModal(
    productName: String,
    quantity: Int,
    selectedUnit: String = "ud",
    onProductNameChange: (String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onUnitChange: (String) -> Unit = {},
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val unitOptions = listOf("ud", "g", "1 kg", "2 kg", "3 kg")
    val sheetInteraction = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x66000000))
            .clickable(onClick = onDismiss),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(ModalSurface)
                .clickable(
                    interactionSource = sheetInteraction,
                    indication = null,
                    onClick = {},
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 32.dp, height = 4.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(ModalOutlineVariant),
            )

            Text(
                text = "Nuevo producto personalizado",
                color = ModalInk,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.SemiBold,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Nombre del producto",
                    color = ModalMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                OutlinedTextField(
                    value = productName,
                    onValueChange = onProductNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            text = "e.g. Leche de almendras",
                            color = ModalMuted.copy(alpha = 0.6f),
                            fontSize = 16.sp,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ModalPrimary,
                        unfocusedBorderColor = ModalOutline,
                        focusedTextColor = ModalInk,
                        unfocusedTextColor = ModalInk,
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ModalSurfaceLow)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("#", color = ModalMuted, fontSize = 28.sp, lineHeight = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text("Cantidad", color = ModalInk, fontSize = 16.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(ModalSurface)
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onDecrease, modifier = Modifier.size(40.dp)) {
                        Text("−", color = ModalMuted, fontSize = 22.sp, fontWeight = FontWeight.Medium)
                    }
                    Text(
                        text = quantity.toString(),
                        color = ModalInk,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.width(28.dp),
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(ModalPrimary),
                        contentAlignment = Alignment.Center,
                    ) {
                        IconButton(onClick = onIncrease, modifier = Modifier.size(40.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_nc_plus),
                                contentDescription = "Aumentar cantidad",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ModalSurfaceLow)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                unitOptions.forEach { unit ->
                    val isSelected = unit == selectedUnit
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isSelected) ModalPrimary else ModalSurface)
                            .clickable { onUnitChange(unit) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = unit,
                            color = if (isSelected) Color.White else ModalMuted,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        )
                    }
                }
            }

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ModalPrimary),
            ) {
                Text(
                    text = "Confirmar y añadir",
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
