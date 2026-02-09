package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.repository.AuthRepository
import com.example.loginapp.utils.AuthValidator
import com.example.loginapp.utils.ErrorMessages.getJapaneseErrorMessage
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val emailError: String? = null,
    val passwordError: String? = null,
    val firebaseError: String? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun logIn(email: String, password: String) {
        val validationResult = AuthValidator.validateLogin(email, password)
        if (validationResult.emailError != null || validationResult.passwordError != null) {
            _uiState.update {
                it.copy(
                    emailError = validationResult.emailError,
                    passwordError = validationResult.passwordError,
                    firebaseError = null
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                isLoading = true,
                emailError = null,
                passwordError = null,
                firebaseError = null
            )
        }

        viewModelScope.launch {
            try {
                authRepository.logIn(email, password)
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: FirebaseAuthException) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        firebaseError = getJapaneseErrorMessage(e.errorCode)
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, firebaseError = "予期しないエラーが発生しました")
                }
            }
        }
    }

    fun clearErrors() {
        _uiState.update {
            it.copy(
                emailError = null,
                passwordError = null,
                firebaseError = null,
            )
        }
    }
}
