package es.neverachefai.feature.recipes.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.neverachefai.feature.recipes.domain.model.Difficulty
import es.neverachefai.feature.recipes.domain.model.Recipe
import es.neverachefai.feature.recipes.domain.model.RecipeStep
import es.neverachefai.feature.shopping.ui.inferShoppingIconKey
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_cat_bread
import neverachefai.shared.generated.resources.ic_cat_cheese
import neverachefai.shared.generated.resources.ic_cat_eggs
import neverachefai.shared.generated.resources.ic_cat_fish
import neverachefai.shared.generated.resources.ic_cat_fruits
import neverachefai.shared.generated.resources.ic_cat_juice
import neverachefai.shared.generated.resources.ic_cat_legumes
import neverachefai.shared.generated.resources.ic_cat_meat
import neverachefai.shared.generated.resources.ic_cat_milk
import neverachefai.shared.generated.resources.ic_cat_other
import neverachefai.shared.generated.resources.ic_cat_pasta
import neverachefai.shared.generated.resources.ic_cat_rice
import neverachefai.shared.generated.resources.ic_cat_snacks
import neverachefai.shared.generated.resources.ic_cat_sweets
import neverachefai.shared.generated.resources.ic_cat_vegetables
import neverachefai.shared.generated.resources.ic_cat_yogurts
import neverachefai.shared.generated.resources.ic_bread_baguette
import neverachefai.shared.generated.resources.ic_bread_burger
import neverachefai.shared.generated.resources.ic_bread_hotdog
import neverachefai.shared.generated.resources.ic_bread_sliced
import neverachefai.shared.generated.resources.ic_fish_cod
import neverachefai.shared.generated.resources.ic_fish_hake
import neverachefai.shared.generated.resources.ic_fish_salmon
import neverachefai.shared.generated.resources.ic_fish_sardine
import neverachefai.shared.generated.resources.ic_fish_seabass
import neverachefai.shared.generated.resources.ic_fish_trout
import neverachefai.shared.generated.resources.ic_fish_tuna
import neverachefai.shared.generated.resources.ic_fruit_apple
import neverachefai.shared.generated.resources.ic_fruit_avocado
import neverachefai.shared.generated.resources.ic_fruit_banana
import neverachefai.shared.generated.resources.ic_fruit_blackberry
import neverachefai.shared.generated.resources.ic_fruit_blueberry
import neverachefai.shared.generated.resources.ic_fruit_cherry
import neverachefai.shared.generated.resources.ic_fruit_cherimoya
import neverachefai.shared.generated.resources.ic_fruit_coconut
import neverachefai.shared.generated.resources.ic_fruit_date
import neverachefai.shared.generated.resources.ic_fruit_fig
import neverachefai.shared.generated.resources.ic_fruit_grape
import neverachefai.shared.generated.resources.ic_fruit_grapefruit
import neverachefai.shared.generated.resources.ic_fruit_kiwi
import neverachefai.shared.generated.resources.ic_fruit_lemon
import neverachefai.shared.generated.resources.ic_fruit_lime
import neverachefai.shared.generated.resources.ic_fruit_loquat
import neverachefai.shared.generated.resources.ic_fruit_mandarin
import neverachefai.shared.generated.resources.ic_fruit_mango
import neverachefai.shared.generated.resources.ic_fruit_melon
import neverachefai.shared.generated.resources.ic_fruit_orange
import neverachefai.shared.generated.resources.ic_fruit_papaya
import neverachefai.shared.generated.resources.ic_fruit_peach
import neverachefai.shared.generated.resources.ic_fruit_pear
import neverachefai.shared.generated.resources.ic_fruit_persimmon
import neverachefai.shared.generated.resources.ic_fruit_pineapple
import neverachefai.shared.generated.resources.ic_fruit_plum
import neverachefai.shared.generated.resources.ic_fruit_pomegranate
import neverachefai.shared.generated.resources.ic_fruit_raspberry
import neverachefai.shared.generated.resources.ic_fruit_strawberry
import neverachefai.shared.generated.resources.ic_fruit_tomato
import neverachefai.shared.generated.resources.ic_fruit_watermelon
import neverachefai.shared.generated.resources.ic_juice_apple
import neverachefai.shared.generated.resources.ic_juice_blueberry
import neverachefai.shared.generated.resources.ic_juice_grape
import neverachefai.shared.generated.resources.ic_juice_multifruit
import neverachefai.shared.generated.resources.ic_juice_orange
import neverachefai.shared.generated.resources.ic_juice_peach
import neverachefai.shared.generated.resources.ic_juice_pineapple
import neverachefai.shared.generated.resources.ic_juice_tomato
import neverachefai.shared.generated.resources.ic_meat_beef
import neverachefai.shared.generated.resources.ic_meat_chicken
import neverachefai.shared.generated.resources.ic_meat_lamb
import neverachefai.shared.generated.resources.ic_meat_pork
import neverachefai.shared.generated.resources.ic_meat_rabbit
import neverachefai.shared.generated.resources.ic_meat_sausage
import neverachefai.shared.generated.resources.ic_meat_turkey
import neverachefai.shared.generated.resources.ic_milk_lactose_free
import neverachefai.shared.generated.resources.ic_milk_semi
import neverachefai.shared.generated.resources.ic_milk_skimmed
import neverachefai.shared.generated.resources.ic_milk_whole
import neverachefai.shared.generated.resources.ic_snack_chips
import neverachefai.shared.generated.resources.ic_snack_crackers
import neverachefai.shared.generated.resources.ic_snack_nuts
import neverachefai.shared.generated.resources.ic_sweets_bonbons
import neverachefai.shared.generated.resources.ic_sweets_candy
import neverachefai.shared.generated.resources.ic_sweets_chocolate
import neverachefai.shared.generated.resources.ic_sweets_cookies
import neverachefai.shared.generated.resources.ic_sweets_pastry
import neverachefai.shared.generated.resources.ic_vegetable_artichoke
import neverachefai.shared.generated.resources.ic_vegetable_carrot
import neverachefai.shared.generated.resources.ic_vegetable_cauliflower
import neverachefai.shared.generated.resources.ic_vegetable_cucumber
import neverachefai.shared.generated.resources.ic_vegetable_eggplant
import neverachefai.shared.generated.resources.ic_vegetable_garlic
import neverachefai.shared.generated.resources.ic_vegetable_green_beans
import neverachefai.shared.generated.resources.ic_vegetable_leek
import neverachefai.shared.generated.resources.ic_vegetable_lettuce
import neverachefai.shared.generated.resources.ic_vegetable_onion
import neverachefai.shared.generated.resources.ic_vegetable_peas
import neverachefai.shared.generated.resources.ic_vegetable_pepper
import neverachefai.shared.generated.resources.ic_vegetable_potato
import neverachefai.shared.generated.resources.ic_vegetable_pumpkin
import neverachefai.shared.generated.resources.ic_vegetable_spinach
import neverachefai.shared.generated.resources.ic_vegetable_zucchini
import neverachefai.shared.generated.resources.ic_nc_arrow_back
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_leaf
import neverachefai.shared.generated.resources.ic_nc_servings
import neverachefai.shared.generated.resources.ic_recipe_stat_clock
import neverachefai.shared.generated.resources.ic_recipe_status_check
import neverachefai.shared.generated.resources.ic_recipe_status_shopping_basket
import neverachefai.shared.generated.resources.recipe_step_bowl_premium
import neverachefai.shared.generated.resources.recipe_step_pan_premium
import neverachefai.shared.generated.resources.recipe_step_stir_premium
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val DetailInk = Color(0xFF063D29)
private val DetailGreen = Color(0xFF008557)
private val DetailMint = Color(0xFFEAF3ED)
private val DetailCream = Color(0xFFFFFAF3)
private val DetailLine = Color(0xFFE8DDCA)
private val DetailMuted = Color(0xFF596273)
private val DetailWarn = Color(0xFFC46C00)
private val DetailMedium = Color(0xFFE0A400)
private val DetailHard = Color(0xFFE2262C)

