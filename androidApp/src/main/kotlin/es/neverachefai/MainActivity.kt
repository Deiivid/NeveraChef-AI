package es.neverachefai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import es.neverachefai.core.preferences.initializeAppPreferences

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initializeAppPreferences(applicationContext)

        setContent {
            var cameraGranted by remember { mutableStateOf(isPermissionGranted(Manifest.permission.CAMERA)) }
            var microphoneGranted by remember { mutableStateOf(isPermissionGranted(Manifest.permission.RECORD_AUDIO)) }

            val cameraPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { granted ->
                cameraGranted = granted
            }
            val microphonePermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
            ) { granted ->
                microphoneGranted = granted
            }

            App(
                cameraPermissionGranted = cameraGranted,
                microphonePermissionGranted = microphoneGranted,
                onRequestCameraPermission = {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onRequestMicrophonePermission = {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
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
