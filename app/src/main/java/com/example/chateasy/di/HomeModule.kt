package com.example.chateasy.di


import com.example.chateasy.auth.repo.HomeRepositoryImpl
import com.example.chateasy.domain.repository.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object HomeModule {
    @Provides
    @Singleton

    fun provideHomeRepository(
        firebaseStorage: FirebaseStorage,
        firebaseFirestore: FirebaseFirestore
    ): HomeRepository {
        return HomeRepositoryImpl(firebaseStorage, firebaseFirestore)
    }
}