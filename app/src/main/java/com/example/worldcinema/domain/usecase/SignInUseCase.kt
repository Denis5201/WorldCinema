package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Credentials
import com.example.worldcinema.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(credentials: Credentials): Flow<Result<Unit>> =
        repository.signIn(credentials)
}