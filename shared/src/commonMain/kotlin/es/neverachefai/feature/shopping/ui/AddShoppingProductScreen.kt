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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_camera
import neverachefai.shared.generated.resources.ic_nc_microphone
import neverachefai.shared.generated.resources.ic_nc_pencil
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val Ink = Color(0xFF1A1A1A)
private val Muted = Color(0xFF666666)
private val Accent = Color(0xFF0066FF)
private val AccentSoft = Color(0xFFEAF2FF)
private val SuccessSoft = Color(0xFFEAF7EF)
private val Soft = Color(0xFFF6F8FB)
private val Line = Color(0xFFE6E8EC)

enum class AddShoppingMode(
    val label: String,
    val iconRes: DrawableResource,
) {
    Manual("Manual", Res.drawable.ic_nc_pencil),
    Voice("Voz", Res.drawable.ic_nc_microphone),
    Camera("Cámara", Res.drawable.ic_nc_camera),
}

data class AddShoppingProductUiState(
    val selectedMode: AddShoppingMode = AddShoppingMode.Manual,
    val productName: String = "",
    val quantity: String = "",
    val destination: String = "",
    val previewProducts: List<String> = emptyList(),
)

@Composable
fun AddShoppingProductScreen(
    state: AddShoppingProductUiState,
    onModeSelected: (AddShoppingMode) -> Unit,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onVoiceClick: () -> Unit,
    onCameraClick: () -> Unit,
    onAddToShoppingListClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.background(Color.White),
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Header()
            ModeSelector(
                selectedMode = state.selectedMode,
                onModeSelected = onModeSelected,
            )
            ManualProductForm(
                productName = state.productName,
                quantity = state.quantity,
                destination = state.destination,
                onProductNameChange = onProductNameChange,
                onQuantityChange = onQuantityChange,
                onDestinationChange = onDestinationChange,
            )
            AssistHint(
                iconRes = Res.drawable.ic_nc_microphone,
                text = "Voz: \"añade leche, huevos y arroz a la compra\"",
                highlighted = true,
                onClick = onVoiceClick,
            )
            AssistHint(
                iconRes = Res.drawable.ic_nc_camera,
                text = "Cámara: escanea ticket o lista escrita y revisa antes de sumar.",
                highlighted = false,
                onClick = onCameraClick,
            )
            PreviewProducts(products = state.previewProducts)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                onClick = onAddToShoppingListClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                color = Accent,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Añadir a lista de compra",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Añadir a compra",
            color = Ink,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Añade productos manualmente, dictando o escaneando una lista.",
            color = Muted,
            fontSize = 13.sp,
            lineHeight = 18.sp,
        )
    }
}

@Composable
private fun ModeSelector(
    selectedMode: AddShoppingMode,
    onModeSelected: (AddShoppingMode) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AddShoppingMode.values().forEach { mode ->
            ModeCard(
                mode = mode,
                selected = mode == selectedMode,
                onClick = { onModeSelected(mode) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ModeCard(
    mode: AddShoppingMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Accent else Color.White,
        border = if (selected) null else BorderStroke(1.dp, Line),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(mode.iconRes),
                contentDescription = mode.label,
                tint = if (selected) Color.White else Accent,
                modifier = Modifier.size(22.dp),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = mode.label,
                color = if (selected) Color.White else Ink,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun ManualProductForm(
    productName: String,
    quantity: String,
    destination: String,
    onProductNameChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Soft)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ProductField(
            label = "Producto",
            value = productName,
            onValueChange = onProductNameChange,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ProductField(
                label = "Cantidad",
                value = quantity,
                onValueChange = onQuantityChange,
                modifier = Modifier.weight(1f),
            )
            ProductField(
                label = "Destino",
                value = destination,
                onValueChange = onDestinationChange,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProductField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
    )
}

@Composable
private fun AssistHint(
    iconRes: DrawableResource,
    text: String,
    highlighted: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (highlighted) AccentSoft else Color.White,
        border = if (highlighted) null else BorderStroke(1.dp, Line),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Accent,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = text,
                color = if (highlighted) Accent else Muted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun PreviewProducts(products: List<String>) {
    val previewText = products.joinToString(separator = " · ")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SuccessSoft)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Se sumarán ${products.size} productos",
            color = Ink,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = previewText.ifBlank { "Sin productos preparados" },
            color = Muted,
            fontSize = 12.sp,
        )
    }
}

fun sampleAddShoppingProductState(): AddShoppingProductUiState =
    AddShoppingProductUiState(
        selectedMode = AddShoppingMode.Manual,
        productName = "Tomates cherry",
        quantity = "250 g",
        destination = "Nevera",
        previewProducts = listOf("Tomates cherry", "Leche", "Arroz"),
    )
