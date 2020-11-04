package uz.suhrob.todoapp.ui.auth

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.util.setStatusBarColor

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        setStatusBarColor(Color.WHITE)
    }

    override fun onBackPressed() {
        if (!findNavController(R.id.auth_navigation_container).popBackStack()) {
            super.onBackPressed()
        }
    }
}