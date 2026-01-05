package com.example.loginapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.loginapp.screen.HomeScreen as HomeScreenComposable
import com.example.loginapp.screen.auth.LoginScreen as LoginScreenComposable
import com.example.loginapp.screen.auth.SignUpScreen as SignUpScreenComposable
import com.example.loginapp.state.AuthState
import com.example.loginapp.viewmodel.AuthViewModel
import com.example.loginapp.navigation.LoginAppDestination.HomeScreen
import com.example.loginapp.navigation.LoginAppDestination.LoadingScreen
import com.example.loginapp.navigation.LoginAppDestination.LoginScreen
import com.example.loginapp.navigation.LoginAppDestination.SignUpScreen


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
                is AuthState.LoggedIn -> HomeScreen
                is AuthState.LoggedOut -> LoginScreen()
                else -> LoginScreen()
            },
            modifier = modifier
        ) {
            composable<LoginScreen> { backStackEntry ->
                val loginScreen = backStackEntry.toRoute<LoginScreen>()
                LoginScreenComposable(
                    showEmailVerificationMessage = loginScreen.showEmailVerification,
                    onNavigateToSignUp = {
                        navController.navigateSingleTopTo(SignUpScreen)
                    }
                )
            }

            composable<SignUpScreen> {
                SignUpScreenComposable(
                    onNavigateToLogin = {
                        navController.navigateSingleTopTo(LoginScreen())
                    },
                    onSignUpSuccess = {
                        navController.navigateSingleTopTo(LoginScreen(showEmailVerification = true))
                    },
                    onSignUpFailed = {

                    }
                )
            }

            composable<HomeScreen> {
                val viewModel: AuthViewModel = hiltViewModel()
                val user = (authState as? AuthState.LoggedIn)?.user
                HomeScreenComposable(
                    userName = user?.displayName ?: "",
                    onLogOut = { viewModel.logOut() },
                    onDeleteAccount = { viewModel.deleteAccount() }
                )
            }
            composable<LoadingScreen> {
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
