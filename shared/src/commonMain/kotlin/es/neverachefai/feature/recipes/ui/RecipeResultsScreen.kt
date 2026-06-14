package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
private const val FreeRecipeLimit = 12
private const val RewardRecipeBatchSize = 20

@Composable
fun RecipeResultsScreen(
    result: RecipeGenerationResult?,
    unlockedRecipeIds: Set<String> = emptySet(),
    showGeneratedRecipeNotice: Boolean,
    onGeneratedRecipeNoticeDismiss: () -> Unit,
    onRecipeInfoClick: () -> Unit,
    onOpenDetail: (Recipe) -> Unit,
    onWatchRewardedAdForRecipes: (List<String>) -> Unit = {},
    onGenerateAgain: () -> Unit,
) {
    val recipes = result?.recipes.orEmpty()
    val freeRecipes = recipes.take(FreeRecipeLimit)
    val extraRecipes = recipes.drop(FreeRecipeLimit)
    val unlockedExtraRecipes = extraRecipes.filter { it.id in unlockedRecipeIds }
    val remainingExtraRecipes = extraRecipes.filterNot { it.id in unlockedRecipeIds }
    val rewardBatchRecipes = remainingExtraRecipes.take(RewardRecipeBatchSize)
    val rewardBatchRecipeIds = rewardBatchRecipes.map { it.id }
    val visibleRecipes = freeRecipes + unlockedExtraRecipes
    val availableRecipes = visibleRecipes.filter { it.missingIngredients.isEmpty() }
    val almostReadyRecipes = visibleRecipes.filter { it.missingIngredients.size in 1..2 }
    val shoppingNeededRecipes = visibleRecipes.filter { it.missingIngredients.size > 2 }
    val featuredRecipe = availableRecipes.firstOrNull() ?: almostReadyRecipes.firstOrNull()

    if (result != null && showGeneratedRecipeNotice) {
        GeneratedRecipeNoticeDialog(
            onDismiss = onGeneratedRecipeNoticeDismiss,
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { RecipesHeroHeader(onInfoClick = onRecipeInfoClick) }
        item {
            SummaryRow(
                pantryFoodCount = result?.pantryFoodCount ?: 0,
                recipeCount = recipes.size,
            )
        }
        item { FilterRow() }

        if (recipes.isEmpty()) {
            item { EmptyResultsCard(onGenerateAgain = onGenerateAgain) }
        } else {
            if (featuredRecipe != null) {
                item {
                    FeaturedRecipeCard(
                        recipe = featuredRecipe,
                        onClick = { onOpenDetail(featuredRecipe) },
                    )
                }
            }
            recipeSection(
                title = "Disponibles",
                subtitle = "Puedes cocinarlas con lo que tienes.",
                recipes = availableRecipes.filterNot { it.id == featuredRecipe?.id },
                onOpenDetail = onOpenDetail,
            )
            recipeSection(
                title = "Casi listas",
                subtitle = "Faltan 1 o 2 ingredientes.",
                recipes = almostReadyRecipes.filterNot { it.id == featuredRecipe?.id },
                onOpenDetail = onOpenDetail,
            )
            recipeSection(
                title = "Para completar",
                subtitle = "Necesitan varios ingredientes más.",
                recipes = shoppingNeededRecipes.filterNot { it.id == featuredRecipe?.id },
                onOpenDetail = onOpenDetail,
            )
            if (rewardBatchRecipes.isNotEmpty()) {
                item {
                    MoreRecipesGateCard(
                        visibleLimit = FreeRecipeLimit,
                        onUnlock = { onWatchRewardedAdForRecipes(rewardBatchRecipeIds) },
                    )
                }
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.recipeSection(
    title: String,
    subtitle: String,
    recipes: List<Recipe>,
    onOpenDetail: (Recipe) -> Unit,
) {
    if (recipes.isEmpty()) return
    item {
        RecipeSectionHeader(
            title = title,
            subtitle = subtitle,
            count = recipes.size,
        )
    }
    items(
        items = recipes,
        key = { it.id },
    ) { recipe ->
        CompactRecipeRow(
            recipe = recipe,
            onClick = { onOpenDetail(recipe) },
        )
    }
}

@Composable
private fun MoreRecipesGateCard(
    visibleLimit: Int,
    onUnlock: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, RecipeLine, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = RecipeCream),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFFFFEDD3), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_chef_hat),
                    contentDescription = null,
                    tint = RecipeGreen,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "Ver más recetas",
                    color = RecipeInk,
                    fontSize = 17.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Ahora ves $visibleLimit. Con un vídeo puedes ver 20 más.",
                    color = RecipeMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                )
            }
            Button(
                modifier = Modifier.height(42.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RecipeGreen),
                onClick = onUnlock,
            ) {
                Text("Vídeo", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GeneratedRecipeNoticeDialog(onDismiss: () -> Unit) {
    val shape = RoundedCornerShape(30.dp)
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(10.dp, shape)
                .border(1.dp, RecipeLine, shape),
            colors = CardDefaults.cardColors(containerColor = RecipeCream),
            shape = shape,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 26.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .background(RecipeMint, RoundedCornerShape(41.dp))
                        .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(41.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_nc_chef_hat),
                        contentDescription = null,
                        tint = RecipeGreen,
                        modifier = Modifier.size(39.dp),
                    )
                }
                Text(
                    text = "Recetas sugeridas",
                    color = RecipeInk,
                    fontSize = 31.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 34.sp,
                    textAlign = TextAlign.Center,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.68f)
                        .height(1.dp)
                        .background(Color(0xFFDCEBDD), RoundedCornerShape(1.dp)),
                )
                Text(
                    text = "Las recetas salen de una base local estructurada. El cálculo de ingredientes, raciones y orden es determinista. Revísalas antes de cocinar.",
                    color = RecipeMuted,
                    fontSize = 17.sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Center,
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RecipeGreen),
                    onClick = onDismiss,
                ) {
                    Text(
                        text = "Entendido",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipesHeroHeader(
    onInfoClick: () -> Unit,
) {
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
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
                RecipeInfoButton(onClick = onInfoClick)
            }
        }
        HeroFoodCluster()
    }
}

