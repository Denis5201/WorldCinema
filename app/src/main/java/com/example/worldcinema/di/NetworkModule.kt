package com.example.worldcinema.di

import com.example.worldcinema.Constants
import com.example.worldcinema.data.AuthInterceptor
import com.example.worldcinema.data.Authenticator
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesHttpClient(
        authInterceptor: AuthInterceptor,
        authenticator: Authenticator
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            val logLevel = HttpLoggingInterceptor.Level.BODY
            addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
            addInterceptor(authInterceptor)
            authenticator(authenticator)
            connectTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
        }.build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    const val AUTHORIZATION_HEADER = "Authorization"
}