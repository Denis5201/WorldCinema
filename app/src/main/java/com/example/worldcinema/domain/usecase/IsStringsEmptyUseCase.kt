package com.example.worldcinema.domain.usecase

import javax.inject.Inject

class IsStringsEmptyUseCase @Inject constructor() {

    operator fun invoke(vararg field: String): Boolean {
        for (str in field) {
            if (str.isEmpty()) {
                return true
            }
        }
        return false
    }
}