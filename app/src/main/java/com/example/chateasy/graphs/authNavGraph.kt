package com.example.chateasy.graphs

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.chateasy.auth.screen.ForgotPasswordScreen
import com.example.chateasy.auth.screen.LoginScreen
import com.example.chateasy.auth.screen.MobileNumber
import com.example.chateasy.auth.screen.RegisterScreen
import com.example.chateasy.auth.screen.SplashScreen
import com.example.chateasy.auth.viewmodel.AuthenticationViewmodel



fun NavGraphBuilder.authNavGraph(navController: NavHostController,viewModel: AuthenticationViewmodel) {
    Log.e("TAG", "NavigationGraph: ", )
    navigation(route = Graph.Authenticate, startDestination = AuthScreen.SplashScreen.route) {


        composable(route = AuthScreen.SplashScreen.route){

            SplashScreen(navController = navController)
        }
        composable(route = AuthScreen.Login.route) {

            LoginScreen(navController = navController,viewModel=viewModel)
        }
        composable(route = AuthScreen.SignUp.route) {
            RegisterScreen(navController = navController,viewModel=viewModel)
        }
        composable(route = AuthScreen.Forgot.route) {
             ForgotPasswordScreen(navController = navController,viewModel=viewModel)
        }

    }

}

