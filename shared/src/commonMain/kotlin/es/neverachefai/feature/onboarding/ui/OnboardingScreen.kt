package es.neverachefai.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
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
import neverachefai.shared.generated.resources.ic_nc_microphone
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun OnboardingScreen(onContinue: () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        val compactHeight = maxHeight < 760.dp
        val horizontalPadding = if (maxWidth < 380.dp) 18.dp else 24.dp
        val verticalSpacing = if (compactHeight) 11.dp else 16.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = horizontalPadding, vertical = if (compactHeight) 14.dp else 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            ) {
                LocalFirstBadge()
                Text(
                    text = "Cocina mejor con lo que ya tienes",
                    color = NeveraChefColors.Ink,
                    fontSize = if (compactHeight) 25.sp else 29.sp,
                    lineHeight = if (compactHeight) 28.sp else 31.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "NeveraChef ordena tu nevera, revisa la IA antes de guardar y convierte tus alimentos en recetas y compra útil.",
                    color = NeveraChefColors.Muted,
                    fontSize = if (compactHeight) 13.sp else 14.sp,
                    lineHeight = if (compactHeight) 17.sp else 19.sp,
                    textAlign = TextAlign.Center,
                )
                OnboardingStoryCard(
                    compact = compactHeight,
                    modifier = Modifier.fillMaxWidth(),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(if (compactHeight) 7.dp else 9.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OnboardingStep(
                        number = "1",
                        title = "Añade alimentos como te venga mejor",
                        body = "Manual o por voz. Tú decides antes de guardar.",
                        numberBackground = NeveraChefColors.AccentSoft,
                        numberColor = NeveraChefColors.Blue,
                        compact = compactHeight,
                    )
                    OnboardingStep(
                        number = "2",
                        title = "Cocina con tu inventario real",
                        body = "Recetas con lo que tienes y avisos de caducidad.",
                        numberBackground = NeveraChefColors.SuccessSoft,
                        numberColor = Color(0xFF2F8F5B),
                        compact = compactHeight,
                    )
                    OnboardingStep(
                        number = "3",
                        title = "Compra solo lo que falta",
                        body = "La lista se ordena por nevera y despensa.",
                        numberBackground = NeveraChefColors.WarningSoft,
                        numberColor = Color(0xFF9B6A00),
                        compact = compactHeight,
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            Spacer(modifier = Modifier.height(if (compactHeight) 12.dp else 16.dp))
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (compactHeight) 50.dp else 52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeveraChefColors.Blue),
            ) {
                Text(
                    "Configurar NeveraChef",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
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
            text = "Sin login · datos en tu móvil",
            color = Color(0xFF2F8F5B),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun OnboardingStoryCard(
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(if (compact) 210.dp else 258.dp)
            .background(NeveraChefColors.Soft, RoundedCornerShape(30.dp))
            .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(30.dp))
            .padding(if (compact) 14.dp else 18.dp),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .size(
                    width = if (compact) 176.dp else 205.dp,
                    height = if (compact) 176.dp else 214.dp,
                )
                .background(Color.White, RoundedCornerShape(28.dp))
                .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(28.dp))
                .padding(if (compact) 10.dp else 12.dp),
            verticalArrangement = Arrangement.spacedBy(if (compact) 6.dp else 9.dp),
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
            MiniStoryRow("🥬", "Espinacas caducan", NeveraChefColors.SuccessSoft, compact)
            MiniStoryRow("🥚", "Receta en 15 min", NeveraChefColors.WarningSoft, compact)
            MiniStoryRow("🛒", "Solo falta yogur", NeveraChefColors.AccentSoft, compact)
        }
        FloatingTag(
            text = "Voz",
            iconRes = Res.drawable.ic_nc_microphone,
            background = NeveraChefColors.Blue,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = if (compact) 20.dp else 30.dp),
        )
        FloatingTag(
            text = "Manual",
            iconRes = Res.drawable.ic_nc_check_square,
            background = Color.White,
            contentColor = NeveraChefColors.Ink,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = if (compact) 36.dp else 50.dp),
        )
        FloatingTag(
            text = "Compra útil",
            iconRes = Res.drawable.ic_nc_shopping_basket,
            background = Color.White,
            contentColor = NeveraChefColors.Ink,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = if (compact) 10.dp else 14.dp),
        )
    }
}

@Composable
private fun MiniStoryRow(
    emoji: String,
    text: String,
    background: Color,
    compact: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (compact) 34.dp else 43.dp)
            .background(background, RoundedCornerShape(14.dp))
            .padding(horizontal = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(emoji, fontSize = if (compact) 15.sp else 18.sp)
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
    compact: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(18.dp))
            .border(1.dp, NeveraChefColors.Line, RoundedCornerShape(18.dp))
            .padding(if (compact) 9.dp else 12.dp),
        horizontalArrangement = Arrangement.spacedBy(if (compact) 9.dp else 11.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(if (compact) 27.dp else 30.dp)
                .background(numberBackground, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(number, color = numberColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = NeveraChefColors.Ink, fontSize = if (compact) 13.sp else 14.sp, fontWeight = FontWeight.Bold)
            Text(body, color = NeveraChefColors.Muted, fontSize = 12.sp, lineHeight = if (compact) 15.sp else 16.sp)
        }
    }
}
