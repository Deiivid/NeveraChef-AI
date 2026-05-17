package es.neverachefai.core.preferences

import platform.Foundation.NSUserDefaults

private const val KEY_ONBOARDING_SEEN = "onboarding_seen"

actual object AppPreferences {
    private val defaults: NSUserDefaults = NSUserDefaults.standardUserDefaults

    actual fun isOnboardingSeen(): Boolean {
        return defaults.boolForKey(KEY_ONBOARDING_SEEN)
    }

    actual fun setOnboardingSeen(seen: Boolean) {
        defaults.setBool(seen, KEY_ONBOARDING_SEEN)
    }

    actual fun clearAll() {
        defaults.removeObjectForKey(KEY_ONBOARDING_SEEN)
    }
}
