package es.neverachefai.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import es.neverachefai.core.designsystem.NeveraChefColors
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_check_square
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_scan
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LocalFirstBadge()
        Text(
            text = "Cocina mejor con lo que ya tienes",
            color = NeveraChefColors.Ink,
            fontSize = 29.sp,
            lineHeight = 31.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "NeveraChef ordena tu nevera, revisa la IA antes de guardar y convierte tus alimentos en recetas y compra util.",
            color = NeveraChefColors.Muted,
            fontSize = 14.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
        )
        OnboardingStoryCard(modifier = Modifier.fillMaxWidth())
        Column(verticalArrangement = Arrangement.spacedBy(9.dp), modifier = Modifier.fillMaxWidth()) {
            OnboardingStep(
                number = "1",
                title = "Anade alimentos como te venga mejor",
                body = "Manual, voz o camara. Tu decides antes de guardar.",
                numberBackground = NeveraChefColors.AccentSoft,
                numberColor = NeveraChefColors.Blue,
            )
            OnboardingStep(
                number = "2",
                title = "Cocina con tu inventario real",
                body = "Recetas con lo que tienes y avisos de caducidad.",
                numberBackground = NeveraChefColors.SuccessSoft,
                numberColor = Color(0xFF2F8F5B),
            )
            OnboardingStep(
                number = "3",
                title = "Compra solo lo que falta",
                body = "La lista se ordena por nevera y despensa.",
                numberBackground = NeveraChefColors.WarningSoft,
                numberColor = Color(0xFF9B6A00),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
        ) {
            Text("Configurar NeveraChef", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun LocalFirstBadge() {
    Row(
        modifier = Modifier
            .background(NeveraChefColors.SuccessSoft, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_check_square),
            contentDescription = null,
            tint = Color(0xFF2F8F5B),
            modifier = Modifier.size(15.dp),
        )
        Text(
            text = "Sin login · datos en tu movil",
            color = Color(0xFF2F8F5B),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun OnboardingStoryCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(258.dp)
            .background(NeveraChefColors.Soft, RoundedCornerShape(30.dp))
            .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(30.dp))
            .padding(18.dp),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 205.dp, height = 214.dp)
                .background(Color.White, RoundedCornerShape(28.dp))
                .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(28.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Hoy", color = NeveraChefColors.Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(NeveraChefColors.AccentSoft, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_chef_hat),
                        contentDescription = null,
                        tint = NeveraChefColors.Blue,
                        modifier = Modifier.size(15.dp),
                    )
                }
            }
            MiniStoryRow("🥬", "Espinacas caducan", NeveraChefColors.SuccessSoft)
            MiniStoryRow("🥚", "Receta en 15 min", NeveraChefColors.WarningSoft)
            MiniStoryRow("🛒", "Solo falta yogur", NeveraChefColors.AccentSoft)
        }
        FloatingTag(
            text = "Voz",
            iconRes = Res.drawable.ic_nc_scan,
            background = NeveraChefColors.Blue,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 30.dp),
        )
        FloatingTag(
            text = "Foto",
            iconRes = Res.drawable.ic_nc_scan,
            background = Color.White,
            contentColor = NeveraChefColors.Ink,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 50.dp),
        )
        FloatingTag(
            text = "Compra util",
            iconRes = Res.drawable.ic_nc_shopping_basket,
            background = Color.White,
            contentColor = NeveraChefColors.Ink,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 14.dp),
        )
    }
}

@Composable
private fun MiniStoryRow(
    emoji: String,
    text: String,
    background: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(43.dp)
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(text, color = NeveraChefColors.Ink, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun FloatingTag(
    text: String,
    iconRes: DrawableResource,
    background: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(background, RoundedCornerShape(21.dp))
            .border(1.dp, if (background == Color.White) NeveraChefColors.Line else background, RoundedCornerShape(21.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(17.dp),
        )
        Text(text, color = contentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun OnboardingStep(
    number: String,
    title: String,
    body: String,
    numberBackground: Color,
    numberColor: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(18.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(numberBackground, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(number, color = numberColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = NeveraChefColors.Ink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(body, color = NeveraChefColors.Muted, fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}
