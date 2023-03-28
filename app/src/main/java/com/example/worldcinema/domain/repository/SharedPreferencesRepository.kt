package com.example.worldcinema.domain.repository

interface SharedPreferencesRepository {

    fun getFirstRunApp(): Boolean

    fun setFirstRunFalse()

    fun setTokens(accessToken: String, refreshToken: String)

    fun clearTokens()
}