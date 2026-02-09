package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.repository.AuthRepository
import com.example.loginapp.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            refreshAuthState()
        }
        viewModelScope.launch {
            authRepository.authChangeNotifier.collect {
                refreshAuthState()
            }
        }
    }

    private suspend fun refreshAuthState() {
        val user = authRepository.currentUser
        if (user == null) {
            _authState.value = AuthState.Unauthenticated
            return
        }
        try {
            authRepository.reloadUser()
        } catch (_: Exception) {
            // reloadが失敗してもキャッシュされた状態で評価を続ける
        }
        val reloadedUser = authRepository.currentUser
        _authState.value = when {
            reloadedUser == null -> AuthState.Unauthenticated
            reloadedUser.isEmailVerified -> AuthState.Authenticated(
                userName = reloadedUser.displayName
                    ?: reloadedUser.email?.substringBefore('@') ?: ""
            )
            else -> AuthState.NeedsVerification(
                email = reloadedUser.email ?: ""
            )
        }
    }
}
