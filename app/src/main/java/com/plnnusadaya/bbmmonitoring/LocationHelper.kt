package com.plnnusadaya.bbmmonitoring

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onLocationResult: (android.location.Location?) -> Unit
    ) {

        val finePermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        val coarsePermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        if (
            finePermission !=
            PackageManager.PERMISSION_GRANTED &&
            coarsePermission !=
            PackageManager.PERMISSION_GRANTED
        ) {

            onLocationResult(null)

            return
        }

        val fusedLocationClient =
            LocationServices
                .getFusedLocationProviderClient(
                    context
                )

        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            )
            .addOnSuccessListener { location ->

                onLocationResult(
                    location
                )
            }
            .addOnFailureListener {

                onLocationResult(
                    null
                )
            }
    }
}