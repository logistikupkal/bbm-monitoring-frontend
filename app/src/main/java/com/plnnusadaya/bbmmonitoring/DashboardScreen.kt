package com.plnnusadaya.bbmmonitoring

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen() {

    val blueDark = Color(0xFF004A87)
    val bluePrimary = Color(0xFF087AC1)
    val blueLight = Color(0xFFEAF5FF)
    val textGray = Color(0xFF60758A)

    /*
     * ============================================================
     * MENU STATE
     *
     * 0 = Home
     * 1 = Penerimaan
     * 2 = Monitoring
     * 3 = Penerimaan Baru
     * ============================================================
     */

    var selectedMenu by remember {
        mutableIntStateOf(0)
    }

    Scaffold(

        bottomBar = {

            /*
             * Bottom Navigation hanya digunakan untuk:
             * Home
             * Penerimaan
             * Monitoring
             *
             * Penerimaan Baru bukan menu bottom navigation.
             */

            NavigationBar {

                // =================================================
                // HOME
                // =================================================

                NavigationBarItem(
                    selected = selectedMenu == 0,

                    onClick = {
                        selectedMenu = 0
                    },

                    icon = {
                        Text(
                            text = "⌂",
                            fontSize = 22.sp
                        )
                    },

                    label = {
                        Text("Home")
                    }
                )

                // =================================================
                // PENERIMAAN
                // =================================================

                NavigationBarItem(
                    selected =
                        selectedMenu == 1 ||
                                selectedMenu == 3,

                    onClick = {
                        selectedMenu = 1
                    },

                    icon = {
                        Text(
                            text = "⛽",
                            fontSize = 20.sp
                        )
                    },

                    label = {
                        Text("Penerimaan")
                    }
                )

                // =================================================
                // MONITORING
                // =================================================

                NavigationBarItem(
                    selected = selectedMenu == 2,

                    onClick = {
                        selectedMenu = 2
                    },

                    icon = {
                        Text(
                            text = "▥",
                            fontSize = 20.sp
                        )
                    },

                    label = {
                        Text("Monitoring")
                    }
                )
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {

            // ====================================================
            // HOME
            // ====================================================

            if (selectedMenu == 0) {

                /*
                 * =================================================
                 * HOME / OVERVIEW
                 * =================================================
                 */

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp)
                ) {

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    // =================================================
                    // HEADER
                    // =================================================

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column {

                            Text(
                                text = "BBM Monitoring",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = blueDark
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Selamat datang, Admin",
                                fontSize = 13.sp,
                                color = textGray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = blueLight,
                                    shape = RoundedCornerShape(14.dp)
                                ),

                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text = "A",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = bluePrimary
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // =================================================
                    // OVERVIEW
                    // =================================================

                    Text(
                        text = "Overview",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDark
                    )

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    // =================================================
                    // KPI ROW 1
                    // =================================================

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        DashboardCard(
                            title = "Penerimaan",
                            value = "24",
                            subtitle = "Transaksi",

                            modifier =
                                Modifier.weight(1f),

                            backgroundColor =
                                blueLight,

                            valueColor =
                                blueDark
                        )

                        DashboardCard(
                            title = "Volume BBM",
                            value = "125.430",
                            subtitle = "Liter",

                            modifier =
                                Modifier.weight(1f),

                            backgroundColor =
                                Color(0xFFF3F7FA),

                            valueColor =
                                blueDark
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    // =================================================
                    // KPI ROW 2
                    // =================================================

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        DashboardCard(
                            title = "Normal",
                            value = "21",
                            subtitle = "Transaksi",

                            modifier =
                                Modifier.weight(1f),

                            backgroundColor =
                                Color(0xFFEFF9F2),

                            valueColor =
                                Color(0xFF16834B)
                        )

                        DashboardCard(
                            title = "Anomali",
                            value = "3",
                            subtitle = "Perlu dicek",

                            modifier =
                                Modifier.weight(1f),

                            backgroundColor =
                                Color(0xFFFFF4E8),

                            valueColor =
                                Color(0xFFE47A16)
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // =================================================
                    // CHART
                    // =================================================

                    Text(
                        text = "Penerimaan BBM",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDark
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Card(
                        modifier =
                            Modifier.fillMaxWidth(),

                        shape =
                            RoundedCornerShape(20.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color(0xFFF8FBFD)
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {

                            Text(
                                text = "Total bulan ini",
                                fontSize = 13.sp,
                                color = textGray
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "125.430 Liter",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = blueDark
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(20.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),

                                horizontalArrangement =
                                    Arrangement.SpaceEvenly,

                                verticalAlignment =
                                    Alignment.Bottom
                            ) {

                                ChartBar(
                                    35,
                                    bluePrimary
                                )

                                ChartBar(
                                    55,
                                    bluePrimary
                                )

                                ChartBar(
                                    75,
                                    bluePrimary
                                )

                                ChartBar(
                                    45,
                                    bluePrimary
                                )

                                ChartBar(
                                    90,
                                    bluePrimary
                                )

                                ChartBar(
                                    65,
                                    bluePrimary
                                )

                                ChartBar(
                                    100,
                                    bluePrimary
                                )
                            }

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceEvenly
                            ) {

                                Text(
                                    "Sen",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Sel",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Rab",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Kam",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Jum",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Sab",
                                    fontSize = 10.sp,
                                    color = textGray
                                )

                                Text(
                                    "Min",
                                    fontSize = 10.sp,
                                    color = textGray
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(20.dp)
                    )

                    // =================================================
                    // TRANSAKSI TERBARU
                    // =================================================

                    Text(
                        text = "Transaksi Terbaru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = blueDark
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    TransactionItem(
                        location = "PLTD Batulicin",
                        volume = "20.000 Liter",
                        status = "Normal"
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    TransactionItem(
                        location = "PLTD Batulicin",
                        volume = "15.000 Liter",
                        status = "Normal"
                    )
                }
            }

            // ====================================================
            // PENERIMAAN
            // ====================================================

            else if (selectedMenu == 1) {

                PenerimaanScreen(

                    onBack = {
                        selectedMenu = 0
                    },

                    onPenerimaanBaru = {
                        selectedMenu = 3
                    }
                )
            }

            // ====================================================
            // MONITORING
            // ====================================================

            else if (selectedMenu == 2) {

                MonitoringScreen(
                    onBack = {
                        selectedMenu = 0
                    }
                )
            }

            // ====================================================
            // PENERIMAAN BARU
            // ====================================================

            else if (selectedMenu == 3) {

                PenerimaanBaruScreen(
                    onBack = {
                        selectedMenu = 1
                    }
                )
            }
        }
    }
}


/*
 * ============================================================
 * DASHBOARD CARD
 * ============================================================
 */

@Composable
fun DashboardCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    valueColor: Color
) {

    Card(
        modifier = modifier,

        shape =
            RoundedCornerShape(18.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    backgroundColor
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF60758A)
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = value,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = Color(0xFF60758A)
            )
        }
    }
}


/*
 * ============================================================
 * CHART BAR
 * ============================================================
 */

@Composable
fun ChartBar(
    height: Int,
    color: Color
) {

    Box(
        modifier = Modifier
            .width(20.dp)
            .height(height.dp)
            .background(
                color = color,

                shape =
                    RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp
                    )
            )
    )
}


/*
 * ============================================================
 * TRANSACTION ITEM
 * ============================================================
 */

@Composable
fun TransactionItem(
    location: String,
    volume: String,
    status: String
) {

    val blueDark =
        Color(0xFF004A87)

    Card(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(16.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color(0xFFF8FBFD)
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color =
                            Color(0xFFEAF5FF),

                        shape =
                            RoundedCornerShape(12.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "⛽",
                    fontSize = 18.sp
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
                    text = location,
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color = blueDark
                )

                Text(
                    text = volume,
                    fontSize = 12.sp,
                    color =
                        Color(0xFF60758A)
                )
            }

            Text(
                text = status,
                fontSize = 11.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    Color(0xFF16834B)
            )
        }
    }
}