@Composable
private fun RecipeInfoButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE9DED3), CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "i",
            color = RecipeGreen,
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
        )
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
private fun RecipeSectionHeader(
    title: String,
    subtitle: String,
    count: Int,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = RecipeInk,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp,
            )
            Text(
                text = subtitle,
                color = RecipeMuted,
                fontSize = 13.sp,
                lineHeight = 16.sp,
            )
        }
        Text(
            text = "$count",
            color = RecipeGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .background(RecipeMint, RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 7.dp),
        )
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
                    TimeMetaChip("${recipe.servings} rac.")
                    MatchMetaChip("${recipe.matchScore}%")
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
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("Ver receta", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.16f), RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
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
                width = 112,
                height = 78,
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
                    lineHeight = 20.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    TimeMetaChip("${recipe.estimatedMinutes} min")
                    MatchMetaChip("${recipe.matchScore}%")
                    DifficultyMetaChip(recipe.difficulty)
                }
            }
        }
    }
}

@Composable
private fun LockedRecipeRow(
    recipe: Recipe,
    unlocked: Boolean,
    onUnlock: () -> Unit,
    onOpen: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .border(1.dp, if (unlocked) Color(0xFFDCEBDD) else RecipeLine, RoundedCornerShape(20.dp))
            .clickable(enabled = unlocked, onClick = onOpen),
        colors = CardDefaults.cardColors(containerColor = if (unlocked) Color.White else RecipeCream),
        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            RecipeImageTile(
                recipe = recipe,
                width = 96,
                height = 72,
                radius = 14,
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Text(
                    recipe.title,
                    color = RecipeInk,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 19.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    TimeMetaChip("${recipe.estimatedMinutes} min")
                    MatchMetaChip("${recipe.matchScore}%")
                }
                Text(
                    text = if (unlocked) {
                        "Desbloqueada"
                    } else {
                        "${recipe.missingIngredients.size} ingredientes faltan"
                    },
                    color = if (unlocked) RecipeGreen else RecipeMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Button(
                modifier = Modifier.height(40.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (unlocked) RecipeGreen else Color(0xFFFFEDD3),
                    contentColor = if (unlocked) Color.White else RecipeGreen,
                ),
                onClick = if (unlocked) onOpen else onUnlock,
            ) {
                Text(
                    text = if (unlocked) "Ver" else "Vídeo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
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
    radius: Int = 24,
) {
    val shape = RoundedCornerShape(radius.dp)
    Box(
        modifier = Modifier
            .size(width = width.dp, height = height.dp)
            .clip(shape)
            .background(Color(0xFFFFF6E8), shape)
            .border(1.dp, Color(0xFFE7E2D9), shape),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(recipeImage(recipe)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
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
private fun MatchMetaChip(text: String) {
    Row(
        modifier = Modifier
            .background(Color(0xFFEAF3ED), RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFD8EAE2), RoundedCornerShape(18.dp))
            .padding(horizontal = 9.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = text,
            color = RecipeGreen,
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
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(width = 5.dp, height = 16.dp)
                        .background(
                            color = if (index < level) color else Color(0xFFE5E5E0),
                            shape = RoundedCornerShape(4.dp),
                        ),
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
