package com.example.loginapp.state

data class AuthFormState(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val firebaseError: String? = null
)