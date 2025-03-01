package com.example.loginapp.utils

import com.example.loginapp.extension.isValidEmail
import com.example.loginapp.extension.isValidPassword
import com.example.loginapp.state.AuthFormState

object AuthValidator {
    fun validate(
        email: String,
        password: String,
        confirmPassword: String? = null,
        isLogIn: Boolean = false
    ): AuthFormState {
        var formState = AuthFormState()

        if (!email.isValidEmail()) {
            formState = formState.copy(
                emailError = "メールアドレスが無効です"
            )
        }

        if (!password.isValidPassword()) {
            formState =
                formState.copy(
                    passwordError = "パスワードは6文字以上20文字以下である必要があります"
                )
        }

        if (!isLogIn && (confirmPassword.isNullOrBlank() ||
                    password != confirmPassword)
        ) {
            formState = formState.copy(confirmPasswordError = "パスワードが一致していません")
        }

        return formState
    }
}