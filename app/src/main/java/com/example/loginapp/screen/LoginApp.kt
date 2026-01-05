package com.example.loginapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.navigation.AppNavigation
import com.example.loginapp.ui.theme.LoginAppTheme
import com.example.loginapp.viewmodel.AuthViewModel

@Composable
fun LoginApp(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val navController = rememberNavController()
    LoginAppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavigation(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                authState = authState,
                navController = navController
            )
        }
    }
}
