package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.AgeGrade
import javax.inject.Inject

class GetAgeColorStringUseCase @Inject constructor() {

    operator fun invoke(age: String): String {
        return AgeGrade.valueOf("A${age.dropLast(1)}").color
    }
}