package uz.suhrob.todoapp.data.pref

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.suhrob.todoapp.data.model.User

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
    val userId: Flow<String?>
        get() = dataStore.data.map { prefs ->
            prefs[KEY_USER_ID]
        }
    val userName: Flow<String?>
        get() = dataStore.data.map { prefs ->
            prefs[KEY_USER_NAME]
        }
    val userEmail: Flow<String?>
        get() = dataStore.data.map { prefs ->
            prefs[KEY_USER_EMAIL]
        }

    suspend fun saveFirstRun(firstRun: Boolean) {
        dataStore.edit { prefs->
            prefs[KEY_FIRST_RUN] = firstRun
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { prefs->
            prefs[KEY_USER_ID] = user.uid
            prefs[KEY_USER_NAME] = user.name
            prefs[KEY_USER_EMAIL] = user.email
        }
    }

    companion object {
        private val KEY_FIRST_RUN = preferencesKey<Boolean>("is_first_run")
        private val KEY_USER_ID = preferencesKey<String>("user_id")
        private val KEY_USER_NAME = preferencesKey<String>("user_name")
        private val KEY_USER_EMAIL = preferencesKey<String>("user_email")
    }

}