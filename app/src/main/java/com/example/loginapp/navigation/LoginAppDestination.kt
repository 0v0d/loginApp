package com.example.loginapp.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class LoginAppDestination: NavKey {
    @Serializable
    data object Login : LoginAppDestination()

    @Serializable
    data object SignUp : LoginAppDestination()

    @Serializable
    data class EmailVerification(val email: String) : LoginAppDestination()

    @Serializable
    data class Home(val userName: String = "") : LoginAppDestination()

    @Serializable
    data object Loading : LoginAppDestination()
}
