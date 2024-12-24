package com.example.chateasy.auth.domain.repository


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.chateasy.Response
import com.example.chateasy.auth.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow



interface AuthenticationRepository {
    val currentUser: FirebaseUser?


    suspend fun firebaseSignUp(
        name: String,
        email: String,
        password: String,
        profilePhotoUri: Uri,
        context: Context
    ): Response<FirebaseUser>


    suspend fun firebaseLogin(
        email: String,
        password: String
    ): Response<FirebaseUser>
    suspend fun googleSignIn(data: Intent?): Response<FirebaseUser>
    // Logout method
    fun logout()
//    suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String?
//
//
//
//    suspend fun saveUserDataToFirestore(userId: String, user: User)

}
