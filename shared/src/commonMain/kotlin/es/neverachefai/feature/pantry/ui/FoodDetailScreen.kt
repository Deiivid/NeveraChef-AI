package es.neverachefai.feature.pantry.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_expiry
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_freezer
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_pantry
import neverachefai.shared.generated.resources.ic_nc_pencil
import neverachefai.shared.generated.resources.ref_food_eggs
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun FoodDetailScreen(
    food: PantryFoodUi?,
    onBack: () -> Unit,
    onGenerateRecipe: () -> Unit,
    onSaveEditedFood: (PantryFoodUi) -> Unit,
) {
    if (food == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("No hay alimento seleccionado", color = NeveraChefColors.Ink, fontWeight = FontWeight.Bold)
            Surface(onClick = onBack, shape = RoundedCornerShape(12.dp), color = Color(0xFFE7EFE8)) {
                Text("Volver", modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = NeveraChefColors.Ink)
            }
        }
        return
    }

    val expiryText = getExpirationText(food.expiryDateIso) ?: food.expiryLabel ?: "Sin fecha"
    val expiryLevel = expirationPriority(food.expiryDateIso)
    val expiryBadge = when (expiryLevel) {
        ExpirationPriority.EXPIRED -> "Caducado"
        ExpirationPriority.SOON -> "Próximo a caducar"
        ExpirationPriority.NORMAL -> "En buen estado"
        ExpirationPriority.UNKNOWN -> "Caducidad estimada"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Header(onBack = onBack, onEdit = { onSaveEditedFood(food) })
        HeroCard(food = food)
        ExpiryCard(expiryText = expiryText, badgeText = expiryBadge)
        MetaGrid(food = food)
        TipCard(food = food)
        ActionRow(
            onRecipe = onGenerateRecipe,
            onMove = {},
            onEdit = { onSaveEditedFood(food) },
        )
    }
}

@Composable
private fun Header(
    onBack: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_arrow_back),
            contentDescription = "Volver",
            tint = Color(0xFF0A5A3A),
            modifier = Modifier
                .size(30.dp)
                .clickable(onClick = onBack),
        )
        Text(
            text = "Detalle de producto",
            color = Color(0xFF063D29),
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            painter = painterResource(Res.drawable.ic_nc_pencil),
            contentDescription = "Editar",
            tint = Color(0xFF0A5A3A),
            modifier = Modifier
                .size(28.dp)
                .clickable(onClick = onEdit),
        )
    }
}

