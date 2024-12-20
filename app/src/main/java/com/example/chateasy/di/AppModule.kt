package com.example.chateasy.di


import android.app.Application
import android.content.Context
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
object AppModule {
    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseFireStore():FirebaseFirestore{
        return FirebaseFirestore.getInstance()
    }
    @Provides
    @Singleton
    fun provideFirebaseStorage():FirebaseStorage{
        return FirebaseStorage.getInstance()
    }
}