package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.ChatInfo
import com.example.worldcinema.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserChatListUseCase @Inject constructor(
    private val repository: ChatRepository
) {

    operator fun invoke(): Flow<Result<List<ChatInfo>>> = repository.getUserChatList()
}