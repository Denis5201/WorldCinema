package com.example.worldcinema.data.repository

import com.example.worldcinema.data.SharedPref
import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

class SharedPreferencesRepositoryImpl @Inject constructor(
    private val sharedPref: SharedPref
) : SharedPreferencesRepository {
    override fun getFirstRunApp(): Boolean {
        return sharedPref.getBoolean(SharedPref.FIRST_RUN)
    }

    override fun setFirstRunFalse() {
        sharedPref.setBoolean(SharedPref.FIRST_RUN, false)
    }

    override fun setTokens(accessToken: String, refreshToken: String) {
        sharedPref.setString(SharedPref.ACCESS_TOKEN, accessToken)
        sharedPref.setString(SharedPref.REFRESH_TOKEN, refreshToken)
    }

    override fun getUserId(): String? {
        return sharedPref.getString(SharedPref.USER_ID)
    }

    override fun setUserId(userId: String) {
        sharedPref.setString(SharedPref.USER_ID, userId)
    }

    override fun clearUserInfo() {
        sharedPref.clearTokens()
    }

}