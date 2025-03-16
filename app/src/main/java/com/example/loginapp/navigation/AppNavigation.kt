package com.example.loginapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.navigation.Destinations.EMAIL_VERIFICATION_SCREEN
import com.example.loginapp.navigation.Destinations.HOME_SCREEN
import com.example.loginapp.navigation.Destinations.LOGIN_SCREEN
import com.example.loginapp.navigation.Destinations.SIGN_UP_SCREEN
import com.example.loginapp.screen.HomeScreen
import com.example.loginapp.screen.auth.EmailVerificationScreen
import com.example.loginapp.screen.auth.LoginScreen
import com.example.loginapp.screen.auth.SignUpScreen
import com.example.loginapp.state.AuthState
import com.example.loginapp.viewmodel.AuthViewModel

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = when (authState) {
            is AuthState.LoggedOut -> LOGIN_SCREEN
            is AuthState.LogIn -> HOME_SCREEN
        },
        modifier = modifier
    ) {
        composable(LOGIN_SCREEN) {
            LoginScreen(
                viewModel = viewModel,
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
                onNavigateToEmailVerification = {
                    navController.navigateSingleTopTo(EMAIL_VERIFICATION_SCREEN)
                }
            )
        }

        composable(EMAIL_VERIFICATION_SCREEN) {
            EmailVerificationScreen(
                onAppear = { viewModel.logOut() },
                onNavigateToLogin = {
                    navController.navigateSingleTopTo(LOGIN_SCREEN)
                }
            )
        }

        composable(HOME_SCREEN) {
            HomeScreen(
                userName = (authState as? AuthState.LogIn)?.user?.displayName ?: "",
                onLogOut = {
                    viewModel.logOut()
                    navController.navigateSingleTopTo(LOGIN_SCREEN)
                },
                onDeleteAccount = {
                    viewModel.deleteAccount()
                    navController.navigateSingleTopTo(LOGIN_SCREEN)
                }
            )
        }
    }
}

