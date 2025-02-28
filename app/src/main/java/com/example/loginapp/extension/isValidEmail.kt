package com.example.loginapp.extension

import android.util.Patterns

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() && this.length >= 6 && this.length <= 20
}
