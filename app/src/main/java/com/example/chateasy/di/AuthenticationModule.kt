package com.example.chateasy.di

import android.content.Context
import com.example.chateasy.auth.domain.repository.AuthenticationRepository
import com.example.chateasy.auth.repo.AuthenticationRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton




@InstallIn(SingletonComponent::class)
@Module
object AuthenticationModule {

    @Provides
    @Singleton
    fun providesAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore,
         context: Context

    ): AuthenticationRepository {
        return AuthenticationRepositoryImpl(
            firebaseAuth = firebaseAuth,
            firestore = firestore,
                   context= context

        )
    }



}