private enum class IngredientAvailability {
    Available,
    MissingRequired,
    MissingOptional,
    AddedToShopping,
}

@Composable
fun RecipeDetailScreen(
    recipe: Recipe?,
    onBack: () -> Unit,
    onAddIngredientToShoppingList: (String) -> Unit,
    shoppingIngredientNames: List<String> = emptyList(),
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
                IngredientOverview(
                    recipe = recipe,
                    onAddIngredientToShoppingList = onAddIngredientToShoppingList,
                    shoppingIngredientNames = shoppingIngredientNames,
                )
                StepsCard(recipe = recipe)
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
            .shadow(6.dp, RoundedCornerShape(30.dp))
            .background(DetailCream, RoundedCornerShape(30.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(30.dp)),
    ) {
        Image(
            painter = painterResource(detailRecipeImage(recipe)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(226.dp)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "IA local privada",
                    color = DetailGreen,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(DetailMint, RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                )
                Text(
                    text = "Lista",
                    color = DetailGreen,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(18.dp))
                        .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                )
            }
            Text(
                text = recipe.title,
                color = DetailInk,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 30.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = if (recipe.missingIngredients.isEmpty()) {
                    "Puedes cocinarla con lo que tienes ahora."
                } else {
                    "Te falta poco: ${recipe.missingIngredients.joinToString(limit = 2) { it.prettyName() }}."
                },
                color = DetailMuted,
                fontSize = 15.sp,
                lineHeight = 21.sp,
            )
            HeroStatsBar(recipe)
        }
    }
}

