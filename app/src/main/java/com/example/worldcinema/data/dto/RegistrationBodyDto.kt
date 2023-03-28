package com.example.worldcinema.data.dto

import com.example.worldcinema.domain.model.RegistrationForm
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationBodyDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
) {
    companion object {
        fun fromRegistrationForm(registrationForm: RegistrationForm): RegistrationBodyDto {
            return RegistrationBodyDto(
                email = registrationForm.email,
                password = registrationForm.password,
                firstName = registrationForm.name,
                lastName = registrationForm.surname
            )
        }
    }
}
