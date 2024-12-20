package com.example.chateasy.auth.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.collection.emptyIntSet
import androidx.compose.ui.window.application
import com.example.chateasy.Response
import com.example.chateasy.auth.User
import com.example.chateasy.auth.domain.repository.AuthenticationRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val context: Context
//    private val googleSignInClient: GoogleSignInClient
) : AuthenticationRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser



    override suspend fun firebaseSignUp(
        name: String,
        email: String,
        password: String,
        profilePhotoUri: Uri?
    ): Response<FirebaseUser> {
        return try {
            // Create the user with Firebase Authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                Log.e("TAG", "profilePhotoUrl: "+profilePhotoUri, )
                var profilePhotoUrl: String? = null
                Log.e("TAG", "profilePhotoUrl:1 "+profilePhotoUrl, )
                if (profilePhotoUri != null) {
                    Log.d("TAG", "Uploading profile photo: $profilePhotoUri")
                    val filePath = profilePhotoUri.path
                    Log.e("TAG", "Profile photo file path: $filePath")
                    profilePhotoUrl = uploadProfilePhotoToStorage(profilePhotoUri)
                } else {
                    Log.e("TAG", "Profile photo URI is null")
                }
                val user = User(
                    name = name,
                    email = email,
                    profilePhotoUrl = profilePhotoUrl
                )
                // Save user data to Firestore
                Log.e("TAG", "firebaseSignUp: "+user, )
                saveUserDataToFirestore(firebaseUser.uid,user)

                Response.Success(firebaseUser)
            } else {
                Log.e("TAG", "User creation failed: ", )
                Response.Error("User creation failed.")

            }
        } catch (e: Exception) {
            Log.e("TAG", "User creation failed:1 "+e.message, )
            Response.Error("Sign-up failed: ${e.message}")
        }
    }

//    override suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String? {
//        val storageRef = FirebaseStorage.getInstance().reference.child("profile_photos/${profilePhotoUri.lastPathSegment}")
//        val uploadTask = storageRef.putFile(profilePhotoUri).await()
//        return uploadTask.metadata?.reference?.downloadUrl?.await().toString()
//    }
//    override suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String? {
//        return try {
//            val storageRef = FirebaseStorage.getInstance().reference.child("profile_photos/${profilePhotoUri.lastPathSegment}")
//            val uploadTask = storageRef.putFile(profilePhotoUri).await()
//            val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await().toString()
//            Log.e("TAG", "Profile photo uploaded successfully: $downloadUrl")
//            downloadUrl
//        } catch (e: Exception) {
//            Log.e("TAG", "Profile photo upload failed: ${e.message}")
//            null
//        }
//    }
    override suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String? {
        return try {
            // Resolve URI to InputStream

            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(profilePhotoUri)

            if (inputStream != null) {
                // Create a unique file name for the profile photo (e.g., based on current time)
                val fileName = "profile_photo_${System.currentTimeMillis()}.jpg"

                // Reference to Firebase Storage
                val storageRef = FirebaseStorage.getInstance()
                    .reference.child("profile_photos/$fileName")

                // Upload the InputStream to Firebase Storage
                val uploadTask = storageRef.putStream(inputStream).await()

                // Retrieve the download URL after the upload is complete
                val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()

                if (downloadUrl != null) {
                    Log.d("TAG", "Profile photo uploaded successfully: $downloadUrl")
                    return downloadUrl.toString()
                } else {
                    Log.e("TAG", "Download URL is null")
                    return null
                }
            } else {
                Log.e("TAG", "Failed to resolve input stream for URI: $profilePhotoUri")
                return null
            }
        } catch (e: Exception) {
            Log.e("TAG", "Profile photo upload failed: ${e.message}")
            return null
        }
    }

    override suspend fun saveUserDataToFirestore(userId: String, user: User) {


        firestore.collection("users").document(userId).set(user).await()
    }


//    override suspend fun firebaseSignUp(
//        name: String,
//        email: String,
//        password: String
//    ): Response<FirebaseUser> {
//        return try {
//            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
//
//            result.user?.let { user ->
//                // Update display name
//                user.updateProfile(
//                    UserProfileChangeRequest.Builder().setDisplayName(name).build()
//                ).await()
//
//                Response.Success(user) // Return success response with non-null user
//            } ?: Response.Error("User creation failed: user is null") // Handle the null case
//        } catch (e: Exception) {
//            Response.Error(e.localizedMessage ?: "An unknown error occurred") // Return error response directly
//        }
//    }


    override suspend fun firebaseLogin(email: String, password: String): Response<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            result.user?.let { user ->
              Response.Success(user) // Return success response with non-null user
            } ?:Response.Error("Login failed: user is null") // Handle the null case
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "An unknown error occurred") // Return error response directly
        }
    }



    // Firebase Login with PhoneAuthCredential
//    override suspend fun firebaseLogin(credential: PhoneAuthCredential): Response<FirebaseUser> {
//        return try {
//            // Authenticate user using the credential
//            val result = firebaseAuth.signInWithCredential(credential).await()
//            result.user?.let { user ->
//                Response.Success(user) // Return success response with non-null user
//            } ?: Response.Error("Login failed: user is null") // Handle the null case
//        } catch (e: Exception) {
//            Response.Error(e.localizedMessage ?: "An unknown error occurred") // Return error response directly
//        }
//    }


    override suspend fun googleSignIn(data: Intent?): Response<FirebaseUser> {
        return try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java) // Throws exception if sign-in failed
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            Response.Success(result.user!!)
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Google Sign-In failed")
        }
    }



    override fun logout() {
        firebaseAuth.signOut()
    }
}
