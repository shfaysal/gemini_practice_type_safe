package com.example.typesafenavigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.typesafenavigation.screens.MainScreen
import com.example.typesafenavigation.ui.theme.TypeSafeNavigationTheme
import com.example.typesafenavigation.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel by viewModels()
            val isDarkMode by themeViewModel.isDarkMode.collectAsState()

            TypeSafeNavigationTheme(darkTheme = isDarkMode) {
                MainScreen(themeViewModel = themeViewModel)
            }
        }
    }
}