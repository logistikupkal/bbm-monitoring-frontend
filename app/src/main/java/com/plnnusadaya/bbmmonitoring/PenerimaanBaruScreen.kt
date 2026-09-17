package com.plnnusadaya.bbmmonitoring

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// =============================================================
// PENERIMAAN BARU SCREEN
// =============================================================

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PenerimaanBaruScreen(
    onBack: () -> Unit
) {

    // =========================================================
    // WARNA
    // =========================================================

    val bluePrimary = Color(0xFF087AC1)
    val blueDark = Color(0xFF004A87)
    val backgroundColor = Color(0xFFF5F9FC)
    val textGray = Color(0xFF60758A)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // =========================================================
    // KONFIGURASI MASTER TETAP
    // =========================================================

    val lokasiPenerimaan = "PLTD Sewa Batulicin"

    val locationId =
        "7d3a9735-e769-44ea-a08d-5e0a1e588278"

    val storageTankId =
        "9207f666-d0fb-4edf-ba3a-466659ac466f"

    val fuelProductId =
        "925184f8-e6a3-434c-8512-5a1f69fa765e"

    // =========================================================
    // STEP
    // =========================================================

    var currentStep by remember {
        mutableStateOf(1)
    }

    // =========================================================
    // KAMERA
    // =========================================================

    var kameraTerbuka by remember {
        mutableStateOf(false)
    }

    var cameraTarget by remember {
        mutableStateOf("")
    }

    // =========================================================
    // DATA HEADER
    // =========================================================

    var sondingAwal by remember {
        mutableStateOf("")
    }

    var nomorPolisi by remember {
        mutableStateOf("")
    }

    var namaDriver by remember {
        mutableStateOf("")
    }

    val jenisBBM = "B-50"

    var nomorDO by remember {
        mutableStateOf("")
    }

    var tanggalDO by remember {
        mutableStateOf<Long?>(null)
    }

    var volumeDO by remember {
        mutableStateOf("")
    }

    var tanggalPenerimaan by remember {
        mutableStateOf<Long?>(null)
    }

    // =========================================================
    // DATA MEASUREMENT
    // =========================================================

    var sondingDepan by remember {
        mutableStateOf("")
    }

    var kadarAirDepan by remember {
        mutableStateOf("")
    }

    var densityDepan by remember {
        mutableStateOf("0,820")
    }

    var temperaturDepan by remember {
        mutableStateOf("")
    }

    var sondingBelakang by remember {
        mutableStateOf("")
    }

    var kadarAirBelakang by remember {
        mutableStateOf("")
    }

    var densityBelakang by remember {
        mutableStateOf("0,820")
    }

    var temperaturBelakang by remember {
        mutableStateOf("")
    }

    var flowmeterCode by remember {
        mutableStateOf("FM-3287")
    }

    var flowmeterAwal by remember {
        mutableStateOf("")
    }

    var flowmeterAkhir by remember {
        mutableStateOf("")
    }

    var sondingAkhir by remember {
        mutableStateOf("")
    }

    // =========================================================
    // FOTO BITMAP
    // =========================================================

    var fotoSondingAwal by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoTruck by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoDO by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSegelManholeDepan by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSegelManholeBelakang by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSegelValveDepan by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSegelValveBelakang by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSondingDepan by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoKadarAirDepan by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoDensityDepan by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSondingBelakang by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoKadarAirBelakang by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoDensityBelakang by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoFlowmeterAwal by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoFlowmeterAkhir by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var fotoSondingAkhir by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // =========================================================
    // FOTO PATH
    // =========================================================

    var fotoSondingAwalPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoTruckPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoDOPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSegelManholeDepanPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSegelManholeBelakangPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSegelValveDepanPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSegelValveBelakangPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSondingDepanPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoKadarAirDepanPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoDensityDepanPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSondingBelakangPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoKadarAirBelakangPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoDensityBelakangPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoFlowmeterAwalPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoFlowmeterAkhirPath by remember {
        mutableStateOf<String?>(null)
    }

    var fotoSondingAkhirPath by remember {
        mutableStateOf<String?>(null)
    }

    // =========================================================
    // PREVIEW FOTO
    // =========================================================

    var fotoPreview by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // =========================================================
    // DATE PICKER
    // =========================================================

    var showDatePickerDO by remember {
        mutableStateOf(false)
    }

    var showDatePickerPenerimaan by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // SAVE STATE
    // =========================================================

    var deliveryId by remember {
        mutableStateOf<String?>(null)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    var saveMessage by remember {
        mutableStateOf("")
    }

    var saveError by remember {
        mutableStateOf(false)
    }

    // =========================================================
    // FORMAT TANGGAL
    // =========================================================

    fun formatTanggal(timestamp: Long?): String {

        if (timestamp == null) {
            return "Pilih tanggal"
        }

        val formatter =
            SimpleDateFormat(
                "dd MMMM yyyy",
                Locale("id", "ID")
            )

        formatter.timeZone =
            TimeZone.getTimeZone(
                "Asia/Makassar"
            )

        return formatter.format(
            Date(timestamp)
        )
    }

    fun formatTanggalApi(timestamp: Long): String {

        val formatter =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

        formatter.timeZone =
            TimeZone.getTimeZone(
                "Asia/Makassar"
            )

        return formatter.format(
            Date(timestamp)
        )
    }

    // =========================================================
    // CAMERA SCREEN
    // =========================================================

    if (kameraTerbuka) {

        CameraCaptureScreen(

            jenisDokumentasi =
                cameraTarget,

            lokasiPenerimaan =
                lokasiPenerimaan,

            onFotoDiambil = { pathFoto ->

                val bitmap =
                    BitmapFactory.decodeFile(
                        pathFoto
                    )

                when (cameraTarget) {

                    "sondingAwal" -> {
                        fotoSondingAwal = bitmap
                        fotoSondingAwalPath = pathFoto
                    }

                    "truck" -> {
                        fotoTruck = bitmap
                        fotoTruckPath = pathFoto
                    }

                    "do" -> {
                        fotoDO = bitmap
                        fotoDOPath = pathFoto
                    }

                    "segelManholeDepan" -> {
                        fotoSegelManholeDepan = bitmap
                        fotoSegelManholeDepanPath = pathFoto
                    }

                    "segelManholeBelakang" -> {
                        fotoSegelManholeBelakang = bitmap
                        fotoSegelManholeBelakangPath = pathFoto
                    }

                    "segelValveDepan" -> {
                        fotoSegelValveDepan = bitmap
                        fotoSegelValveDepanPath = pathFoto
                    }

                    "segelValveBelakang" -> {
                        fotoSegelValveBelakang = bitmap
                        fotoSegelValveBelakangPath = pathFoto
                    }

                    "sondingDepan" -> {
                        fotoSondingDepan = bitmap
                        fotoSondingDepanPath = pathFoto
                    }

                    "kadarAirDepan" -> {
                        fotoKadarAirDepan = bitmap
                        fotoKadarAirDepanPath = pathFoto
                    }

                    "densityDepan" -> {
                        fotoDensityDepan = bitmap
                        fotoDensityDepanPath = pathFoto
                    }

                    "sondingBelakang" -> {
                        fotoSondingBelakang = bitmap
                        fotoSondingBelakangPath = pathFoto
                    }

                    "kadarAirBelakang" -> {
                        fotoKadarAirBelakang = bitmap
                        fotoKadarAirBelakangPath = pathFoto
                    }

                    "densityBelakang" -> {
                        fotoDensityBelakang = bitmap
                        fotoDensityBelakangPath = pathFoto
                    }

                    "flowmeterAwal" -> {
                        fotoFlowmeterAwal = bitmap
                        fotoFlowmeterAwalPath = pathFoto
                    }

                    "flowmeterAkhir" -> {
                        fotoFlowmeterAkhir = bitmap
                        fotoFlowmeterAkhirPath = pathFoto
                    }

                    "sondingAkhir" -> {
                        fotoSondingAkhir = bitmap
                        fotoSondingAkhirPath = pathFoto
                    }
                }

                kameraTerbuka = false
            },

            onBack = {
                kameraTerbuka = false
            }
        )

        return
    }

    // =========================================================
    // DATE PICKER DO
    // =========================================================

    if (showDatePickerDO) {

        val dateState =
            androidx.compose.material3.rememberDatePickerState()

        DatePickerDialog(

            onDismissRequest = {
                showDatePickerDO = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        tanggalDO =
                            dateState.selectedDateMillis

                        showDatePickerDO = false
                    }
                ) {

                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePickerDO = false
                    }
                ) {

                    Text("Batal")
                }
            }

        ) {

            DatePicker(
                state = dateState,
                showModeToggle = false
            )
        }
    }

    // =========================================================
    // DATE PICKER PENERIMAAN
    // =========================================================

    if (showDatePickerPenerimaan) {

        val dateState =
            androidx.compose.material3.rememberDatePickerState()

        DatePickerDialog(

            onDismissRequest = {
                showDatePickerPenerimaan = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        tanggalPenerimaan =
                            dateState.selectedDateMillis

                        showDatePickerPenerimaan = false
                    }
                ) {

                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePickerPenerimaan = false
                    }
                ) {

                    Text("Batal")
                }
            }

        ) {

            DatePicker(
                state = dateState,
                showModeToggle = false
            )
        }
    }

    // =========================================================
    // LAYOUT UTAMA
    // =========================================================

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Column {

                        Text(
                            text = "Penerimaan BBM",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = lokasiPenerimaan,
                            fontSize = 12.sp,
                            color = textGray
                        )
                    }
                },

                navigationIcon = {

                    TextButton(
                        onClick = onBack
                    ) {

                        Text(
                            text = "←",
                            fontSize = 28.sp,
                            color = bluePrimary
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        Column(

            modifier =
                Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues)

        ) {

            StepProgress(
                currentStep = currentStep
            )

            Column(

                modifier =
                    Modifier
                        .weight(1f)
                        .verticalScroll(
                            rememberScrollState()
                        )
                        .padding(16.dp)

            ) {

                when (currentStep) {

                    // =================================================
                    // STEP 1
                    // =================================================

                    1 -> {

                        SectionHeader(
                            number = "1",
                            title = "Persiapan Storage Tank",
                            subtitle =
                                "Lakukan pengukuran awal sebelum penerimaan BBM."
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        AutoField(
                            label = "Lokasi Penerimaan",
                            value = lokasiPenerimaan
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = sondingAwal,
                            onValueChange = {
                                sondingAwal =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Sonding Level BBM Storage Tank 3287 Sebelum Penerimaan",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Sonding Sebelum Penerimaan",
                            bitmap = fotoSondingAwal,
                            onTakePhoto = {
                                cameraTarget = "sondingAwal"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview = fotoSondingAwal
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        InstructionCard(
                            text =
                                "Pastikan hasil sonding telah dicatat sebelum proses penerimaan dimulai."
                        )
                    }

                    // =================================================
                    // STEP 2
                    // =================================================

                    2 -> {

                        SectionHeader(
                            number = "2",
                            title = "Identitas Truck & DO",
                            subtitle =
                                "Catat identitas pengiriman dan dokumentasikan dokumen serta segel."
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        OutlinedTextField(

                            value = nomorPolisi,

                            onValueChange = {
                                nomorPolisi =
                                    it.uppercase(
                                        Locale.getDefault()
                                    )
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text(
                                    "Nomor Polisi Truck Tangki"
                                )
                            },

                            placeholder = {
                                Text(
                                    "Contoh: DA 1234 XX"
                                )
                            },

                            singleLine = true,

                            shape =
                                RoundedCornerShape(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Foto Truck Tangki",
                            bitmap = fotoTruck,
                            onTakePhoto = {
                                cameraTarget = "truck"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview = fotoTruck
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        OutlinedTextField(

                            value = namaDriver,

                            onValueChange = {
                                namaDriver =
                                    it.uppercase(
                                        Locale.getDefault()
                                    )
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text("Nama Driver")
                            },

                            placeholder = {
                                Text("Contoh: BUDI SANTOSO")
                            },

                            singleLine = true,

                            shape =
                                RoundedCornerShape(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        AutoField(
                            label = "Jenis BBM",
                            value = jenisBBM
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        OutlinedTextField(

                            value = nomorDO,

                            onValueChange = {
                                nomorDO =
                                    it.filter { char ->
                                        char.isDigit()
                                    }
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text(
                                    "Nomor Delivery Order (DO)"
                                )
                            },

                            singleLine = true,

                            shape =
                                RoundedCornerShape(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DateField(
                            label =
                                "Tanggal Delivery Order (DO)",
                            value =
                                formatTanggal(tanggalDO),
                            onClick = {
                                showDatePickerDO = true
                            }
                        )

                        Text(
                            text = "WITA • GMT+8",
                            fontSize = 11.sp,
                            color = textGray,
                            modifier =
                                Modifier.padding(
                                    start = 4.dp,
                                    top = 4.dp
                                )
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        OutlinedTextField(

                            value = volumeDO,

                            onValueChange = {

                                val angka =
                                    it.filter { char ->
                                        char.isDigit()
                                    }

                                if (angka.length <= 6) {
                                    volumeDO = angka
                                }
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {
                                Text(
                                    "Volume Delivery Order (DO)"
                                )
                            },

                            suffix = {
                                Text("Liter")
                            },

                            singleLine = true,

                            shape =
                                RoundedCornerShape(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Dokumen Delivery Order (DO)",
                            bitmap = fotoDO,
                            onTakePhoto = {
                                cameraTarget = "do"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview = fotoDO
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        Text(
                            text = "Dokumentasi Segel",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = blueDark
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        PhotoButton(
                            title = "Segel - Manhole Depan",
                            bitmap = fotoSegelManholeDepan,
                            onTakePhoto = {
                                cameraTarget =
                                    "segelManholeDepan"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSegelManholeDepan
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Segel - Manhole Belakang",
                            bitmap = fotoSegelManholeBelakang,
                            onTakePhoto = {
                                cameraTarget =
                                    "segelManholeBelakang"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSegelManholeBelakang
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Segel - Valve Depan",
                            bitmap = fotoSegelValveDepan,
                            onTakePhoto = {
                                cameraTarget =
                                    "segelValveDepan"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSegelValveDepan
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Segel - Valve Belakang",
                            bitmap = fotoSegelValveBelakang,
                            onTakePhoto = {
                                cameraTarget =
                                    "segelValveBelakang"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSegelValveBelakang
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        DateField(
                            label = "Tanggal Penerimaan",
                            value =
                                formatTanggal(
                                    tanggalPenerimaan
                                ),
                            onClick = {
                                showDatePickerPenerimaan = true
                            }
                        )

                        Text(
                            text = "WITA • GMT+8",
                            fontSize = 11.sp,
                            color = textGray,
                            modifier =
                                Modifier.padding(
                                    start = 4.dp,
                                    top = 4.dp
                                )
                        )
                    }

                    // =================================================
                    // STEP 3
                    // =================================================

                    3 -> {

                        SectionHeader(
                            number = "3",
                            title = "Pengukuran Tangki Truck",
                            subtitle =
                                "Lakukan pengukuran sisi depan dan sisi belakang."
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        SubTitle(
                            text = "Tangki Truck Sisi Depan"
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        DecimalField(
                            value = sondingDepan,
                            onValueChange = {
                                sondingDepan =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Hasil Sonding Level Tangki Truck Sisi Depan",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Foto Dokumentasi Sonding",
                            bitmap = fotoSondingDepan,
                            onTakePhoto = {
                                cameraTarget = "sondingDepan"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview = fotoSondingDepan
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = kadarAirDepan,
                            onValueChange = {
                                kadarAirDepan =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Kadar Air Tangki Truck Sisi Depan",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Cek Kadar Air",
                            bitmap = fotoKadarAirDepan,
                            onTakePhoto = {
                                cameraTarget = "kadarAirDepan"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoKadarAirDepan
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DensityDropdown(
                            value = densityDepan,
                            onValueChange = {
                                densityDepan = it
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Pengujian Density",
                            bitmap = fotoDensityDepan,
                            onTakePhoto = {
                                cameraTarget = "densityDepan"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoDensityDepan
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = temperaturDepan,
                            onValueChange = {
                                temperaturDepan =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Temperatur BBM Tangki Truck Sisi Depan",
                            suffix = "°C"
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        SubTitle(
                            text = "Tangki Truck Sisi Belakang"
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        DecimalField(
                            value = sondingBelakang,
                            onValueChange = {
                                sondingBelakang =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Hasil Sonding Level Tangki Truck Sisi Belakang",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title = "Foto Dokumentasi Sonding",
                            bitmap = fotoSondingBelakang,
                            onTakePhoto = {
                                cameraTarget =
                                    "sondingBelakang"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSondingBelakang
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = kadarAirBelakang,
                            onValueChange = {
                                kadarAirBelakang =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Kadar Air Tangki Truck Sisi Belakang",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Cek Kadar Air",
                            bitmap = fotoKadarAirBelakang,
                            onTakePhoto = {
                                cameraTarget =
                                    "kadarAirBelakang"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoKadarAirBelakang
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DensityDropdown(
                            value = densityBelakang,
                            onValueChange = {
                                densityBelakang = it
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Pengujian Density",
                            bitmap = fotoDensityBelakang,
                            onTakePhoto = {
                                cameraTarget =
                                    "densityBelakang"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoDensityBelakang
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = temperaturBelakang,
                            onValueChange = {
                                temperaturBelakang =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Temperatur BBM Tangki Truck Sisi Belakang",
                            suffix = "°C"
                        )
                    }

                    // =================================================
                    // STEP 4
                    // =================================================

                    4 -> {

                        SectionHeader(
                            number = "4",
                            title = "Persiapan Flowmeter",
                            subtitle =
                                "Catat flowmeter dan stand awal sebelum proses penerimaan."
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        OutlinedTextField(
                            value = flowmeterCode,
                            onValueChange = {
                                flowmeterCode =
                                    it.uppercase(
                                        Locale.getDefault()
                                    )
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            label = {
                                Text("Kode Flowmeter")
                            },
                            placeholder = {
                                Text("Contoh: FM-3287")
                            },
                            singleLine = true,
                            shape =
                                RoundedCornerShape(14.dp)
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        DecimalField(
                            value = flowmeterAwal,
                            onValueChange = {
                                flowmeterAwal = it
                            },
                            label = "Stand Flowmeter Awal"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Stand Awal Flowmeter",
                            bitmap = fotoFlowmeterAwal,
                            onTakePhoto = {
                                cameraTarget =
                                    "flowmeterAwal"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoFlowmeterAwal
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        InstructionCard(
                            text =
                                "Pastikan flowmeter telah di-reset untuk pengukuran penerimaan dan dokumentasikan stand awal."
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        ProcessInfoCard(
                            title =
                                "Proses Penerimaan BBM",
                            text =
                                "Setelah data persiapan lengkap, lakukan proses pemompaan sesuai Instruksi Kerja. Pastikan jalur selang aman dan tidak terjadi kebocoran."
                        )
                    }

                    // =================================================
                    // STEP 5
                    // =================================================

                    5 -> {

                        SectionHeader(
                            number = "5",
                            title =
                                "Setelah Proses Penerimaan",
                            subtitle =
                                "Catat stand akhir flowmeter setelah proses penerimaan selesai."
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        DecimalField(
                            value = flowmeterAkhir,
                            onValueChange = {
                                flowmeterAkhir = it
                            },
                            label = "Stand Flowmeter Akhir"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Stand Akhir Flowmeter",
                            bitmap = fotoFlowmeterAkhir,
                            onTakePhoto = {
                                cameraTarget =
                                    "flowmeterAkhir"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoFlowmeterAkhir
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        ProcessInfoCard(
                            title =
                                "Pemeriksaan Setelah Penerimaan",
                            text =
                                "Pastikan proses pemompaan telah selesai dan seluruh kondisi area kerja aman sebelum melanjutkan."
                        )
                    }

                    // =================================================
                    // STEP 6
                    // =================================================

                    6 -> {

                        SectionHeader(
                            number = "6",
                            title =
                                "Penyelesaian & Sonding Akhir",
                            subtitle =
                                "Lakukan pengukuran Storage Tank 3287 setelah penerimaan."
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        DecimalField(
                            value = sondingAkhir,
                            onValueChange = {
                                sondingAkhir =
                                    limitDecimal(it, 3, 1)
                            },
                            label =
                                "Sonding Level BBM Storage Tank 3287 Setelah Penerimaan",
                            suffix = "cm"
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        PhotoButton(
                            title =
                                "Foto Dokumentasi Sonding Setelah Penerimaan",
                            bitmap = fotoSondingAkhir,
                            onTakePhoto = {
                                cameraTarget =
                                    "sondingAkhir"
                                kameraTerbuka = true
                            },
                            onViewPhoto = {
                                fotoPreview =
                                    fotoSondingAkhir
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        ReconciliationCard(
                            volumeDO = volumeDO,
                            flowmeterAwal = flowmeterAwal,
                            flowmeterAkhir = flowmeterAkhir
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        Button(

                            onClick = {

                                if (isSaving) {
                                    return@Button
                                }

                                saveMessage = ""
                                saveError = false

                                val token =
                                    SessionManager
                                        .getAccessToken(
                                            context
                                        )

                                if (token.isNullOrBlank()) {

                                    saveError = true
                                    saveMessage =
                                        "Session login tidak ditemukan. Login kembali."
                                    return@Button
                                }

                                // ==================================================
                                // VALIDASI HEADER
                                // ==================================================

                                if (nomorPolisi.isBlank() ||
                                    namaDriver.isBlank() ||
                                    nomorDO.isBlank() ||
                                    volumeDO.isBlank() ||
                                    tanggalDO == null ||
                                    tanggalPenerimaan == null ||
                                    flowmeterCode.isBlank()
                                ) {

                                    saveError = true
                                    saveMessage =
                                        "Data identitas, DO, flowmeter dan tanggal wajib lengkap."
                                    return@Button
                                }

                                // ==================================================
                                // VALIDASI MEASUREMENT
                                // ==================================================

                                val storageBefore =
                                    sondingAwal
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val frontSounding =
                                    sondingDepan
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val frontWater =
                                    kadarAirDepan
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val frontDensity =
                                    densityDepan
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val frontTemperature =
                                    temperaturDepan
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val backSounding =
                                    sondingBelakang
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val backWater =
                                    kadarAirBelakang
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val backDensity =
                                    densityBelakang
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val backTemperature =
                                    temperaturBelakang
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val meterInitial =
                                    flowmeterAwal
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val meterFinal =
                                    flowmeterAkhir
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val storageAfter =
                                    sondingAkhir
                                        .replace(",", ".")
                                        .toDoubleOrNull()

                                val volume =
                                    volumeDO.toDoubleOrNull()

                                if (
                                    storageBefore == null ||
                                    frontSounding == null ||
                                    frontWater == null ||
                                    frontDensity == null ||
                                    frontTemperature == null ||
                                    backSounding == null ||
                                    backWater == null ||
                                    backDensity == null ||
                                    backTemperature == null ||
                                    meterInitial == null ||
                                    meterFinal == null ||
                                    storageAfter == null ||
                                    volume == null ||
                                    volume <= 0
                                ) {

                                    saveError = true
                                    saveMessage =
                                        "Semua data pengukuran wajib diisi dengan angka yang valid."
                                    return@Button
                                }

                                // ==================================================
                                // VALIDASI 16 FOTO
                                // ==================================================

                                val allPhotosReady =
                                    listOf(
                                        fotoSondingAwalPath,
                                        fotoTruckPath,
                                        fotoDOPath,
                                        fotoSegelManholeDepanPath,
                                        fotoSegelManholeBelakangPath,
                                        fotoSegelValveDepanPath,
                                        fotoSegelValveBelakangPath,
                                        fotoSondingDepanPath,
                                        fotoKadarAirDepanPath,
                                        fotoDensityDepanPath,
                                        fotoSondingBelakangPath,
                                        fotoKadarAirBelakangPath,
                                        fotoDensityBelakangPath,
                                        fotoFlowmeterAwalPath,
                                        fotoFlowmeterAkhirPath,
                                        fotoSondingAkhirPath
                                    ).all { !it.isNullOrBlank() }

                                if (!allPhotosReady) {

                                    saveError = true
                                    saveMessage =
                                        "Semua 16 foto dokumentasi wajib diambil sebelum disimpan."
                                    return@Button
                                }

                                coroutineScope.launch {

                                    isSaving = true
                                    saveError = false
                                    saveMessage =
                                        "Menyimpan data penerimaan..."

                                    try {

                                        // ==================================================
                                        // 1. CREATE DELIVERY
                                        // ==================================================

                                        val deliveryResult =
                                            BackendApi.createDelivery(

                                                accessToken =
                                                    token,

                                                locationId =
                                                    locationId,

                                                storageTankId =
                                                    storageTankId,

                                                fuelProductId =
                                                    fuelProductId,

                                                truckPlateNumber =
                                                    nomorPolisi,

                                                driverName =
                                                    namaDriver,

                                                flowmeterCode =
                                                    flowmeterCode,

                                                doNumber =
                                                    nomorDO,

                                                doDate =
                                                    formatTanggalApi(
                                                        tanggalDO!!
                                                    ),

                                                doVolumeLiter =
                                                    volume,

                                                receivedDate =
                                                    formatTanggalApi(
                                                        tanggalPenerimaan!!
                                                    ),

                                                status =
                                                    "DRAFT",

                                                notes =
                                                    null
                                            )

                                        if (
                                            !deliveryResult.success ||
                                            deliveryResult.deliveryId.isNullOrBlank()
                                        ) {

                                            isSaving = false
                                            saveError = true
                                            saveMessage =
                                                deliveryResult.error
                                                    ?: "Gagal membuat data penerimaan."

                                            return@launch
                                        }

                                        val newDeliveryId =
                                            deliveryResult.deliveryId!!

                                        deliveryId =
                                            newDeliveryId

                                        // ==================================================
                                        // 2. SAVE MEASUREMENTS
                                        // ==================================================

                                        saveMessage =
                                            "Menyimpan hasil pengukuran..."

                                        val measurementResult =
                                            BackendApi.saveMeasurements(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                storageSoundingBeforeCm =
                                                    storageBefore,

                                                truckFrontSoundingCm =
                                                    frontSounding,

                                                truckFrontWaterCm =
                                                    frontWater,

                                                truckFrontDensity =
                                                    frontDensity,

                                                truckFrontTemperatureC =
                                                    frontTemperature,

                                                truckBackSoundingCm =
                                                    backSounding,

                                                truckBackWaterCm =
                                                    backWater,

                                                truckBackDensity =
                                                    backDensity,

                                                truckBackTemperatureC =
                                                    backTemperature,

                                                flowmeterInitial =
                                                    meterInitial,

                                                flowmeterFinal =
                                                    meterFinal,

                                                storageSoundingAfterCm =
                                                    storageAfter
                                            )

                                        if (!measurementResult.success) {

                                            isSaving = false
                                            saveError = true
                                            saveMessage =
                                                measurementResult.error
                                                    ?: "Gagal menyimpan pengukuran."

                                            return@launch
                                        }

                                        // ==================================================
                                        // 3. UPLOAD FOTO + SEAL
                                        // ==================================================

                                        val location =
                                            getCurrentLocationSuspend(
                                                context
                                            )

                                        val latitude =
                                            location?.latitude

                                        val longitude =
                                            location?.longitude

                                        val capturedAt =
                                            getCurrentWitaIsoTime()

                                        suspend fun upload(
                                            path: String,
                                            type: String
                                        ): BackendApi.PhotoResult {

                                            return BackendApi.uploadPhoto(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                filePath =
                                                    path,

                                                documentationType =
                                                    type,

                                                latitude =
                                                    latitude,

                                                longitude =
                                                    longitude,

                                                capturedAt =
                                                    capturedAt
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Foto non-seal
                                        // --------------------------------------------------

                                        saveMessage =
                                            "Upload foto dokumentasi..."

                                        val photoSondingAwal =
                                            upload(
                                                fotoSondingAwalPath!!,
                                                "sonding_awal"
                                            )

                                        if (!photoSondingAwal.success) {
                                            throw Exception(
                                                photoSondingAwal.error
                                                    ?: "Upload foto sonding awal gagal."
                                            )
                                        }

                                        val photoTruck =
                                            upload(
                                                fotoTruckPath!!,
                                                "truck"
                                            )

                                        if (!photoTruck.success) {
                                            throw Exception(
                                                photoTruck.error
                                                    ?: "Upload foto truck gagal."
                                            )
                                        }

                                        val photoDO =
                                            upload(
                                                fotoDOPath!!,
                                                "do"
                                            )

                                        if (!photoDO.success) {
                                            throw Exception(
                                                photoDO.error
                                                    ?: "Upload foto DO gagal."
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Seal 1
                                        // --------------------------------------------------

                                        val photoManholeDepan =
                                            upload(
                                                fotoSegelManholeDepanPath!!,
                                                "MANHOLE_FRONT"
                                            )

                                        if (!photoManholeDepan.success) {
                                            throw Exception(
                                                photoManholeDepan.error
                                                    ?: "Upload foto manhole depan gagal."
                                            )
                                        }

                                        val sealManholeDepan =
                                            BackendApi.saveSeal(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                sealType =
                                                    "MANHOLE_FRONT",

                                                condition =
                                                    "BAIK",

                                                photoId =
                                                    photoManholeDepan.photoId
                                            )

                                        if (!sealManholeDepan.success) {
                                            throw Exception(
                                                sealManholeDepan.error
                                                    ?: "Simpan seal manhole depan gagal."
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Seal 2
                                        // --------------------------------------------------

                                        val photoManholeBelakang =
                                            upload(
                                                fotoSegelManholeBelakangPath!!,
                                                "MANHOLE_BACK"
                                            )

                                        if (!photoManholeBelakang.success) {
                                            throw Exception(
                                                photoManholeBelakang.error
                                                    ?: "Upload foto manhole belakang gagal."
                                            )
                                        }

                                        val sealManholeBelakang =
                                            BackendApi.saveSeal(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                sealType =
                                                    "MANHOLE_BACK",

                                                condition =
                                                    "BAIK",

                                                photoId =
                                                    photoManholeBelakang.photoId
                                            )

                                        if (!sealManholeBelakang.success) {
                                            throw Exception(
                                                sealManholeBelakang.error
                                                    ?: "Simpan seal manhole belakang gagal."
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Seal 3
                                        // --------------------------------------------------

                                        val photoValveDepan =
                                            upload(
                                                fotoSegelValveDepanPath!!,
                                                "VALVE_FRONT"
                                            )

                                        if (!photoValveDepan.success) {
                                            throw Exception(
                                                photoValveDepan.error
                                                    ?: "Upload foto valve depan gagal."
                                            )
                                        }

                                        val sealValveDepan =
                                            BackendApi.saveSeal(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                sealType =
                                                    "VALVE_FRONT",

                                                condition =
                                                    "BAIK",

                                                photoId =
                                                    photoValveDepan.photoId
                                            )

                                        if (!sealValveDepan.success) {
                                            throw Exception(
                                                sealValveDepan.error
                                                    ?: "Simpan seal valve depan gagal."
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Seal 4
                                        // --------------------------------------------------

                                        val photoValveBelakang =
                                            upload(
                                                fotoSegelValveBelakangPath!!,
                                                "VALVE_BACK"
                                            )

                                        if (!photoValveBelakang.success) {
                                            throw Exception(
                                                photoValveBelakang.error
                                                    ?: "Upload foto valve belakang gagal."
                                            )
                                        }

                                        val sealValveBelakang =
                                            BackendApi.saveSeal(

                                                accessToken =
                                                    token,

                                                deliveryId =
                                                    newDeliveryId,

                                                sealType =
                                                    "VALVE_BACK",

                                                condition =
                                                    "BAIK",

                                                photoId =
                                                    photoValveBelakang.photoId
                                            )

                                        if (!sealValveBelakang.success) {
                                            throw Exception(
                                                sealValveBelakang.error
                                                    ?: "Simpan seal valve belakang gagal."
                                            )
                                        }

                                        // --------------------------------------------------
                                        // Foto measurement lainnya
                                        // --------------------------------------------------

                                        val photoSondingDepan =
                                            upload(
                                                fotoSondingDepanPath!!,
                                                "sonding_depan"
                                            )

                                        if (!photoSondingDepan.success) {
                                            throw Exception(
                                                photoSondingDepan.error
                                                    ?: "Upload foto sonding depan gagal."
                                            )
                                        }

                                        val photoKadarAirDepan =
                                            upload(
                                                fotoKadarAirDepanPath!!,
                                                "kadar_air_depan"
                                            )

                                        if (!photoKadarAirDepan.success) {
                                            throw Exception(
                                                photoKadarAirDepan.error
                                                    ?: "Upload foto kadar air depan gagal."
                                            )
                                        }

                                        val photoDensityDepan =
                                            upload(
                                                fotoDensityDepanPath!!,
                                                "density_depan"
                                            )

                                        if (!photoDensityDepan.success) {
                                            throw Exception(
                                                photoDensityDepan.error
                                                    ?: "Upload foto density depan gagal."
                                            )
                                        }

                                        val photoSondingBelakang =
                                            upload(
                                                fotoSondingBelakangPath!!,
                                                "sonding_belakang"
                                            )

                                        if (!photoSondingBelakang.success) {
                                            throw Exception(
                                                photoSondingBelakang.error
                                                    ?: "Upload foto sonding belakang gagal."
                                            )
                                        }

                                        val photoKadarAirBelakang =
                                            upload(
                                                fotoKadarAirBelakangPath!!,
                                                "kadar_air_belakang"
                                            )

                                        if (!photoKadarAirBelakang.success) {
                                            throw Exception(
                                                photoKadarAirBelakang.error
                                                    ?: "Upload foto kadar air belakang gagal."
                                            )
                                        }

                                        val photoDensityBelakang =
                                            upload(
                                                fotoDensityBelakangPath!!,
                                                "density_belakang"
                                            )

                                        if (!photoDensityBelakang.success) {
                                            throw Exception(
                                                photoDensityBelakang.error
                                                    ?: "Upload foto density belakang gagal."
                                            )
                                        }

                                        val photoFlowmeterAwal =
                                            upload(
                                                fotoFlowmeterAwalPath!!,
                                                "flowmeter_awal"
                                            )

                                        if (!photoFlowmeterAwal.success) {
                                            throw Exception(
                                                photoFlowmeterAwal.error
                                                    ?: "Upload foto flowmeter awal gagal."
                                            )
                                        }

                                        val photoFlowmeterAkhir =
                                            upload(
                                                fotoFlowmeterAkhirPath!!,
                                                "flowmeter_akhir"
                                            )

                                        if (!photoFlowmeterAkhir.success) {
                                            throw Exception(
                                                photoFlowmeterAkhir.error
                                                    ?: "Upload foto flowmeter akhir gagal."
                                            )
                                        }

                                        val photoSondingAkhir =
                                            upload(
                                                fotoSondingAkhirPath!!,
                                                "sonding_akhir"
                                            )

                                        if (!photoSondingAkhir.success) {
                                            throw Exception(
                                                photoSondingAkhir.error
                                                    ?: "Upload foto sonding akhir gagal."
                                            )
                                        }

                                        // ==================================================
                                        // SELESAI
                                        // ==================================================

                                        isSaving = false
                                        saveError = false

                                        saveMessage =
                                            "Penerimaan berhasil disimpan.\nID: $newDeliveryId"

                                    } catch (e: Exception) {

                                        isSaving = false
                                        saveError = true

                                        saveMessage =
                                            e.message
                                                ?: "Terjadi kesalahan saat menyimpan."
                                    }
                                }
                            },

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),

                            enabled =
                                !isSaving,

                            shape =
                                RoundedCornerShape(16.dp),

                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor =
                                        bluePrimary
                                )

                        ) {

                            Text(
                                text =
                                    if (isSaving) {
                                        "Menyimpan..."
                                    } else {
                                        "Simpan Penerimaan"
                                    },

                                fontSize =
                                    16.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        if (saveMessage.isNotBlank()) {

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            Card(

                                modifier =
                                    Modifier.fillMaxWidth(),

                                shape =
                                    RoundedCornerShape(14.dp),

                                colors =
                                    CardDefaults.cardColors(

                                        containerColor =
                                            if (saveError) {
                                                Color(0xFFFFEBEE)
                                            } else {
                                                Color(0xFFE8F5E9)
                                            }
                                    )

                            ) {

                                Text(

                                    text =
                                        saveMessage,

                                    modifier =
                                        Modifier.padding(14.dp),

                                    fontSize =
                                        13.sp,

                                    color =
                                        if (saveError) {
                                            Color(0xFFC62828)
                                        } else {
                                            Color(0xFF2E7D32)
                                        },

                                    fontWeight =
                                        FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // =================================================
            // NAVIGASI STEP
            // =================================================

            StepNavigation(

                currentStep =
                    currentStep,

                onPrevious = {

                    if (
                        currentStep > 1 &&
                        !isSaving
                    ) {
                        currentStep--
                    }
                },

                onNext = {

                    if (
                        currentStep < 6 &&
                        !isSaving
                    ) {
                        currentStep++
                    }
                }
            )
        }
    }

    // =========================================================
    // PREVIEW FOTO
    // =========================================================

    if (fotoPreview != null) {

        AlertDialog(

            onDismissRequest = {
                fotoPreview = null
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        fotoPreview = null
                    }
                ) {

                    Text("Tutup")
                }
            },

            title = {
                Text("Foto Dokumentasi")
            },

            text = {

                Image(

                    bitmap =
                        fotoPreview!!.asImageBitmap(),

                    contentDescription =
                        "Foto dokumentasi",

                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                )
            }
        )
    }
}

// =============================================================
// LOCATION HELPER SUSPEND
// =============================================================

private suspend fun getCurrentLocationSuspend(
    context: Context
): Location? = suspendCoroutine { continuation ->

    LocationHelper.getCurrentLocation(
        context = context
    ) { location ->

        continuation.resume(location)
    }
}

// =============================================================
// WITA TIME
// =============================================================

private fun getCurrentWitaIsoTime(): String {

    val formatter =
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            Locale.US
        )

    formatter.timeZone =
        TimeZone.getTimeZone(
            "Asia/Makassar"
        )

    return formatter.format(
        Date()
    )
}

// =============================================================
// STEP PROGRESS
// =============================================================

@Composable
fun StepProgress(
    currentStep: Int
) {

    val stepNames = listOf(
        "Storage",
        "Truck & DO",
        "Pengukuran",
        "Flowmeter",
        "Pasca",
        "Selesai"
    )

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(12.dp)
        ) {

            Text(
                text =
                    "Tahap $currentStep dari 6",

                fontSize =
                    12.sp,

                color =
                    Color(0xFF60758A)
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                stepNames.forEachIndexed {
                        index,
                        name ->

                    val step =
                        index + 1

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Box(

                            modifier =
                                Modifier
                                    .size(28.dp)
                                    .background(

                                        color =
                                            if (
                                                step <= currentStep
                                            ) {
                                                Color(
                                                    0xFF087AC1
                                                )
                                            } else {
                                                Color(
                                                    0xFFCFD8DC
                                                )
                                            },

                                        shape =
                                            RoundedCornerShape(
                                                50
                                            )
                                    ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    step.toString(),

                                color =
                                    Color.White,

                                fontSize =
                                    11.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                name,

                            fontSize =
                                8.sp,

                            color =
                                Color(0xFF60758A)
                        )
                    }
                }
            }
        }
    }
}

// =============================================================
// STEP NAVIGATION
// =============================================================

@Composable
fun StepNavigation(
    currentStep: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    Color.White
                )
                .padding(12.dp),

        horizontalArrangement =
            Arrangement.spacedBy(10.dp)
    ) {

        if (currentStep > 1) {

            Button(

                onClick =
                    onPrevious,

                modifier =
                    Modifier.weight(1f),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFE7EEF3),

                        contentColor =
                            Color(0xFF455A64)
                    )
            ) {

                Text("← Sebelumnya")
            }
        }

        if (currentStep < 6) {

            Button(

                onClick =
                    onNext,

                modifier =
                    Modifier.weight(1f),

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFF087AC1)
                    )
            ) {

                Text("Lanjut →")
            }
        }
    }
}

// =============================================================
// SECTION HEADER
// =============================================================

@Composable
fun SectionHeader(
    number: String,
    title: String,
    subtitle: String
) {

    Column {

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier =
                    Modifier
                        .size(38.dp)
                        .background(
                            Color(0xFF087AC1),
                            RoundedCornerShape(11.dp)
                        ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        number,

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.width(10.dp)
            )

            Text(
                text =
                    title,

                fontSize =
                    20.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF004A87)
            )
        }

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                subtitle,

            fontSize =
                13.sp,

            color =
                Color(0xFF60758A)
        )
    }
}

// =============================================================
// SUB TITLE
// =============================================================

@Composable
fun SubTitle(
    text: String
) {

    Text(
        text =
            text,

        fontSize =
            17.sp,

        fontWeight =
            FontWeight.Bold,

        color =
            Color(0xFF004A87)
    )
}

// =============================================================
// AUTO FIELD
// =============================================================

@Composable
fun AutoField(
    label: String,
    value: String
) {

    OutlinedTextField(

        value =
            value,

        onValueChange = {},

        readOnly = true,

        modifier =
            Modifier.fillMaxWidth(),

        label = {
            Text(label)
        },

        singleLine =
            true,

        shape =
            RoundedCornerShape(14.dp)
    )
}

// =============================================================
// DECIMAL FIELD
// =============================================================

@Composable
fun DecimalField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suffix: String? = null
) {

    OutlinedTextField(

        value =
            value,

        onValueChange =
            onValueChange,

        modifier =
            Modifier.fillMaxWidth(),

        label = {
            Text(label)
        },

        suffix = {

            if (suffix != null) {
                Text(suffix)
            }
        },

        singleLine =
            true,

        shape =
            RoundedCornerShape(14.dp)
    )
}

// =============================================================
// DATE FIELD
// =============================================================

@Composable
fun DateField(
    label: String,
    value: String,
    onClick: () -> Unit
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        OutlinedTextField(

            value =
                value,

            onValueChange = {},

            readOnly = true,

            modifier =
                Modifier.weight(1f),

            label = {
                Text(label)
            },

            singleLine =
                true,

            shape =
                RoundedCornerShape(14.dp)
        )

        Spacer(
            modifier =
                Modifier.width(6.dp)
        )

        Button(
            onClick =
                onClick,

            shape =
                RoundedCornerShape(12.dp)
        ) {

            Text("Pilih")
        }
    }
}

// =============================================================
// DENSITY
// =============================================================

@Composable
fun DensityDropdown(
    value: String,
    onValueChange: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val densityList =
        (820..860).map {
            "0," + it
        }

    Box(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(

            value =
                value,

            onValueChange = {},

            readOnly =
                true,

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                Text("Density")
            },

            singleLine =
                true,

            shape =
                RoundedCornerShape(14.dp)
        )

        Button(

            onClick = {
                expanded = true
            },

            modifier =
                Modifier
                    .align(
                        Alignment.CenterEnd
                    )
                    .padding(
                        end = 6.dp
                    ),

            shape =
                RoundedCornerShape(10.dp)
        ) {

            Text("Pilih")
        }

        DropdownMenu(

            expanded =
                expanded,

            onDismissRequest = {
                expanded = false
            }

        ) {

            densityList.forEach {
                    density ->

                DropdownMenuItem(

                    text = {
                        Text(density)
                    },

                    onClick = {

                        onValueChange(
                            density
                        )

                        expanded = false
                    }
                )
            }
        }
    }
}

// =============================================================
// PHOTO BUTTON
// =============================================================

@Composable
fun PhotoButton(
    title: String,
    bitmap: Bitmap?,
    onTakePhoto: () -> Unit,
    onViewPhoto: () -> Unit
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "📷",

                fontSize =
                    20.sp
            )

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Text(
                text =
                    title,

                fontSize =
                    13.sp,

                color =
                    Color(0xFF455A64),

                modifier =
                    Modifier.weight(1f)
            )

            if (bitmap == null) {

                TextButton(
                    onClick =
                        onTakePhoto
                ) {

                    Text(
                        text =
                            "Ambil Foto",

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF087AC1)
                    )
                }

            } else {

                TextButton(
                    onClick =
                        onViewPhoto
                ) {

                    Text(
                        text =
                            "Lihat Foto",

                        fontSize =
                            12.sp,

                        fontWeight =
                            FontWeight.Bold,

                        color =
                            Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}

// =============================================================
// INSTRUCTION CARD
// =============================================================

@Composable
fun InstructionCard(
    text: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFEAF5FF)
            )
    ) {

        Text(

            text =
                "Instruksi Kerja\n$text",

            modifier =
                Modifier.padding(14.dp),

            fontSize =
                13.sp,

            color =
                Color(0xFF004A87)
        )
    }
}

// =============================================================
// PROCESS INFO
// =============================================================

@Composable
fun ProcessInfoCard(
    title: String,
    text: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    title,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF004A87)
            )

            Spacer(
                modifier =
                    Modifier.height(6.dp)
            )

            Text(
                text =
                    text,

                fontSize =
                    13.sp,

                color =
                    Color(0xFF60758A)
            )
        }
    }
}

// =============================================================
// RECONCILIATION
// =============================================================

@Composable
fun ReconciliationCard(
    volumeDO: String,
    flowmeterAwal: String,
    flowmeterAkhir: String
) {

    Card(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    "Rekonsiliasi Penerimaan",

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color(0xFF004A87)
            )

            Spacer(
                modifier =
                    Modifier.height(14.dp)
            )

            ReconciliationRow(
                label =
                    "Volume DO",

                value =
                    if (volumeDO.isBlank()) {
                        "-"
                    } else {
                        "$volumeDO Liter"
                    }
            )

            ReconciliationRow(
                label =
                    "Flowmeter Awal",

                value =
                    if (flowmeterAwal.isBlank()) {
                        "-"
                    } else {
                        flowmeterAwal
                    }
            )

            ReconciliationRow(
                label =
                    "Flowmeter Akhir",

                value =
                    if (flowmeterAkhir.isBlank()) {
                        "-"
                    } else {
                        flowmeterAkhir
                    }
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text =
                    "Selisih dan status anomali akan dihitung otomatis setelah aturan perhitungan ditetapkan.",

                fontSize =
                    12.sp,

                color =
                    Color(0xFF60758A)
            )
        }
    }
}

// =============================================================
// RECONCILIATION ROW
// =============================================================

@Composable
fun ReconciliationRow(
    label: String,
    value: String
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 5.dp
                ),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                label,

            fontSize =
                13.sp,

            color =
                Color(0xFF60758A)
        )

        Text(
            text =
                value,

            fontSize =
                13.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color(0xFF004A87)
        )
    }
}

// =============================================================
// LIMIT DECIMAL
// =============================================================

fun limitDecimal(
    input: String,
    maxInteger: Int,
    maxDecimal: Int
): String {

    val value =
        input.replace(
            ',',
            '.'
        )

    if (
        value.count {
            it == '.'
        } > 1
    ) {
        return value.dropLast(1)
    }

    val parts =
        value.split(".")

    if (
        parts[0].length >
        maxInteger
    ) {
        return value.dropLast(1)
    }

    if (
        parts.size == 2 &&
        parts[1].length >
        maxDecimal
    ) {
        return value.dropLast(1)
    }

    return value
}