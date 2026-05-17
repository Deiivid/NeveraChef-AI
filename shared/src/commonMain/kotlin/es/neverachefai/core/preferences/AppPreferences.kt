package es.neverachefai.core.preferences

expect object AppPreferences {
    fun isOnboardingSeen(): Boolean
    fun setOnboardingSeen(seen: Boolean)
    fun clearAll()
}
