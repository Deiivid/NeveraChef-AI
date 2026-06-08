package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeGenerationResult
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_clock
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.recipe_hero_cooking_tools
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val RecipeInk = Color(0xFF063D29)
private val RecipeGreen = Color(0xFF008557)
private val RecipeMint = Color(0xFFEAF3ED)
private val RecipeCream = Color(0xFFFFFAF3)
private val RecipeLine = Color(0xFFE8DDCA)
private val RecipeMuted = Color(0xFF596273)
private val DifficultyMedium = Color(0xFFF4B000)
private val DifficultyHard = Color(0xFFD94A38)

@Composable
fun RecipeResultsScreen(
    result: RecipeGenerationResult?,
    onOpenDetail: (Recipe) -> Unit,
    onGenerateAgain: () -> Unit,
) {
    val recipes = result?.recipes.orEmpty()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        RecipesHeroHeader()
        SummaryRow(
            pantryFoodCount = result?.pantryFoodCount ?: 0,
            recipeCount = recipes.size,
        )
        FilterRow()

        if (recipes.isEmpty()) {
            EmptyResultsCard(onGenerateAgain = onGenerateAgain)
        } else {
            FeaturedRecipeCard(
                recipe = recipes.first(),
                onClick = { onOpenDetail(recipes.first()) },
            )
            recipes.drop(1).take(3).forEach { recipe ->
                CompactRecipeRow(
                    recipe = recipe,
                    onClick = { onOpenDetail(recipe) },
                )
            }
        }
    }
}

@Composable
private fun RecipesHeroHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, start = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Recetas",
                color = RecipeInk,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 37.sp,
            )
            Row(
                modifier = Modifier
                    .background(RecipeMint, RoundedCornerShape(18.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_chef_hat),
                    contentDescription = null,
                    tint = RecipeGreen,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    "IA local privada",
                    color = RecipeGreen,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        HeroFoodCluster()
    }
}

@Composable
private fun HeroFoodCluster() {
    Box(
        modifier = Modifier.size(width = 228.dp, height = 132.dp),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.recipe_hero_cooking_tools),
            contentDescription = null,
            modifier = Modifier.size(width = 228.dp, height = 128.dp),
        )
    }
}

@Composable
private fun SummaryRow(
    pantryFoodCount: Int,
    recipeCount: Int,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        SummaryCard(
            icon = Res.drawable.ic_nc_fridge,
            value = pantryFoodCount.toString(),
            label = "alimentos",
            green = true,
            modifier = Modifier.weight(1f),
        )
        SummaryCard(
            icon = Res.drawable.ic_nc_chef_hat,
            value = recipeCount.toString(),
            label = "recetas",
            green = false,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SummaryCard(
    icon: DrawableResource,
    value: String,
    label: String,
    green: Boolean,
    modifier: Modifier = Modifier,
) {
    val tint = if (green) RecipeGreen else Color(0xFFC46C00)
    val background = if (green) Color(0xFFF4FBF6) else RecipeCream
    val iconBackground = if (green) RecipeMint else Color(0xFFFFEDD3)
    Card(
        modifier = modifier
            .height(76.dp)
            .border(1.dp, if (green) Color(0xFFDCEBDD) else RecipeLine, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(iconBackground, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(24.dp),
                )
            }
            Column {
                Text(value, color = RecipeInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(label, color = RecipeGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FilterRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
        FilterPill("Recomendadas", true)
        FilterPill("Rápidas", false)
        FilterPill("Desayuno", false)
        FilterPill("Cena", false)
    }
}

@Composable
private fun FeaturedRecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(24.dp))
            .border(1.dp, RecipeLine, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = RecipeCream),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            RecipeImageTile(
                recipe = recipe,
                width = 142,
                height = 142,
                imageWidth = 150,
                imageHeight = 118,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    recipe.title,
                    color = RecipeInk,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TimeMetaChip("${recipe.estimatedMinutes} min")
                    DifficultyMetaChip(recipe.difficulty)
                }
                Text(
                    "Usa ${recipe.ingredientsUsed.ifEmpty { listOf("tu inventario") }.joinToString()}",
                    color = RecipeMuted,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RecipeGreen),
                    onClick = onClick,
                ) {
                    Text("Ver receta", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_arrow_back),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(17.dp)
                            .graphicsLayer { rotationZ = 180f },
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactRecipeRow(
    recipe: Recipe,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFE7E2D9), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            RecipeImageTile(
                recipe = recipe,
                width = 126,
                height = 74,
                imageWidth = 112,
                imageHeight = 68,
                radius = 14,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    recipe.title,
                    color = RecipeInk,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    TimeMetaChip("${recipe.estimatedMinutes} min")
                    DifficultyMetaChip(recipe.difficulty)
                }
            }
            Text(
                text = ">",
                color = Color(0xFF747A86),
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .width(18.dp),
            )
        }
    }
}

