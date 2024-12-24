package com.example.chateasy.auth.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.collection.emptyIntSet

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
import com.google.firebase.storage.StorageException


import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException
import java.util.UUID
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
        profilePhotoUri: Uri,
        context: Context
    ): Response<FirebaseUser> {
        return try {
            // Create the user with Firebase Authentication
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val file = getFileFromUri(context, profilePhotoUri)
                if (file == null) {
                    Log.e("TAG", "Failed to resolve file from URI.")
                    return Response.Error("Invalid profile photo.")
                }

                val imageRef = FirebaseStorage.getInstance().reference.child("images/${UUID.randomUUID()}.jpg")

                try {
                    // Upload the file to Firebase Storage
                    val fileUri = Uri.fromFile(file)
                    Log.e("TAG", "File upload fileUri: ${fileUri}")
                    imageRef.putFile(fileUri).await()
                    val downloadUrl = imageRef.downloadUrl.await().toString()
                    Log.e("TAG", "File upload downloadUrl: ${downloadUrl}")
                    val userData = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "imageUrl" to downloadUrl
                    )
                    firestore.collection("users").document(firebaseUser.uid).set(userData).await()

                    Response.Success(firebaseUser)
                } catch (e: Exception) {
                    Log.e("TAG", "File upload failed: ${e.message}")
                    Response.Error("Profile photo upload failed.")
                }
            } else {
                Log.e("TAG", "User creation failed.")
                Response.Error("User creation failed.")
            }
        } catch (e: Exception) {
            Log.e("TAG", "Sign-up failed: ${e.message}")
            Response.Error("Sign-up failed: ${e.message}")
        }
    }
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "temp_file_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()
            file
        } catch (e: Exception) {
            Log.e("TAG", "Error resolving file from URI: ${e.message}")
            null
        }
    }



//    override suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String? {
//        return try {
//            val contentResolver = context.contentResolver
//            val inputStream = contentResolver.openInputStream(profilePhotoUri)
//
//            if (inputStream != null) {
//                val fileName = "profile_photo_${System.currentTimeMillis()}.jpg"
//                val storageRef = FirebaseStorage.getInstance()
//                    .reference.child("profile_photos/$fileName")
//
//                val uploadTask = storageRef.putStream(inputStream).await()
//                val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()
//                Log.e("TAG", "downloadUrl"+downloadUrl)
//                if (downloadUrl != null) {
//                    Log.e("TAG", "Profile photo uploaded successfully: $downloadUrl")
//                    return downloadUrl.toString()
//                } else {
//                    Log.e("TAG", "Download URL is null after upload")
//                    return null
//                }
//            } else {
//                Log.e("TAG", "InputStream is null for URI: $profilePhotoUri")
//                return null
//            }
//        } catch (e: StorageException) {
//            Log.e("TAG", "Firebase Storage Exception: ${e.message}")
//            return null
//        } catch (e: IOException) {
//            Log.e("TAG", "I/O Exception during upload: ${e.message}")
//            return null
//        } catch (e: Exception) {
//            Log.e("TAG", "Profile photo upload failed: ${e.message}")
//            return null
//        }
//    }
//    override suspend fun saveUserDataToFirestore(userId: String, user: User) {
//
//
//        firestore.collection("users").document(userId).set(user).await()
//    }


//    override suspend fun uploadProfilePhotoToStorage(profilePhotoUri: Uri): String? {
//        return try {
//            // Resolve URI to InputStream
//
//            val contentResolver = context.contentResolver
//            val inputStream = contentResolver.openInputStream(profilePhotoUri)
//
//            if (inputStream != null) {
//                // Create a unique file name for the profile photo (e.g., based on current time)
//                val fileName = "profile_photo_${System.currentTimeMillis()}.jpg"
//
//                // Reference to Firebase Storage
//                val storageRef = FirebaseStorage.getInstance()
//                    .reference.child("profile_photos/$fileName")
//
//                // Upload the InputStream to Firebase Storage
//                val uploadTask = storageRef.putStream(inputStream).await()
//
//                // Retrieve the download URL after the upload is complete
//                val downloadUrl = uploadTask.metadata?.reference?.downloadUrl?.await()
//
//                if (downloadUrl != null) {
//                    Log.d("TAG", "Profile photo uploaded successfully: $downloadUrl")
//                    return downloadUrl.toString()
//                } else {
//                    Log.e("TAG", "Download URL is null")
//                    return null
//                }
//            } else {
//                Log.e("TAG", "Failed to resolve input stream for URI: $profilePhotoUri")
//                return null
//            }
//        } catch (e: Exception) {
//            Log.e("TAG", "Profile photo upload failed: ${e.message}")
//            return null
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
