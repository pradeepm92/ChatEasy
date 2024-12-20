package com.example.chateasy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel
import com.example.chateasy.auth.viewmodel.HomeViewModel
import com.example.chateasy.graphs.Graph
import com.example.chateasy.graphs.MainNavGraph
import com.example.chateasy.graphs.authNavGraph
import com.example.chateasy.ui.theme.ChatEasyTheme
import dagger.hilt.android.AndroidEntryPoint


import kotlinx.coroutines.launch
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val viewModel by viewModels<AuthenticationViewmodel>()
    val homeviewModel by viewModels<HomeViewModel>()
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ChatEasyTheme {
                val navController = rememberNavController()

                val currentBackStackEntry = navController.currentBackStackEntryAsState()
//                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                // Define the list of main screen routes that should show the top and bottom bars
                val mainRoutes = listOf(
                    BottomBarScreen.Chats.route,
                    BottomBarScreen.Call.route,
                    BottomBarScreen.groups.route,
                    BottomBarScreen.Profile.route,

                )
                val menuOptions = rememberMenuOptions()

                val onBottomBarItemSelected: (String) -> Unit = { selectedRoute ->
                    menuOptions.value = when (selectedRoute) {
                        BottomBarScreen.Chats.route -> listOf("Chats", "Settings", )
                        BottomBarScreen.Call.route -> listOf("Clear Call Log", " Settings")
                        BottomBarScreen.groups.route -> listOf("New Group", "Leave Group")
                        BottomBarScreen.Profile.route -> listOf("Edit Profile", "Settings","Logout")
                        else -> emptyList()
                    }
                }


                // Check if the current route is one of the main navigation routes
                val showBars = currentBackStackEntry.value?.destination?.route in mainRoutes



                val drawerItems = listOf(
                    "chats" to BottomBarScreen.Chats.route,
                    "groups" to BottomBarScreen.Profile.route,
                    "call" to BottomBarScreen.Call.route,
                    "profile" to BottomBarScreen.Profile.route
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        Box(
                            modifier = Modifier
                                .width(LocalConfiguration.current.screenWidthDp.dp / 2) // Set width to half the screen
                                .fillMaxHeight()
                                .background(MaterialTheme.colorScheme.background) // Optional background
                        )
                        Column {
                            Text(
                                text = "Dashboard",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(20.dp)
                            )
                            drawerItems.forEach { (title, route) ->
                                TextButton(
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                        scope.launch { drawerState.close() }
                                    }
                                ) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(5.dp),
                                        color = Color.Black,
                                    )

                                }
                            }
                        }
                    },
                    scrimColor = Color.LightGray

                ) {
                    Scaffold(
                        topBar = {
                            if (showBars) {
                                CustomTopAppBar(title = "Chat Easy", onCameraClick = {
                                    // Handle camera click here
                                },
                                    onMenuOptionClick = {
                                        // Show your dialog or menu here
                                    }, menuOptions = menuOptions.value
                                )
//
                            }
                        },
                        bottomBar = {
                            if (showBars) {
                                BottomBar(navController = navController,onItemSelected = onBottomBarItemSelected)
                            }
                        }
                    ) { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            NavHost(
                                navController = navController,
                                startDestination = Graph.Authenticate,
                                route = Graph.ROOT
                            ) {
                                authNavGraph(navController, viewModel)
                                MainNavGraph(
                                    navController = navController,
                                    viewModel = homeviewModel
                                )
                            }
                        }
                    }
                }
            }
            }
        }
    }

@Composable
fun rememberMenuOptions(): MutableState<List<String>> {
    return remember { mutableStateOf(listOf("Profile", "Settings", "Logout")) }
}

