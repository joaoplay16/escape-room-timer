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
        val RATE_BUTTON_CLICKED_kEY = stringPreferencesKey("rate_button_clicked")
    }

    val appOpensCount: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_OPEN_KEY]?.toInt() ?: 0
        }

    suspend fun setAppOpensCount(count: Int){
        context.dataStore.edit { preferences ->
            preferences[FIRST_OPEN_KEY] = count.toString()
        }
    }

    val isRateButtonClicked: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[RATE_BUTTON_CLICKED_kEY]?.toBoolean() ?: false
        }

    suspend fun setRateButtonClicked(isRateButtonClicked: Boolean){
        context.dataStore.edit { preferences ->
            preferences[RATE_BUTTON_CLICKED_kEY] = isRateButtonClicked.toString()
        }
    }
}