package com.plnnusadaya.bbmmonitoring

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.io.File


// =============================================================
// CAMERA CAPTURE SCREEN
// =============================================================

@Composable
fun CameraCaptureScreen(
    jenisDokumentasi: String,
    onFotoDiambil: (String) -> Unit,
    onBack: () -> Unit,
    lokasiPenerimaan: String = "PLTD Sewa Batulicin"
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // =========================================================
    // CAMERA PERMISSION
    // =========================================================

    var cameraGranted by remember {

        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // =========================================================
    // LOCATION PERMISSION
    // =========================================================

    var locationGranted by remember {

        mutableStateOf(

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||

                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // =========================================================
    // IMAGE CAPTURE
    // =========================================================

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    // =========================================================
    // STATUS
    // =========================================================

    var cameraStatus by remember {
        mutableStateOf("Menyiapkan kamera...")
    }

    var cameraError by remember {
        mutableStateOf<String?>(null)
    }

    var sedangMengambilFoto by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // CAMERA PERMISSION LAUNCHER
    // =========================================================

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            cameraGranted = granted

            if (granted) {

                cameraStatus = "Izin kamera diberikan"
                cameraError = null

            } else {

                cameraStatus = "Izin kamera ditolak"

                cameraError =
                    "Izin kamera diperlukan untuk mengambil dokumentasi."
            }
        }

    // =========================================================
    // LOCATION PERMISSION LAUNCHER
    // =========================================================

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            locationGranted =

                permissions[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||

                        permissions[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true
        }

    // =========================================================
    // REQUEST PERMISSION
    // =========================================================

    LaunchedEffect(Unit) {

        // Kamera
        if (!cameraGranted) {

            cameraPermissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }

        // Lokasi
        if (!locationGranted) {

            locationPermissionLauncher.launch(

                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // =========================================================
    // CLEANUP CAMERA
    // =========================================================

    DisposableEffect(lifecycleOwner) {

        onDispose {

            try {

                val providerFuture =
                    ProcessCameraProvider.getInstance(context)

                if (providerFuture.isDone) {

                    providerFuture
                        .get()
                        .unbindAll()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }

            imageCapture = null
        }
    }

    // =========================================================
    // CAMERA PERMISSION BELUM ADA
    // =========================================================

    if (!cameraGranted) {

        Box(

            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White),

            contentAlignment =
                Alignment.Center
        ) {

            Column(

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center,

                modifier =
                    Modifier.padding(24.dp)
            ) {

                Text(
                    text = "Akses Kamera Diperlukan",
                    style =
                        MaterialTheme
                            .typography
                            .titleLarge
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text =
                        "Aplikasi membutuhkan kamera untuk mengambil dokumentasi penerimaan BBM."
                )

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Button(

                    onClick = {

                        cameraPermissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }

                ) {

                    Text("Izinkan Kamera")
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Button(
                    onClick = onBack
                ) {

                    Text("Kembali")
                }
            }
        }

        return
    }

    // =========================================================
    // CAMERA SCREEN
    // =========================================================

    Box(
        modifier =
            Modifier.fillMaxSize()
    ) {

        // =====================================================
        // CAMERA PREVIEW
        // =====================================================

        AndroidView(

            factory = { ctx ->

                val previewView =
                    PreviewView(ctx).apply {

                        implementationMode =
                            PreviewView
                                .ImplementationMode
                                .COMPATIBLE

                        scaleType =
                            PreviewView
                                .ScaleType
                                .FILL_CENTER
                    }

                val cameraProviderFuture =
                    ProcessCameraProvider
                        .getInstance(ctx)

                cameraProviderFuture.addListener(

                    {

                        try {

                            // ---------------------------------
                            // CAMERA PROVIDER
                            // ---------------------------------

                            val cameraProvider =
                                cameraProviderFuture.get()

                            // ---------------------------------
                            // PREVIEW
                            // ---------------------------------

                            val preview =
                                Preview.Builder()
                                    .build()
                                    .also { previewUseCase ->

                                        previewUseCase
                                            .surfaceProvider =
                                            previewView
                                                .surfaceProvider
                                    }

                            // ---------------------------------
                            // IMAGE CAPTURE
                            // ---------------------------------

                            val capture =
                                ImageCapture.Builder()
                                    .setCaptureMode(
                                        ImageCapture
                                            .CAPTURE_MODE_MINIMIZE_LATENCY
                                    )
                                    .build()

                            imageCapture = capture

                            // ---------------------------------
                            // BACK CAMERA
                            // ---------------------------------

                            val cameraSelector =
                                CameraSelector
                                    .DEFAULT_BACK_CAMERA

                            // ---------------------------------
                            // RESET CAMERA
                            // ---------------------------------

                            cameraProvider.unbindAll()

                            // ---------------------------------
                            // START CAMERA
                            // ---------------------------------

                            cameraProvider.bindToLifecycle(

                                lifecycleOwner,

                                cameraSelector,

                                preview,

                                capture
                            )

                            cameraStatus =
                                "Kamera siap"

                            cameraError = null

                        } catch (e: Exception) {

                            e.printStackTrace()

                            imageCapture = null

                            cameraStatus =
                                "Kamera gagal dibuka"

                            cameraError =
                                e.message
                                    ?: "Terjadi kesalahan saat membuka kamera."
                        }

                    },

                    ContextCompat.getMainExecutor(ctx)
                )

                previewView
            },

            modifier =
                Modifier.fillMaxSize()
        )

        // =====================================================
        // TOMBOL KEMBALI
        // =====================================================

        Button(

            onClick = onBack,

            modifier =
                Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
        ) {

            Text("‹ Kembali")
        }

        // =====================================================
        // JENIS DOKUMENTASI
        // =====================================================

        Box(

            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp)
                    .background(
                        Color.Black.copy(
                            alpha = 0.65f
                        )
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    )
        ) {

            Text(

                text =
                    namaDokumentasi(
                        jenisDokumentasi
                    ),

                color =
                    Color.White
            )
        }

        // =====================================================
        // ERROR CAMERA
        // =====================================================

        if (cameraError != null) {

            Box(

                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .padding(20.dp)
                        .background(
                            Color.Black.copy(
                                alpha = 0.80f
                            )
                        )
                        .padding(20.dp)
            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(

                        text =
                            cameraStatus,

                        color =
                            Color.White,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(

                        text =
                            cameraError ?: "",

                        color =
                            Color.White
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Button(
                        onClick = onBack
                    ) {

                        Text("Kembali")
                    }
                }
            }
        }

        // =====================================================
        // TOMBOL AMBIL FOTO
        // =====================================================

        Button(

            enabled =
                imageCapture != null &&
                        !sedangMengambilFoto &&
                        cameraError == null,

            onClick = {

                if (imageCapture != null) {

                    sedangMengambilFoto = true

                    cameraStatus =
                        "Mengambil foto..."

                    ambilFoto(

                        context =
                            context,

                        imageCapture =
                            imageCapture,

                        jenisDokumentasi =
                            jenisDokumentasi,

                        lokasiPenerimaan =
                            lokasiPenerimaan,

                        locationGranted =
                            locationGranted,

                        onFotoDiambil =
                            { pathFoto ->

                                sedangMengambilFoto =
                                    false

                                cameraStatus =
                                    "Foto berhasil disimpan"

                                onFotoDiambil(
                                    pathFoto
                                )
                            },

                        onErrorCallback =
                            { pesanError ->

                                sedangMengambilFoto =
                                    false

                                cameraStatus =
                                    "Gagal mengambil foto"

                                cameraError =
                                    pesanError
                            }
                    )
                }
            },

            modifier =
                Modifier
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(
                        bottom = 40.dp
                    )
        ) {

            Text(

                text =
                    if (sedangMengambilFoto) {
                        "Memproses Foto..."
                    } else {
                        "📷 Ambil Foto"
                    }
            )
        }

        // =====================================================
        // STATUS
        // =====================================================

        if (

            imageCapture == null &&
            cameraError == null

        ) {

            Text(

                text =
                    cameraStatus,

                color =
                    Color.White,

                modifier =
                    Modifier
                        .align(
                            Alignment.BottomCenter
                        )
                        .padding(
                            bottom = 100.dp
                        )
                        .background(
                            Color.Black.copy(
                                alpha = 0.60f
                            )
                        )
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
            )
        }
    }
}


// =============================================================
// AMBIL FOTO
// =============================================================

private fun ambilFoto(

    context: Context,

    imageCapture: ImageCapture?,

    jenisDokumentasi: String,

    lokasiPenerimaan: String,

    locationGranted: Boolean,

    onFotoDiambil: (String) -> Unit,

    onErrorCallback: (String) -> Unit
) {

    // =========================================================
    // CEK IMAGE CAPTURE
    // =========================================================

    if (imageCapture == null) {

        onErrorCallback(
            "CameraX belum siap. Silakan tunggu kamera siap."
        )

        return
    }

    // =========================================================
    // FILE FOTO SEMENTARA
    // =========================================================

    val fotoFile =
        File(

            context.cacheDir,

            "foto_${System.currentTimeMillis()}.jpg"
        )

    // =========================================================
    // OUTPUT
    // =========================================================

    val outputOptions =

        ImageCapture
            .OutputFileOptions
            .Builder(fotoFile)
            .build()

    // =========================================================
    // AMBIL FOTO
    // =========================================================

    imageCapture.takePicture(

        outputOptions,

        ContextCompat.getMainExecutor(context),

        object :
            ImageCapture.OnImageSavedCallback {

            // =================================================
            // BERHASIL MENGAMBIL FOTO
            // =================================================

            override fun onImageSaved(

                outputFileResults:
                ImageCapture.OutputFileResults

            ) {

                try {

                    // -----------------------------------------
                    // BACA FOTO
                    // -----------------------------------------

                    val originalBitmap =
                        BitmapFactory.decodeFile(
                            fotoFile.absolutePath
                        )

                    if (originalBitmap == null) {

                        onErrorCallback(
                            "Foto berhasil diambil tetapi tidak dapat dibaca."
                        )

                        return
                    }

                    // -----------------------------------------
                    // JIKA ADA IZIN LOKASI
                    // -----------------------------------------

                    if (locationGranted) {

                        LocationHelper.getCurrentLocation(

                            context = context

                        ) { location ->

                            prosesWatermarkDanSimpan(

                                context =
                                    context,

                                originalBitmap =
                                    originalBitmap,

                                location =
                                    location,

                                lokasiPenerimaan =
                                    lokasiPenerimaan,

                                jenisDokumentasi =
                                    jenisDokumentasi,

                                fotoFile =
                                    fotoFile,

                                onFotoDiambil =
                                    onFotoDiambil,

                                onErrorCallback =
                                    onErrorCallback
                            )
                        }

                    } else {

                        // -------------------------------------
                        // LOKASI TIDAK TERSEDIA
                        // -------------------------------------

                        prosesWatermarkDanSimpan(

                            context =
                                context,

                            originalBitmap =
                                originalBitmap,

                            location =
                                null,

                            lokasiPenerimaan =
                                lokasiPenerimaan,

                            jenisDokumentasi =
                                jenisDokumentasi,

                            fotoFile =
                                fotoFile,

                            onFotoDiambil =
                                onFotoDiambil,

                            onErrorCallback =
                                onErrorCallback
                        )
                    }

                } catch (e: Exception) {

                    e.printStackTrace()

                    onErrorCallback(

                        "Gagal memproses foto: " +
                                (
                                        e.message
                                            ?: "Kesalahan tidak diketahui."
                                        )
                    )
                }
            }

            // =================================================
            // GAGAL MENGAMBIL FOTO
            // =================================================

            override fun onError(

                exception:
                ImageCaptureException

            ) {

                exception.printStackTrace()

                onErrorCallback(

                    "Gagal mengambil foto: " +
                            (
                                    exception.message
                                        ?: exception
                                            .imageCaptureError
                                            .toString()
                                    )
                )
            }
        }
    )
}


// =============================================================
// WATERMARK + SIMPAN FOTO
// =============================================================

private fun prosesWatermarkDanSimpan(

    context: Context,

    originalBitmap:
    android.graphics.Bitmap,

    location:
    android.location.Location?,

    lokasiPenerimaan: String,

    jenisDokumentasi: String,

    fotoFile: File,

    onFotoDiambil: (String) -> Unit,

    onErrorCallback: (String) -> Unit
) {

    try {

        // =====================================================
        // BUAT WATERMARK
        // =====================================================

        val watermarkedBitmap =

            PhotoDocument.createWatermarkedBitmap(

                original =
                    originalBitmap,

                location =
                    location,

                lokasiPenerimaan =
                    lokasiPenerimaan,

                jenisDokumentasi =
                    namaDokumentasi(
                        jenisDokumentasi
                    )
            )

        // =====================================================
        // SIMPAN FOTO FINAL
        // =====================================================

        val finalFile =

            PhotoDocument.saveBitmap(

                context =
                    context,

                bitmap =
                    watermarkedBitmap,

                jenisDokumentasi =
                    namaDokumentasi(
                        jenisDokumentasi
                    )
            )

        // =====================================================
        // TUTUP BITMAP
        // =====================================================

        if (!originalBitmap.isRecycled) {
            originalBitmap.recycle()
        }

        if (
            watermarkedBitmap !== originalBitmap &&
            !watermarkedBitmap.isRecycled
        ) {
            watermarkedBitmap.recycle()
        }

        // =====================================================
        // HAPUS FOTO SEMENTARA
        // =====================================================

        try {

            if (fotoFile.exists()) {
                fotoFile.delete()
            }

        } catch (e: Exception) {

            e.printStackTrace()
        }

        // =====================================================
        // KIRIM PATH FOTO FINAL
        // =====================================================

        onFotoDiambil(
            finalFile.absolutePath
        )

    } catch (e: Exception) {

        e.printStackTrace()

        onErrorCallback(

            "Gagal membuat watermark foto: " +
                    (
                            e.message
                                ?: "Kesalahan tidak diketahui."
                            )
        )
    }
}


// =============================================================
// NAMA DOKUMENTASI
// =============================================================

private fun namaDokumentasi(
    key: String
): String {

    return when (key) {

        "sondingAwal" ->
            "Sonding Storage Tank 3287 Sebelum Penerimaan"

        "truck" ->
            "Foto Truck Tangki"

        "do" ->
            "Dokumen Delivery Order (DO)"

        "segelManholeDepan" ->
            "Segel Manhole Depan"

        "segelManholeBelakang" ->
            "Segel Manhole Belakang"

        "segelValveDepan" ->
            "Segel Valve Depan"

        "segelValveBelakang" ->
            "Segel Valve Belakang"

        "sondingDepan" ->
            "Sonding Tangki Truck Sisi Depan"

        "kadarAirDepan" ->
            "Kadar Air Tangki Truck Sisi Depan"

        "densityDepan" ->
            "Density Tangki Truck Sisi Depan"

        "sondingBelakang" ->
            "Sonding Tangki Truck Sisi Belakang"

        "kadarAirBelakang" ->
            "Kadar Air Tangki Truck Sisi Belakang"

        "densityBelakang" ->
            "Density Tangki Truck Sisi Belakang"

        "flowmeterAwal" ->
            "Stand Flowmeter Awal"

        "flowmeterAkhir" ->
            "Stand Flowmeter Akhir"

        "sondingAkhir" ->
            "Sonding Storage Tank 3287 Setelah Penerimaan"

        else ->
            key
    }
}