package com.plnnusadaya.bbmmonitoring

import android.content.Context

object SessionManager {

    private const val PREF_NAME = "bbm_session"

    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"
    private const val KEY_ACCESS_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"

    private fun prefs(context: Context) =
        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

    fun saveSession(
        context: Context,
        userId: String,
        email: String,
        accessToken: String,
        refreshToken: String?
    ) {
        prefs(context)
            .edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun getAccessToken(context: Context): String? {
        return prefs(context)
            .getString(KEY_ACCESS_TOKEN, null)
    }

    fun getUserId(context: Context): String? {
        return prefs(context)
            .getString(KEY_USER_ID, null)
    }

    fun getEmail(context: Context): String? {
        return prefs(context)
            .getString(KEY_EMAIL, null)
    }

    fun clear(context: Context) {
        prefs(context)
            .edit()
            .clear()
            .apply()
    }
}