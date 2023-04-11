package com.example.worldcinema.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPref @Inject constructor(
    @ApplicationContext context: Context
) {

    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val preferences = EncryptedSharedPreferences.create(
        PREFERENCE_NAME,
        masterKey,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }

    fun setString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, true)
    }

    fun setBoolean(key: String, value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun clearTokens() {
        preferences.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(USER_ID)
            .apply()
    }

    companion object {
        const val PREFERENCE_NAME = "pref"
        const val ACCESS_TOKEN = "token"
        const val REFRESH_TOKEN = "refresh_token"
        const val USER_ID = "user_id"
        const val FIRST_RUN = "first_run"
    }
}