@Composable
private fun HeroStatsBar(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(24.dp))
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(24.dp))
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HeroStatItem(
            icon = Res.drawable.ic_recipe_stat_clock,
            value = "${recipe.estimatedMinutes} min",
            modifier = Modifier.weight(1f),
        )
        HeroStatsDivider()
        HeroStatItem(
            icon = Res.drawable.ic_nc_servings,
            value = recipe.servings.toString(),
            modifier = Modifier.weight(1f),
        )
        HeroStatsDivider()
        HeroDifficultyItem(
            difficulty = recipe.difficulty,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun HeroStatItem(
    icon: DrawableResource,
    value: String,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .height(54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(27.dp)
                .background(DetailMint, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = DetailGreen,
                modifier = Modifier.size(16.dp),
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            value,
            color = DetailInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HeroDifficultyItem(
    difficulty: Difficulty,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .height(54.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(27.dp)
                .background(difficulty.iconBackgroundColor, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            DifficultyMeter(difficulty = difficulty, modifier = Modifier.size(21.dp))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            difficultyLabel(difficulty),
            color = DetailInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun HeroStatsDivider() {
    Box(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .width(1.dp)
            .height(48.dp)
            .background(DetailLine),
    )
}

@Composable
private fun DifficultyMeter(
    difficulty: Difficulty,
    modifier: Modifier = Modifier.size(22.dp),
) {
    val progress = when (difficulty) {
        Difficulty.EASY -> 0.35f
        Difficulty.MEDIUM -> 0.68f
        Difficulty.HARD -> 1f
    }
    val color = difficulty.color
    Canvas(modifier = modifier) {
        val strokeWidth = 2.4.dp.toPx()
        val diameter = size.minDimension - strokeWidth
        val topLeft = Offset(
            x = (size.width - diameter) / 2f,
            y = (size.height - diameter) / 2f + 1.dp.toPx(),
        )
        val center = Offset(size.width / 2f, topLeft.y + diameter / 2f)
        val arcSize = androidx.compose.ui.geometry.Size(diameter, diameter)
        drawArc(
            color = Color(0xFFE1E4DF),
            startAngle = 150f,
            sweepAngle = 240f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        drawArc(
            color = color,
            startAngle = 150f,
            sweepAngle = 240f * progress,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
        )
        val angle = (150.0 + 240.0 * progress) * PI / 180.0
        val needleEnd = Offset(
            x = center.x + cos(angle).toFloat() * diameter * 0.3f,
            y = center.y + sin(angle).toFloat() * diameter * 0.3f,
        )
        drawLine(
            color = color,
            start = center,
            end = needleEnd,
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawCircle(color = color, radius = 2.1.dp.toPx(), center = center)
    }
}

@Composable
private fun IngredientOverview(
    recipe: Recipe,
    onAddIngredientToShoppingList: (String) -> Unit,
    shoppingIngredientNames: List<String>,
) {
    val optionalIngredients = recipe.optionalIngredients
    val optionalAvailable = optionalIngredients.filter { it in recipe.ingredientsUsed }
    val requiredAvailable = recipe.ingredientsUsed.filterNot { it in optionalAvailable }
    val shoppingIngredients = shoppingIngredientNames.map { it.toIngredientKey() }.toSet()
    val ingredientAmounts = recipe.ingredientAmounts.mapKeys { it.key.toIngredientKey() }
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
        if (requiredAvailable.isNotEmpty()) {
            IngredientGroup(
                title = "Tienes",
                items = requiredAvailable,
                statusFor = { IngredientAvailability.Available },
                amountFor = { ingredientAmounts[it.toIngredientKey()] },
                onAddIngredientToShoppingList = onAddIngredientToShoppingList,
            )
        }
        if (recipe.missingIngredients.isNotEmpty()) {
            IngredientGroup(
                title = "Te falta",
                items = recipe.missingIngredients,
                statusFor = { ingredient ->
                    if (ingredient.toIngredientKey() in shoppingIngredients) {
                        IngredientAvailability.AddedToShopping
                    } else {
                        IngredientAvailability.MissingRequired
                    }
                },
                amountFor = { ingredientAmounts[it.toIngredientKey()] },
                onAddIngredientToShoppingList = onAddIngredientToShoppingList,
            )
        }
        if (optionalIngredients.isNotEmpty()) {
            IngredientGroup(
                title = "Opcionales",
                items = optionalIngredients,
                statusFor = { ingredient ->
                    when {
                        ingredient in recipe.ingredientsUsed -> IngredientAvailability.Available
                        ingredient.toIngredientKey() in shoppingIngredients -> IngredientAvailability.AddedToShopping
                        else -> IngredientAvailability.MissingOptional
                    }
                },
                amountFor = { ingredientAmounts[it.toIngredientKey()] },
                onAddIngredientToShoppingList = onAddIngredientToShoppingList,
            )
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
    statusFor: (String) -> IngredientAvailability,
    amountFor: (String) -> String?,
    onAddIngredientToShoppingList: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text(title, color = DetailMuted, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        items.take(8).forEach { item ->
            IngredientRow(
                ingredient = item,
                icon = ingredientIcon(item),
                status = statusFor(item),
                amount = amountFor(item),
                onAddIngredientToShoppingList = onAddIngredientToShoppingList,
            )
        }
    }
}

@Composable
private fun IngredientRow(
    ingredient: String,
    icon: DrawableResource,
    status: IngredientAvailability,
    amount: String?,
    onAddIngredientToShoppingList: (String) -> Unit,
) {
    val warn = status == IngredientAvailability.MissingRequired
    val checked = status == IngredientAvailability.Available
    val addedToShopping = status == IngredientAvailability.AddedToShopping
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
        Box(
            modifier = Modifier.size(52.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(44.dp),
            )
        }
        Text(
            text = ingredient.displayIngredientName(amount),
            color = when {
                warn -> DetailWarn
                checked || addedToShopping -> DetailGreen
                else -> DetailMuted
            },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        if (checked) {
            CheckStatusChip()
        } else if (addedToShopping) {
            AddedStatusChip()
        } else {
            BuyIngredientChip(
                onClick = { onAddIngredientToShoppingList(ingredient) },
            )
        }
    }
}

@Composable
private fun CheckStatusChip() {
    Box(
        modifier = Modifier
            .size(38.dp)
            .background(Color(0xFFF5FAF6), RoundedCornerShape(19.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(19.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_recipe_status_check),
            contentDescription = null,
            tint = DetailGreen,
            modifier = Modifier.size(19.dp),
        )
    }
}

@Composable
private fun BuyIngredientChip(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF5FAF6),
            contentColor = DetailGreen,
        ),
        border = BorderStroke(1.dp, Color(0xFFDCEBDD)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 14.dp),
        onClick = onClick,
    ) {
        Text("Comprar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            painter = painterResource(Res.drawable.ic_recipe_status_shopping_basket),
            contentDescription = null,
            modifier = Modifier.size(17.dp),
        )
    }
}

@Composable
private fun AddedStatusChip() {
    Row(
        modifier = Modifier
            .height(40.dp)
            .background(Color(0xFFE6F2EA), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFFD7E7DB), RoundedCornerShape(20.dp))
            .padding(start = 14.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Añadido", color = DetailGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(Color(0xFF8FC9A2), RoundedCornerShape(13.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_recipe_status_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(15.dp),
            )
        }
    }
}

@Composable
private fun StepsCard(recipe: Recipe) {
    val steps = recipe.steps
    val visibleSteps = steps.take(5)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(30.dp))
            .background(DetailCream, RoundedCornerShape(30.dp))
            .border(1.dp, DetailLine, RoundedCornerShape(30.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        PreparationHeader()
        PreparationSummary(recipe = recipe)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, Color(0xFFF0E4D0), RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
        ) {
            visibleSteps.forEachIndexed { index, step ->
                StepPreview(index = index, step = step, isLast = index == visibleSteps.lastIndex)
            }
        }
    }
}

@Composable
private fun PreparationHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFFFEDD3), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_nc_chef_hat),
                contentDescription = null,
                tint = DetailWarn,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = "Preparación",
            color = DetailInk,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun PreparationSummary(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FCF9), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PreparationSummaryItem(
            value = "${recipe.estimatedMinutes} min",
            modifier = Modifier.weight(1f),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_recipe_stat_clock),
                contentDescription = null,
                tint = DetailGreen,
                modifier = Modifier.size(20.dp),
            )
        }
        PreparationSummaryDivider()
        PreparationDifficultyItem(
            difficulty = recipe.difficulty,
            value = difficultyLabel(recipe.difficulty),
            modifier = Modifier.weight(1f),
        )
        PreparationSummaryDivider()
        PreparationSummaryItem(
            value = "${recipe.steps.size} pasos",
            modifier = Modifier.weight(1f),
        ) {
            StepsIndicatorIcon(modifier = Modifier.size(width = 22.dp, height = 20.dp))
        }
    }
}

