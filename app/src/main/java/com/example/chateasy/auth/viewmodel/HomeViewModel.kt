package com.example.chateasy.auth.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chateasy.domain.repository.HomeRepository
import com.example.tamilmedicine.authentication.domain.Response

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel@Inject constructor(
    private val repository: HomeRepository
):ViewModel(){

//    private val _uploadState = MutableStateFlow<Response<String>>(Response.Loading)
//    val uploadState: StateFlow<Response<String>> = _uploadState
//
//
//
//    var images by remember { mutableStateOf<Uri?>(null) }
//
//    var text by rememberSaveable { mutableStateOf("") }
//
//
//
//    fun uploadImageWithText(context: Context, image: Uri?, text: String) {
//        if (image != null) {
//            viewModelScope.launch {
//                _uploadState.value = Response.Loading
//                val response = repository.uploadImageWithText( image, text)
//
//                // Handle the upload result
//                when (response) {
//                    is Response.Success -> {
//                        // Show success toast
//                        Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
//                        images = null
//                        this@HomeViewModel.text = ""
//                    }
//                    is Response.Error -> {
//                        // Show error toast
//                        Toast.makeText(context, "Upload failed: ${response.message}", Toast.LENGTH_SHORT).show()
//                    }
//                    else -> {
//                        // Handle any other cases (unlikely)
//                        Toast.makeText(context, "Unexpected response.", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                // Update the state with the result
//                _uploadState.value = response
//            }
//        } else {
//            // Handle null image case
//            Toast.makeText(context, "No image selected!", Toast.LENGTH_SHORT).show()
//        }
//    }


//    private val _medicines = MutableStateFlow<List<MedicineList>>(emptyList())
//    val medicines: StateFlow<List<MedicineList>> = _medicines

    private val _uploadState = MutableStateFlow<Response<String>>(Response.Loading)
    val uploadState: StateFlow<Response<String>> = _uploadState

    // State for image and text
    private val _images = MutableStateFlow<Uri?>(null)
    var images: StateFlow<Uri?> = _images

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text

    fun setImage(imageUri: Uri?) {
        _images.value = imageUri
    }

    fun setText(newText: String) {
        _text.value = newText
    }
    fun clearFields() {
        setImage(null)
        setText("")
    }

    fun uploadImageWithText() {
        val image = _images.value
        val text = _text.value

        if (image != null&& text!=null) {
            viewModelScope.launch {
                _uploadState.value = Response.Loading
                val response = repository.uploadImageWithText(image, text)
                _uploadState.value = response

                // Clear image and text fields after successful upload
                if (response is Response.Success) {
                    _images.value = null
                    _text.value = ""
                }
            }
        } else {
            _uploadState.value = Response.Error("No image selected!")
        }
    }

//    fun fetchMedicines() {
//        viewModelScope.launch {
//            val medicineList = repository.getMedicines()
//            Log.e("TAG", "fetchMedicines: "+medicineList, )
////            _medicines.value = medicineList
//        }
//    }
}


