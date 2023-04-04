package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadAvatarUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    operator fun invoke(imageArray: ByteArray): Flow<Result<Unit>> = repository.loadAvatar(imageArray)
}