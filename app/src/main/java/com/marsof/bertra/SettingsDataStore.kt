package com.marsof.bertra

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {

    // Settings keys
    companion object {
        val DEFAULT_CIRCLES_NUMBER = intPreferencesKey("default_circles_number")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    suspend fun setDefaultCirclesNumber(nummber: Int) {
        context.dataStore.edit { preferences ->
            preferences[DEFAULT_CIRCLES_NUMBER] = nummber
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = enabled
        }
    }

    val defaultCirclesNumber: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[DEFAULT_CIRCLES_NUMBER] ?: 0 }
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_DARK_MODE] ?: false }


    // Removing values
    suspend fun clearDefaultCirclesNumber() {
        context.dataStore.edit { preferences -> preferences.remove(DEFAULT_CIRCLES_NUMBER) }
    }

    suspend fun clearDarkMode() {
        context.dataStore.edit { preferences -> preferences.remove(IS_DARK_MODE) }
    }

//    suspend fun clearAll() {
//        context.dataStore.edit { preferences ->
//            preferences.clear()
//        }

}