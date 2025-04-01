package com.example.md_android_cas_paul.ui.photo

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

@Composable
fun PhotoScreen() {
    val context = LocalContext.current

    val cameraPermissionState = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED)
    }

    val capturedImage = remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bitmap ->
                capturedImage.value = bitmap
            })

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
            })

    if (!cameraPermissionState.value) {
        SideEffect { requestPermissionLauncher.launch(Manifest.permission.CAMERA) }
    }

    Column(
        modifier =
            Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (cameraPermissionState.value) {
                    cameraLauncher.launch(null)
                } else {
                    Toast.makeText(context, "Camera permission not granted", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.7f)) {
            Text("Take Picture")
        }

        Spacer(modifier = Modifier.height(16.dp))

        capturedImage.value?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.height(200.dp).width(200.dp))
        }
        if (capturedImage.value != null) {
            Button(
                onClick = {
                    capturedImage.value?.let { bitmap -> saveImageToGallery(context, bitmap) }
                },
                modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("Save Picture")
            }
            Button(
                onClick = {
                    capturedImage.value?.let { bitmap -> shareImage(context, bitmap) }
                },
                modifier = Modifier.fillMaxWidth(0.7f)) {
                Text("Share Picture")
            }
        }
    }
}

fun saveImageToGallery(context: Context, bitmap: Bitmap) {
    val filename = "IMG_${UUID.randomUUID()}.jpg"
    val mimeType = "image/jpeg"

    val values =
        ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

    val collection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    val imageUri = context.contentResolver.insert(collection, values)

    imageUri?.let { uri ->
        try {
            val outputStream: OutputStream? = context.contentResolver.openOutputStream(uri)
            outputStream?.use {
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        context.contentResolver.update(uri, values, null, null)
                    }
                    Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
        ?: run { Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show() }
}

fun shareImage(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "shared_image.png")
        val fileOutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, "Look at my booooook!!!")
        intent.setType("image/png")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        context.startActivity(Intent.createChooser(intent, "Share Image"))
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to share image", Toast.LENGTH_SHORT).show()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        Toast.makeText(context, "FileProvider configuration error", Toast.LENGTH_SHORT).show()
    }
}
