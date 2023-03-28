package com.example.worldcinema.presentation

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.worldcinema.R

fun createErrorDialog(
    context: Context,
    message: String,
    close: () -> Unit
): AlertDialog {
    return AlertDialog.Builder(context)
        .setTitle(context.resources.getString(R.string.error_dialog_title))
        .setMessage(message)
        .setPositiveButton(
            context.resources.getString(R.string.error_dialog_button)
        ) { dialog, _ ->
            close.invoke()
            dialog.dismiss()
        }
        .create()
}