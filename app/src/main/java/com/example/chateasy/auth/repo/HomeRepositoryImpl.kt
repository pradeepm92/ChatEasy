package com.example.chateasy.auth.repo



import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.tamilmedicine.authentication.domain.Response

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chateasy.domain.repository.HomeRepository

import com.google.firebase.storage.StorageException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeRepositoryImpl@Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseFirestore: FirebaseFirestore
) : HomeRepository {


    override suspend fun uploadImageWithText(imageUri: Uri, description: String): Response<String> {
        return try {
            val imageRef = firebaseStorage.reference.child("images/${UUID.randomUUID()}.jpg")
            imageRef.putFile(imageUri).await()

            val downloadUrl = imageRef.downloadUrl.await().toString()

            val imageData = hashMapOf(
                "imageUrl" to downloadUrl,
                "description" to description
            )
            firebaseFirestore.collection("uploads").add(imageData).await()

            Response.Success("Image uploaded successfully: $downloadUrl")
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "An unknown error occurred")
        }
    }


//    override suspend fun getMedicines(): List<MedicineList> {
//        return try {
//            val snapshot = firebaseFirestore.collection("uploads").get().await()
//            snapshot.documents.mapNotNull { document ->
//                val name = document.getString("description")
//                val imageUri = document.getString("imageUrl")
//                Log.e("TAG", "name: "+name, )
//                Log.e("TAG", "imageUri: "+imageUri, )
//                if (name != null && imageUri != null) {
//                    MedicineList(name, imageUri)
//                } else null
//            }
//        } catch (e: Exception) {
//            emptyList() // Return an empty list on failure
//        }
//    }




}
