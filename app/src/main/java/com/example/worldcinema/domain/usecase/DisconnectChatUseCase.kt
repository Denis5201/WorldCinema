package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DisconnectChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<Result<Unit>> = repository.close()
}