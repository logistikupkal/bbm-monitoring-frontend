package com.plnnusadaya.bbmmonitoring

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

// ============================================================
// COLORS
// ============================================================

private val BlueDark = Color(0xFF004A87)
private val BluePrimary = Color(0xFF087AC1)
private val BlueSoft = Color(0xFFEAF5FF)
private val Background = Color(0xFFF5F9FC)
private val TextGray = Color(0xFF60758A)
private val Green = Color(0xFF2E7D32)
private val GreenSoft = Color(0xFFE8F5E9)
private val Red = Color(0xFFC62828)
private val RedSoft = Color(0xFFFFEBEE)
private val BorderSoft = Color(0xFFE3EBF2)

// ============================================================
// UI MODEL
// ============================================================

private data class MonitoringItemUi(
    val id: String,
    val dateTime: String,
    val status: String,
    val doNumber: String,
    val plateNumber: String,
    val driverName: String,
    val fuelProduct: String,
    val doVolumeLiter: Double,
    val location: String,
    val storageTank: String,
    val flowmeter: String,
    val notes: String
)

// ============================================================
// SCREEN ENTRY
// ============================================================

@Composable
fun MonitoringScreen(
    onBack: (() -> Unit)? = null
) {
    var selectedItem by remember {
        mutableStateOf<MonitoringItemUi?>(null)
    }

    if (selectedItem == null) {
        MonitoringListScreen(
            onBack = onBack,
            onItemClick = { clickedItem ->
                selectedItem = clickedItem
            }
        )
    } else {
        MonitoringDetailScreen(
            item = selectedItem!!,
            onBack = {
                selectedItem = null
            }
        )
    }
}

