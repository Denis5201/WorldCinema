package com.example.worldcinema.di

import com.example.worldcinema.Constants
import com.example.worldcinema.data.api.*
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)

    @Provides
    @Singleton
    fun provideMovieApi(retrofit: Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    @Singleton
    fun provideEpisodeApi(retrofit: Retrofit): EpisodeApi = retrofit.create(EpisodeApi::class.java)

    @Provides
    @Singleton
    fun provideCollectionApi(retrofit: Retrofit): CollectionApi = retrofit.create(CollectionApi::class.java)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideRefreshApi(): RefreshApi {
        val refreshRetrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder().apply {
                    val logLevel = HttpLoggingInterceptor.Level.BODY
                    addInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                }.build()
            )
            .build()
        return refreshRetrofit.create(RefreshApi::class.java)
    }
}