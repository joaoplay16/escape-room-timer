package com.playlab.escaperoomtimer.ui.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore("settings")

class PreferencesDataStore (val context: Context) {

    companion object {
        val FIRST_OPEN_KEY = stringPreferencesKey("first_open")
    }

    val isFirstOpen: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_OPEN_KEY].toBoolean()
        }

    suspend fun setIsFirstOpen(isFirstOpen: Boolean){
        context.dataStore.edit { preferences ->
            preferences[FIRST_OPEN_KEY] = isFirstOpen.toString()
        }
    }
}