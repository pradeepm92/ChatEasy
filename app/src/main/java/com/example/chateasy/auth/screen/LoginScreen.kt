package com.example.chateasy.auth.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.chateasy.R
import com.example.chateasy.Response
import com.example.chateasy.auth.domain.repository.AuthenticationRepository
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel
import com.example.chateasy.graphs.AuthScreen
import com.example.chateasy.graphs.Graph
import com.example.chateasy.ui.theme.ChatEasyTheme
import com.example.chateasy.utils
import com.google.ai.client.generativeai.type.content
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


@Composable
fun LoginScreen(navController: NavController, viewModel: AuthenticationViewmodel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val loginflow = viewModel.loginflow.collectAsState()
    var isLoggingIn by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(150.dp)
                // Use size for both width and height
                .clip(CircleShape) // Clip the image to be circular
                .border(2.dp, Color.Gray, CircleShape) // Optional: Add a border if you like

        )



        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            label = { Text("Email") },
            isError = emailError != null,
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if (emailError != null) {
            Text(
                text = emailError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = if (password.length < 8) {
                    "Password must contain at least 8 characters"

                } else {
                    null // Clear error when the password is valid
                }
            },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            isError = passwordError != null,
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),


            )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .align(Alignment.Start)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))



            Text(text = "Forgot Password?", modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp)
                .clickable {
                    // Handle navigation to the Register screen
                    navController.navigate(AuthScreen.Forgot.route)
                }, textAlign = TextAlign.End)


       utils.GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            gradientColors = utils.buttonGradient, text = "Login",
            onClick = {


                var isValid = true

                // Email validation
                if (email.isEmpty()) {
                    emailError = "please enter the email"
                } else
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Invalid email format"
                        isValid = false
                    } else

                    // Password validation
                        if (password.isEmpty()) {
                            passwordError = "Please enter the password"
                            isValid = false
                        } else if (password.length <= 8) {
                            passwordError = "Password must contain atleast 8 characters"
                        } else {
                            viewModel.login(email, password)
                            isLoggingIn = true
                        }
//

            },

            )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left line
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray
            )

            // Center text
            Text(
                text = "Or",
                modifier = Modifier
                    .padding(horizontal = 8.dp),

                color = Color.Black,
                textAlign = TextAlign.Center
            )

            // Right line
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = Color.Gray
            )
        }
        GoogleSignInButton(navController = navController, viewModel = viewModel)
        Spacer(modifier = Modifier.height(5.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()

                .height(1.dp),
            color = Color.Gray
        )

        Text(text = "Don't have an account? Register", modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp)
            .clickable {
                // Handle navigation to the Register screen
                navController.navigate(AuthScreen.SignUp.route)
            }, textAlign = TextAlign.Center
        )
    }














    if (isLoggingIn) {

        loginflow.value.let {

            when (it) {
                is Response.Error -> {
                    val context = LocalContext.current
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }

                Response.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Response.Success -> {
                    LaunchedEffect(Unit) {
//
                        navController.navigate(Graph.MainScreen) {
                            popUpTo(AuthScreen.Login.route) { inclusive = true }
                        }
//                    navController.navigate(MainScreen.MedicineList.route) {
//                        popUpTo(AuthScreen.Login.route) { inclusive = true }
//                    }
                    }
                }

                null -> {
                    Log.e("TAG", "LoginScreen:null ")
                }


            }
        }
    }


}


@Composable
fun GoogleSignInButton(navController: NavController, viewModel: AuthenticationViewmodel) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()

    // Configure Google Sign-In
    val googleSignInClient = GoogleSignIn.getClient(
        activity,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID") // Replace with your actual Web Client ID from Firebase Console
            .requestEmail()
            .build()
    )

    // ActivityResultLauncher to handle result
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result
            if (account != null) {
                Log.e("account.idToken  ", " " + account.idToken)
                // Call firebaseAuthWithGoogle from the outside function
                firebaseAuthWithGoogle(account.idToken!!, auth, activity, navController)
            }
        }
    }



    Image(
        painter = painterResource(id = R.drawable.google), // Replace with your logo file
        contentDescription = "Sign in with Google",
        modifier = Modifier
            .padding(end = 8.dp)
            .size(50.dp)
            // Use size for both width and height

            .clickable {
                val signInIntent: Intent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
                Log.e("signInIntent", " " + signInIntent)
            },
    )

}

// Function to authenticate with Firebase using Google Sign-In
fun firebaseAuthWithGoogle(
    idToken: String,
    auth: FirebaseAuth,
    activity: Activity,
    navController: NavController
) {
    Log.e("Google Sign-In", "Authentication: ")
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign-in success

                val user = auth.currentUser
                Log.e("Google Sign-In", "Authentication:1 " + user)
                if (user != null) {
                    Log.e("Google Sign-In", "Authentication success: ")
                    // Navigate to main screen after successful login
                    navController.navigate(Graph.MainScreen) {
                        popUpTo(Graph.Authenticate) { inclusive = true }
                    }
                }
            } else {
                // If sign-in fails
                Log.e("Google Sign-In", "Authentication Failed: ${task.exception?.message}")
                Toast.makeText(activity, "Authentication Failed", Toast.LENGTH_SHORT).show()
            }
        }
}