// ============================================================
// LIST SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonitoringListScreen(
    onBack: (() -> Unit)?,
    onItemClick: (MonitoringItemUi) -> Unit
) {

    val context = LocalContext.current

    var deliveryData by remember {
        mutableStateOf<List<BackendApi.DeliveryItem>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var selectedFilter by remember {
        mutableStateOf("SEMUA")
    }

    val scope = rememberCoroutineScope()

    fun loadMonitoringData() {
        scope.launch {
            isLoading = true
            errorMessage = null

            val accessToken =
                SessionManager.getAccessToken(context)

            if (accessToken.isNullOrBlank()) {
                errorMessage =
                    "Session login tidak ditemukan."
                isLoading = false
                return@launch
            }

            val result =
                BackendApi.getDeliveries(
                    accessToken = accessToken
                )

            if (result.success) {
                deliveryData = result.data
            } else {
                errorMessage =
                    result.error
                        ?: "Gagal mengambil data penerimaan."
            }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadMonitoringData()
    }

    val filteredData =
        when (selectedFilter) {
            "DRAFT" ->
                deliveryData.filter {
                    it.status == "DRAFT"
                }

            "COMPLETED" ->
                deliveryData.filter {
                    it.status == "COMPLETED"
                }

            "CANCELLED" ->
                deliveryData.filter {
                    it.status == "CANCELLED"
                }

            else ->
                deliveryData
        }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Monitoring",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Monitoring Penerimaan BBM",
                            color = Color.White.copy(
                                alpha = 0.82f
                            ),
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(
                            onClick = onBack
                        ) {
                            Text(
                                text = "‹",
                                color = Color.White,
                                fontSize = 34.sp
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {}
                        ) {
                            Text(
                                text = "☰",
                                color = Color.White,
                                fontSize = 21.sp
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            loadMonitoringData()
                        }
                    ) {
                        Text(
                            text = "↻",
                            color = Color.White,
                            fontSize = 25.sp
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor =
                            BluePrimary
                    )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {

            // ====================================================
            // DATE CARD
            // ====================================================

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    ),
                shape =
                    RoundedCornerShape(18.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                BlueSoft,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        Text(
                            text = "▣",
                            color =
                                BluePrimary,
                            fontSize = 20.sp
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        Text(
                            text =
                                "Pilih Tanggal",
                            color =
                                TextGray,
                            fontSize = 12.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(3.dp)
                        )

                        Text(
                            text =
                                "Semua Penerimaan",
                            color =
                                BlueDark,
                            fontSize = 14.sp,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = "›",
                        color =
                            TextGray,
                        fontSize = 26.sp
                    )
                }
            }

            // ====================================================
            // STATUS FILTER
            // ====================================================

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 10.dp,
                        start = 16.dp,
                        end = 16.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                listOf(
                    "SEMUA",
                    "DRAFT",
                    "COMPLETED",
                    "CANCELLED"
                ).forEach { filter ->

                    FilterChip(
                        selected =
                            selectedFilter == filter,
                        onClick = {
                            selectedFilter =
                                filter
                        },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 10.sp
                            )
                        }
                    )
                }
            }

            // ====================================================
            // CONTENT
            // ====================================================

            when {

                isLoading -> {

                    Box(
                        modifier =
                            Modifier.fillMaxSize(),
                        contentAlignment =
                            Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color =
                                BluePrimary
                        )
                    }
                }

                errorMessage != null -> {

                    ErrorMonitoringState(
                        message =
                            errorMessage
                                ?: "Terjadi kesalahan.",
                        onRetry = {
                            loadMonitoringData()
                        }
                    )
                }

                filteredData.isEmpty() -> {

                    EmptyMonitoringState()
                }

                else -> {

                    LazyColumn(
                        modifier =
                            Modifier.fillMaxSize(),
                        verticalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        item {
                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )
                        }

                        items(
                            items = filteredData,
                            key = {
                                it.id
                            }
                        ) { delivery ->

                            val uiItem =
                                delivery.toMonitoringItemUi()

                            MonitoringCard(
                                item = uiItem,
                                onClick = {
                                    onItemClick(
                                        uiItem
                                    )
                                }
                            )
                        }

                        item {
                            Spacer(
                                modifier =
                                    Modifier.height(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ============================================================
// DELIVERY -> UI MODEL
// ============================================================

private fun BackendApi.DeliveryItem
        .toMonitoringItemUi():
        MonitoringItemUi {

    return MonitoringItemUi(
        id = id,

        dateTime =
            formatMonitoringDate(
                receivedDate
            ),

        status =
            status.ifBlank {
                "DRAFT"
            },

        doNumber =
            doNumber,

        plateNumber =
            truckPlateNumber,

        driverName =
            driverName,

        fuelProduct =
            fuelProductName.ifBlank {
                fuelProductCode
            },

        doVolumeLiter =
            doVolumeLiter,

        location =
            locationName.ifBlank {
                "PLTD Sewa Batulicin"
            },

        storageTank =
            if (storageTankCode.isBlank()) {
                storageTankName
            } else {
                "$storageTankName ($storageTankCode)"
            },

        flowmeter =
            flowmeterCode,

        notes =
            notes.ifBlank {
                "-"
            }
    )
}

// ============================================================
// DATE FORMAT
// ============================================================

private fun formatMonitoringDate(
    value: String
): String {

    if (value.isBlank()) {
        return "-"
    }

    return try {

        val input =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

        val output =
            SimpleDateFormat(
                "dd MMM yyyy",
                Locale(
                    "id",
                    "ID"
                )
            )

        val parsed =
            input.parse(
                value.take(10)
            )

        if (parsed != null) {
            output.format(parsed)
        } else {
            value
        }

    } catch (_: Exception) {
        value
    }
}

// ============================================================
// MONITORING CARD
// ============================================================

@Composable
private fun MonitoringCard(
    item: MonitoringItemUi,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
            .clickable(
                onClick = onClick
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 1.dp
            ),
        shape =
            RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        item.dateTime,
                    color =
                        TextGray,
                    fontSize = 12.sp,
                    modifier =
                        Modifier.weight(1f)
                )

                StatusBadge(
                    status =
                        item.status
                )
            }

            Spacer(
                modifier =
                    Modifier.height(11.dp)
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Penerimaan BBM",
                        color =
                            TextGray,
                        fontSize = 12.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            item.plateNumber.ifBlank {
                                "-"
                            },
                        color =
                            BlueDark,
                        fontSize = 19.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            item.fuelProduct.ifBlank {
                                "-"
                            },
                        color =
                            TextGray,
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.Medium
                    )

                    Spacer(
                        modifier =
                            Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            item.doNumber.ifBlank {
                                "-"
                            },
                        color =
                            TextGray,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )
                }

                Column(
                    horizontalAlignment =
                        Alignment.End
                ) {

                    Text(
                        text =
                            "Volume DO",
                        color =
                            TextGray,
                        fontSize = 11.sp
                    )

                    Text(
                        text =
                            formatLiter(
                                item.doVolumeLiter
                            ),
                        color =
                            BlueDark,
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text = "Liter",
                        color =
                            TextGray,
                        fontSize = 11.sp
                    )

                    Text(
                        text = "›",
                        color =
                            BluePrimary,
                        fontSize = 25.sp
                    )
                }
            }
        }
    }
}

