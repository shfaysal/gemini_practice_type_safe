package com.example.typesafenavigation.screens.app.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.typesafenavigation.components.AppButton
import com.example.typesafenavigation.viewmodel.ThemeViewModel

@Composable
fun AccountScreen(
    themeViewModel: ThemeViewModel,
    navigateToLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppButton(
            onClick = { themeViewModel.toggleTheme() },
            text = "Toggle Theme"
        )
        AppButton(
            onClick = {
                navigateToLogin()
            },
            text = "Logout"
        )
    }
}
