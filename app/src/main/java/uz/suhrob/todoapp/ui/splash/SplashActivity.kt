package uz.suhrob.todoapp.ui.splash

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.ui.auth.AuthActivity
import uz.suhrob.todoapp.ui.home.HomeActivity
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
        var firstRunObserved = false
        todoPreferences.isFirstRun.asLiveData().observe(this) { isFirstRun ->
            if (firstRunObserved) {
                return@observe
            }
            when {
                isFirstRun -> startNewActivity(OnboardingActivity::class.java)
                firebaseAuth.currentUser == null -> startNewActivity(AuthActivity::class.java)
                else -> startNewActivity(HomeActivity::class.java)
            }
            firstRunObserved = true
        }
    }
}