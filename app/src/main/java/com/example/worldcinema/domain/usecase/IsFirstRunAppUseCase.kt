package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

class IsFirstRunAppUseCase @Inject constructor(
    private val sharedPreferencesRepository: SharedPreferencesRepository
) {

    operator fun invoke(): Boolean = sharedPreferencesRepository.getFirstRunApp()
}