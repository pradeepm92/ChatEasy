package com.example.chateasy.graphs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chateasy.BottomBarScreen
import com.example.chateasy.auth.screen.Profile
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel
import com.example.chateasy.auth.viewmodel.HomeViewModel


fun NavGraphBuilder. MainNavGraph(navController: NavHostController, viewModel: HomeViewModel) {

    navigation(route = Graph.MainScreen, startDestination = BottomBarScreen.Chats.route)
    {
            composable(route = BottomBarScreen.Chats.route) {
//                ChatScreen(navController = navController, viewModel = viewModel)
            }
            composable(route = BottomBarScreen.Profile.route) {


                        Profile(
                            onLogoutClick = {
                                // Handle Logout
                                navController.navigate(Graph.Authenticate) // Example to navigate to login screen
                            },
                            onEditProfileClick = {
                                // Handle Edit Profile
//                                navController.navigate("edit_profile_route") // Example navigation
                            },
                            onAccountSettingsClick = {
                                // Handle Account Settings
//                                navController.navigate("account_settings_route") // Example navigation
                            }
                        )}

            }
            composable(route = BottomBarScreen.Call.route) {
                ScreenContent(
                    name = BottomBarScreen.Call.route,
                    onClick = {

                    }
                )
            }
        composable(route = BottomBarScreen.groups.route) {
            ScreenContent(
                name = BottomBarScreen.groups.route,
                onClick = {

                }
            )
        }
            detailsNavGraph(navController = navController)
        }





fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.BTM_DETAIL_PAGE.route
    ) {
        composable(route = DetailsScreen.BTM_DETAIL_PAGE.route) {
            ScreenContent(name = "Detail Page") {
                navController.navigate(DetailsScreen.BTM_SUB_DETAILS_PAGE.route)
            }
        }
        composable(route = DetailsScreen.BTM_SUB_DETAILS_PAGE.route) {
            ScreenContent(name = "Sub Detail Page") {}
        }
    }
}
@Composable
fun ScreenContent(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = name,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

sealed class DetailsScreen(val route: String) {
    object BTM_DETAIL_PAGE : DetailsScreen(route = "DETAIL_PAGE_")
    object BTM_SUB_DETAILS_PAGE : DetailsScreen(route = "DETAIL_PAGE_SUB")
}