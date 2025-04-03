package com.example.md_android_cas_paul.ui.photo

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PhotoScreen() {
    val context = LocalContext.current
    val viewModel: PhotoViewModel = viewModel()

    val cameraPermissionState = remember {
        mutableStateOf(viewModel.checkCameraPermission(context))
    }

    val capturedImage = remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bitmap ->
                capturedImage.value = bitmap
            }
        )

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    cameraPermissionState.value = true
                    cameraLauncher.launch(null)
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        )

    if (!cameraPermissionState.value) {
        SideEffect { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (cameraPermissionState.value) {
                    cameraLauncher.launch(null)
                } else {
                    Toast
                        .makeText(
                            context,
                            "Camera permission not granted",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        capturedImage.value?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.height(200.dp).width(200.dp)
            )
        }
        if (capturedImage.value != null) {
            Button(
                onClick = {
                    capturedImage.value?.let { bitmap ->
                        viewModel.saveImageToGallery(context, bitmap)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Save Picture")
            }
            Button(
                onClick = {
                    capturedImage.value?.let { bitmap -> viewModel.shareImage(context, bitmap) }
                },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Share Picture")
            }
        }
    }
}
