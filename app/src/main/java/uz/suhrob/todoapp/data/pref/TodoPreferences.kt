package uz.suhrob.todoapp.data.pref

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoPreferences(
    context: Context
) {
    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = "todo_data_store"
    )

    val isFirstRun: Flow<Boolean>
        get() = dataStore.data.map { prefs ->
            prefs[KEY_FIRST_RUN] ?: true
        }

    suspend fun saveFirstRun(firstRun: Boolean) {
        dataStore.edit { prefs->
            prefs[KEY_FIRST_RUN] = firstRun
        }
    }

    companion object {
        private val KEY_FIRST_RUN = preferencesKey<Boolean>("is_first_run")
    }

}