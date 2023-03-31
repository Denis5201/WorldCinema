package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.ProfileApi
import com.example.worldcinema.domain.model.UserAccount
import com.example.worldcinema.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {
    override fun getUserAccount(): Flow<Result<UserAccount>> = flow {
        try {
            val account = api.getUserAccount().toUserAccount()
            emit(Result.success(account))
        }
        catch (e: Exception) {
            Log.e("OPS getUserAccount", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun loadAvatar(avatar: String): Flow<Result<Unit>> = flow {
        try {
            api.loadAvatar(avatar)
            emit(Result.success(Unit))
        }
        catch (e: Exception) {
            Log.e("OPS loadAvatar", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}