// ============================================================
// DETAIL SCREEN
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MonitoringDetailScreen(
    item: MonitoringItemUi,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var detail by remember(item.id) {
        mutableStateOf<BackendApi.DeliveryDetailResult?>(null)
    }

    var isLoading by remember(item.id) {
        mutableStateOf(true)
    }

    var errorMessage by remember(item.id) {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(item.id) {

        isLoading = true
        errorMessage = null
        detail = null

        val accessToken =
            SessionManager.getAccessToken(context)

        if (accessToken.isNullOrBlank()) {

            errorMessage =
                "Session login tidak ditemukan."
            isLoading = false

        } else {

            val result =
                BackendApi.getDeliveryDetail(
                    accessToken = accessToken,
                    deliveryId = item.id
                )

            if (result.success) {
                detail = result
            } else {
                errorMessage =
                    result.error
                        ?: "Gagal mengambil detail penerimaan."
            }

            isLoading = false
        }
    }

    Scaffold(
        containerColor =
            Background,
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text =
                            "Detail Penerimaan",
                        color =
                            Color.White,
                        fontWeight =
                            FontWeight.Bold
                    )
                },
                navigationIcon = {

                    IconButton(
                        onClick =
                            onBack
                    ) {
                        Text(
                            text = "‹",
                            color =
                                Color.White,
                            fontSize = 34.sp
                        )
                    }
                },
                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                BluePrimary
                        )
            )
        }
    ) { paddingValues ->

        when {

            isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Background
                        )
                        .padding(
                            paddingValues
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        CircularProgressIndicator(
                            color =
                                BluePrimary
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    12.dp
                                )
                        )

                        Text(
                            text =
                                "Memuat detail penerimaan...",
                            color =
                                TextGray,
                            fontSize =
                                12.sp
                        )
                    }
                }
            }

            errorMessage != null -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Background
                        )
                        .padding(
                            paddingValues
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(
                        modifier =
                            Modifier.padding(
                                24.dp
                            ),
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text =
                                "Gagal memuat detail",
                            color =
                                BlueDark,
                            fontSize =
                                18.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )

                        Text(
                            text =
                                errorMessage
                                    ?: "Terjadi kesalahan.",
                            color =
                                TextGray,
                            fontSize =
                                12.sp
                        )
                    }
                }
            }

            detail != null -> {

                MonitoringDetailContent(
                    item = item,
                    detail = detail!!,
                    paddingValues =
                        paddingValues
                )
            }

            else -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            paddingValues
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "Detail tidak tersedia.",
                        color =
                            TextGray
                    )
                }
            }
        }
    }
}

// ============================================================
// DETAIL CONTENT
// ============================================================

