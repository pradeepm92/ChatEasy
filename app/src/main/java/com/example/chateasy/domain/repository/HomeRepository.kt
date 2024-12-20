package com.example.chateasy.domain.repository

import android.content.Context
import android.net.Uri
import com.example.tamilmedicine.authentication.domain.Response


interface HomeRepository {
    suspend fun uploadImageWithText(
        imageUri: Uri,
        description: String
    ): Response<String>




}