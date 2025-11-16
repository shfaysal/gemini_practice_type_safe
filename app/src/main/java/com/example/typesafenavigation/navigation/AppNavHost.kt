package com.example.typesafenavigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.navigation
import com.example.typesafenavigation.screens.app.account.AccountScreen
import com.example.typesafenavigation.screens.app.home.HomeScreen
import com.example.typesafenavigation.screens.app.product.ProductDetailsScreen
import com.example.typesafenavigation.screens.app.search.SearchScreen
import com.example.typesafenavigation.screens.auth.login.LoginScreen
import com.example.typesafenavigation.screens.auth.signup.SignUpScreen
import com.example.typesafenavigation.viewmodel.ThemeViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    themeViewModel: ThemeViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AppGraph.Auth,
        modifier = modifier
    ) {
        navigation<AppGraph.Auth>(
            startDestination = Login
        ) {
            composable<Login> {
                LoginScreen(
                    navigateToSignUp = { navController.navigate(Signup) },
                    navigateToHome = {
                        navController.navigate(AppGraph.Main) {
                            popUpTo(AppGraph.Auth) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<Signup> {
                SignUpScreen(
                    navigateToLogin = { navController.navigate(Login) }
                )
            }
        }
        navigation<AppGraph.Main>(
            startDestination = Home
        ) {
            composable<Home> {
                HomeScreen(
                    navigateToProduct = { productId ->
                        navController.navigate(ProductDetails(productId))
                    }
                )
            }
            composable<Search> {
                SearchScreen()
            }
            composable<Account> {
                AccountScreen(
                    themeViewModel = themeViewModel,
                    navigateToLogin = {
                        navController.navigate(AppGraph.Auth) {
                            popUpTo(AppGraph.Main) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable<ProductDetails> {
                val args = it.toRoute<ProductDetails>()
                ProductDetailsScreen(productId = args.productId)
            }
        }
    }
}
