package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.UserAccount
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    fun getUserAccount(): Flow<Result<UserAccount>>

    fun loadAvatar(avatarArray: ByteArray): Flow<Result<Unit>>
}