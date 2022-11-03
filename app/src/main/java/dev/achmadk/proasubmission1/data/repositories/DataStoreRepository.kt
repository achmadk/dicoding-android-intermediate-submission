package dev.achmadk.proasubmission1.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.achmadk.proasubmission1.services.UserPreferences
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = UserPreferences.NAME)

@Singleton
class DataStoreRepository @Inject constructor(
    private val context: Context
) {
    suspend fun putString(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    suspend fun putBoolean(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[preferenceKey] = value
        }
    }

    suspend fun getString(key: String): String? {
        return try {
            val preferenceKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferenceKey]
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getBoolean(key: String): Boolean? {
        return try {
            val preferenceKey = booleanPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            preferences[preferenceKey]
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun clearPreference(key: String) {
        val preferenceKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if (it.contains(preferenceKey)) {
                it.remove(preferenceKey)
            }
        }
    }
}