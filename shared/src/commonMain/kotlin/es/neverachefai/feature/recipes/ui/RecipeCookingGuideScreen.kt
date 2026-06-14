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
import neverachefai.shared.generated.resources.ic_nc_check_circle
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_clock
import neverachefai.shared.generated.resources.ic_nc_difficulty
import neverachefai.shared.generated.resources.ic_nc_leaf
import neverachefai.shared.generated.resources.recipe_family_pescado
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

private val GuideInk = Color(0xFF063D29)
private val GuideGreen = Color(0xFF008557)
private val GuideMint = Color(0xFFEAF3ED)
private val GuideCream = Color(0xFFFFFAF3)
private val GuideLine = Color(0xFFE8DDCA)
private val GuideMuted = Color(0xFF596273)
private val GuideSoft = Color(0xFFF4FBF6)
private val GuideGold = Color(0xFFD9C6A4)

@Composable
fun RecipeCookingGuideScreen(
    recipe: Recipe?,
    onBack: () -> Unit,
) {
    var stepIndex by remember(recipe?.id) { mutableStateOf(0) }
    val steps = recipe?.steps.orEmpty().ifEmpty {
        listOf(
            RecipeStep(
                order = 1,
                title = "Selecciona una receta",
                instruction = "Vuelve a seleccionar una receta para empezar.",
            ),
        )
    }
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
            ProgressCard(
                current = safeIndex + 1,
                total = steps.size,
                progress = progress,
                action = guideStepAction(steps[safeIndex]),
            )
            CurrentStepCard(index = safeIndex, step = steps[safeIndex])
            IngredientsStrip(items = recipe?.visibleIngredients().orEmpty())
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
            .padding(top = 12.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(
            onClick = onBack,
            shape = RoundedCornerShape(20.dp),
            color = GuideMint,
            modifier = Modifier.size(52.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_arrow_back),
                    contentDescription = "Volver",
                    tint = GuideGreen,
                    modifier = Modifier.size(25.dp),
                )
            }
        }
        Text("Cocina guiada", color = GuideInk, fontSize = 32.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GuideHero(recipe: Recipe?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(28.dp))
            .background(GuideCream, RoundedCornerShape(28.dp))
            .border(1.dp, GuideLine, RoundedCornerShape(28.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(width = 172.dp, height = 178.dp)
                .background(GuideMint, RoundedCornerShape(28.dp))
                .border(1.dp, Color(0xFFD8ECD9), RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(recipe?.let { guideRecipeImage(it) } ?: Res.drawable.recipe_family_pescado),
                contentDescription = null,
                modifier = Modifier.size(width = 150.dp, height = 124.dp),
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(13.dp),
        ) {
            Text(
                text = recipe?.title ?: "Receta",
                color = GuideInk,
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
                InfoPill("${recipe?.estimatedMinutes ?: 15} min", icon = Res.drawable.ic_nc_clock)
                InfoPill(recipe?.difficulty?.label ?: "Fácil", icon = Res.drawable.ic_nc_difficulty)
            }
        }
    }
}

@Composable
private fun ProgressCard(
    current: Int,
    total: Int,
    progress: Float,
    action: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideSoft, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFD8ECD9), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, GuideGold, RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(47.dp)
                    .background(GuideInk, RoundedCornerShape(19.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_chef_hat),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp),
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Paso $current de $total", color = GuideInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                text = action,
                color = GuideMuted,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
    step: RecipeStep,
) {
    val detail = guideStepDetail(step)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(28.dp))
            .background(Color.White, RoundedCornerShape(28.dp))
            .border(1.dp, GuideLine, RoundedCornerShape(28.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .background(GuideMint, RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0xFFD8ECD9), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text("${index + 1}", color = GuideInk, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                Text(
                    text = "Hazlo ahora",
                    color = GuideGreen,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(GuideMint, RoundedCornerShape(15.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                )
                Text(
                    guideStepAction(step),
                    color = GuideInk,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 31.sp,
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GuideLine),
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            if (detail.isNotEmpty()) {
                Text(detail, color = GuideMuted, fontSize = 17.sp, lineHeight = 24.sp)
            }
            Text(
                text = guideStepGuidance(step),
                color = GuideInk,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GuideSoft, RoundedCornerShape(18.dp))
                    .padding(13.dp),
            )
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
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        SectionTitle(
            title = "Ingredientes",
            icon = Res.drawable.ic_nc_leaf,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items.take(3).forEach { item ->
                IngredientStatusCard(
                    item = item,
                    modifier = Modifier
                        .weight(1f),
                )
            }
        }
    }
}

@Composable
private fun StepTimeline(
    steps: List<RecipeStep>,
    current: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuideMint, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFD8ECD9), RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SectionTitle(
            title = "Plan rápido",
            icon = Res.drawable.ic_nc_clock,
        )
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
                    text = guideStepAction(step),
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
private fun InfoPill(
    text: String,
    icon: DrawableResource,
) {
    Row(
        modifier = Modifier
            .shadow(1.dp, RoundedCornerShape(18.dp))
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFDCEBDD), RoundedCornerShape(14.dp))
            .padding(horizontal = 11.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = GuideGreen,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = text,
            color = GuideInk,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    icon: DrawableResource,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = GuideGreen,
            modifier = Modifier.size(23.dp),
        )
        Text(title, color = GuideInk, fontSize = 23.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun IngredientStatusCard(
    item: String,
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .background(Color(0xFFFFFDF8), RoundedCornerShape(18.dp))
            .border(1.dp, GuideLine, RoundedCornerShape(18.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(43.dp)
                .background(GuideMint, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(ingredientIcon(item)),
                contentDescription = null,
                tint = GuideGreen,
                modifier = Modifier.size(25.dp),
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(
                text = item.prettyName(),
                color = GuideInk,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_nc_check_circle),
                    contentDescription = null,
                    tint = GuideGreen,
                    modifier = Modifier.size(13.dp),
                )
                Text("Listo", color = GuideGreen, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
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
    return recipeDrawableResource(recipe)
}

private fun Recipe.visibleIngredients(): List<String> {
    return (ingredientsUsed + missingIngredients + optionalIngredients).distinct()
}

private fun ingredientIcon(item: String): DrawableResource {
    return when (inferShoppingIconKey(item)) {
        "fish" -> Res.drawable.ic_cat_fish
        "fish_hake" -> Res.drawable.ic_fish_hake
        "fish_cod" -> Res.drawable.ic_fish_cod
        "fish_seabass" -> Res.drawable.ic_fish_seabass
        "fish_salmon" -> Res.drawable.ic_fish_salmon
        "fish_tuna" -> Res.drawable.ic_fish_tuna
        "fish_sardine" -> Res.drawable.ic_fish_sardine
        "fish_trout" -> Res.drawable.ic_fish_trout
        "eggs" -> Res.drawable.ic_cat_eggs
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
        "other" -> Res.drawable.ic_cat_other
        else -> Res.drawable.ic_nc_chef_hat
    }
}

private fun guideStepAction(step: RecipeStep): String {
    return step.title.ifBlank { "Sigue este paso" }
}

private fun guideStepDetail(step: RecipeStep): String {
    return step.instruction
}

private fun guideStepGuidance(step: RecipeStep): String {
    step.tip?.let { return it }
    val lower = step.instruction.lowercase()
    return when {
        "seca" in lower || "escurr" in lower -> "Clave: retira humedad para que el rebozado agarre mejor y el pescado no quede cocido."
        "huevo" in lower && ("bate" in lower || "batido" in lower) -> "Clave: cubre bien cada pieza y deja caer el exceso antes de pasar a la sartén."
        "sartén" in lower || "sarten" in lower || "cocina" in lower || "saltea" in lower -> "Clave: fuego medio, poco aceite y dale la vuelta cuando la base esté dorada."
        "horno" in lower || "hornea" in lower -> "Clave: precalienta y revisa a mitad de tiempo para ajustar el punto."
        "cuece" in lower || "hierve" in lower || "guis" in lower -> "Clave: hervor suave y prueba textura antes de apagar."
        "sirve" in lower || "limón" in lower || "limon" in lower -> "Clave: termina justo al final para mantener sabor fresco y buena textura."
        "mezcla" in lower || "remueve" in lower -> "Clave: integra sin aplastar; busca una textura uniforme."
        else -> "Clave: comprueba textura, punto de sal y temperatura antes de pasar al siguiente paso."
    }
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
