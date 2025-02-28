package com.example.loginapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.extension.isValidEmail
import com.example.loginapp.extension.isValidPassword
import com.example.loginapp.repository.AuthRepository
import com.example.loginapp.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO Firebase側のエラーメッセージを表示する
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    init {
        checkAuthState()
    }

    fun signUp(
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (validateInput(email, password, confirmPassword)) {
            viewModelScope.launch {
                authRepository.signUp(email, password)
                checkAuthState()
            }
        }
    }

    fun logIn(email: String, password: String) {
        Log.d("AuthViewModel", validateInput(email, password).toString())
        if (validateInput(email, password, isLogIn = true)) {
            viewModelScope.launch {
                authRepository.logIn(email, password)
                checkAuthState()
            }
        }
    }


    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
            checkAuthState()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
            checkAuthState()
        }
    }

    private fun checkAuthState() {
        val currentUser = authRepository.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.LogIn(currentUser)
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    private fun clearErrors() {
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    private fun validateInput(
        email: String,
        password: String,
        confirmPassword: String? = null,
        isLogIn: Boolean = false,
    ): Boolean {
        clearErrors()

        var isValid = true

        if (!email.isValidEmail()) {
            _emailError.value = "メールアドレスが無効です"
            isValid = false
        }

        if (!password.isValidPassword()) {
            _passwordError.value = "パスワードは6文字以上20文字以下である必要があります"
            isValid = false
        }

        if (isLogIn) {
            return isValid
        }

        if (confirmPassword.isNullOrBlank() || password != confirmPassword) {
            _confirmPasswordError.value = "パスワードが一致していません"
            isValid = false
        }
        return isValid
    }
}
