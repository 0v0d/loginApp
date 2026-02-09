package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = false,
            errorMessage = null
        )
    )
    val uiState = _uiState.asStateFlow()

    fun logOut() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.logOut()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "ログアウトに失敗しました")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.deleteAccount()
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "アカウントの削除に失敗しました")
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
