package es.neverachefai.core.preferences

import android.content.Context
import android.content.SharedPreferences

private const val PREFS_NAME = "neverachef_prefs"
private const val KEY_ONBOARDING_SEEN = "onboarding_seen"

private var prefs: SharedPreferences? = null

fun initializeAppPreferences(context: Context) {
    prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}

actual object AppPreferences {
    actual fun isOnboardingSeen(): Boolean {
        return prefs?.getBoolean(KEY_ONBOARDING_SEEN, false) ?: false
    }

    actual fun setOnboardingSeen(seen: Boolean) {
        prefs?.edit()?.putBoolean(KEY_ONBOARDING_SEEN, seen)?.apply()
    }

    actual fun getString(key: String): String? {
        return prefs?.getString(key, null)
    }

    actual fun setString(key: String, value: String?) {
        prefs?.edit()?.apply {
            if (value == null) {
                remove(key)
            } else {
                putString(key, value)
            }
        }?.apply()
    }

    actual fun clearAll() {
        prefs?.edit()?.clear()?.apply()
    }
}
