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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.Recipe
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_fruits
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_other
import neverachefai.shared.generated.resources.ic_cat_pasta_rice_legumes
import neverachefai.shared.generated.resources.ic_cat_vegetables
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_check_circle
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_clock
import neverachefai.shared.generated.resources.ic_nc_leaf
import neverachefai.shared.generated.resources.ic_nc_servings
import neverachefai.shared.generated.resources.recipe_fish_garlic
import neverachefai.shared.generated.resources.recipe_pancakes
import neverachefai.shared.generated.resources.recipe_tomato_scramble
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val DetailInk = Color(0xFF063D29)
private val DetailGreen = Color(0xFF008557)
private val DetailMint = Color(0xFFEAF3ED)
private val DetailCream = Color(0xFFFFFAF3)
private val DetailLine = Color(0xFFE8DDCA)
private val DetailMuted = Color(0xFF596273)
private val DetailWarn = Color(0xFFC46C00)
private val DetailHard = Color(0xFFE2262C)

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    onBack: () -> Unit,
    onStartGuide: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        DetailHeader(onBack = onBack)

        if (recipe == null) {
            EmptyDetail(onBack = onBack)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                HeroCard(recipe = recipe)
                QuickStats(recipe = recipe)
                IngredientOverview(recipe = recipe)
                StepsCard(steps = recipe.steps)
                StartButton(onStartGuide = onStartGuide)
                SourceCard(recipe = recipe)
            }
        }
    }
}

@Composable
private fun DetailHeader(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            onClick = onBack,
            shape = RoundedCornerShape(18.dp),
            color = DetailMint,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(42.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_arrow_back),
                    contentDescription = "Volver",
                    tint = DetailGreen,
                    modifier = Modifier.size(21.dp),
                )
            }
        }
        Text(
            text = "Detalle de receta",
            color = DetailInk,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HeroCard(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(30.dp))
            .background(DetailCream, RoundedCornerShape(30.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(30.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
                .background(Color(0xFFF0F7EF), RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(detailRecipeImage(recipe)),
                contentDescription = null,
                modifier = Modifier.size(width = 230.dp, height = 160.dp),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "IA local privada",
                        color = DetailGreen,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(DetailMint, RoundedCornerShape(17.dp))
                            .padding(horizontal = 11.dp, vertical = 7.dp),
                    )
                    Text(
                        text = "Lista",
                        color = DetailGreen,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(17.dp))
                            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(17.dp))
                            .padding(horizontal = 11.dp, vertical = 7.dp),
                    )
                }
                Text(
                    text = recipe.title,
                    color = DetailInk,
                    fontSize = 29.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (recipe.missingIngredients.isEmpty()) {
                        "Puedes cocinarla con lo que tienes ahora."
                    } else {
                        "Te falta poco: ${recipe.missingIngredients.joinToString(limit = 2) { it.prettyName() }}."
                    },
                    color = DetailMuted,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                )
            }
        }
    }
}

