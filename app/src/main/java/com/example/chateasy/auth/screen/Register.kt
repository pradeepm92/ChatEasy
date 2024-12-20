package com.example.chateasy.auth.screen

import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.chateasy.R
import com.example.chateasy.Response
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel
import com.example.chateasy.graphs.AuthScreen
import com.example.chateasy.graphs.Graph
import com.example.chateasy.utils


@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthenticationViewmodel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) } // Define this for confirm password

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }
    val signinflow = viewModel.signupflow.collectAsState()
    val context = LocalContext.current
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // Handle the selected image URI
            profilePhotoUri = uri // Update the profile photo URI
            if (uri != null) {
                Toast.makeText(context, "Image Selected: $uri", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .clickable {
                    imagePickerLauncher.launch("image/*")
                }
        ) {   Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                color = Color.Black, // Stroke color
                radius = size.minDimension / 2, // Radius for the stroke
                style = Stroke(width = 1.dp.toPx()) // Stroke width
            )
        }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)

            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = Color.LightGray, // Stroke color
                        radius = size.minDimension / 2, // Radius for the stroke
                        style = Stroke(width = 1.dp.toPx()) // Stroke width
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        // Inner content size (image or placeholder)
                        .clip(CircleShape)
                ) {

                    if (profilePhotoUri == null) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Photo",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(120.dp)
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(profilePhotoUri),
                            contentDescription = "Profile Photo",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)

                        )
                    }
                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") }, // Trigger the image upload function
                        modifier = Modifier
                            .size(75.dp) // Size of the plus button
                            .align(Alignment.BottomEnd) // Position it at the bottom right
                            .padding(8.dp) // Padding from the edge
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // Plus icon
                            contentDescription = "Upload Profile Photo",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(75.dp)
                                .background(color = Color.White, CircleShape)
                                .clip(CircleShape)
                                .border(2.dp, color = Color.Black, CircleShape)
                        )
                    }

                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            isError = nameError != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if (nameError != null) {
            Text(
                text = nameError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .align(Alignment.Start)
            )}

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it
                            emailError=null},
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
            )}
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it
                passwordError = if (password.length < 8) {
                    "Password must contain at least 8 characters"

                } else {
                    null // Clear error when the password is valid
                }},
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            isError = passwordError != null,
            trailingIcon = {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp)
                    .align(Alignment.Start)
            )}
        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it
                confirmPasswordError =null
                            },
            label = { Text("Confirm Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            }, // Correct icon here
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                    Icon(
                        imageVector = if (confirmPasswordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null
                    )
                }
            },
            isError = confirmPasswordError != null,
            visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

            if (confirmPasswordError != null) {
                Text(
                    text = confirmPasswordError!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 8.dp, top = 4.dp)
                        .align(Alignment.Start)
                )}
        Spacer(modifier = Modifier.height(16.dp))
        utils.GradientButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(8.dp),
            gradientColors = utils.buttonGradient, text = "Register",
            onClick = {


                var isValid = true

                // Name validation
                if (name.isEmpty()) {
                    nameError = "please enter the name"
                    isValid = false
                } else

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
                            } else if (password != confirmPassword) {
                                confirmPasswordError = "Passwords do not match"
                                isValid = false
                            } else {
                                Log.e("TAG", "RegisterScreen:profilePhotoUri "+profilePhotoUri, )
                                viewModel.signup(name, email, password,profilePhotoUri)
                            }
//

            },

            )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Already have an account? Login", modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp)
            .clickable {
                // Handle navigation to the Register screen
                navController.popBackStack()
            }, textAlign = TextAlign.Center
        )

    }

    signinflow.value.let {

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
                    navController.navigate(Graph.MainScreen) {
                        popUpTo(AuthScreen.Dashboard.route) { inclusive = true }
                    }
                }
            }

            null -> {
                Log.e("TAG", "registerScreen:null ")
            }
        }
    }

}





