package com.example.chateasy.auth.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chateasy.Response
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel

@Composable
fun MobileNumber(navController: NavController, viewModel: AuthenticationViewmodel){
    val loginState by viewModel.loginState.collectAsState()
//    val otpState by viewModel.otpState.collectAsState()

    var phoneNumber by remember { mutableStateOf("+91") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = {
                // Allow editing only after the prefix (+91)
                if (it.startsWith("+91")) {
                    phoneNumber = it.filterIndexed { index, char ->
                        // Allow all digits and retain the "+91" prefix
                        index < 3 || char.isDigit()
                    }
                }
            },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Button(onClick = {
//            viewModel.sendOtp(phoneNumber, activity)
        }) {
            Text("Send OTP")
            Log.e("TAG", "Verify OTP ", )
        }

//        if (otpState is Response.Success && (otpState as Response.Success).data.isNotEmpty()) {
//            verificationId = (otpState as Response.Success).data
//        }

        if (verificationId.isNotEmpty()) {
            TextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Enter OTP") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
//                viewModel.verifyOtp(verificationId, otp)
            }) {
                Text("Verify OTP")
                Log.e("TAG", "Verify OTP ", )
            }
        }

        when (loginState) {
            is Response.Loading -> CircularProgressIndicator()
            is Response.Success -> {
                Text("Login successful!")
//                onLoginSuccess()
            }
            is Response.Error -> {
                (loginState as Response.Error).message?.let { Text(it, color = Color.Red) }
            }
            else -> {}
        }
    }
}