@Composable
private fun PreparationSummaryItem(
    value: String,
    modifier: Modifier,
    icon: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        icon()
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = value,
            color = DetailInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PreparationDifficultyItem(
    difficulty: Difficulty,
    value: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        DifficultyMeter(difficulty = difficulty, modifier = Modifier.size(21.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Text(
            text = value,
            color = DetailInk,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun PreparationSummaryDivider() {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(1.dp)
            .height(30.dp)
            .background(Color(0xFFDCEBDD)),
    )
}

@Composable
private fun StepsIndicatorIcon(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(3) { index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = "${index + 1}",
                    color = DetailGreen,
                    fontSize = 6.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 6.sp,
                    modifier = Modifier.width(4.dp),
                )
                Box(
                    modifier = Modifier
                        .width(14.dp)
                        .height(2.dp)
                        .background(DetailGreen, RoundedCornerShape(2.dp)),
                )
            }
        }
    }
}

@Composable
private fun StepPreview(
    index: Int,
    step: RecipeStep,
    isLast: Boolean,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(bottom = if (isLast) 0.dp else 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .width(36.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                StepNumberBadge(index = index)
                if (!isLast) {
                    TimelineDottedLine(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .weight(1f),
                    )
                }
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        text = step.title,
                        color = DetailInk,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 19.sp,
                    )
                    Text(
                        text = step.tip ?: step.instruction,
                        color = DetailMuted,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                    )
                }
                StepCookingIcon(index = index)
            }
        }
        if (!isLast) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 54.dp, bottom = 14.dp)
                    .height(1.dp)
                    .background(Color(0xFFF0E4D0)),
            )
        }
    }
}

