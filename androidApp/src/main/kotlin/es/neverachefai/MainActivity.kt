package es.neverachefai

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import es.neverachefai.core.ads.initializeNeveraChefAds
import es.neverachefai.core.preferences.initializeAppPreferences
import es.neverachefai.feature.recipes.data.ai.initializeLocalRecipeLlm
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initializeAppPreferences(applicationContext)
        initializeLocalRecipeLlm(applicationContext)
        initializeNeveraChefAds(this)

        setContent {
            var microphoneGranted by remember { mutableStateOf(isPermissionGranted(Manifest.permission.RECORD_AUDIO)) }

            val microphonePermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { granted ->
                microphoneGranted = granted
            }
            var onSpeechResult by remember { mutableStateOf<((String) -> Unit)?>(null) }
            val speechLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                val speechResult = onSpeechResult
                onSpeechResult = null
                if (result.resultCode == Activity.RESULT_OK) {
                    val spokenText = result.data
                        ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        ?.firstOrNull()
                        .orEmpty()
                    speechResult?.invoke(spokenText)
                }
            }

            App(
                microphonePermissionGranted = microphoneGranted,
                onRequestMicrophonePermission = {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
                onRequestSpeechToText = { prompt, onResult ->
                    onSpeechResult = onResult
                    val speechLocale = Locale.forLanguageTag("es-ES")
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(
                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                        )
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLocale.toLanguageTag())
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, speechLocale.toLanguageTag())
                        putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
                        putExtra(RecognizerIntent.EXTRA_PROMPT, prompt)
                        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5_000L)
                        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1_800L)
                        putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1_200L)
                    }
                    speechLauncher.launch(intent)
                },
                onExitApp = { finish() },
            )
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
