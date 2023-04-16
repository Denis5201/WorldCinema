package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val repository: SharedPreferencesRepository
) {

    operator fun invoke(): String? = repository.getUserId()
}