@Composable
private fun StepNumberBadge(index: Int) {
    Box(
        modifier = Modifier
            .size(width = 32.dp, height = 42.dp)
            .background(DetailMint, RoundedCornerShape(13.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(13.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${index + 1}",
            color = DetailGreen,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun TimelineDottedLine(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .width(2.dp)
            .fillMaxHeight(),
    ) {
        drawLine(
            color = Color(0xFFB9DEC9),
            start = Offset(size.width / 2f, 0f),
            end = Offset(size.width / 2f, size.height),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 6.dp.toPx())),
        )
    }
}

@Composable
private fun StepCookingIcon(index: Int) {
    val icon = when (index % 3) {
        0 -> Res.drawable.recipe_step_pan_premium
        1 -> Res.drawable.recipe_step_stir_premium
        else -> Res.drawable.recipe_step_bowl_premium
    }
    Box(
        modifier = Modifier
            .size(44.dp)
            .background(DetailMint, RoundedCornerShape(22.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
        )
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
    return recipeDrawableResource(recipe)
}

private fun ingredientIcon(ingredient: String): DrawableResource {
    return when (inferShoppingIconKey(ingredient)) {
        "eggs" -> Res.drawable.ic_cat_eggs
        "fish" -> Res.drawable.ic_cat_fish
        "fish_hake" -> Res.drawable.ic_fish_hake
        "fish_cod" -> Res.drawable.ic_fish_cod
        "fish_seabass" -> Res.drawable.ic_fish_seabass
        "fish_salmon" -> Res.drawable.ic_fish_salmon
        "fish_tuna" -> Res.drawable.ic_fish_tuna
        "fish_sardine" -> Res.drawable.ic_fish_sardine
        "fish_trout" -> Res.drawable.ic_fish_trout
        "meat" -> Res.drawable.ic_cat_meat
        "meat_beef" -> Res.drawable.ic_meat_beef
        "meat_pork" -> Res.drawable.ic_meat_pork
        "meat_chicken" -> Res.drawable.ic_meat_chicken
        "meat_turkey" -> Res.drawable.ic_meat_turkey
        "meat_lamb" -> Res.drawable.ic_meat_lamb
        "meat_rabbit" -> Res.drawable.ic_meat_rabbit
        "meat_sausage" -> Res.drawable.ic_meat_sausage
        "bread" -> Res.drawable.ic_cat_bread
        "bread_baguette" -> Res.drawable.ic_bread_baguette
        "bread_sliced" -> Res.drawable.ic_bread_sliced
        "bread_burger" -> Res.drawable.ic_bread_burger
        "bread_hotdog" -> Res.drawable.ic_bread_hotdog
        "milk" -> Res.drawable.ic_cat_milk
        "milk_whole" -> Res.drawable.ic_milk_whole
        "milk_semi" -> Res.drawable.ic_milk_semi
        "milk_lactose_free" -> Res.drawable.ic_milk_lactose_free
        "milk_skimmed" -> Res.drawable.ic_milk_skimmed
        "yogurts" -> Res.drawable.ic_cat_yogurts
        "cheese" -> Res.drawable.ic_cat_cheese
        "pasta" -> Res.drawable.ic_cat_pasta
        "rice" -> Res.drawable.ic_cat_rice
        "legumes" -> Res.drawable.ic_cat_legumes
        "juice" -> Res.drawable.ic_cat_juice
        "juice_orange" -> Res.drawable.ic_juice_orange
        "juice_pineapple" -> Res.drawable.ic_juice_pineapple
        "juice_peach" -> Res.drawable.ic_juice_peach
        "juice_blueberry" -> Res.drawable.ic_juice_blueberry
        "juice_multifruit" -> Res.drawable.ic_juice_multifruit
        "juice_apple" -> Res.drawable.ic_juice_apple
        "juice_tomato" -> Res.drawable.ic_juice_tomato
        "juice_grape" -> Res.drawable.ic_juice_grape
        "snacks" -> Res.drawable.ic_cat_snacks
        "snack_chips" -> Res.drawable.ic_snack_chips
        "snack_nuts" -> Res.drawable.ic_snack_nuts
        "snack_crackers" -> Res.drawable.ic_snack_crackers
        "sweets" -> Res.drawable.ic_cat_sweets
        "sweets_chocolate" -> Res.drawable.ic_sweets_chocolate
        "sweets_candy" -> Res.drawable.ic_sweets_candy
        "sweets_bonbons" -> Res.drawable.ic_sweets_bonbons
        "sweets_cookies" -> Res.drawable.ic_sweets_cookies
        "sweets_pastry" -> Res.drawable.ic_sweets_pastry
        "fruits" -> Res.drawable.ic_cat_fruits
        "fruit_tomato" -> Res.drawable.ic_fruit_tomato
        "fruit_apple" -> Res.drawable.ic_fruit_apple
        "fruit_pear" -> Res.drawable.ic_fruit_pear
        "fruit_banana" -> Res.drawable.ic_fruit_banana
        "fruit_orange" -> Res.drawable.ic_fruit_orange
        "fruit_mandarin" -> Res.drawable.ic_fruit_mandarin
        "fruit_lemon" -> Res.drawable.ic_fruit_lemon
        "fruit_lime" -> Res.drawable.ic_fruit_lime
        "fruit_grapefruit" -> Res.drawable.ic_fruit_grapefruit
        "fruit_strawberry" -> Res.drawable.ic_fruit_strawberry
        "fruit_raspberry" -> Res.drawable.ic_fruit_raspberry
        "fruit_blackberry" -> Res.drawable.ic_fruit_blackberry
        "fruit_blueberry" -> Res.drawable.ic_fruit_blueberry
        "fruit_grape" -> Res.drawable.ic_fruit_grape
        "fruit_kiwi" -> Res.drawable.ic_fruit_kiwi
        "fruit_melon" -> Res.drawable.ic_fruit_melon
        "fruit_watermelon" -> Res.drawable.ic_fruit_watermelon
        "fruit_peach" -> Res.drawable.ic_fruit_peach
        "fruit_plum" -> Res.drawable.ic_fruit_plum
        "fruit_cherry" -> Res.drawable.ic_fruit_cherry
        "fruit_pomegranate" -> Res.drawable.ic_fruit_pomegranate
        "fruit_persimmon" -> Res.drawable.ic_fruit_persimmon
        "fruit_loquat" -> Res.drawable.ic_fruit_loquat
        "fruit_fig" -> Res.drawable.ic_fruit_fig
        "fruit_date" -> Res.drawable.ic_fruit_date
        "fruit_coconut" -> Res.drawable.ic_fruit_coconut
        "fruit_cherimoya" -> Res.drawable.ic_fruit_cherimoya
        "fruit_mango" -> Res.drawable.ic_fruit_mango
        "fruit_papaya" -> Res.drawable.ic_fruit_papaya
        "fruit_pineapple" -> Res.drawable.ic_fruit_pineapple
        "fruit_avocado" -> Res.drawable.ic_fruit_avocado
        "vegetables" -> Res.drawable.ic_cat_vegetables
        "vegetable_potato" -> Res.drawable.ic_vegetable_potato
        "vegetable_onion" -> Res.drawable.ic_vegetable_onion
        "vegetable_carrot" -> Res.drawable.ic_vegetable_carrot
        "vegetable_pepper" -> Res.drawable.ic_vegetable_pepper
        "vegetable_zucchini" -> Res.drawable.ic_vegetable_zucchini
        "vegetable_cucumber" -> Res.drawable.ic_vegetable_cucumber
        "vegetable_lettuce" -> Res.drawable.ic_vegetable_lettuce
        "vegetable_spinach" -> Res.drawable.ic_vegetable_spinach
        "vegetable_garlic" -> Res.drawable.ic_vegetable_garlic
        "vegetable_peas" -> Res.drawable.ic_vegetable_peas
        "vegetable_green_beans" -> Res.drawable.ic_vegetable_green_beans
        "vegetable_eggplant" -> Res.drawable.ic_vegetable_eggplant
        "vegetable_pumpkin" -> Res.drawable.ic_vegetable_pumpkin
        "vegetable_artichoke" -> Res.drawable.ic_vegetable_artichoke
        "vegetable_leek" -> Res.drawable.ic_vegetable_leek
        "vegetable_cauliflower" -> Res.drawable.ic_vegetable_cauliflower
        else -> Res.drawable.ic_cat_other
    }
}

private fun stepAction(step: String): String {
    return step
        .substringBefore(".")
        .trim()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        .ifBlank { "Sigue este paso" }
}

private fun stepGuidance(step: String): String {
    val explicitDetail = step.substringAfter(".", missingDelimiterValue = "")
        .trim()
        .removeSuffix(".")
    if (explicitDetail.isNotBlank()) {
        return explicitDetail
    }

    val lower = step.lowercase()
    return when {
        "seca" in lower || "escurr" in lower -> "Quita humedad antes de cocinar para que dore mejor y no suelte agua."
        "huevo" in lower && ("bate" in lower || "batido" in lower) -> "Bate el huevo justo antes de usarlo y cubre bien cada pieza."
        "sartén" in lower || "sarten" in lower || "cocina" in lower || "saltea" in lower -> "Usa fuego medio y deja que coja color antes de moverlo demasiado."
        "horno" in lower || "hornea" in lower -> "Precalienta y revisa a mitad de tiempo para que no se seque."
        "cuece" in lower || "hierve" in lower || "guis" in lower -> "Mantén un hervor suave y prueba el punto antes de apagar."
        "sirve" in lower || "limón" in lower || "limon" in lower -> "Termina con sal, aceite o limón al gusto justo antes de servir."
        "mezcla" in lower || "remueve" in lower -> "Mezcla con suavidad para integrar sin romper los ingredientes."
        else -> "Hazlo sin prisa y comprueba textura y punto de sal antes de avanzar."
    }
}

private fun String.prettyName(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

private fun String.displayIngredientName(amount: String?): String {
    val resolvedAmount = amount ?: estimatedIngredientAmount()
    return if (resolvedAmount == null) {
        prettyName()
    } else {
        "${prettyName()} · $resolvedAmount"
    }
}

private fun String.estimatedIngredientAmount(): String? {
    val key = toIngredientKey()
    return when {
        "merluza" in key || "bacalao" in key || "lubina" in key || "atun" in key ||
            "sardina" in key || "trucha" in key || "salmon" in key -> "250 g"
        "gamba" in key || "calamar" in key || "mejillon" in key || "almeja" in key -> "100 g"
        "pollo" in key || "ternera" in key || "cerdo" in key || "conejo" in key || "costilla" in key -> "250 g"
        "huevo" in key -> "1 ud"
        "patata" in key -> "2 uds"
        "cebolla" in key -> "1/2 ud"
        "ajo" in key -> "1 diente"
        "zanahoria" in key -> "1 ud"
        "pimiento" in key -> "1/2 ud"
        "tomate" in key -> "1 ud"
        "fideo" in key || "pasta" in key || "macarron" in key || "arroz" in key -> "60 g"
        "lenteja" in key || "garbanzo" in key || "alubia" in key -> "120 g"
        "harina" in key -> "2 cdas"
        "vino" in key -> "60 ml"
        "aceite" in key -> "1 cda"
        "perejil" in key || "sal" in key || "pimienta" in key || "pimenton" in key -> "al gusto"
        "limon" in key -> "1/2 ud"
        else -> null
    }
}

private fun String.toIngredientKey(): String {
    return lowercase()
        .map { char ->
            when (char) {
                'á', 'à', 'ä', 'â' -> 'a'
                'é', 'è', 'ë', 'ê' -> 'e'
                'í', 'ì', 'ï', 'î' -> 'i'
                'ó', 'ò', 'ö', 'ô' -> 'o'
                'ú', 'ù', 'ü', 'û' -> 'u'
                'ñ' -> 'n'
                else -> char
            }
        }
        .joinToString("")
        .replace(Regex("[^a-z0-9 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