@Composable
private fun MonitoringDetailContent(
    item: MonitoringItemUi,
    detail: BackendApi.DeliveryDetailResult,
    paddingValues: androidx.compose.foundation.layout.PaddingValues
) {

    val delivery =
        detail.delivery

    val measurements =
        detail.measurements

    val seals =
        detail.seals

    val photos =
        detail.photos

    val actualItem =
        delivery ?: run {
            null
        }

    val shownDoNumber =
        actualItem?.doNumber
            ?.ifBlank { item.doNumber }
            ?: item.doNumber

    val shownPlate =
        actualItem?.truckPlateNumber
            ?.ifBlank { item.plateNumber }
            ?: item.plateNumber

    val shownDriver =
        actualItem?.driverName
            ?.ifBlank { item.driverName }
            ?: item.driverName

    val shownFuel =
        actualItem?.fuelProductName
            ?.ifBlank {
                actualItem.fuelProductCode
            }
            ?.ifBlank {
                item.fuelProduct
            }
            ?: item.fuelProduct

    val shownVolume =
        actualItem?.doVolumeLiter
            ?: item.doVolumeLiter

    val shownLocation =
        actualItem?.locationName
            ?.ifBlank {
                item.location
            }
            ?: item.location

    val shownStorage =
        actualItem?.let {
            if (it.storageTankCode.isBlank()) {
                it.storageTankName
            } else {
                "${it.storageTankName} (${it.storageTankCode})"
            }
        } ?: item.storageTank

    val shownFlowmeter =
        actualItem?.flowmeterCode
            ?.ifBlank {
                item.flowmeter
            }
            ?: item.flowmeter

    val shownStatus =
        actualItem?.status
            ?.ifBlank {
                item.status
            }
            ?: item.status

    val shownNotes =
        actualItem?.notes
            ?.ifBlank {
                item.notes
            }
            ?: item.notes

    val frontSeal =
        seals.firstOrNull {
            it.sealType == "MANHOLE_FRONT"
        }

    val backSeal =
        seals.firstOrNull {
            it.sealType == "MANHOLE_BACK"
        }

    val frontValveSeal =
        seals.firstOrNull {
            it.sealType == "VALVE_FRONT"
        }

    val backValveSeal =
        seals.firstOrNull {
            it.sealType == "VALVE_BACK"
        }

    val photoDefinitions =
        listOf(
            "sonding_awal" to "1. Sonding Awal",
            "truck" to "2. Truck Tangki",
            "do" to "3. Dokumen DO",
            "segel_manhole_depan" to "4. Manhole Depan",
            "segel_manhole_belakang" to "5. Manhole Belakang",
            "segel_valve_depan" to "6. Valve Depan",
            "segel_valve_belakang" to "7. Valve Belakang",
            "sonding_depan" to "8. Sonding Depan",
            "kadar_air_depan" to "9. Kadar Air Depan",
            "density_depan" to "10. Density Depan",
            "sonding_belakang" to "11. Sonding Belakang",
            "kadar_air_belakang" to "12. Kadar Air Belakang",
            "density_belakang" to "13. Density Belakang",
            "flowmeter_awal" to "14. Flowmeter Awal",
            "flowmeter_akhir" to "15. Flowmeter Akhir",
            "sonding_akhir" to "16. Sonding Akhir"
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Background
            )
            .padding(
                paddingValues
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                bottom = 24.dp
            )
    ) {

        // ========================================================
        // SUMMARY
        // ========================================================

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp
                ),
            shape =
                RoundedCornerShape(
                    20.dp
                ),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Color.White
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(
                        18.dp
                    )
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    StatusBadge(
                        status =
                            shownStatus
                    )

                    Spacer(
                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    Text(
                        text =
                            formatMonitoringDate(
                                actualItem?.receivedDate
                                    ?: item.dateTime
                            ),
                        color =
                            TextGray,
                        fontSize =
                            12.sp
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            14.dp
                        )
                )

                Text(
                    text =
                        shownDoNumber.ifBlank {
                            "-"
                        },
                    color =
                        BlueDark,
                    fontSize =
                        23.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            16.dp
                        )
                )

                DetailSummaryRow(
                    "No Polisi",
                    shownPlate
                )

                DetailSummaryRow(
                    "Nama Driver",
                    shownDriver
                )

                DetailSummaryRow(
                    "Produk BBM",
                    shownFuel
                )

                DetailSummaryRow(
                    "Volume DO",
                    "${formatLiter(shownVolume)} Liter"
                )

                DetailSummaryRow(
                    "Lokasi",
                    shownLocation
                )

                DetailSummaryRow(
                    "Storage Tank",
                    shownStorage
                )

                DetailSummaryRow(
                    "Flowmeter",
                    shownFlowmeter
                )
            }
        }

        // ========================================================
        // INFORMASI UMUM
        // ========================================================

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        DetailSectionCard(
            title =
                "Informasi Umum",
            icon = "▣"
        ) {

            DetailInfoRow(
                "Nomor DO",
                shownDoNumber
            )

            DetailInfoRow(
                "Tanggal DO",
                formatMonitoringDate(
                    actualItem?.doDate
                        ?: "-"
                )
            )

            DetailInfoRow(
                "Tanggal Penerimaan",
                formatMonitoringDate(
                    actualItem?.receivedDate
                        ?: "-"
                )
            )

            DetailInfoRow(
                "No Polisi",
                shownPlate
            )

            DetailInfoRow(
                "Nama Driver",
                shownDriver
            )

            DetailInfoRow(
                "Jenis BBM",
                shownFuel
            )

            DetailInfoRow(
                "Volume DO",
                "${formatLiter(shownVolume)} Liter"
            )

            DetailInfoRow(
                "Lokasi",
                shownLocation
            )

            DetailInfoRow(
                "Storage Tank",
                shownStorage
            )

            DetailInfoRow(
                "Flowmeter",
                shownFlowmeter
            )

            DetailInfoRow(
                "Status",
                shownStatus
            )

            DetailInfoRow(
                "Catatan",
                shownNotes
            )
        }

        // ========================================================
        // PENGUKURAN
        // ========================================================

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        DetailSectionCard(
            title =
                "Hasil Pengukuran",
            icon = "⌁"
        ) {

            if (measurements == null) {

                EmptyDetailData(
                    text =
                        "Data pengukuran belum tersedia."
                )

            } else {

                MeasurementPreviewRow(
                    "Storage Tank (Sebelum)",
                    "Tinggi Sounding",
                    formatCm(
                        measurements
                            .storageSoundingBeforeCm
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Depan)",
                    "Sounding",
                    formatCm(
                        measurements
                            .truckFrontSoundingCm
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Depan)",
                    "Kadar Air",
                    formatCm(
                        measurements
                            .truckFrontWaterCm
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Depan)",
                    "Density",
                    formatDensity(
                        measurements
                            .truckFrontDensity
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Depan)",
                    "Temperature",
                    formatTemperature(
                        measurements
                            .truckFrontTemperatureC
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Belakang)",
                    "Sounding",
                    formatCm(
                        measurements
                            .truckBackSoundingCm
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Belakang)",
                    "Kadar Air",
                    formatCm(
                        measurements
                            .truckBackWaterCm
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Belakang)",
                    "Density",
                    formatDensity(
                        measurements
                            .truckBackDensity
                    )
                )

                MeasurementPreviewRow(
                    "Mobil Tangki (Belakang)",
                    "Temperature",
                    formatTemperature(
                        measurements
                            .truckBackTemperatureC
                    )
                )

                MeasurementPreviewRow(
                    "Flowmeter",
                    "Angka Awal",
                    formatNumber(
                        measurements
                            .flowmeterInitial
                    )
                )

                MeasurementPreviewRow(
                    "Flowmeter",
                    "Angka Akhir",
                    formatNumber(
                        measurements
                            .flowmeterFinal
                    )
                )

                val flowmeterDifference =
                    if (
                        measurements.flowmeterInitial != null &&
                        measurements.flowmeterFinal != null
                    ) {
                        measurements.flowmeterFinal -
                                measurements.flowmeterInitial
                    } else {
                        null
                    }

                MeasurementPreviewRow(
                    "Flowmeter",
                    "Selisih",
                    formatNumber(
                        flowmeterDifference
                    )
                )

                MeasurementPreviewRow(
                    "Storage Tank (Sesudah)",
                    "Tinggi Sounding",
                    formatCm(
                        measurements
                            .storageSoundingAfterCm
                    )
                )
            }
        }

        // ========================================================
        // SEGEL
        // ========================================================

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        DetailSectionCard(
            title = "Segel",
            icon = "◈"
        ) {

            SealPreviewRow(
                name = "Manhole Depan",
                seal = frontSeal
            )

            SealPreviewRow(
                name = "Manhole Belakang",
                seal = backSeal
            )

            SealPreviewRow(
                name = "Valve Depan",
                seal = frontValveSeal
            )

            SealPreviewRow(
                name = "Valve Belakang",
                seal = backValveSeal
            )
        }

        // ========================================================
        // FOTO
        // ========================================================

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        DetailSectionCard(
            title =
                "Foto Dokumentasi",
            icon = "▣"
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(
                    text =
                        "${photos.size} / 16 dokumentasi tersedia",
                    color =
                        BlueDark,
                    fontSize =
                        13.sp,
                    fontWeight =
                        FontWeight.Bold,
                    modifier =
                        Modifier.weight(1f)
                )

                Text(
                    text =
                        if (photos.size == 16) {
                            "Lengkap"
                        } else {
                            "Belum lengkap"
                        },
                    color =
                        if (photos.size == 16) {
                            Green
                        } else {
                            TextGray
                        },
                    fontSize =
                        11.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            for (index in photoDefinitions.indices step 2) {

                val first =
                    photoDefinitions[index]

                val second =
                    photoDefinitions.getOrNull(
                        index + 1
                    )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    DocumentationPhotoCard(
                        title =
                            first.second,
                        photo =
                            photos.firstOrNull {
                                it.documentationType
                                    .equals(
                                        first.first,
                                        ignoreCase = true
                                    )
                            },
                        modifier =
                            Modifier.weight(1f)
                    )

                    if (second != null) {

                        DocumentationPhotoCard(
                            title =
                                second.second,
                            photo =
                                photos.firstOrNull {
                                    it.documentationType
                                        .equals(
                                            second.first,
                                            ignoreCase = true
                                        )
                                },
                            modifier =
                                Modifier.weight(1f)
                        )

                    } else {

                        Spacer(
                            modifier =
                                Modifier.weight(1f)
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )
            }
        }
    }
}