@Composable
private fun HeroCard(food: PantryFoodUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F5F1), RoundedCornerShape(22.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(Color(0xFFE8EFE5), RoundedCornerShape(28.dp)),
        ) {
            Image(
                painter = painterResource(Res.drawable.ref_food_eggs),
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(Color(0xFFE8EFE5), RoundedCornerShape(22.dp)),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .size(64.dp)
                    .background(Color(0xFFE5ECE2), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(food.iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(38.dp),
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f)) {
            Text(
                text = food.name,
                color = Color(0xFF003C2A),
                fontSize = 64.sp,
                lineHeight = 66.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(text = food.quantity, color = Color(0xFF4C596A), fontSize = 28.sp)
            Tag(text = food.category, icon = food.iconRes, background = Color(0xFFE2ECDD), tint = Color(0xFF0A5A3A))
            Tag(text = food.location.label, icon = locationIconRes(food.location), background = Color(0xFFE3ECF7), tint = Color(0xFF204A71))
        }
    }
}

@Composable
private fun Tag(
    text: String,
    icon: DrawableResource,
    background: Color,
    tint: Color,
) {
    Row(
        modifier = Modifier
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = if (icon == Res.drawable.ic_expiry || icon == Res.drawable.ic_nc_fridge) tint else Color.Unspecified,
            modifier = Modifier.size(22.dp),
        )
        Text(text = text, color = tint, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ExpiryCard(
    expiryText: String,
    badgeText: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF7EA), RoundedCornerShape(22.dp))
            .border(1.dp, Color(0xFFF5CF8A), RoundedCornerShape(22.dp))
            .padding(horizontal = 18.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFFFEDCC), RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_expiry),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(44.dp),
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text("Caducidad", color = Color(0xFFAB6700), fontSize = 20.sp)
            Text(expiryText, color = Color(0xFFCC7600), fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }

        Text(
            text = badgeText,
            color = Color(0xFFE38A00),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .background(Color(0xFFFFE9C8), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun MetaGrid(food: PantryFoodUi) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetaCard(
                modifier = Modifier.weight(1f),
                title = "Ubicación",
                value = food.location.label,
                icon = locationIconRes(food.location),
            )
            MetaCard(
                modifier = Modifier.weight(1f),
                title = "Cantidad",
                value = food.quantity,
                icon = Res.drawable.ic_nc_fridge,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetaCard(
                modifier = Modifier.weight(1f),
                title = "Categoría",
                value = food.category,
                icon = food.iconRes,
            )
            MetaCard(
                modifier = Modifier.weight(1f),
                title = "Añadido",
                value = "hace 2 días",
                icon = Res.drawable.ic_expiry,
            )
        }
    }
}

@Composable
private fun MetaCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: DrawableResource,
) {
    Row(
        modifier = modifier
            .height(116.dp)
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFEAF2E7), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = if (icon == Res.drawable.ic_expiry || icon == Res.drawable.ic_nc_fridge) Color(0xFF115E3B) else Color.Unspecified,
                modifier = Modifier.size(24.dp),
            )
        }
        Column {
            Text(title, color = Color(0xFF566172), fontSize = 16.sp)
            Text(value, color = Color(0xFF063D29), fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TipCard(food: PantryFoodUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFF5EF), RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color(0xFFD9EAD6), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("💡", fontSize = 22.sp)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text("Consejo", color = Color(0xFF113D2C), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Guarda ${food.name.lowercase()} en la zona media de la nevera para mantener su frescura por más tiempo.",
                color = Color(0xFF3E4D5C),
                fontSize = 16.sp,
                lineHeight = 22.sp,
            )
        }
        Icon(
            painter = painterResource(Res.drawable.ic_nc_fridge),
            contentDescription = null,
            tint = Color(0xFFBFD1C0),
            modifier = Modifier.size(74.dp),
        )
    }
}

@Composable
private fun ActionRow(
    onRecipe: () -> Unit,
    onMove: () -> Unit,
    onEdit: () -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        ActionButton(
            modifier = Modifier.weight(1.2f),
            text = "Receta rápida",
            icon = Res.drawable.ic_nc_chef_hat,
            onClick = onRecipe,
        )
        ActionButton(
            modifier = Modifier.weight(0.95f),
            text = "Mover",
            icon = Res.drawable.ic_nc_arrow_back,
            rotateIcon = 180f,
            onClick = onMove,
        )
        ActionButton(
            modifier = Modifier.weight(0.95f),
            text = "Editar",
            icon = Res.drawable.ic_nc_pencil,
            onClick = onEdit,
        )
    }
}

@Composable
private fun ActionButton(
    modifier: Modifier,
    text: String,
    icon: DrawableResource,
    rotateIcon: Float = 0f,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(62.dp),
        color = Color(0xFFF0F4EF),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E8E0)),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = Color(0xFF0C4E35),
                modifier = Modifier
                    .size(22.dp)
                    .rotate(rotateIcon),
            )
            Spacer(Modifier.width(8.dp))
            Text(text = text, color = Color(0xFF0C4E35), fontSize = 19.sp, fontWeight = FontWeight.Medium)
        }
    }
}

private fun locationIconRes(location: PantryLocation) = when (location) {
    PantryLocation.FRIDGE -> Res.drawable.ic_nc_fridge
    PantryLocation.PANTRY -> Res.drawable.ic_nc_pantry
    PantryLocation.FREEZER -> Res.drawable.ic_nc_freezer
}