@Composable
private fun QuickStats(recipe: Recipe) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        StatCard(
            icon = Res.drawable.ic_nc_clock,
            value = "${recipe.estimatedMinutes} min",
            label = "tiempo",
            tint = DetailGreen,
            background = Color(0xFFF4FBF6),
            iconBackground = DetailMint,
            modifier = Modifier.weight(1f),
        )
        StatCard(
            icon = Res.drawable.ic_nc_servings,
            value = recipe.servings.toString(),
            label = if (recipe.servings == 1) "ración" else "raciones",
            tint = DetailGreen,
            background = Color(0xFFF4FBF6),
            iconBackground = DetailMint,
            modifier = Modifier.weight(1f),
        )
        DifficultyStatCard(recipe.difficulty, Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(
    icon: DrawableResource,
    value: String,
    label: String,
    tint: Color,
    background: Color,
    iconBackground: Color,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .height(82.dp)
            .shadow(2.dp, RoundedCornerShape(22.dp))
            .background(background, RoundedCornerShape(22.dp))
            .border(1.dp, tint.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconBackground, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                value,
                color = DetailInk,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(label, color = tint, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
private fun DifficultyStatCard(
    difficulty: Difficulty,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .height(82.dp)
            .shadow(2.dp, RoundedCornerShape(22.dp))
            .background(difficulty.backgroundColor, RoundedCornerShape(22.dp))
            .border(1.dp, difficulty.color.copy(alpha = 0.18f), RoundedCornerShape(22.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(difficulty.iconBackgroundColor, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center,
        ) {
            DifficultyBars(difficulty = difficulty)
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                difficultyLabel(difficulty),
                color = DetailInk,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text("dificultad", color = difficulty.color, fontSize = 11.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

@Composable
private fun DifficultyBars(difficulty: Difficulty) {
    val active = when (difficulty) {
        Difficulty.EASY -> 1
        Difficulty.MEDIUM -> 2
        Difficulty.HARD -> 3
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        listOf(13.dp, 18.dp, 23.dp).forEachIndexed { index, height ->
            val selected = index < active
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(height)
                    .background(
                        color = if (selected) difficulty.color else Color(0xFFE1E4DF),
                        shape = RoundedCornerShape(4.dp),
                    ),
            )
        }
    }
}

@Composable
private fun IngredientOverview(recipe: Recipe) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF4FBF6), RoundedCornerShape(26.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(26.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
    ) {
        IconSectionTitleRow(
            icon = Res.drawable.ic_nc_leaf,
            title = "Ingredientes",
            badge = "${recipe.missingIngredients.size} faltan",
            badgeWarn = recipe.missingIngredients.isNotEmpty(),
        )
        IngredientGroup(
            title = "Tienes",
            items = recipe.ingredientsUsed.ifEmpty { listOf("Sin coincidencias") },
            warn = false,
        )
        if (recipe.missingIngredients.isNotEmpty()) {
            IngredientGroup(title = "Te falta", items = recipe.missingIngredients, warn = true)
        }
    }
}

@Composable
private fun IconSectionTitleRow(
    icon: DrawableResource,
    title: String,
    badge: String,
    badgeWarn: Boolean,
    iconTint: Color = DetailGreen,
    iconBackground: Color = DetailMint,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(11.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(iconBackground, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(23.dp),
                )
            }
            Text(title, color = DetailInk, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = badge,
            color = if (badgeWarn) DetailWarn else DetailGreen,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(18.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun SectionTitleRow(
    title: String,
    badge: String? = null,
    badgeWarn: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = DetailInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        if (badge != null) {
            Text(
                text = badge,
                color = if (badgeWarn) DetailWarn else DetailGreen,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 11.dp, vertical = 7.dp),
            )
        }
    }
}

@Composable
private fun IngredientGroup(
    title: String,
    items: List<String>,
    warn: Boolean,
) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text(title, color = DetailMuted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        items.take(5).forEach { item ->
            IngredientRow(text = item.prettyName(), icon = ingredientIcon(item), warn = warn)
        }
    }
}

@Composable
private fun IngredientRow(
    text: String,
    icon: DrawableResource,
    warn: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, if (warn) Color(0xFFF4D8AC) else Color(0xFFDCEBDD), RoundedCornerShape(16.dp))
            .padding(horizontal = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(42.dp),
        )
        Text(
            text = text,
            color = if (warn) DetailWarn else DetailGreen,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (!warn) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_check_circle),
                contentDescription = null,
                tint = DetailGreen,
                modifier = Modifier.size(27.dp),
            )
        }
    }
}

@Composable
private fun StepsCard(steps: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DetailCream, RoundedCornerShape(26.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(26.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        IconSectionTitleRow(
            icon = Res.drawable.ic_nc_chef_hat,
            title = "Preparación",
            badge = "${steps.size} pasos",
            badgeWarn = false,
            iconTint = DetailWarn,
            iconBackground = Color(0xFFFFEDD3),
        )
        steps.take(4).forEachIndexed { index, step ->
            StepPreview(index = index, step = step, isLast = index == steps.take(4).lastIndex)
        }
    }
}

@Composable
private fun StepPreview(
    index: Int,
    step: String,
    isLast: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier.width(44.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(top = 36.dp)
                        .width(2.dp)
                        .height(48.dp)
                        .background(if (index == 0) DetailGreen else Color(0xFFD5E8DA)),
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(if (index == 0) DetailGreen else DetailMint, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "${index + 1}",
                    color = if (index == 0) Color.White else DetailGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text("Paso ${index + 1}", color = DetailInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(step, color = DetailMuted, fontSize = 16.sp, lineHeight = 22.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun SourceCard(recipe: Recipe) {
    val attribution = if (recipe.image.attributionRequired) {
        "Imagen: ${recipe.image.authorName.orEmpty()} · ${recipe.image.licenseName}"
    } else {
        "Imagen local"
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DetailMint, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(recipe.groundingSummary, color = DetailMuted, style = MaterialTheme.typography.bodySmall)
        Text(attribution, color = DetailMuted, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun StartButton(onStartGuide: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DetailGreen),
        onClick = onStartGuide,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_chef_hat),
            contentDescription = null,
            modifier = Modifier.size(21.dp),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text("Empezar cocina guiada", fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun EmptyDetail(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DetailCream, RoundedCornerShape(24.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(24.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Sin receta seleccionada", color = DetailInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Vuelve a generar recetas.", color = DetailMuted)
        Button(
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DetailGreen),
            onClick = onBack,
        ) {
            Text("Volver")
        }
    }
}

private fun difficultyLabel(difficulty: Difficulty): String {
    return when (difficulty) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Media"
        Difficulty.HARD -> "Alta"
    }
}

private val Difficulty.color: Color
    get() = when (this) {
        Difficulty.EASY -> DetailGreen
        Difficulty.MEDIUM -> DetailWarn
        Difficulty.HARD -> DetailHard
    }

private val Difficulty.backgroundColor: Color
    get() = when (this) {
        Difficulty.EASY -> Color(0xFFF4FBF6)
        Difficulty.MEDIUM -> Color(0xFFFFFAF3)
        Difficulty.HARD -> Color(0xFFFFF1F1)
    }

private val Difficulty.iconBackgroundColor: Color
    get() = when (this) {
        Difficulty.EASY -> DetailMint
        Difficulty.MEDIUM -> Color(0xFFFFEDD3)
        Difficulty.HARD -> Color(0xFFFFDADA)
    }

private fun detailRecipeImage(recipe: Recipe): DrawableResource {
    val joined = (recipe.title + " " + recipe.ingredientsUsed.joinToString()).lowercase()
    return when {
        "pescado" in joined || "lubina" in joined || "bacalao" in joined -> Res.drawable.recipe_fish_garlic
        "tomate" in joined || "espinaca" in joined || "verdura" in joined -> Res.drawable.recipe_tomato_scramble
        "platano" in joined || "fresa" in joined || "huevo" in joined || "tortilla" in joined -> Res.drawable.recipe_pancakes
        else -> recipeDrawableResource(recipe)
    }
}

private fun ingredientIcon(ingredient: String): DrawableResource {
    val normalized = ingredient.lowercase()
    return when {
        "huevo" in normalized -> Res.drawable.ic_cat_eggs
        "pescado" in normalized || "bacalao" in normalized || "lubina" in normalized -> Res.drawable.ic_cat_fish
        "pollo" in normalized || "ternera" in normalized || "carne" in normalized -> Res.drawable.ic_cat_meat
        "arroz" in normalized || "pasta" in normalized || "lenteja" in normalized || "garbanzo" in normalized -> Res.drawable.ic_cat_pasta_rice_legumes
        "platano" in normalized || "fresa" in normalized || "fruta" in normalized -> Res.drawable.ic_cat_fruits
        "espinaca" in normalized || "tomate" in normalized || "verdura" in normalized -> Res.drawable.ic_cat_vegetables
        else -> Res.drawable.ic_cat_other
    }
}

private fun String.prettyName(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
