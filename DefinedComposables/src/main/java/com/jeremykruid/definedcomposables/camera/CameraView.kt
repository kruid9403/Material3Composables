package com.jeremykruid.definedcomposables.camera

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.jeremykruid.definedcomposables.R.drawable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@Composable
fun CameraView(
    context: Context,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit,
    facingFront: Boolean = true
) {

    var lensFacing by remember { mutableStateOf(
        if (facingFront){
            CameraSelector.LENS_FACING_FRONT
        }else{
            CameraSelector.LENS_FACING_BACK
        }) }
    val imageCapture: ImageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) onImageCaptured(uri, true)
    }

    CameraPreviewView(
        context,
        imageCapture,
        lensFacing
    ) { cameraUIAction ->
        when (cameraUIAction) {
            is CameraUIAction.OnCameraClick -> {
                imageCapture.takePicture(context, lensFacing, onImageCaptured, onError)
            }
            is CameraUIAction.OnSwitchCameraClick -> {
                when(lensFacing){
                    CameraSelector.LENS_FACING_FRONT -> lensFacing = CameraSelector.LENS_FACING_BACK
                    CameraSelector.LENS_FACING_BACK -> lensFacing = CameraSelector.LENS_FACING_FRONT
                }
            }
            is CameraUIAction.OnGalleryViewClick -> {
                if (true == context.getOutputDirectory().listFiles()?.isNotEmpty()) {
                    galleryLauncher.launch("image/*")
                }
            }
        }
    }
}


@SuppressLint("RestrictedApi")
@Composable
fun CameraPreviewView(
    context: Context,
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_FRONT,
    cameraUIAction: (CameraUIAction) -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember {
        PreviewView(context)
            .apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
    }

    val preview = androidx.camera.core.Preview
        .Builder()
        .build()
        .also{
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()


    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize()) {

        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ) {
            CameraControls(cameraUIAction)
        }

    }
}
suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}

@Composable
fun CameraControls(cameraUIAction: (CameraUIAction) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        CameraControl(
            Icons.Sharp.CheckCircle,
            "Content Description",
            modifier= Modifier
                .size(64.dp)
                .padding(1.dp)
                .border(1.dp, Color.White, CircleShape),
            onClick = { cameraUIAction(CameraUIAction.OnCameraClick) },
            changeCamera = {cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
        )
    }
}


@Composable
fun CameraControl(
    imageVector: ImageVector,
    contentDescId: String,
    modifier: Modifier = Modifier,
    changeCamera: () -> Unit,
    onClick: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {

        val (photo, switch) = createRefs()

        Box(modifier = modifier
            .constrainAs(photo) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            },) {
            IconButton(
                onClick = onClick,
                modifier = modifier
            ) {
                Icon(
                    imageVector,
                    contentDescription = contentDescId,
                    modifier = modifier,
                    tint = Color.White
                )
            }
        }

        Image(
            painter = painterResource(id = drawable.rotate),
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .size(24.dp, 24.dp)
                .constrainAs(switch){
                    start.linkTo(photo.end)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    changeCamera.invoke()
                }
        )

//        FaIcon(faIcon = FaIcons.Sync,
//            tint = Color.White,
//            size = 36.dp,
//            modifier = Modifier
//                .constrainAs(switch){
//                    start.linkTo(photo.end)
//                    end.linkTo(parent.end)
//                    top.linkTo(parent.top)
//                    bottom.linkTo(parent.bottom)
//                }
//                .clickable {
//                    changeCamera.invoke()
//                }
//        )
    }

}

sealed class CameraUIAction {
    object OnCameraClick : CameraUIAction()
    object OnGalleryViewClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
}


private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
private const val PHOTO_EXTENSION = ".jpg"


fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputDirectory = context.getOutputDirectory()
    // Create output file to hold the image
    val photoFile = createFile(outputDirectory, FILENAME, PHOTO_EXTENSION)
    val outputFileOptions = getOutputFileOptions(lensFacing, photoFile)

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Log.e("PhotoScreen", output.savedUri.toString())
                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                // If the folder selected is an external media directory, this is
                // unnecessary but otherwise other apps will not be able to access our
                // images unless we scan them using [MediaScannerConnection]
                val mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(savedUri.toFile().extension)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(savedUri.toFile().absolutePath),
                    arrayOf(mimeType)
                ) { _, uri ->

                    Log.e("PhotoScreen", uri.path.toString())
                }
                onImageCaptured(savedUri, false)
            }
            override fun onError(exception: ImageCaptureException) {
                Log.e("PhotoScreen", exception.localizedMessage!!)
                onError(exception)
            }
        })
}

fun Context.getOutputDirectory(): File {
    val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
        File(it, "Camera").apply { mkdirs() }
    }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else this.filesDir
}

fun getOutputFileOptions(
    lensFacing: Int,
    photoFile: File
): ImageCapture.OutputFileOptions {

    // Setup image capture metadata
    val metadata = ImageCapture.Metadata().apply {
        // Mirror image when using the front camera
        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
    }
    // Create output options object which contains file + metadata
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
        .setMetadata(metadata)
        .build()

    return outputOptions
}

fun createFile(baseFolder: File, format: String, extension: String) =
    File(
        baseFolder, SimpleDateFormat(format, Locale.US)
            .format(System.currentTimeMillis()) + extension
    )

