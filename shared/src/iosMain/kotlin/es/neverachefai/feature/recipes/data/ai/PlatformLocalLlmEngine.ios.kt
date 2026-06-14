package es.neverachefai.feature.recipes.data.ai

internal var registeredIosLocalLlmEngine: LocalLlmEngine? = null

actual fun createPlatformLocalLlmEngine(): LocalLlmEngine {
    return registeredIosLocalLlmEngine ?: DisabledLocalLlmEngine
}
