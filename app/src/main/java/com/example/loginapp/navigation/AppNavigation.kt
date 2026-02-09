package com.example.loginapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.loginapp.navigation.LoginAppDestination.EmailVerification
import com.example.loginapp.navigation.LoginAppDestination.Home
import com.example.loginapp.navigation.LoginAppDestination.Loading
import com.example.loginapp.navigation.LoginAppDestination.Login
import com.example.loginapp.navigation.LoginAppDestination.SignUp
import com.example.loginapp.screen.HomeScreen
import com.example.loginapp.screen.auth.EmailVerificationScreen
import com.example.loginapp.screen.auth.LoginScreen
import com.example.loginapp.screen.auth.SignUpScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    backStack: MutableList<NavKey>
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Login> {
                LoginScreen(
                    onNavigateToSignUp = {
                        backStack.add(SignUp)
                    }
                )
            }
            entry<SignUp> {
                SignUpScreen(
                    onNavigateToLogin = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<EmailVerification> { destination ->
                EmailVerificationScreen(
                    email = destination.email
                )
            }
            entry<Home> { destination ->
                HomeScreen(
                    userName = destination.userName
                )
            }
            entry<Loading> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        },
    )
}
