package com.example.loginapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.navigation.Destinations.HOME_SCREEN
import com.example.loginapp.navigation.Destinations.LOGIN_SCREEN
import com.example.loginapp.navigation.Destinations.SIGN_UP_SCREEN
import com.example.loginapp.screen.HomeScreen
import com.example.loginapp.screen.auth.LoginScreen
import com.example.loginapp.screen.auth.SignUpScreen
import com.example.loginapp.state.AuthState
import com.example.loginapp.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseUser

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val authState by viewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is AuthState.LoggedOut -> {
            AuthNavGraph(viewModel = viewModel)
        }

        is AuthState.LoggedIn -> {
            MainNavGraph(
                user = (authState as AuthState.LoggedIn).user,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun AuthNavGraph(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = LOGIN_SCREEN) {
        composable(LOGIN_SCREEN) {
            // SavedStateHandleからメッセージ表示フラグを取得
            val showMessage = it.savedStateHandle.get<Boolean>("showEmailVerification") ?: false

            LoginScreen(
                viewModel = viewModel,
                showEmailVerificationMessage = showMessage,
                onNavigateToSignUp = {
                    navController.navigateSingleTopTo(SIGN_UP_SCREEN)
                }
            )
        }

        composable(SIGN_UP_SCREEN) {
            SignUpScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigateSingleTopTo(LOGIN_SCREEN)
                },
                onSignUpSuccess = {
                    viewModel.logOut()
                    navController.getBackStackEntry(LOGIN_SCREEN)
                        .savedStateHandle["showEmailVerification"] = true
                    navController.navigateSingleTopTo(LOGIN_SCREEN)
                }
            )
        }
    }
}

@Composable
fun MainNavGraph(
    user: FirebaseUser,
    viewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = HOME_SCREEN) {
        composable(HOME_SCREEN) {
            HomeScreen(
                userName = user.displayName ?: "",
                onLogOut = { viewModel.logOut() },
                onDeleteAccount = { viewModel.deleteAccount() }
            )
        }
    }
}

