package com.example.chateasy

import android.app.Activity
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chateasy.BottomBarScreen
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthenticationViewmodel
) {

//    Scaffold(
//        topBar = {SimpleLightTopAppBar("Tamil Medicine")},
//        bottomBar = { BottomBar(navController = navController) }) { padding ->
//        var modifier = Modifier.padding(padding)
////        MainNavGraph(navController = navController,viewModel=viewModel)
//    }
}

@ExperimentalMaterial3Api
@ExperimentalLayoutApi
@Composable
fun SimpleLightTopAppBar(
    title: String,
    onNavigationClick: (() -> Unit)? = null // Optional callback for navigation actions
) {
    // Extract the activity outside the onClick context
    val act = LocalContext.current as? Activity

    TopAppBar(
        title = {
            Text(title, style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    onNavigationClick?.invoke() ?: act?.finish()
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavHostController, onItemSelected: (String) -> Unit) {
    val screens = listOf(
        BottomBarScreen.Chats,
        BottomBarScreen.Call,
        BottomBarScreen.groups,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                    onItemSelected = onItemSelected
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController,
    onItemSelected: (String) -> Unit
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
            onItemSelected(screen.route)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
//
@Composable
fun CustomTopAppBar(
    title: String,
    onCameraClick: () -> Unit,
    onMenuOptionClick: (String) -> Unit,
    menuOptions: List<String>
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f) // Pushes the icons to the end
                )

                IconButton(onClick = onCameraClick) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = "Camera"
                    )
                }

                IconButton(onClick = { menuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu"
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    offset = DpOffset(x = (-40).dp, y = 0.dp)
                ) {
                    menuOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                menuExpanded = false
                                onMenuOptionClick(option)
                            }
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

