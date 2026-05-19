package es.neverachefai.core.preferences

expect object AppPreferences {
    fun isOnboardingSeen(): Boolean
    fun setOnboardingSeen(seen: Boolean)
    fun getString(key: String): String?
    fun setString(key: String, value: String?)
    fun clearAll()
}
