package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.Credentials
import com.example.worldcinema.domain.model.RegistrationForm
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun register(registrationForm: RegistrationForm): Flow<Result<Unit>>

    fun signIn(credentials: Credentials): Flow<Result<Unit>>
}