package com.example.chateasy.graphs

object Graph {
    const val ROOT = "root_graph"
    const val Authenticate ="authentic_graph"
    const val MainScreen ="main_screen_graph"
    const val DETAILS = "details_graph"
}
sealed class AuthScreen(val route: String) {
    data object SplashScreen : AuthScreen("SplashScreen")
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Forgot : AuthScreen(route = "FORGOT")
    object Dashboard : AuthScreen(route = "Dashboard")
}
sealed class MainScreen(val route: String) {

    object MedicineList : MainScreen(route = "MedicineList")
    object Dashboard : MainScreen(route = "Dashboard")
}