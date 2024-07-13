package com.example.dans_android.presentation.routes

import android.util.Log
import com.example.dans_android.presentation.login.LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dans_android.presentation.account.AccountScreen
import com.example.dans_android.presentation.base.BaseScreen
import com.example.dans_android.presentation.home.HomeScreen
import com.example.dans_android.presentation.job_detail.JobDetailScreen

@Preview(showBackground = true)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screen.Base.route) {
            BaseScreen(navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Account.route) {
            AccountScreen(navController)
        }
        composable(
            route = "${Screen.JobDetail.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            Log.d("JobDetailScreen", "Job ID: $id")
            if (id != null) {
                JobDetailScreen(jobId = id)
            } else {
                Log.e("JobDetailScreen", "empty id")
            }
        }
    }
}

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Base : Screen("base")
    data object Home : Screen("home")
    data object Account : Screen("account")
    data object JobDetail : Screen("job_detail")
}
