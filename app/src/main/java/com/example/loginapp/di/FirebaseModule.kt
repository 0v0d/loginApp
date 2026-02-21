package com.example.loginapp.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.example.loginapp.R
import com.example.loginapp.repository.AuthRepository
import com.example.loginapp.repository.AuthRepositoryImpl
import com.example.loginapp.repository.GoogleCredentialHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager =
        CredentialManager.create(context)

    @Provides
    @Singleton
    fun provideGoogleCredentialHelper(
        credentialManager: CredentialManager,
        @ApplicationContext context: Context
    ): GoogleCredentialHelper = GoogleCredentialHelper(
        credentialManager,
        context.getString(R.string.default_web_client_id)
    )

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        googleCredentialHelper: GoogleCredentialHelper
    ): AuthRepository = AuthRepositoryImpl(auth, googleCredentialHelper)
}
