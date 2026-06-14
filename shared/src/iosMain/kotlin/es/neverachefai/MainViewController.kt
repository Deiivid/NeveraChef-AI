package es.neverachefai

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController(
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit = { _, _ -> },
) = ComposeUIViewController {
    App(
        microphonePermissionGranted = true,
        onRequestSpeechToText = onRequestSpeechToText,
    )
}