// ============================================================
// STATUS
// ============================================================

@Composable
private fun StatusBadge(
    status: String
) {

    val normalized =
        status.uppercase()

    val background =
        when (normalized) {
            "COMPLETED" ->
                GreenSoft

            "CANCELLED" ->
                RedSoft

            else ->
                BlueSoft
        }

    val textColor =
        when (normalized) {
            "COMPLETED" ->
                Green

            "CANCELLED" ->
                Red

            else ->
                BluePrimary
        }

    Box(
        modifier = Modifier
            .background(
                background,
                RoundedCornerShape(50)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {

        Text(
            text =
                normalized,
            color =
                textColor,
            fontSize =
                10.sp,
            fontWeight =
                FontWeight.Bold
        )
    }
}

// ============================================================
// DETAIL HELPERS
// ============================================================

@Composable
private fun DetailSummaryRow(
    label: String,
    value: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 5.dp
            )
    ) {

        Text(
            text = label,
            color =
                TextGray,
            fontSize = 12.sp,
            modifier =
                Modifier.width(
                    120.dp
                )
        )

        Text(
            text =
                value.ifBlank {
                    "-"
                },
            color =
                Color(0xFF23313F),
            fontSize = 13.sp,
            fontWeight =
                FontWeight.SemiBold,
            modifier =
                Modifier.weight(
                    1f
                )
        )
    }
}

