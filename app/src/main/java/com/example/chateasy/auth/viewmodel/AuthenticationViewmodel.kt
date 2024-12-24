package com.example.chateasy.auth.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chateasy.Response
import com.example.chateasy.auth.User
import com.example.chateasy.auth.domain.repository.AuthenticationRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth


import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewmodel @Inject constructor(
    private val repository: AuthenticationRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {


    private var _login = MutableStateFlow<Response<FirebaseUser>?>(null)
    var loginflow: StateFlow<Response<FirebaseUser>?> = _login

    private var _signup = MutableStateFlow<Response<FirebaseUser>?>(null)
    val signupflow: StateFlow<Response<FirebaseUser>?> = _signup

    private val _loginState = MutableStateFlow<Response<FirebaseUser>?>(null)
    val loginState: StateFlow<Response<FirebaseUser>?> = _loginState
    private val _images = MutableStateFlow<Uri?>(null)
    var images: StateFlow<Uri?> = _images

//    private val _otpState = MutableStateFlow<Response<String>?>(null) // For OTP-specific responses
//    val otpState: StateFlow<Response<String>?> = _otpState
fun setImage(imageUri: Uri?) {
    _images.value = imageUri
}

    val currentUser: FirebaseUser?
        get() = repository.currentUser
//    val loginflow = MutableStateFlow<Response<FirebaseUser>?>(null)

    init {
        if (repository.currentUser != null) {
            _loginState.value = Response.Success(repository.currentUser!!)
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _login.value = Response.Loading
        val result = repository.firebaseLogin(email, password)

        _login.value = result
    }




    fun signup(context: Context, name: String, email: String, password: String) = viewModelScope.launch {
        _signup.value = Response.Loading
        try {
            val imageUri = _images.value
            if (imageUri == null) {
                _signup.value = Response.Error("Profile image is required.")
                return@launch
            }

            val authResult = repository.firebaseSignUp( name, email, password, imageUri,context)

            if (authResult is Response.Success) {
                _signup.value = Response.Success(authResult.data)
            } else {
                _signup.value = authResult
            }
        } catch (e: Exception) {
            _signup.value = Response.Error("An error occurred: ${e.message}")
        }
    }



    //    fun sendOtp(phoneNumber: String, activity: Activity) {
//        _otpState.value = Response.Loading
//        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
//            .setPhoneNumber(phoneNumber)
//            .setTimeout(60L, TimeUnit.SECONDS)
//            .setActivity(activity)
//            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                    signInWithCredential(credential)
//                }
//
//                override fun onVerificationFailed(e: FirebaseException) {
//                    _otpState.value = Response.Error("OTP verification failed: ${e.message}")
//                    Log.e("TAG", "onVerificationFailed: "+e.message, )
//                }
//
//                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
//                    _otpState.value = Response.Success(verificationId) // Verification ID sent successfully
//                    Log.e("TAG", "onCodeSent "+verificationId, )
//                }
//            }).build()
//        PhoneAuthProvider.verifyPhoneNumber(options)
//    }
//
//    fun verifyOtp(verificationId: String, otp: String) {
//        _otpState.value = Response.Loading
//        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
//        signInWithCredential(credential)
//    }
//
//    private fun signInWithCredential(credential: PhoneAuthCredential) {
//        firebaseAuth.signInWithCredential(credential)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val user = task.result?.user
//                    if (user != null) {
//                        _loginState.value = Response.Success(user)
//                        Log.e("TAG", "signInWithCredential:+Login successful ", )
//                        _otpState.value = Response.Success("Login successful")
//                    } else {
//                        _loginState.value = Response.Error("Failed to retrieve user details")
//                        Log.e("TAG", "signInWithCredential:+Login failed ", )
//                    }
//                } else {
//                    _otpState.value = Response.Error("Login failed: ${task.exception?.message}")
//                    Log.e("TAG", "Login failed::+Login Login failed: ", )
//                }
//            }
//    }
fun googleSignIn(data: Intent?) = viewModelScope.launch {
    _login.value = Response.Loading
    val result = repository.googleSignIn(data)
    _login.value = result
}
    fun logout() {
        repository.logout()
        _loginState.value = null
//        _otpState.value = null
    }

}


