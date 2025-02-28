package com.example.loginapp.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.loginapp.ui.theme.LoginAppTheme

@Composable
fun LoginApp(modifier: Modifier = Modifier) {
    LoginAppTheme {
        Scaffold(
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            MainScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}
