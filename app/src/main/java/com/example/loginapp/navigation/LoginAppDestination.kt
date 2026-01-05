package com.example.loginapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class LoginAppDestination {
    @Serializable
    data class LoginScreen(
        val showEmailVerification: Boolean = false
    )

    @Serializable
    object SignUpScreen

    @Serializable
    object HomeScreen

    @Serializable
    object LoadingScreen
}
