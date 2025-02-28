package com.example.loginapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    userName: String,
    onLogOut: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userName.isNotBlank()) {
                Text("ようこそ $userName さん")
            } else {
                Text("ようこそ")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLogOut) {
                Text("ログアウト")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onDeleteAccount) {
                Text("アカウントを削除")
            }
        }
    }
}
