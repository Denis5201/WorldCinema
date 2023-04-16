package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.ChatRepository
import javax.inject.Inject

class SetChatConnectionUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(chatId: String) =
        repository.setConnection(chatId)
}