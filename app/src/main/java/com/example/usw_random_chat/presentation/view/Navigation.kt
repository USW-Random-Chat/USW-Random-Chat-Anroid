package com.example.usw_random_chat.presentation.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.usw_random_chat.data.repository.ProfileRepository
import com.example.usw_random_chat.domain.usecase.ProfileUseCase
import com.example.usw_random_chat.presentation.ViewModel.ProfileViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LoadingScreen.route
    )
    {
        composable(route = Screen.SignInScreen.route) {
            SignInScreen(navController)
        }
        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController)
        }
        composable(route = Screen.SignUpDoneScreen.route) {
            SignUpDoneScreen(navController)
        }
        composable(route = Screen.PwChangeScreen.route) {
            PwChangeScreen(navController)
        }
        composable(route = Screen.PwSearchScreen.route) {
            PwSearchScreen(navController)
        }
        composable(route = Screen.IdSearchScreen.route) {
            IdSearch(navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(viewModel(),navController)
        }
        composable(route = Screen.FeedBackScreen.route) {
            FeedbackShow(navController)
        }
        composable(route = Screen.PolicyScreen.route) {
            PolicyScreen(navController)
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(navController, viewModel())
        }
        composable(route = Screen.LoadingScreen.route) {
            LoadingScreen(navController)
        }
        composable(route = Screen.ChatScreen.route) {
            ChattingScreen(navController)
        }
        composable(route = Screen.MainPageScreen.route) {
            MainScreen(navController)
        }
        composable(route = Screen.MatchingScreen.route) {
            MatchingScreen(navController = navController)
        }



    }
}