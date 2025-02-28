package com.example.loginapp.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun logIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun logOut()
    suspend fun deleteAccount()
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser? get() = auth.currentUser

    override suspend fun signUp(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(email.substringBefore('@')) // メールアドレスからユーザー名部分を抽出
                .build()

            // 3. ユーザープロフィールを更新
            currentUser?.updateProfile(profileUpdates)?.await()
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Unexpected error: ${e.message}")
        }
    }

    override suspend fun logIn(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("AuthRepositoryImpl", "Unexpected error: ${e.message}")
        }
    }

    override suspend fun logOut() {
        if (auth.currentUser?.isAnonymous == true) {
            auth.currentUser?.delete()?.await()
        }
        auth.signOut()
    }

    override suspend fun deleteAccount() {
        auth.currentUser?.delete()?.await()
        logOut()
    }
}
