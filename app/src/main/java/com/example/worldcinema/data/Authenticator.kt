package com.example.worldcinema.data

import com.example.worldcinema.data.api.RefreshApi
import com.example.worldcinema.di.NetworkModule.AUTHORIZATION_HEADER
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class Authenticator @Inject constructor(
    private val sharedPref: SharedPref,
    private val refreshApi: RefreshApi
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = sharedPref.getString(SharedPref.REFRESH_TOKEN)

        val newTokensResult = runBlocking {
            refreshToken?.let {
                try {
                    val token = refreshApi.refresh("Bearer $it")
                    Result.success(token)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        }

        newTokensResult?.onSuccess {
            sharedPref.setString(SharedPref.ACCESS_TOKEN, it.accessToken)
            sharedPref.setString(SharedPref.REFRESH_TOKEN, it.refreshToken)
        }

        return if (newTokensResult?.isSuccess == false || response.responseCount > 1) {
            null
        } else {
            response.request.newBuilder()
                .header(
                    AUTHORIZATION_HEADER,
                    "Bearer ${newTokensResult!!.getOrNull()!!.accessToken}"
                )
                .build()
        }
    }

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()
}