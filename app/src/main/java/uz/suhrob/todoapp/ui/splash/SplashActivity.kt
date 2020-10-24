package uz.suhrob.todoapp.ui.splash

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.ui.home.HomeActivity
import uz.suhrob.todoapp.ui.login.LoginActivity
import uz.suhrob.todoapp.ui.onboarding.OnboardingActivity
import uz.suhrob.todoapp.util.setStatusBarColor
import uz.suhrob.todoapp.util.startNewActivity
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    @Inject
    lateinit var todoPreferences: TodoPreferences

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(Color.WHITE)
        todoPreferences.isFirstRun.asLiveData().observe(this) { isFirstRun ->
            when {
                isFirstRun -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        todoPreferences.saveFirstRun(false)
                    }
                    startNewActivity(OnboardingActivity::class.java)
                }
                firebaseAuth.currentUser == null -> startNewActivity(LoginActivity::class.java)
                else -> startNewActivity(HomeActivity::class.java)
            }
        }
    }
}