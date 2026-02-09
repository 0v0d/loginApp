package com.example.loginapp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    val currentUser: FirebaseUser?
    val authChangeNotifier: SharedFlow<Unit>
    suspend fun reloadUser()
    suspend fun logIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun logOut()
    suspend fun deleteAccount()
    suspend fun resendVerificationEmail()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser

    private val _authChangeNotifier = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val authChangeNotifier: SharedFlow<Unit> = _authChangeNotifier.asSharedFlow()

    private fun notifyAuthChanged() {
        _authChangeNotifier.tryEmit(Unit)
    }

    override suspend fun reloadUser() {
        currentUser?.reload()?.await()
    }

    override suspend fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
        val user = auth.currentUser ?: return
        user.reload().await()
        if (auth.currentUser?.isEmailVerified == true && auth.currentUser?.displayName.isNullOrBlank()) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(email.substringBefore('@'))
                .build()
            auth.currentUser?.updateProfile(profileUpdates)?.await()
        }
        notifyAuthChanged()
    }

    override suspend fun signUp(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(email.substringBefore('@'))
            .build()
        user.updateProfile(profileUpdates).await()
        user.sendEmailVerification().await()
        notifyAuthChanged()
    }

    override suspend fun logOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()?.await()
        }
        auth.signOut()
        notifyAuthChanged()
    }

    override suspend fun deleteAccount() {
        auth.currentUser?.delete()?.await()
        notifyAuthChanged()
    }

    override suspend fun resendVerificationEmail() {
        currentUser?.sendEmailVerification()?.await()
    }
}
