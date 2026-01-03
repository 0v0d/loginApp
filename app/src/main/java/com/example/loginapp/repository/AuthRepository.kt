package com.example.loginapp.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun reloadUser()
    suspend fun logIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun logOut()
    suspend fun deleteAccount()

}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser? get() = auth.currentUser

    override suspend fun reloadUser() {
        currentUser?.reload()?.await()
    }

    override suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        currentUser?.sendEmailVerification()?.await()
        Log.d("AuthRepositoryImpl", "SignUp: Verification email sent to $email")
        Log.d("AuthRepositoryImpl", "SignUp: Verification email sent to $currentUser")
    }

    override suspend fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
        if (currentUser?.isEmailVerified != false) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(email.substringBefore('@'))
                .build()
            currentUser?.updateProfile(profileUpdates)?.await()
        } else {
            throw FirebaseAuthException(
                "ERROR_EMAIl_IS_NOT_VERIFIED", "Email not verified"
            )
        }
    }

    override suspend fun logOut() {
        if (currentUser?.isAnonymous == true) {
            currentUser?.delete()?.await()
        }
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        currentUser?.delete()?.await()
        logOut()
    }
}
