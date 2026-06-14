package es.neverachefai.feature.recipes.data.ai

fun configureIosLocalLlmEngine(
    modelName: String?,
    isReady: Boolean,
    generate: (String) -> String?,
) {
    registeredIosLocalLlmEngine = CallbackLocalLlmEngine(
        modelName = modelName,
        ready = isReady,
        generateCallback = generate,
    )
}

fun clearIosLocalLlmEngine() {
    registeredIosLocalLlmEngine = null
}

private class CallbackLocalLlmEngine(
    override val modelName: String?,
    private val ready: Boolean,
    private val generateCallback: (String) -> String?,
) : LocalLlmEngine {
    override val isReady: Boolean
        get() = ready

    override fun generate(prompt: String): String? {
        return if (ready) generateCallback(prompt) else null
    }
}
