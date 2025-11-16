package com.example.typesafenavigation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(val route: Route, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(com.example.typesafenavigation.navigation.Home, Icons.Default.Home, "Home")
    data object Search : BottomNavItem(com.example.typesafenavigation.navigation.Search, Icons.Default.Search, "Search")
    data object Account : BottomNavItem(com.example.typesafenavigation.navigation.Account, Icons.Default.AccountCircle, "Account")
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Account
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentDestination?.route == item.route::class.qualifiedName,
                onClick = {
                    if (currentDestination?.route != item.route::class.qualifiedName) {
                        navController.navigate(item.route::class.qualifiedName!!) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
