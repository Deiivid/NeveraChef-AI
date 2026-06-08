package es.neverachefai

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import es.neverachefai.app.NeveraChefApp
import es.neverachefai.core.designsystem.NeveraChefTheme

@Composable
@Preview
fun App(
    cameraPermissionGranted: Boolean = false,
    microphonePermissionGranted: Boolean = false,
    onRequestCameraPermission: () -> Unit = {},
    onRequestMicrophonePermission: () -> Unit = {},
    onRequestSpeechToText: ((String) -> Unit) -> Unit = {},
    onExitApp: () -> Unit = {},
) {
    NeveraChefTheme {
        NeveraChefApp(
            cameraPermissionGranted = cameraPermissionGranted,
            microphonePermissionGranted = microphonePermissionGranted,
            onRequestCameraPermission = onRequestCameraPermission,
            onRequestMicrophonePermission = onRequestMicrophonePermission,
            onRequestSpeechToText = onRequestSpeechToText,
            onExitApp = onExitApp,
        )
    }
}
