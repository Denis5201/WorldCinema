package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.Constants
import com.example.worldcinema.data.api.AuthApi
import com.example.worldcinema.data.api.CollectionApi
import com.example.worldcinema.data.dto.CollectionFormDto
import com.example.worldcinema.data.dto.CredentialsDto
import com.example.worldcinema.data.dto.RegistrationBodyDto
import com.example.worldcinema.domain.model.Credentials
import com.example.worldcinema.domain.model.RegistrationForm
import com.example.worldcinema.domain.repository.AuthRepository
import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sharedPreferencesRepository: SharedPreferencesRepository,
    private val collectionApi: CollectionApi
) : AuthRepository{

    override fun register(registrationForm: RegistrationForm): Flow<Result<Unit>> = flow {
        try {
            sharedPreferencesRepository.clearUserInfo()

            val tokens = authApi.register(RegistrationBodyDto.fromRegistrationForm(registrationForm))

            sharedPreferencesRepository.setTokens(tokens.accessToken, tokens.refreshToken)
            sharedPreferencesRepository.setFirstRunFalse()

            collectionApi.createCollection(CollectionFormDto(Constants.FAVOUR_COLLECTION))

            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS register", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun signIn(credentials: Credentials): Flow<Result<Unit>> = flow {
        try {
            sharedPreferencesRepository.clearUserInfo()

            val tokens = authApi.login(CredentialsDto.fromCredentials(credentials))

            sharedPreferencesRepository.setTokens(tokens.accessToken, tokens.refreshToken)
            sharedPreferencesRepository.setFirstRunFalse()

            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS signIn", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}