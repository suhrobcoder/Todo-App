package uz.suhrob.todoapp.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.suhrob.todoapp.data.model.User

val Context.dataStore:  DataStore<Preferences> by preferencesDataStore(name = "todo_data_store")

class TodoPreferences(
    private val context: Context
) {
    val isFirstRun: Flow<Boolean>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_FIRST_RUN] ?: true
        }
    val userId: Flow<String?>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_USER_ID]
        }
    val userName: Flow<String?>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_USER_NAME]
        }
    val userEmail: Flow<String?>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_USER_EMAIL]
        }
    val userProfilePicture: Flow<String?>
        get() = context.dataStore.data.map { prefs ->
            prefs[KEY_PROFILE_PICTURE]
        }

    suspend fun saveFirstRun(firstRun: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FIRST_RUN] = firstRun
        }
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = user.uid
            prefs[KEY_USER_NAME] = user.name
            prefs[KEY_USER_EMAIL] = user.email
            prefs[KEY_PROFILE_PICTURE] = user.profilePicture
        }
    }

    companion object {
        private val KEY_FIRST_RUN = booleanPreferencesKey("is_first_run")
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_PROFILE_PICTURE = stringPreferencesKey("profile_picture")
    }
}