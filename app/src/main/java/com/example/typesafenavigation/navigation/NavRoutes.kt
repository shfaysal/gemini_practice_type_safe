package com.example.typesafenavigation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppGraph {
    @Serializable
    data object Auth : AppGraph
    @Serializable
    data object Main : AppGraph
}

@Serializable
sealed interface Route

@Serializable
data object Login : Route

@Serializable
data object Signup : Route

@Serializable
data object Home : Route

@Serializable
data object Search : Route

@Serializable
data object Account : Route

@Serializable
data class ProductDetails(val productId: String) : Route
