package com.example.loginapp.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.screen.auth.AuthDestinations.LOGIN_SCREEN
import com.example.loginapp.screen.auth.AuthDestinations.SIGN_UP_SCREEN
import com.example.loginapp.viewmodel.AuthViewModel

@Composable
fun AuthScreens(
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LOGIN_SCREEN,
        modifier = modifier
    ) {
        composable(LOGIN_SCREEN) {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToSignUp = {
                    navController.navigateSingleTopTo(
                        SIGN_UP_SCREEN
                    )
                }
            )
        }

        composable(SIGN_UP_SCREEN) {
            SignUpScreen(
                viewModel = viewModel,
                onNavigateToLogin = {
                    navController.navigateSingleTopTo(
                        LOGIN_SCREEN,
                    )
                }
            )
        }
    }
}

private fun NavHostController.navigateSingleTopTo(
    route: String,
) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
    }
}

private object AuthDestinations {
    const val LOGIN_SCREEN = "LoginScreen"
    const val SIGN_UP_SCREEN = "SignUpScreen"
}
