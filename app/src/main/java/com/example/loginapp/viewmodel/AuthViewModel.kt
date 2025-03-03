package com.example.loginapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.repository.AuthRepository
import com.example.loginapp.state.AuthFormState
import com.example.loginapp.state.AuthState
import com.example.loginapp.utils.AuthValidator
import com.example.loginapp.utils.ErrorMessages.getJapaneseErrorMessage
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.LoggedOut)
    val authState = _authState.asStateFlow()

    private val _formState = MutableStateFlow(AuthFormState())
    val formState = _formState.asStateFlow()

    init {
        checkAuthState()
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        val validationResult = AuthValidator.validate(
            email, password, confirmPassword
        )
        if (validationResult != AuthFormState()) {
            _formState.value = validationResult
            return
        }

        performAuthAction {
            authRepository.signUp(email, password)
        }
    }

    fun logIn(email: String, password: String) {
        val validationResult = AuthValidator.validate(
            email,
            password,
            isLogIn = true
        )
        if (validationResult != AuthFormState()) {
            _formState.value = validationResult
            return
        }

        // 認証処理
        performAuthAction {
            authRepository.logIn(email, password)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
            _authState.value = AuthState.LoggedOut
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            authRepository.deleteAccount()
            _authState.value = AuthState.LoggedOut
        }
    }

    fun clearErrors() {
        _formState.value = AuthFormState()
    }

    private fun performAuthAction(action: suspend () -> Unit) {
        clearErrors()
        viewModelScope.launch {
            try {
                action()
                checkAuthState()
            } catch (e: FirebaseAuthException) {
                updateFormState(firebaseError = getJapaneseErrorMessage(e.errorCode))
            } catch (e: Exception) {
                updateFormState(firebaseError = "予期しないエラーが発生しました")
            }
        }
    }

    private fun checkAuthState() {
        val currentUser = authRepository.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            _authState.value = AuthState.LogIn(currentUser)
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    private fun updateFormState(firebaseError: String? = null) {
        clearErrors()
        _formState.value = _formState.value.copy(firebaseError = firebaseError)
    }
}
