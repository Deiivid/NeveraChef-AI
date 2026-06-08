package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.Recipe
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.recipe_fish_garlic
import neverachefai.shared.generated.resources.recipe_pancakes
import neverachefai.shared.generated.resources.recipe_tomato_scramble
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val GuideInk = Color(0xFF063D29)
private val GuideGreen = Color(0xFF008557)
private val GuideMint = Color(0xFFEAF3ED)
private val GuideCream = Color(0xFFFFFAF3)
private val GuideLine = Color(0xFFE8DDCA)
private val GuideMuted = Color(0xFF596273)

@Composable
fun RecipeCookingGuideScreen(
    recipe: Recipe?,
    onBack: () -> Unit,
) {
    var stepIndex by remember(recipe?.id) { mutableStateOf(0) }
    val steps = recipe?.steps.orEmpty().ifEmpty { listOf("Vuelve a seleccionar una receta para empezar.") }
    val safeIndex = stepIndex.coerceIn(0, steps.lastIndex)
    val progress = (safeIndex + 1).toFloat() / steps.size.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        GuideHeader(onBack = onBack)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            GuideHero(recipe = recipe)
            ProgressCard(current = safeIndex + 1, total = steps.size, progress = progress)
            CurrentStepCard(index = safeIndex, step = steps[safeIndex])
            IngredientsStrip(items = recipe?.ingredientsUsed.orEmpty())
            StepTimeline(steps = steps, current = safeIndex)
            ActionRow(
                isFirst = safeIndex == 0,
                isLast = safeIndex == steps.lastIndex,
                onPrevious = { if (stepIndex > 0) stepIndex -= 1 },
                onNext = {
                    if (safeIndex < steps.lastIndex) {
                        stepIndex += 1
                    } else {
                        onBack()
                    }
                },
            )
        }
    }
}

@Composable
private fun GuideHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Surface(
            onClick = onBack,
            shape = RoundedCornerShape(18.dp),
            color = GuideMint,
            modifier = Modifier.size(42.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_arrow_back),
                    contentDescription = "Volver",
                    tint = GuideGreen,
                    modifier = Modifier.size(21.dp),
                )
            }
        }
        Text("Cocina guiada", color = GuideInk, fontSize = 29.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GuideHero(recipe: Recipe?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideCream, RoundedCornerShape(25.dp))
            .border(1.dp, GuideLine, RoundedCornerShape(25.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(126.dp)
                .background(GuideMint, RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(recipe?.let { guideRecipeImage(it) } ?: Res.drawable.recipe_pancakes),
                contentDescription = null,
                modifier = Modifier.size(114.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = recipe?.title ?: "Receta",
                color = GuideInk,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 29.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoPill("${recipe?.estimatedMinutes ?: 15} min")
                InfoPill(recipe?.difficulty?.label ?: "Fácil")
            }
        }
    }
}

@Composable
private fun ProgressCard(
    current: Int,
    total: Int,
    progress: Float,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF3FBF6), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFD8ECD9), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(GuideMint, RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_chef_hat),
                contentDescription = null,
                tint = GuideGreen,
                modifier = Modifier.size(25.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Paso $current de $total", color = GuideInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFCDE7D8), RoundedCornerShape(8.dp)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .height(8.dp)
                        .background(GuideGreen, RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}

@Composable
private fun CurrentStepCard(
    index: Int,
    step: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(28.dp))
            .background(Color.White, RoundedCornerShape(28.dp))
            .border(1.dp, GuideLine, RoundedCornerShape(28.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(GuideMint, RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("${index + 1}", color = GuideInk, fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(stepTitle(step), color = GuideInk, fontSize = 23.sp, fontWeight = FontWeight.Bold, lineHeight = 27.sp)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GuideLine),
            )
            Text(step, color = GuideMuted, fontSize = 17.sp, lineHeight = 24.sp)
        }
    }
}

@Composable
private fun IngredientsStrip(items: List<String>) {
    if (items.isEmpty()) return
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(24.dp))
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Ingredientes", color = GuideInk, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.take(3).forEach { item ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF6FBF8), RoundedCornerShape(17.dp))
                        .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(17.dp))
                        .padding(horizontal = 12.dp, vertical = 13.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                ) {
                    Text(
                        text = item.prettyName(),
                        color = GuideInk,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text("Listo", color = GuideMuted, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun StepTimeline(
    steps: List<String>,
    current: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideMint, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Plan rápido", color = GuideInk, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        steps.take(4).forEachIndexed { index, step ->
            val active = index == current
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(if (active) GuideGreen else Color.White, RoundedCornerShape(13.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${index + 1}",
                        color = if (active) Color.White else GuideGreen,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = stepTitle(step),
                    color = if (active) GuideInk else GuideMuted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (active) FontWeight.Bold else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun InfoPill(text: String) {
    Text(
        text = text,
        color = GuideInk,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        maxLines = 1,
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(14.dp))
            .padding(horizontal = 11.dp, vertical = 7.dp),
    )
}

@Composable
private fun ActionRow(
    isFirst: Boolean,
    isLast: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedButton(
            modifier = Modifier
                .weight(1f)
                .height(58.dp),
            enabled = !isFirst,
            shape = RoundedCornerShape(19.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GuideGreen),
            onClick = onPrevious,
        ) {
            Text("Anterior", fontWeight = FontWeight.Bold)
        }
        Button(
            modifier = Modifier
                .weight(1.35f)
                .height(58.dp),
            shape = RoundedCornerShape(19.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GuideGreen),
            onClick = onNext,
        ) {
            Icon(painterResource(Res.drawable.ic_nc_chef_hat), null, modifier = Modifier.size(19.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (isLast) "Terminar" else "Siguiente paso", fontWeight = FontWeight.Bold)
        }
    }
}

private fun guideRecipeImage(recipe: Recipe): DrawableResource {
    val joined = (recipe.title + " " + recipe.ingredientsUsed.joinToString()).lowercase()
    return when {
        "pescado" in joined || "lubina" in joined || "bacalao" in joined -> Res.drawable.recipe_fish_garlic
        "tomate" in joined || "espinaca" in joined || "verdura" in joined -> Res.drawable.recipe_tomato_scramble
        "platano" in joined || "fresa" in joined || "huevo" in joined || "tortilla" in joined -> Res.drawable.recipe_pancakes
        else -> recipeDrawableResource(recipe)
    }
}

private fun stepTitle(step: String): String {
    return step.substringBefore(".").take(42).ifBlank { "Sigue este paso" }
}

private fun String.prettyName(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

private val Difficulty.label: String
    get() = when (this) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Media"
        Difficulty.HARD -> "Difícil"
    }
