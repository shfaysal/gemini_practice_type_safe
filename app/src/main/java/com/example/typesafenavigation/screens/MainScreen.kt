package com.example.typesafenavigation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.typesafenavigation.navigation.BottomNavItem
import com.example.typesafenavigation.navigation.BottomNavigationBar
import com.example.typesafenavigation.navigation.AppNavHost
import com.example.typesafenavigation.viewmodel.ThemeViewModel

@Composable
fun MainScreen(themeViewModel: ThemeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            val showBottomBar = when (currentRoute) {
                BottomNavItem.Home.route::class.qualifiedName,
                BottomNavItem.Search.route::class.qualifiedName,
                BottomNavItem.Account.route::class.qualifiedName -> true
                else -> false
            }
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        AppNavHost(
            navController = navController,
            themeViewModel = themeViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
