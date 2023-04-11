package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.ProfileApi
import com.example.worldcinema.domain.model.UserAccount
import com.example.worldcinema.domain.repository.ProfileRepository
import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : ProfileRepository {
    override fun getUserAccount(): Flow<Result<UserAccount>> = flow {
        try {
            val account = api.getUserAccount().toUserAccount()
            sharedPreferencesRepository.setUserId(account.userId)
            emit(Result.success(account))
        }
        catch (e: Exception) {
            Log.e("OPS getUserAccount", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun loadAvatar(avatarArray: ByteArray): Flow<Result<Unit>> = flow {
        try {
            val body = avatarArray.toRequestBody(MEDIA_PNG.toMediaType(), 0, avatarArray.size)
            val part = MultipartBody.Part.createFormData(FILE, FILENAME, body)

            api.loadAvatar(part)
            emit(Result.success(Unit))
        }
        catch (e: Exception) {
            Log.e("OPS loadAvatar", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    private companion object {
        const val MEDIA_PNG = "image/png"
        const val FILE = "file"
        const val FILENAME = "avatar.png"
    }
}