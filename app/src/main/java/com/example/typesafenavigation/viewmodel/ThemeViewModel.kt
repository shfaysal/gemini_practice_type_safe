package com.example.typesafenavigation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.typesafenavigation.datastore.ThemeDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val themeDataStore: ThemeDataStore) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        viewModelScope.launch {
            themeDataStore.isDarkMode.collect { isDarkMode ->
                _isDarkMode.value = isDarkMode
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            themeDataStore.setDarkMode(!_isDarkMode.value)
        }
    }
}
