package es.neverachefai.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.neverachefai.core.designsystem.NeveraChefColors
import es.neverachefai.feature.navigation.MainTab
import neverachefai.shared.generated.resources.Res
import neverachefai.shared.generated.resources.ic_nc_chef_hat
import neverachefai.shared.generated.resources.ic_nc_fridge
import neverachefai.shared.generated.resources.ic_nc_settings
import neverachefai.shared.generated.resources.ic_nc_shopping_basket
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun NeveraTopBar(
    title: String,
    actionLabel: String = "Reset",
    onActionClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        TextButton(onClick = onActionClick) { Text(actionLabel) }
    }
}

@Composable
fun NeveraBottomNavigation(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 18.dp, top = 10.dp, end = 18.dp, bottom = 8.dp)
                .height(76.dp)
                .background(Color.White, RoundedCornerShape(38.dp))
                .border(1.dp, Color(0xFFE5E7E4), RoundedCornerShape(38.dp))
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MainTab.entries.forEach { tab ->
                BottomTab(
                    tab = tab,
                    selected = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BottomTab(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(28.dp),
        color = if (selected) Color(0xFFE8F2E6) else Color.White,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(tab.iconRes()),
                contentDescription = tab.label,
                tint = if (selected) Color(0xFF0E5B45) else NeveraChefColors.Muted,
                modifier = Modifier.size(26.dp),
            )
            Text(
                text = tab.label,
                color = if (selected) Color(0xFF0E5B45) else NeveraChefColors.Muted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private fun MainTab.iconRes(): DrawableResource {
    return when (this) {
        MainTab.PANTRY -> Res.drawable.ic_nc_fridge
        MainTab.RECIPES -> Res.drawable.ic_nc_chef_hat
        MainTab.SHOPPING -> Res.drawable.ic_nc_shopping_basket
        MainTab.SETTINGS -> Res.drawable.ic_nc_settings
    }
}

@Composable
fun NeveraMainScaffold(
    selectedTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    showBottomBar: Boolean = true,
    contentHorizontalPadding: androidx.compose.ui.unit.Dp = 12.dp,
    contentVerticalPadding: androidx.compose.ui.unit.Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = if (showBottomBar) {
            {
                NeveraBottomNavigation(
                    selectedTab = selectedTab,
                    onTabSelected = onTabSelected
                )
            }
        } else {
            {}
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = contentHorizontalPadding, vertical = contentVerticalPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 640.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                content()
            }
        }
    }
}

@Composable
fun ScreenCard(
    title: String,
    description: String,
    backgroundColor: androidx.compose.ui.graphics.Color = NeveraChefColors.Soft,
    actions: @Composable (() -> Unit)? = null,
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = backgroundColor),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                description,
                color = NeveraChefColors.Muted,
                style = MaterialTheme.typography.bodyMedium
            )
            actions?.invoke()
        }
    }
}
