package es.neverachefai.core.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun NeveraChefBannerAd(modifier: Modifier = Modifier)

expect fun showNeveraChefRewardedAd(onRewardEarned: () -> Unit)