@Composable
private fun EmptyResultsCard(onGenerateAgain: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, RecipeLine, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = RecipeCream),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text("Sin recetas útiles", color = RecipeInk, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Añade alimentos reconocibles o prueba con más inventario.", color = RecipeMuted)
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RecipeGreen),
                onClick = onGenerateAgain,
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun RecipeImageTile(
    recipe: Recipe,
    width: Int,
    height: Int,
    imageWidth: Int,
    imageHeight: Int,
    radius: Int = 24,
) {
    Box(
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .background(Color(0xFFFFF6E8), RoundedCornerShape(radius.dp))
            .border(1.dp, Color(0xFFE7E2D9), RoundedCornerShape(radius.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(recipeImage(recipe)),
            contentDescription = null,
            modifier = Modifier.size(width = imageWidth.dp, height = imageHeight.dp),
        )
    }
}

@Composable
private fun FilterPill(
    text: String,
    selected: Boolean,
) {
    val background = if (selected) RecipeGreen else Color.White
    val foreground = if (selected) Color.White else Color(0xFF3F4858)
    Text(
        text = text,
        color = foreground,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        modifier = Modifier
            .background(background, RoundedCornerShape(20.dp))
            .border(1.dp, if (selected) RecipeGreen else Color(0xFFE1E2E3), RoundedCornerShape(20.dp))
            .padding(horizontal = 11.dp, vertical = 9.dp),
    )
}

@Composable
private fun TimeMetaChip(text: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFFF6FAF8), RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFD8EAE2), RoundedCornerShape(18.dp))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_nc_clock),
            contentDescription = null,
            tint = RecipeGreen,
            modifier = Modifier.size(14.dp),
        )
        Text(
            text = text,
            color = RecipeInk,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
        )
    }
}

@Composable
private fun DifficultyMetaChip(difficulty: Difficulty) {
    val level = difficultyLevel(difficulty)
    val color = difficultyColor(difficulty)
    Row(
        modifier = Modifier
            .background(Color(0xFFFFFEFC), RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFE3E6E0), RoundedCornerShape(18.dp))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp), verticalAlignment = Alignment.CenterVertically) {
            repeat(level) {
                Box(
                    modifier = Modifier
                        .size(width = 5.dp, height = 16.dp)
                        .background(color, RoundedCornerShape(4.dp)),
                )
            }
        }
        Text(
            text = difficultyLabel(difficulty),
            color = Color(0xFF3F4858),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

private fun recipeImage(recipe: Recipe): DrawableResource {
    return recipeDrawableResource(recipe)
}

private fun difficultyLevel(difficulty: Difficulty): Int {
    return when (difficulty) {
        Difficulty.EASY -> 1
        Difficulty.MEDIUM -> 2
        Difficulty.HARD -> 3
    }
}

private fun difficultyColor(difficulty: Difficulty): Color {
    return when (difficulty) {
        Difficulty.EASY -> RecipeGreen
        Difficulty.MEDIUM -> DifficultyMedium
        Difficulty.HARD -> DifficultyHard
    }
}

private fun difficultyLabel(difficulty: Difficulty): String {
    return when (difficulty) {
        Difficulty.EASY -> "Fácil"
        Difficulty.MEDIUM -> "Media"
        Difficulty.HARD -> "Difícil"
    }
}
