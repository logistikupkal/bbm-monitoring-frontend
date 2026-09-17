package com.plnnusadaya.bbmmonitoring

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object BackendApi {

    // ============================================================
    // BASE URL
    // ============================================================

    // Android Emulator -> PC host
    private const val BASE_URL = "https://bbm-monitoring-backend.vercel.app"

    // ============================================================
    // HTTP CLIENT
    // ============================================================

    private val client = OkHttpClient()

    // ============================================================
    // LOGIN
    // ============================================================

    data class LoginResult(
        val success: Boolean,
        val userId: String? = null,
        val email: String? = null,
        val accessToken: String? = null,
        val refreshToken: String? = null,
        val error: String? = null
    )

    suspend fun login(
        email: String,
        password: String
    ): LoginResult = withContext(Dispatchers.IO) {

        try {

            val json = JSONObject().apply {
                put("email", email)
                put("password", password)
            }

            val requestBody =
                json.toString()
                    .toRequestBody(
                        "application/json".toMediaType()
                    )

            val request =
                Request.Builder()
                    .url("$BASE_URL/api/login")
                    .post(requestBody)
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext LoginResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext LoginResult(
                        success = false,
                        error = result.optString(
                            "error",
                            "Login gagal (${response.code})"
                        )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext LoginResult(
                        success = false,
                        error = result.optString(
                            "error",
                            "Login gagal"
                        )
                    )
                }

                val user =
                    result.optJSONObject("user")

                val session =
                    result.optJSONObject("session")

                LoginResult(
                    success = true,
                    userId =
                        user?.optString("id"),
                    email =
                        user?.optString("email"),
                    accessToken =
                        session?.optString("access_token"),
                    refreshToken =
                        session?.optString("refresh_token")
                )
            }

        } catch (e: Exception) {

            LoginResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal terhubung ke backend"
            )
        }
    }

    // ============================================================
    // GET DELIVERIES
    // Dipakai untuk Monitoring / Riwayat
    // ============================================================

    data class DeliveryItem(
        val id: String,
        val locationId: String,
        val storageTankId: String,
        val fuelProductId: String,
        val truckPlateNumber: String,
        val driverName: String,
        val flowmeterCode: String,
        val doNumber: String,
        val doDate: String,
        val doVolumeLiter: Double,
        val receivedDate: String,
        val status: String,
        val notes: String,
        val createdBy: String,
        val createdAt: String,
        val updatedAt: String,
        val locationCode: String,
        val locationName: String,
        val locationAddress: String,
        val storageTankCode: String,
        val storageTankName: String,
        val storageTankCapacityLiter: Double,
        val fuelProductCode: String,
        val fuelProductName: String
    )

    data class DeliveriesResult(
        val success: Boolean,
        val data: List<DeliveryItem> = emptyList(),
        val error: String? = null
    )

    suspend fun getDeliveries(
        accessToken: String
    ): DeliveriesResult = withContext(Dispatchers.IO) {

        try {

            val request =
                Request.Builder()
                    .url("$BASE_URL/api/deliveries")
                    .get()
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext DeliveriesResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext DeliveriesResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengambil data penerimaan (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext DeliveriesResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengambil data penerimaan"
                            )
                    )
                }

                val dataArray =
                    result.optJSONArray("data")
                        ?: JSONArray()

                val list =
                    mutableListOf<DeliveryItem>()

                for (index in 0 until dataArray.length()) {

                    val item =
                        dataArray.optJSONObject(index)
                            ?: continue

                    val location =
                        getRelationObject(
                            item,
                            "locations"
                        )

                    val storageTank =
                        getRelationObject(
                            item,
                            "storage_tanks"
                        )

                    val fuelProduct =
                        getRelationObject(
                            item,
                            "fuel_products"
                        )

                    list.add(
                        DeliveryItem(
                            id =
                                item.optString("id"),

                            locationId =
                                item.optString("location_id"),

                            storageTankId =
                                item.optString("storage_tank_id"),

                            fuelProductId =
                                item.optString("fuel_product_id"),

                            truckPlateNumber =
                                item.optString(
                                    "truck_plate_number"
                                ),

                            driverName =
                                item.optString(
                                    "driver_name"
                                ),

                            flowmeterCode =
                                item.optString(
                                    "flowmeter_code"
                                ),

                            doNumber =
                                item.optString(
                                    "do_number"
                                ),

                            doDate =
                                item.optString(
                                    "do_date"
                                ),

                            doVolumeLiter =
                                item.optDouble(
                                    "do_volume_liter",
                                    0.0
                                ),

                            receivedDate =
                                item.optString(
                                    "received_date"
                                ),

                            status =
                                item.optString(
                                    "status",
                                    "DRAFT"
                                ),

                            notes =
                                item.optString(
                                    "notes"
                                ),

                            createdBy =
                                item.optString(
                                    "created_by"
                                ),

                            createdAt =
                                item.optString(
                                    "created_at"
                                ),

                            updatedAt =
                                item.optString(
                                    "updated_at"
                                ),

                            locationCode =
                                location.optString(
                                    "code"
                                ),

                            locationName =
                                location.optString(
                                    "name",
                                    "PLTD Sewa Batulicin"
                                ),

                            locationAddress =
                                location.optString(
                                    "address"
                                ),

                            storageTankCode =
                                storageTank.optString(
                                    "tank_code"
                                ),

                            storageTankName =
                                storageTank.optString(
                                    "tank_name",
                                    "Storage Tank 3287"
                                ),

                            storageTankCapacityLiter =
                                storageTank.optDouble(
                                    "capacity_liter",
                                    0.0
                                ),

                            fuelProductCode =
                                fuelProduct.optString(
                                    "code"
                                ),

                            fuelProductName =
                                fuelProduct.optString(
                                    "name",
                                    "B-50"
                                )
                        )
                    )
                }

                return@withContext DeliveriesResult(
                    success = true,
                    data = list
                )
            }

        } catch (e: Exception) {

            DeliveriesResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal mengambil data penerimaan"
            )
        }
    }

    private fun getRelationObject(
        parent: JSONObject,
        key: String
    ): JSONObject {

        val value =
            parent.opt(key)

        return when (value) {

            is JSONObject -> value

            is JSONArray -> {
                if (value.length() > 0) {
                    value.optJSONObject(0)
                        ?: JSONObject()
                } else {
                    JSONObject()
                }
            }

            else -> JSONObject()
        }
    }

    // ============================================================
    // GET DELIVERY DETAIL
    // Dipakai untuk Detail Monitoring
    // ============================================================

    data class DeliveryDetailMeasurements(
        val id: String,
        val deliveryId: String,
        val storageSoundingBeforeCm: Double?,
        val truckFrontSoundingCm: Double?,
        val truckFrontWaterCm: Double?,
        val truckFrontDensity: Double?,
        val truckFrontTemperatureC: Double?,
        val truckBackSoundingCm: Double?,
        val truckBackWaterCm: Double?,
        val truckBackDensity: Double?,
        val truckBackTemperatureC: Double?,
        val flowmeterInitial: Double?,
        val flowmeterFinal: Double?,
        val storageSoundingAfterCm: Double?,
        val createdAt: String,
        val updatedAt: String
    )

    data class DeliveryDetailSeal(
        val id: String,
        val deliveryId: String,
        val sealType: String,
        val sealNumber: String?,
        val condition: String?,
        val notes: String?,
        val photoId: String?,
        val createdAt: String
    )

    data class DeliveryDetailPhoto(
        val id: String,
        val deliveryId: String,
        val documentationType: String,
        val storagePath: String,
        val latitude: Double?,
        val longitude: Double?,
        val capturedAt: String?,
        val fileSizeBytes: Long?,
        val mimeType: String?,
        val createdBy: String?,
        val createdAt: String,
        val signedUrl: String?
    )

    data class DeliveryDetailResult(
        val success: Boolean,
        val delivery: DeliveryItem? = null,
        val measurements: DeliveryDetailMeasurements? = null,
        val seals: List<DeliveryDetailSeal> = emptyList(),
        val photos: List<DeliveryDetailPhoto> = emptyList(),
        val error: String? = null
    )

    suspend fun getDeliveryDetail(
        accessToken: String,
        deliveryId: String
    ): DeliveryDetailResult = withContext(Dispatchers.IO) {

        try {

            if (accessToken.isBlank()) {

                return@withContext DeliveryDetailResult(
                    success = false,
                    error = "Access token kosong"
                )
            }

            if (deliveryId.isBlank()) {

                return@withContext DeliveryDetailResult(
                    success = false,
                    error = "Delivery ID kosong"
                )
            }

            val request =
                Request.Builder()
                    .url(
                        "$BASE_URL/api/deliveries/$deliveryId"
                    )
                    .get()
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext DeliveryDetailResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext DeliveryDetailResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengambil detail penerimaan (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext DeliveryDetailResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengambil detail penerimaan"
                            )
                    )
                }

                val data =
                    result.optJSONObject("data")

                if (data == null) {

                    return@withContext DeliveryDetailResult(
                        success = false,
                        error = "Data detail penerimaan kosong"
                    )
                }

                // ====================================================
                // DELIVERY
                // ====================================================

                val deliveryJson =
                    data.optJSONObject("delivery")

                val delivery =
                    if (deliveryJson != null) {

                        val location =
                            getRelationObject(
                                deliveryJson,
                                "locations"
                            )

                        val storageTank =
                            getRelationObject(
                                deliveryJson,
                                "storage_tanks"
                            )

                        val fuelProduct =
                            getRelationObject(
                                deliveryJson,
                                "fuel_products"
                            )

                        DeliveryItem(
                            id =
                                deliveryJson.optString("id"),

                            locationId =
                                deliveryJson.optString(
                                    "location_id"
                                ),

                            storageTankId =
                                deliveryJson.optString(
                                    "storage_tank_id"
                                ),

                            fuelProductId =
                                deliveryJson.optString(
                                    "fuel_product_id"
                                ),

                            truckPlateNumber =
                                deliveryJson.optString(
                                    "truck_plate_number"
                                ),

                            driverName =
                                deliveryJson.optString(
                                    "driver_name"
                                ),

                            flowmeterCode =
                                deliveryJson.optString(
                                    "flowmeter_code"
                                ),

                            doNumber =
                                deliveryJson.optString(
                                    "do_number"
                                ),

                            doDate =
                                deliveryJson.optString(
                                    "do_date"
                                ),

                            doVolumeLiter =
                                deliveryJson.optDouble(
                                    "do_volume_liter",
                                    0.0
                                ),

                            receivedDate =
                                deliveryJson.optString(
                                    "received_date"
                                ),

                            status =
                                deliveryJson.optString(
                                    "status",
                                    "DRAFT"
                                ),

                            notes =
                                deliveryJson.optString(
                                    "notes"
                                ),

                            createdBy =
                                deliveryJson.optString(
                                    "created_by"
                                ),

                            createdAt =
                                deliveryJson.optString(
                                    "created_at"
                                ),

                            updatedAt =
                                deliveryJson.optString(
                                    "updated_at"
                                ),

                            locationCode =
                                location.optString(
                                    "code"
                                ),

                            locationName =
                                location.optString(
                                    "name",
                                    "PLTD Sewa Batulicin"
                                ),

                            locationAddress =
                                location.optString(
                                    "address"
                                ),

                            storageTankCode =
                                storageTank.optString(
                                    "tank_code"
                                ),

                            storageTankName =
                                storageTank.optString(
                                    "tank_name",
                                    "Storage Tank 3287"
                                ),

                            storageTankCapacityLiter =
                                storageTank.optDouble(
                                    "capacity_liter",
                                    0.0
                                ),

                            fuelProductCode =
                                fuelProduct.optString(
                                    "code"
                                ),

                            fuelProductName =
                                fuelProduct.optString(
                                    "name",
                                    "B-50"
                                )
                        )

                    } else {
                        null
                    }

                // ====================================================
                // MEASUREMENTS
                // ====================================================

                val measurementsJson =
                    data.optJSONObject("measurements")

                val measurements =
                    if (measurementsJson != null) {

                        DeliveryDetailMeasurements(
                            id =
                                measurementsJson.optString("id"),

                            deliveryId =
                                measurementsJson.optString(
                                    "delivery_id"
                                ),

                            storageSoundingBeforeCm =
                                nullableDouble(
                                    measurementsJson,
                                    "storage_sounding_before_cm"
                                ),

                            truckFrontSoundingCm =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_front_sounding_cm"
                                ),

                            truckFrontWaterCm =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_front_water_cm"
                                ),

                            truckFrontDensity =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_front_density"
                                ),

                            truckFrontTemperatureC =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_front_temperature_c"
                                ),

                            truckBackSoundingCm =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_back_sounding_cm"
                                ),

                            truckBackWaterCm =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_back_water_cm"
                                ),

                            truckBackDensity =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_back_density"
                                ),

                            truckBackTemperatureC =
                                nullableDouble(
                                    measurementsJson,
                                    "truck_back_temperature_c"
                                ),

                            flowmeterInitial =
                                nullableDouble(
                                    measurementsJson,
                                    "flowmeter_initial"
                                ),

                            flowmeterFinal =
                                nullableDouble(
                                    measurementsJson,
                                    "flowmeter_final"
                                ),

                            storageSoundingAfterCm =
                                nullableDouble(
                                    measurementsJson,
                                    "storage_sounding_after_cm"
                                ),

                            createdAt =
                                measurementsJson.optString(
                                    "created_at"
                                ),

                            updatedAt =
                                measurementsJson.optString(
                                    "updated_at"
                                )
                        )

                    } else {
                        null
                    }

                // ====================================================
                // SEALS
                // ====================================================

                val sealsArray =
                    data.optJSONArray("seals")
                        ?: JSONArray()

                val seals =
                    mutableListOf<DeliveryDetailSeal>()

                for (index in 0 until sealsArray.length()) {

                    val seal =
                        sealsArray.optJSONObject(index)
                            ?: continue

                    seals.add(
                        DeliveryDetailSeal(
                            id =
                                seal.optString("id"),

                            deliveryId =
                                seal.optString(
                                    "delivery_id"
                                ),

                            sealType =
                                seal.optString(
                                    "seal_type"
                                ),

                            sealNumber =
                                seal.optString(
                                    "seal_number"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            condition =
                                seal.optString(
                                    "condition"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            notes =
                                seal.optString(
                                    "notes"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            photoId =
                                seal.optString(
                                    "photo_id"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            createdAt =
                                seal.optString(
                                    "created_at"
                                )
                        )
                    )
                }

                // ====================================================
                // PHOTOS
                // ====================================================

                val photosArray =
                    data.optJSONArray("photos")
                        ?: JSONArray()

                val photos =
                    mutableListOf<DeliveryDetailPhoto>()

                for (index in 0 until photosArray.length()) {

                    val photo =
                        photosArray.optJSONObject(index)
                            ?: continue

                    photos.add(
                        DeliveryDetailPhoto(
                            id =
                                photo.optString("id"),

                            deliveryId =
                                photo.optString(
                                    "delivery_id"
                                ),

                            documentationType =
                                photo.optString(
                                    "documentation_type"
                                ),

                            storagePath =
                                photo.optString(
                                    "storage_path"
                                ),

                            latitude =
                                nullableDouble(
                                    photo,
                                    "latitude"
                                ),

                            longitude =
                                nullableDouble(
                                    photo,
                                    "longitude"
                                ),

                            capturedAt =
                                photo.optString(
                                    "captured_at"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            fileSizeBytes =
                                nullableLong(
                                    photo,
                                    "file_size_bytes"
                                ),

                            mimeType =
                                photo.optString(
                                    "mime_type"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            createdBy =
                                photo.optString(
                                    "created_by"
                                ).takeIf {
                                    it.isNotBlank()
                                },

                            createdAt =
                                photo.optString(
                                    "created_at"
                                ),

                            signedUrl =
                                photo.optString(
                                    "signed_url"
                                ).takeIf {
                                    it.isNotBlank()
                                }
                        )
                    )
                }

                DeliveryDetailResult(
                    success = true,
                    delivery = delivery,
                    measurements = measurements,
                    seals = seals,
                    photos = photos
                )
            }

        } catch (e: Exception) {

            DeliveryDetailResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal mengambil detail penerimaan"
            )
        }
    }

    private fun nullableDouble(
        json: JSONObject,
        key: String
    ): Double? {

        if (!json.has(key) || json.isNull(key)) {
            return null
        }

        return try {
            json.getDouble(key)
        } catch (_: Exception) {
            null
        }
    }

    private fun nullableLong(
        json: JSONObject,
        key: String
    ): Long? {

        if (!json.has(key) || json.isNull(key)) {
            return null
        }

        return try {
            json.getLong(key)
        } catch (_: Exception) {
            null
        }
    }

    // ============================================================
    // CREATE DELIVERY
    // ============================================================

    data class DeliveryResult(
        val success: Boolean,
        val deliveryId: String? = null,
        val error: String? = null
    )

    suspend fun createDelivery(
        accessToken: String,
        locationId: String,
        storageTankId: String,
        fuelProductId: String,
        truckPlateNumber: String,
        driverName: String,
        flowmeterCode: String,
        doNumber: String,
        doDate: String,
        doVolumeLiter: Double,
        receivedDate: String,
        status: String = "DRAFT",
        notes: String? = null
    ): DeliveryResult = withContext(Dispatchers.IO) {

        try {

            val json = JSONObject().apply {

                put(
                    "location_id",
                    locationId
                )

                put(
                    "storage_tank_id",
                    storageTankId
                )

                put(
                    "fuel_product_id",
                    fuelProductId
                )

                put(
                    "truck_plate_number",
                    truckPlateNumber
                        .trim()
                        .uppercase()
                )

                put(
                    "driver_name",
                    driverName
                        .trim()
                        .uppercase()
                )

                put(
                    "flowmeter_code",
                    flowmeterCode
                        .trim()
                        .uppercase()
                )

                put(
                    "do_number",
                    doNumber.trim()
                )

                put(
                    "do_date",
                    doDate
                )

                put(
                    "do_volume_liter",
                    doVolumeLiter
                )

                put(
                    "received_date",
                    receivedDate
                )

                put(
                    "status",
                    status
                )

                if (!notes.isNullOrBlank()) {

                    put(
                        "notes",
                        notes
                    )
                }
            }

            val requestBody =
                json.toString()
                    .toRequestBody(
                        "application/json".toMediaType()
                    )

            val request =
                Request.Builder()
                    .url("$BASE_URL/api/deliveries")
                    .post(requestBody)
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext DeliveryResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext DeliveryResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal membuat penerimaan (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext DeliveryResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal membuat penerimaan"
                            )
                    )
                }

                val data =
                    result.optJSONObject("data")

                DeliveryResult(
                    success = true,
                    deliveryId =
                        data?.optString("id")
                )
            }

        } catch (e: Exception) {

            DeliveryResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal terhubung ke backend"
            )
        }
    }

    // ============================================================
    // UPDATE DELIVERY STATUS
    // ============================================================

    data class UpdateDeliveryStatusResult(
        val success: Boolean,
        val status: String? = null,
        val error: String? = null
    )

    suspend fun updateDeliveryStatus(
        accessToken: String,
        deliveryId: String,
        status: String
    ): UpdateDeliveryStatusResult = withContext(Dispatchers.IO) {

        try {

            val normalizedStatus =
                status.trim().uppercase()

            if (
                normalizedStatus != "DRAFT" &&
                normalizedStatus != "COMPLETED" &&
                normalizedStatus != "CANCELLED"
            ) {

                return@withContext UpdateDeliveryStatusResult(
                    success = false,
                    error =
                        "Status harus DRAFT, COMPLETED, atau CANCELLED"
                )
            }

            val json = JSONObject().apply {

                put(
                    "status",
                    normalizedStatus
                )
            }

            val requestBody =
                json.toString()
                    .toRequestBody(
                        "application/json".toMediaType()
                    )

            val request =
                Request.Builder()
                    .url(
                        "$BASE_URL/api/deliveries/$deliveryId"
                    )
                    .patch(requestBody)
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext UpdateDeliveryStatusResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext UpdateDeliveryStatusResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengubah status (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext UpdateDeliveryStatusResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal mengubah status"
                            )
                    )
                }

                val data =
                    result.optJSONObject("data")

                UpdateDeliveryStatusResult(
                    success = true,
                    status =
                        data?.optString("status")
                )
            }

        } catch (e: Exception) {

            UpdateDeliveryStatusResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal mengubah status transaksi"
            )
        }
    }

    // ============================================================
    // SAVE MEASUREMENTS
    // ============================================================

    data class MeasurementsResult(
        val success: Boolean,
        val error: String? = null
    )

    suspend fun saveMeasurements(
        accessToken: String,
        deliveryId: String,
        storageSoundingBeforeCm: Double,
        truckFrontSoundingCm: Double,
        truckFrontWaterCm: Double,
        truckFrontDensity: Double,
        truckFrontTemperatureC: Double,
        truckBackSoundingCm: Double,
        truckBackWaterCm: Double,
        truckBackDensity: Double,
        truckBackTemperatureC: Double,
        flowmeterInitial: Double,
        flowmeterFinal: Double,
        storageSoundingAfterCm: Double
    ): MeasurementsResult = withContext(Dispatchers.IO) {

        try {

            val json = JSONObject().apply {

                put(
                    "storage_sounding_before_cm",
                    storageSoundingBeforeCm
                )

                put(
                    "truck_front_sounding_cm",
                    truckFrontSoundingCm
                )

                put(
                    "truck_front_water_cm",
                    truckFrontWaterCm
                )

                put(
                    "truck_front_density",
                    truckFrontDensity
                )

                put(
                    "truck_front_temperature_c",
                    truckFrontTemperatureC
                )

                put(
                    "truck_back_sounding_cm",
                    truckBackSoundingCm
                )

                put(
                    "truck_back_water_cm",
                    truckBackWaterCm
                )

                put(
                    "truck_back_density",
                    truckBackDensity
                )

                put(
                    "truck_back_temperature_c",
                    truckBackTemperatureC
                )

                put(
                    "flowmeter_initial",
                    flowmeterInitial
                )

                put(
                    "flowmeter_final",
                    flowmeterFinal
                )

                put(
                    "storage_sounding_after_cm",
                    storageSoundingAfterCm
                )
            }

            val requestBody =
                json.toString()
                    .toRequestBody(
                        "application/json".toMediaType()
                    )

            val request =
                Request.Builder()
                    .url(
                        "$BASE_URL/api/deliveries/$deliveryId/measurements"
                    )
                    .post(requestBody)
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext MeasurementsResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext MeasurementsResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal menyimpan pengukuran (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext MeasurementsResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal menyimpan pengukuran"
                            )
                    )
                }

                MeasurementsResult(
                    success = true
                )
            }

        } catch (e: Exception) {

            MeasurementsResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal menyimpan pengukuran"
            )
        }
    }

    // ============================================================
    // SAVE SEAL
    // ============================================================

    data class SealResult(
        val success: Boolean,
        val sealId: String? = null,
        val error: String? = null
    )

    suspend fun saveSeal(
        accessToken: String,
        deliveryId: String,
        sealType: String,
        sealNumber: String? = null,
        condition: String? = null,
        notes: String? = null,
        photoId: String? = null
    ): SealResult = withContext(Dispatchers.IO) {

        try {

            val json = JSONObject().apply {

                put(
                    "seal_type",
                    sealType
                )

                if (!sealNumber.isNullOrBlank()) {

                    put(
                        "seal_number",
                        sealNumber
                    )
                }

                if (!condition.isNullOrBlank()) {

                    put(
                        "condition",
                        condition
                    )
                }

                if (!notes.isNullOrBlank()) {

                    put(
                        "notes",
                        notes
                    )
                }

                if (!photoId.isNullOrBlank()) {

                    put(
                        "photo_id",
                        photoId
                    )
                }
            }

            val requestBody =
                json.toString()
                    .toRequestBody(
                        "application/json".toMediaType()
                    )

            val request =
                Request.Builder()
                    .url(
                        "$BASE_URL/api/deliveries/$deliveryId/seals"
                    )
                    .post(requestBody)
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .addHeader(
                        "Content-Type",
                        "application/json"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext SealResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext SealResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal menyimpan segel (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext SealResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal menyimpan segel"
                            )
                    )
                }

                val data =
                    result.optJSONObject("data")

                SealResult(
                    success = true,
                    sealId =
                        data?.optString("id")
                )
            }

        } catch (e: Exception) {

            SealResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal menyimpan segel"
            )
        }
    }

    // ============================================================
    // UPLOAD PHOTO
    // ============================================================

    data class PhotoResult(
        val success: Boolean,
        val photoId: String? = null,
        val storagePath: String? = null,
        val error: String? = null
    )

    suspend fun uploadPhoto(
        accessToken: String,
        deliveryId: String,
        filePath: String,
        documentationType: String,
        latitude: Double? = null,
        longitude: Double? = null,
        capturedAt: String? = null
    ): PhotoResult = withContext(Dispatchers.IO) {

        try {

            val file =
                File(filePath)

            if (!file.exists()) {

                return@withContext PhotoResult(
                    success = false,
                    error = "File foto tidak ditemukan: $filePath"
                )
            }

            val mimeType =
                when {
                    file.extension.equals(
                        "png",
                        ignoreCase = true
                    ) -> "image/png"

                    file.extension.equals(
                        "webp",
                        ignoreCase = true
                    ) -> "image/webp"

                    else -> "image/jpeg"
                }

            val fileBody =
                file.asRequestBody(
                    mimeType.toMediaType()
                )

            val multipartBuilder =
                MultipartBody.Builder()
                    .setType(
                        MultipartBody.FORM
                    )
                    .addFormDataPart(
                        "file",
                        file.name,
                        fileBody
                    )
                    .addFormDataPart(
                        "documentation_type",
                        documentationType
                    )

            if (latitude != null) {

                multipartBuilder.addFormDataPart(
                    "latitude",
                    latitude.toString()
                )
            }

            if (longitude != null) {

                multipartBuilder.addFormDataPart(
                    "longitude",
                    longitude.toString()
                )
            }

            if (!capturedAt.isNullOrBlank()) {

                multipartBuilder.addFormDataPart(
                    "captured_at",
                    capturedAt
                )
            }

            val requestBody =
                multipartBuilder.build()

            val request =
                Request.Builder()
                    .url(
                        "$BASE_URL/api/deliveries/$deliveryId/photos"
                    )
                    .post(requestBody)
                    .addHeader(
                        "Authorization",
                        "Bearer $accessToken"
                    )
                    .build()

            client.newCall(request).execute().use { response ->

                val responseBody =
                    response.body?.string().orEmpty()

                if (responseBody.isBlank()) {

                    return@withContext PhotoResult(
                        success = false,
                        error = "Response server kosong"
                    )
                }

                val result =
                    JSONObject(responseBody)

                if (!response.isSuccessful) {

                    return@withContext PhotoResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal upload foto (${response.code})"
                            )
                    )
                }

                if (!result.optBoolean("success")) {

                    return@withContext PhotoResult(
                        success = false,
                        error =
                            result.optString(
                                "error",
                                "Gagal upload foto"
                            )
                    )
                }

                val data =
                    result.optJSONObject("data")

                PhotoResult(
                    success = true,
                    photoId =
                        data?.optString("id"),
                    storagePath =
                        data?.optString("storage_path")
                )
            }

        } catch (e: Exception) {

            PhotoResult(
                success = false,
                error =
                    e.message
                        ?: "Gagal upload foto"
            )
        }
    }
}