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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// ============================================================
// DATA SEMENTARA
// Nanti data ini akan berasal dari Supabase
// ============================================================

data class PenerimaanBBM(
    val lokasi: String,
    val nomorDO: String,
    val volume: String,
    val tanggal: String,
    val status: String
)


// ============================================================
// PENERIMAAN SCREEN
// ============================================================

@Composable
fun PenerimaanScreen(
    onBack: () -> Unit = {},
    onPenerimaanBaru: () -> Unit = {}
) {

    val blueDark = Color(0xFF004A87)
    val bluePrimary = Color(0xFF087AC1)
    val blueLight = Color(0xFFEAF5FF)
    val textGray = Color(0xFF60758A)

    // Data dummy untuk sementara
    val dataPenerimaan = listOf(

        PenerimaanBBM(
            lokasi = "PLTD Batulicin",
            nomorDO = "DO-2026-00125",
            volume = "20.000 Liter",
            tanggal = "15 Sep 2026",
            status = "Normal"
        ),

        PenerimaanBBM(
            lokasi = "PLTD Batulicin",
            nomorDO = "DO-2026-00124",
            volume = "15.000 Liter",
            tanggal = "14 Sep 2026",
            status = "Normal"
        ),

        PenerimaanBBM(
            lokasi = "PLTD Batulicin",
            nomorDO = "DO-2026-00123",
            volume = "20.000 Liter",
            tanggal = "13 Sep 2026",
            status = "Perlu Dicek"
        ),

        PenerimaanBBM(
            lokasi = "PLTD Batulicin",
            nomorDO = "DO-2026-00122",
            volume = "18.000 Liter",
            tanggal = "12 Sep 2026",
            status = "Normal"
        )
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // ====================================================
        // HEADER
        // ====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack
            ) {

                Text(
                    text = "←",
                    fontSize = 28.sp,
                    color = blueDark
                )
            }

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Penerimaan BBM",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueDark
                )

                Text(
                    text = "Monitoring penerimaan bahan bakar",
                    fontSize = 12.sp,
                    color = textGray
                )
            }
        }


        // ====================================================
        // BUTTON PENERIMAAN BARU
        // ====================================================

        Button(
            onClick = onPenerimaanBaru,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = bluePrimary
            )
        ) {

            Text(
                text = "+",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Penerimaan Baru",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ====================================================
        // RINGKASAN
        // ====================================================

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SummaryCard(
                title = "Total",
                value = "24",
                modifier = Modifier.weight(1f),
                backgroundColor = blueLight,
                valueColor = blueDark
            )

            SummaryCard(
                title = "Normal",
                value = "21",
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFEFF9F2),
                valueColor = Color(0xFF16834B)
            )

            SummaryCard(
                title = "Anomali",
                value = "3",
                modifier = Modifier.weight(1f),
                backgroundColor = Color(0xFFFFF4E8),
                valueColor = Color(0xFFE47A16)
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ====================================================
        // JUDUL LIST
        // ====================================================

        Text(
            text = "Riwayat Penerimaan",
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = blueDark
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ====================================================
        // LIST PENERIMAAN
        // ====================================================

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(dataPenerimaan) { item ->

                PenerimaanCard(
                    data = item
                )
            }
        }
    }
}


// ============================================================
// SUMMARY CARD
// ============================================================

@Composable
fun SummaryCard(
    title: String,
    value: String,
    modifier: Modifier,
    backgroundColor: Color,
    valueColor: Color
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            Text(
                text = title,
                fontSize = 11.sp,
                color = Color(0xFF60758A)
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor
            )
        }
    }
}


// ============================================================
// CARD PENERIMAAN
// ============================================================

@Composable
fun PenerimaanCard(
    data: PenerimaanBBM
) {

    val blueDark = Color(0xFF004A87)
    val textGray = Color(0xFF60758A)

    val statusColor =
        if (data.status == "Normal") {
            Color(0xFF16834B)
        } else {
            Color(0xFFE47A16)
        }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FBFD)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ICON BBM

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = Color(0xFFEAF5FF),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "⛽",
                    fontSize = 21.sp
                )
            }


            Spacer(
                modifier = Modifier.width(12.dp)
            )


            // INFORMASI

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = data.lokasi,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = blueDark
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = data.nomorDO,
                    fontSize = 11.sp,
                    color = textGray
                )

                Text(
                    text = data.volume,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF34495E)
                )

                Text(
                    text = data.tanggal,
                    fontSize = 10.sp,
                    color = textGray
                )
            }


            // STATUS

            Text(
                text = data.status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }
    }
}