package com.example.chateasy.auth

data class User(val name: String,
                val email: String,
                val profilePhotoUrl: String? = null)
