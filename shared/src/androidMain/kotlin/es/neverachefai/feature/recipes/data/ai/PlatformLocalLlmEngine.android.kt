package es.neverachefai.feature.recipes.data.ai

import android.content.Context
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.LogSeverity
import java.io.File

private const val MODEL_NAME = "Gemma 3 270M IT"
private const val MODEL_FILE_NAME = "gemma-3-270m-it.litertlm"
private const val MODEL_ASSET_PATH = "ai/$MODEL_FILE_NAME"

private var appContext: Context? = null

fun initializeLocalRecipeLlm(context: Context) {
    appContext = context.applicationContext
}

actual fun createPlatformLocalLlmEngine(): LocalLlmEngine {
    val context = appContext ?: return DisabledLocalLlmEngine
    return AndroidLiteRtLocalLlmEngine(context)
}

private class AndroidLiteRtLocalLlmEngine(
    private val context: Context,
) : LocalLlmEngine {
    private var cachedEngine: Engine? = null
    private var cachedModelPath: String? = null

    override val modelName: String?
        get() = if (isReady) MODEL_NAME else null

    override val isReady: Boolean
        get() = modelFile(context) != null

    override fun generate(prompt: String): String? {
        val model = modelFile(context) ?: return null
        return runCatching {
            Engine.setNativeMinLogSeverity(LogSeverity.ERROR)
            val conversation = engineFor(model).createConversation()
            conversation.use { it.sendMessage(prompt).toString().takeIf(String::isNotBlank) }
        }.getOrNull()
    }

    private fun engineFor(model: File): Engine {
        val modelPath = model.absolutePath
        val current = cachedEngine
        if (current != null && cachedModelPath == modelPath) return current

        cachedEngine?.close()
        val cacheDir = File(context.cacheDir, "litertlm").apply { mkdirs() }
        return Engine(
            EngineConfig(
                modelPath = modelPath,
                backend = Backend.CPU(),
                cacheDir = cacheDir.absolutePath,
            ),
        ).also { engine ->
            engine.initialize()
            cachedEngine = engine
            cachedModelPath = modelPath
        }
    }
}

private fun modelFile(context: Context): File? {
    val modelDir = File(context.filesDir, "ai_models").apply { mkdirs() }
    val privateModel = File(modelDir, MODEL_FILE_NAME)
    if (privateModel.exists()) return privateModel

    copyBundledModelIfPresent(context, privateModel)
    return privateModel.takeIf { it.exists() }
}

private fun copyBundledModelIfPresent(
    context: Context,
    destination: File,
) {
    runCatching {
        context.assets.open(MODEL_ASSET_PATH).use { input ->
            destination.outputStream().use { output -> input.copyTo(output) }
        }
    }.onFailure {
        destination.delete()
    }
}