@Composable
private fun DetailSectionCard(
    title: String,
    icon: String,
    content: @Composable () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            ),
        shape =
            RoundedCornerShape(
                18.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Row(
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            BlueSoft,
                            RoundedCornerShape(
                                11.dp
                            )
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = icon,
                        color =
                            BluePrimary,
                        fontSize = 18.sp
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            10.dp
                        )
                )

                Text(
                    text =
                        title,
                    color =
                        BlueDark,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            content()
        }
    }
}

@Composable
private fun DetailInfoRow(
    label: String,
    value: String
) {

    Column(
        modifier =
            Modifier.padding(
                vertical = 5.dp
            )
    ) {

        Text(
            text =
                label,
            color =
                TextGray,
            fontSize = 11.sp
        )

        Spacer(
            modifier =
                Modifier.height(
                    2.dp
                )
        )

        Text(
            text =
                value.ifBlank {
                    "-"
                },
            color =
                Color(0xFF273746),
            fontSize = 13.sp,
            fontWeight =
                FontWeight.Medium
        )

        Spacer(
            modifier =
                Modifier.height(
                    5.dp
                )
        )
    }
}

@Composable
private fun MeasurementPreviewRow(
    group: String,
    label: String,
    value: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                BlueSoft,
                RoundedCornerShape(
                    12.dp
                )
            )
            .padding(12.dp)
    ) {

        Text(
            text =
                group,
            color =
                BlueDark,
            fontSize =
                12.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(
                    4.dp
                )
        )

        Text(
            text =
                label,
            color =
                TextGray,
            fontSize =
                11.sp
        )

        Spacer(
            modifier =
                Modifier.height(
                    2.dp
                )
        )

        Text(
            text =
                value,
            color =
                Color(0xFF273746),
            fontSize =
                12.sp,
            fontWeight =
                FontWeight.SemiBold
        )
    }

    Spacer(
        modifier =
            Modifier.height(
                8.dp
            )
    )
}

