package es.neverachefai.core.ads

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.lang.ref.WeakReference

private const val BANNER_AD_UNIT_ID = "ca-app-pub-4774421174777892/3998083199"
private const val REWARDED_AD_UNIT_ID = "ca-app-pub-4774421174777892/1483600496"

fun initializeNeveraChefAds(activity: Activity) {
    NeveraChefAdMob.initialize(activity)
}

@Composable
actual fun NeveraChefBannerAd(modifier: Modifier) {
    val adView = remember { mutableListOf<AdView>() }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = BANNER_AD_UNIT_ID
                loadAd(AdRequest.Builder().build())
                adView += this
            }
        },
    )

    DisposableEffect(Unit) {
        onDispose {
            adView.forEach { it.destroy() }
            adView.clear()
        }
    }
}

actual fun showNeveraChefRewardedAd(onRewardEarned: () -> Unit) {
    NeveraChefAdMob.showRewarded(onRewardEarned)
}

private object NeveraChefAdMob {
    private var activityRef: WeakReference<Activity>? = null
    private var rewardedAd: RewardedAd? = null
    private var loadingRewarded = false
    private var pendingReward: (() -> Unit)? = null

    fun initialize(activity: Activity) {
        activityRef = WeakReference(activity)
        MobileAds.initialize(activity.applicationContext)
        loadRewarded(activity)
    }

    fun showRewarded(onRewardEarned: () -> Unit) {
        val activity = activityRef?.get() ?: return
        val ad = rewardedAd
        if (ad != null) {
            show(activity, ad, onRewardEarned)
        } else {
            pendingReward = onRewardEarned
            loadRewarded(activity)
        }
    }

    private fun loadRewarded(activity: Activity) {
        if (loadingRewarded || rewardedAd != null) return
        loadingRewarded = true
        RewardedAd.load(
            activity.applicationContext,
            REWARDED_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    loadingRewarded = false
                    rewardedAd = ad
                    val reward = pendingReward
                    if (reward != null) {
                        pendingReward = null
                        show(activity, ad, reward)
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    loadingRewarded = false
                    rewardedAd = null
                    pendingReward = null
                }
            },
        )
    }

    private fun show(
        activity: Activity,
        ad: RewardedAd,
        onRewardEarned: () -> Unit,
    ) {
        rewardedAd = null
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                loadRewarded(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                loadRewarded(activity)
            }
        }
        ad.show(activity) {
            onRewardEarned()
        }
    }
}
