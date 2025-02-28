package com.example.loginapp.state

import com.google.firebase.auth.FirebaseUser

sealed class AuthState {
    data object Loading : AuthState()
    data object LoggedOut : AuthState()
    data class LogIn(val user: FirebaseUser) : AuthState()
}