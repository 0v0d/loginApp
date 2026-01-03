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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SignUpEvent {
    data object Success : SignUpEvent
    data class Failure(val message: String) : SignUpEvent
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    private val _formState = MutableStateFlow(AuthFormState())
    val formState = _formState.asStateFlow()

    private val _event = MutableSharedFlow<SignUpEvent>()
    val event = _event.asSharedFlow()

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
        _authState.value = AuthState.LoggedOut
        performAuthAction {
            authRepository.signUp(email, password)
            _event.emit(SignUpEvent.Success)
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

        performAuthAction {
            authRepository.logIn(email, password)
        }
    }

    fun logOut() {
        performAuthAction {
            authRepository.logOut()
        }
    }

    fun deleteAccount() {
        performAuthAction {
            authRepository.deleteAccount()
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
                authRepository.reloadUser()
                checkAuthState()
            } catch (e: FirebaseAuthException) {
                updateFormState(firebaseError = getJapaneseErrorMessage(e.errorCode))
                _event.emit(SignUpEvent.Failure(getJapaneseErrorMessage(e.errorCode)))
            } catch (_: Exception) {
                updateFormState(firebaseError = "予期しないエラーが発生しました")
                _event.emit(SignUpEvent.Failure("予期しないエラーが発生しました"))
            }
        }
    }

    private fun checkAuthState() {
        val currentUser = authRepository.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            _authState.value = AuthState.LoggedIn(currentUser)
        } else {
            _authState.value = AuthState.LoggedOut
        }
    }

    private fun updateFormState(firebaseError: String? = null) {
        clearErrors()
        _formState.value = _formState.value.copy(firebaseError = firebaseError)
    }
}
