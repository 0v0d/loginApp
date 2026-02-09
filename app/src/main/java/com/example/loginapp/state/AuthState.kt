package com.example.loginapp.state

sealed interface AuthState {
    data object Unauthenticated : AuthState
    data class Authenticated(val userName: String) : AuthState
    data class NeedsVerification(val email: String) : AuthState
}
