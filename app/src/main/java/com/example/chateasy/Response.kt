package com.example.chateasy

sealed class Response<out T> {

    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String? = null) :
        Response<Nothing>()

    object Loading : Response<Nothing>()

}