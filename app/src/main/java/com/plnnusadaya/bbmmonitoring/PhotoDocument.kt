package com.plnnusadaya.bbmmonitoring

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.location.Location
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object PhotoDocument {

    fun createWatermarkedBitmap(
        original: Bitmap,
        location: Location?,
        lokasiPenerimaan: String,
        jenisDokumentasi: String
    ): Bitmap {

        val result =
            original.copy(
                Bitmap.Config.ARGB_8888,
                true
            )

        val canvas =
            Canvas(result)

        val width =
            result.width.toFloat()

        val height =
            result.height.toFloat()

        val paint =
            Paint(Paint.ANTI_ALIAS_FLAG)

        // =====================================================
        // PANEL WATERMARK
        // =====================================================

        val panelHeight =
            height * 0.20f

        paint.color =
            android.graphics.Color.argb(
                220,
                0,
                0,
                0
            )

        canvas.drawRect(
            0f,
            height - panelHeight,
            width,
            height,
            paint
        )

        // =====================================================
        // FORMAT WITA
        // =====================================================

        val formatter =
            SimpleDateFormat(
                "dd MMMM yyyy • HH:mm:ss 'WITA'",
                Locale("id", "ID")
            )

        formatter.timeZone =
            TimeZone.getTimeZone(
                "Asia/Makassar"
            )

        val tanggalJam =
            formatter.format(
                Date()
            )

        // =====================================================
        // TEXT
        // =====================================================

        paint.color =
            android.graphics.Color.WHITE

        paint.typeface =
            Typeface.create(
                Typeface.DEFAULT,
                Typeface.BOLD
            )

        paint.textSize =
            width * 0.035f

        val left =
            width * 0.035f

        var y =
            height - panelHeight +
                    width * 0.045f

        canvas.drawText(
            "BBM MONITORING",
            left,
            y,
            paint
        )

        y +=
            width * 0.045f

        paint.typeface =
            Typeface.DEFAULT

        canvas.drawText(
            lokasiPenerimaan,
            left,
            y,
            paint
        )

        y +=
            width * 0.04f

        canvas.drawText(
            jenisDokumentasi,
            left,
            y,
            paint
        )

        y +=
            width * 0.04f

        canvas.drawText(
            tanggalJam,
            left,
            y,
            paint
        )

        y +=
            width * 0.04f

        // =====================================================
        // GPS
        // =====================================================

        val gpsText =

            if (location != null) {

                String.format(
                    Locale.US,
                    "GPS: %.6f, %.6f",
                    location.latitude,
                    location.longitude
                )

            } else {

                "GPS: Lokasi tidak tersedia"
            }

        canvas.drawText(
            gpsText,
            left,
            y,
            paint
        )

        return result
    }


    fun saveBitmap(
        context: Context,
        bitmap: Bitmap,
        jenisDokumentasi: String
    ): File {

        val directory =
            File(
                context.filesDir,
                "dokumentasi_bbm"
            )

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val timestamp =
            SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.US
            ).format(
                Date()
            )

        val safeName =
            jenisDokumentasi
                .replace(
                    " ",
                    "_"
                )
                .replace(
                    "/",
                    "_"
                )

        val file =
            File(
                directory,
                "${safeName}_${timestamp}.jpg"
            )

        FileOutputStream(file).use {
                output ->

            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                92,
                output
            )
        }

        return file
    }
}