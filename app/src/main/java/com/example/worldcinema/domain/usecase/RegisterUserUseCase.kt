package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.RegistrationForm
import com.example.worldcinema.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator fun invoke(registrationForm: RegistrationForm): Flow<Result<Unit>> =
        repository.register(registrationForm)
}