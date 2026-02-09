package com.example.loginapp.utils

import com.example.loginapp.extension.isValidEmail
import com.example.loginapp.extension.isValidPassword

data class LoginValidationResult(
    val emailError: String? = null,
    val passwordError: String? = null
)

data class SignUpValidationResult(
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null
)

object AuthValidator {
    fun validateLogin(
        email: String,
        password: String
    ): LoginValidationResult {
        return LoginValidationResult(
            emailError = if (!email.isValidEmail()) "メールアドレスが無効です" else null,
            passwordError = if (!password.isValidPassword()) "パスワードは6文字以上20文字以下である必要があります" else null
        )
    }

    fun validateSignUp(
        email: String,
        password: String,
        confirmPassword: String
    ): SignUpValidationResult {
        return SignUpValidationResult(
            emailError = if (!email.isValidEmail()) "メールアドレスが無効です" else null,
            passwordError = if (!password.isValidPassword()) "パスワードは6文字以上20文字以下である必要があります" else null,
            confirmPasswordError = if (confirmPassword.isBlank() || password != confirmPassword) "パスワードが一致していません" else null
        )
    }
}
