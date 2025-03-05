package com.example.loginapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.loginapp.screen.auth.AuthScreens
import com.example.loginapp.state.AuthState
import com.example.loginapp.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val authState by viewModel.authState.collectAsState()

    Box(modifier = modifier) {
        when (val currentAuthState = authState) {
            AuthState.LoggedOut -> AuthScreens(viewModel)
            is AuthState.LogIn -> HomeScreen(
                userName = currentAuthState.user.displayName ?: "",
                onLogOut = { viewModel.logOut() },
                onDeleteAccount = { viewModel.deleteAccount() }
            )
        }
    }
}

