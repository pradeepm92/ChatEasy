package com.example.chateasy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Chats : BottomBarScreen(
        route = "CHATS",
        title = "Chats",
        icon = Icons.Default.Chat
    )

    object groups : BottomBarScreen(
        route = "GROUPS",
        title = "Groups",
        icon = Icons.Default.SupervisedUserCircle
    )

    object Call : BottomBarScreen(
        route = "CALL",
        title = "Call",
        icon = Icons.Default.Call
    )
    object Profile : BottomBarScreen(
        route = "PROFILE",
        title = "Profile",
        icon = Icons.Default.Person
    )
}
