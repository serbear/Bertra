package com.marsof.bertra.ui.viewmodels

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marsof.bertra.SettingsDataStore
import kotlinx.coroutines.launch


class SettingsScreenViewModel(application: Application) : ViewModel() {
     private val settingsRepository = SettingsDataStore(application)


    /**
     * Сохраняет выбранную пользователем тему и применяет её немедленно.
     * @param themeOption Новая тема (LIGHT, DARK или SYSTEM).
     */
    fun changeTheme(value: Boolean) {
        viewModelScope.launch {

            settingsRepository.setDarkMode(value)


            applyTheme(value)
        }
    }

    /**
     * Применяет выбранную тему ко всему приложению.
     */
    private fun applyTheme(value: Boolean) {
//        val mode = when (themeOption) {
//            ThemeOption.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
//            ThemeOption.DARK -> AppCompatDelegate.MODE_NIGHT_YES
//            ThemeOption.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//        }

        if (value){
            AppCompatDelegate.setDefaultNightMode( AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}