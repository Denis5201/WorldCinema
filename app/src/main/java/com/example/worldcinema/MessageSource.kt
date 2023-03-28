package com.example.worldcinema

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val resources = context.resources

    fun getMessage(reason: Int): String {
        return when(reason) {
            WRONG_EMAIL_FORMAT -> resources.getString(R.string.wrong_email_format)
            EMPTY_INPUT -> resources.getString(R.string.empty_input)
            PASSWORD_NOT_EQUAL_WITH_CONFIRM -> resources.getString(R.string.password_not_equal_with_confirm)
            else -> ERROR
        }
    }

    companion object {
        const val ERROR = "Error"
        const val EMPTY_INPUT = 0
        const val WRONG_EMAIL_FORMAT = 1
        const val PASSWORD_NOT_EQUAL_WITH_CONFIRM = 2
    }
}