package com.example.chateasy.auth.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import coil.compose.rememberAsyncImagePainter

@Composable
fun Profile(
    onLogoutClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onAccountSettingsClick: () -> Unit
) {
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Photo Section

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
                            .size(75.dp).background(color = Color.White, CircleShape)
                            .clip(CircleShape).border(2.dp, color = Color.Black, CircleShape)
                    )
                }

            }

        }


        Spacer(modifier = Modifier.height(16.dp))


        // Options Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileOption(
                title = "Edit Profile",
                icon = Icons.Default.Edit,
                onClick = onEditProfileClick
            )
            ProfileOption(
                title = "Settings",
                icon = Icons.Default.Settings,
                onClick = onAccountSettingsClick
            )
            ProfileOption(
                title = "Languages",
                icon = Icons.Default.Language,
                onClick = onLogoutClick
            )
            ProfileOption(
                title = "Contact Us",
                icon = Icons.Default.Contacts,
                onClick = onLogoutClick
            )
            ProfileOption(
                title = "Terms and Conditions",
                icon = Icons.Default.Receipt,
                onClick = onLogoutClick
            )
            ProfileOption(
                title = "Logout",
                icon = Icons.Default.Logout,
                onClick = onLogoutClick
            )
        }
    }

}
@Composable
fun ProfileOption(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick), // Card itself is clickable
        shape = MaterialTheme.shapes.medium, // Optional: Rounded corners
        elevation = cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start=10.dp,end = 16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.weight(1f)) // Spacer to push the arrow to the end
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }

}

