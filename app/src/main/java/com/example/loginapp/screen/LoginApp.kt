package com.example.loginapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.rememberNavBackStack
import com.example.loginapp.navigation.LoginAppDestination.EmailVerification
import com.example.loginapp.navigation.LoginAppDestination.Home
import com.example.loginapp.navigation.LoginAppDestination.Loading
import com.example.loginapp.navigation.LoginAppDestination.Login
import com.example.loginapp.navigation.NavGraph
import com.example.loginapp.state.AuthState
import com.example.loginapp.ui.theme.LoginAppTheme
import com.example.loginapp.viewmodel.MainViewModel

@Composable
fun LoginApp(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val backStack = rememberNavBackStack(Loading)
    val authState by mainViewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.Unauthenticated -> {
                backStack.clear()
                backStack.add(Login)
            }
            is AuthState.Authenticated -> {
                backStack.clear()
                backStack.add(Home(userName = state.userName))
            }
            is AuthState.NeedsVerification -> {
                backStack.clear()
                backStack.add(EmailVerification(email = state.email))
            }
        }
    }

    LoginAppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            NavGraph(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                backStack = backStack
            )
        }
    }
}
