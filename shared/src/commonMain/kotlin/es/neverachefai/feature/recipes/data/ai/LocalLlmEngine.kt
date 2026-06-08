package es.neverachefai.feature.recipes.data.ai

interface LocalLlmEngine {
    val modelName: String?
    val isReady: Boolean
    fun generate(prompt: String): String?
}

object DisabledLocalLlmEngine : LocalLlmEngine {
    override val modelName: String? = null
    override val isReady: Boolean = false
    override fun generate(prompt: String): String? = null
}

expect fun createPlatformLocalLlmEngine(): LocalLlmEngine
