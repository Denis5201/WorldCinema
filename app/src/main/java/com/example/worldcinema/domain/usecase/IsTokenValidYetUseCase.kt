package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.single
import javax.inject.Inject

class IsTokenValidYetUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend operator fun invoke(): Boolean {
        return repository.getUserAccount().single().isSuccess
    }
}