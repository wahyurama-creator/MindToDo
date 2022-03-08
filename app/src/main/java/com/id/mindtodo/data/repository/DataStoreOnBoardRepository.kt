package com.id.mindtodo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.id.mindtodo.ui.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStores: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCE_NAME_ONBOARD)

class DataStoreOnBoardRepository(context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(name = Constants.PREFERENCE_KEY_ONBOARD)
    }

    private val dataStores = context.dataStores

    suspend fun saveOnBoardingState(completed: Boolean) {
        dataStores.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    fun readOnBoardingState(): Flow<Boolean> {
        return dataStores.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map {
                val onBoardingState = it[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }
}