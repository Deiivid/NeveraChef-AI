package es.neverachefai.core.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun NeveraChefBannerAd(modifier: Modifier) = Unit

actual fun showNeveraChefRewardedAd(onRewardEarned: () -> Unit) {
    onRewardEarned()
}