@Composable
private fun EmptyDetailData(
    text: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFF7FAFC),
                RoundedCornerShape(12.dp)
            )
            .padding(
                16.dp
            ),
        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text =
                text,
            color =
                TextGray,
            fontSize =
                12.sp
        )
    }
}

// ============================================================
// SEAL
// ============================================================

@Composable
private fun SealPreviewRow(
    name: String,
    seal: BackendApi.DeliveryDetailSeal?
) {

    val condition =
        seal?.condition
            ?.ifBlank {
                null
            }

    val sealNumber =
        seal?.sealNumber
            ?.ifBlank {
                null
            }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 6.dp
            )
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        Color(0xFFF2F6FA),
                        RoundedCornerShape(
                            10.dp
                        )
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "◈",
                    color =
                        BluePrimary,
                    fontSize = 18.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.width(
                        10.dp
                    )
            )

            Column(
                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(
                    text =
                        name,
                    color =
                        Color(0xFF273746),
                    fontSize =
                        13.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Text(
                    text =
                        if (sealNumber != null) {
                            "Nomor: $sealNumber"
                        } else {
                            "Data segel belum tersedia"
                        },
                    color =
                        TextGray,
                    fontSize =
                        11.sp
                )
            }

            Text(
                text =
                    condition
                        ?: if (seal == null) {
                            "-"
                        } else {
                            "BAIK"
                        },
                color =
                    when {
                        seal == null ->
                            TextGray

                        condition.equals(
                            "BAIK",
                            ignoreCase = true
                        ) ->
                            Green

                        else ->
                            Red
                    },
                fontSize =
                    10.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }

        if (!seal?.notes.isNullOrBlank()) {

            Spacer(
                modifier =
                    Modifier.height(
                        3.dp
                    )
            )

            Text(
                text =
                    "Catatan: ${seal?.notes}",
                color =
                    TextGray,
                fontSize =
                    10.sp
            )
        }
    }
}

// ============================================================
// DOCUMENTATION PHOTO CARD
// ============================================================

@Composable
private fun DocumentationPhotoCard(
    title: String,
    photo: BackendApi.DeliveryDetailPhoto?,
    modifier: Modifier = Modifier
) {

    Card(
        modifier =
            modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                14.dp
            ),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            )
    ) {

        Column {

            if (
                photo != null &&
                !photo.signedUrl.isNullOrBlank()
            ) {

                RemotePhoto(
                    url =
                        photo.signedUrl!!,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                1.25f
                            )
                )

            } else {

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                1.25f
                            )
                            .background(
                                Color(0xFFF2F6FA)
                            ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "▣",
                            color =
                                TextGray,
                            fontSize =
                                28.sp
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    4.dp
                                )
                        )

                        Text(
                            text =
                                "Belum ada foto",
                            color =
                                TextGray,
                            fontSize =
                                10.sp
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier.padding(
                        9.dp
                    )
            ) {

                Text(
                    text =
                        title,
                    color =
                        BlueDark,
                    fontSize =
                        11.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                if (photo != null) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            "GPS: ${formatCoordinate(photo.latitude)}, ${formatCoordinate(photo.longitude)}",
                        color =
                            TextGray,
                        fontSize =
                            9.sp
                    )
                }
            }
        }
    }
}

// ============================================================
// REMOTE PHOTO
// ============================================================

