package com.example.worldcinema.data

import com.example.worldcinema.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPref: SharedPref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val token = sharedPref.getString(SharedPref.ACCESS_TOKEN)

        val request = chain.request().newBuilder().apply {
            token?.let {
                addHeader(Constants.AUTHORIZATION_HEADER, "Bearer $it")
            }
        }.build()
        return chain.proceed(request)
    }
}