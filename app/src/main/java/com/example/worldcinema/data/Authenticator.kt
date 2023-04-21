package com.example.worldcinema.data

import com.example.worldcinema.Constants
import com.example.worldcinema.data.api.RefreshApi
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

        if (response.responseCount > 1) {
            return null
        }

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

        return if (newTokensResult?.isFailure == true) {
            response.request.newBuilder()
                .header(
                    Constants.AUTHORIZATION_HEADER,
                    "Bearer ${sharedPref.getString(SharedPref.ACCESS_TOKEN)}"
                )
                .build()
        } else {
            response.request.newBuilder()
                .header(
                    Constants.AUTHORIZATION_HEADER,
                    "Bearer ${newTokensResult!!.getOrNull()!!.accessToken}"
                )
                .build()
        }
    }

    private val Response.responseCount: Int
        get() = generateSequence(this) { it.priorResponse }.count()
}