@Composable
private fun RemotePhoto(
    url: String,
    modifier: Modifier
) {

    var bitmap by remember(url) {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    var isLoading by remember(url) {
        mutableStateOf(true)
    }

    var hasError by remember(url) {
        mutableStateOf(false)
    }

    LaunchedEffect(url) {

        isLoading = true
        hasError = false
        bitmap = null

        try {

            val result =
                withContext(
                    Dispatchers.IO
                ) {

                    URL(url)
                        .openConnection()
                        .apply {
                            connectTimeout =
                                15_000
                            readTimeout =
                                20_000
                        }
                        .getInputStream()
                        .use {
                            BitmapFactory.decodeStream(it)
                        }
                }

            if (result != null) {
                bitmap = result
            } else {
                hasError = true
            }

        } catch (_: Exception) {

            hasError = true

        } finally {

            isLoading = false
        }
    }

    Box(
        modifier =
            modifier
                .background(
                    Color(0xFFF2F6FA)
                ),
        contentAlignment =
            Alignment.Center
    ) {

        when {

            isLoading -> {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(
                            28.dp
                        ),
                    color =
                        BluePrimary,
                    strokeWidth =
                        2.dp
                )
            }

            bitmap != null -> {

                Image(
                    bitmap =
                        bitmap!!
                            .asImageBitmap(),
                    contentDescription =
                        null,
                    modifier =
                        Modifier.fillMaxSize(),
                    contentScale =
                        ContentScale.Crop
                )
            }

            hasError -> {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "⚠",
                        color =
                            Red,
                        fontSize =
                            24.sp
                    )

                    Text(
                        text =
                            "Foto gagal dimuat",
                        color =
                            TextGray,
                        fontSize =
                            9.sp
                    )
                }
            }
        }
    }
}

// ============================================================
// EMPTY / ERROR
// ============================================================

@Composable
private fun EmptyMonitoringState() {

    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "Belum ada data",
                color =
                    BlueDark,
                fontSize =
                    18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    "Data penerimaan akan tampil di sini.",
                color =
                    TextGray,
                fontSize =
                    12.sp
            )
        }
    }
}

@Composable
private fun ErrorMonitoringState(
    message: String,
    onRetry: () -> Unit
) {

    Box(
        modifier =
            Modifier.fillMaxSize(),
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            modifier =
                Modifier.padding(
                    24.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Text(
                text =
                    "Gagal memuat data",
                color =
                    BlueDark,
                fontSize =
                    18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Text(
                text =
                    message,
                color =
                    TextGray,
                fontSize =
                    12.sp
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            TextButton(
                onClick = onRetry
            ) {

                Text(
                    text =
                        "Coba Lagi",
                    color =
                        BluePrimary
                )
            }
        }
    }
}

// ============================================================
// FORMAT FUNCTIONS
// ============================================================

private fun formatLiter(
    value: Double
): String {

    val formatter =
        NumberFormat.getNumberInstance(
            Locale(
                "id",
                "ID"
            )
        )

    formatter.minimumFractionDigits =
        0

    formatter.maximumFractionDigits =
        0

    return formatter.format(
        value
    )
}

private fun formatNumber(
    value: Double?
): String {

    if (value == null) {
        return "-"
    }

    val formatter =
        NumberFormat.getNumberInstance(
            Locale(
                "id",
                "ID"
            )
        )

    formatter.minimumFractionDigits =
        0

    formatter.maximumFractionDigits =
        2

    return formatter.format(
        value
    )
}

private fun formatCm(
    value: Double?
): String {

    if (value == null) {
        return "-"
    }

    val formatter =
        NumberFormat.getNumberInstance(
            Locale(
                "id",
                "ID"
            )
        )

    formatter.minimumFractionDigits =
        1

    formatter.maximumFractionDigits =
        1

    return "${formatter.format(value)} cm"
}

private fun formatDensity(
    value: Double?
): String {

    if (value == null) {
        return "-"
    }

    return String.format(
        Locale.US,
        "%.3f",
        value
    )
}

private fun formatTemperature(
    value: Double?
): String {

    if (value == null) {
        return "-"
    }

    return String.format(
        Locale.US,
        "%.1f °C",
        value
    )
}

private fun formatCoordinate(
    value: Double?
): String {

    if (value == null) {
        return "-"
    }

    return String.format(
        Locale.US,
        "%.6f",
        value
    )
}

// ============================================================
// PREVIEW
// ============================================================

@Composable
private fun MonitoringPreview() {

    Surface(
        modifier =
            Modifier.fillMaxSize(),
        color =
            Background
    ) {
        MonitoringScreen()
    }
}