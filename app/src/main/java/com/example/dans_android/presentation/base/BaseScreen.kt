package com.example.dans_android.presentation.base

import com.example.dans_android.presentation.home.HomeScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dans_android.R
import com.example.dans_android.presentation.account.AccountScreen
import com.example.dans_android.presentation.routes.Screen

@Composable
fun BaseScreen(navController: NavController) {
    val navBaseController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val currentRoute = navController.currentDestination?.route

                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.home)) },
                    label = { Text(stringResource(R.string.home)) },
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        navController.navigate(Screen.Home.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                BottomNavigationItem(
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = stringResource(R.string.account)) },
                    label = { Text(stringResource(R.string.account)) },
                    selected = currentRoute == Screen.Account.route,
                    onClick = {
                        navController.navigate(Screen.Account.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navBaseController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Account.route) { AccountScreen(navController) }
        }
    }
}