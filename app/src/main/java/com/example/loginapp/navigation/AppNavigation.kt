package com.example.loginapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.loginapp.navigation.Destinations.HOME_SCREEN
import com.example.loginapp.navigation.Destinations.LOADING_SCREEN
import com.example.loginapp.navigation.Destinations.LOGIN_SCREEN
import com.example.loginapp.navigation.Destinations.SIGN_UP_SCREEN
import com.example.loginapp.screen.HomeScreen
import com.example.loginapp.screen.auth.LoginScreen
import com.example.loginapp.screen.auth.SignUpScreen
import com.example.loginapp.state.AuthState
import com.example.loginapp.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    authState: AuthState,
    navController: NavHostController,
) {
    key(authState::class) {
        NavHost(
            navController = navController,
            startDestination = when (authState) {
                is AuthState.LoggedIn -> HOME_SCREEN
                is AuthState.LoggedOut -> LOGIN_SCREEN
                else -> LOGIN_SCREEN
            },
            modifier = modifier
        ) {
            composable(LOGIN_SCREEN) {
                val showMessage = it.savedStateHandle.get<Boolean>("showEmailVerification") ?: false
                LoginScreen(
                    showEmailVerificationMessage = showMessage,
                    onNavigateToSignUp = {
                        navController.navigateSingleTopTo(SIGN_UP_SCREEN)
                    }
                )
            }

            composable(SIGN_UP_SCREEN) {
                SignUpScreen(
                    onNavigateToLogin = {
                        navController.navigateSingleTopTo(LOGIN_SCREEN)
                    },
                    onSignUpSuccess = {
                        navController.getBackStackEntry(LOGIN_SCREEN)
                            .savedStateHandle["showEmailVerification"] = true
                        navController.navigateSingleTopTo(LOGIN_SCREEN)
                    }
                )
            }

            composable(HOME_SCREEN) {
                val viewModel: AuthViewModel = hiltViewModel()
                val user = (authState as? AuthState.LoggedIn)?.user
                HomeScreen(
                    userName = user?.displayName ?: "",
                    onLogOut = { viewModel.logOut() },
                    onDeleteAccount = { viewModel.deleteAccount() }
                )
            }
            composable(LOADING_SCREEN) {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
