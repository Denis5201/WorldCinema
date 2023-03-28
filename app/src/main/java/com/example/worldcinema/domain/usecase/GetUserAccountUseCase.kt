package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.UserAccount
import com.example.worldcinema.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserAccountUseCase @Inject constructor(
    private val repository: ProfileRepository
) {

    operator fun invoke(): Flow<Result<UserAccount>> = repository.getUserAccount()
}