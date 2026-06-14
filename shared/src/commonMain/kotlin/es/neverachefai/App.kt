package es.neverachefai

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import es.neverachefai.app.NeveraChefApp
import es.neverachefai.core.designsystem.NeveraChefTheme

@Composable
@Preview
fun App(
    microphonePermissionGranted: Boolean = false,
    onRequestMicrophonePermission: () -> Unit = {},
    onRequestSpeechToText: (String, (String) -> Unit) -> Unit = { _, _ -> },
    onExitApp: () -> Unit = {},
) {
    NeveraChefTheme {
        NeveraChefApp(
            microphonePermissionGranted = microphonePermissionGranted,
            onRequestMicrophonePermission = onRequestMicrophonePermission,
            onRequestSpeechToText = onRequestSpeechToText,
            onExitApp = onExitApp,
        )
    }
}
