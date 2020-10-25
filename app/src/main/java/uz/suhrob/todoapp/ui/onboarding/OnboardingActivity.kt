package uz.suhrob.todoapp.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.databinding.ActivityOnboardingBinding
import uz.suhrob.todoapp.ui.login.LoginActivity
import uz.suhrob.todoapp.util.startNewActivity
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var todoPreferences: TodoPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.getStartedBtn.apply {
            background =
                ContextCompat.getDrawable(context, R.drawable.get_started_btn_background_selector)
        }
        binding.onboardingViewpager.apply {
            adapter = OnboardingAdapter()
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        if (position + positionOffset > 1) {
                            binding.getStartedBtn.visibility = View.INVISIBLE
                        } else {
                            binding.getStartedBtn.visibility = View.VISIBLE
                        }
                        binding.waveView.setScroll(position, positionOffset)
                    }
                })
            }
        }
        binding.dotsIndicator.setViewPager2(binding.onboardingViewpager)
        binding.getStartedBtn.setOnClickListener {
            binding.onboardingViewpager.currentItem++
        }
        binding.loginBtn.setOnClickListener {
            startNewActivity(LoginActivity::class.java)
        }
        CoroutineScope(Dispatchers.Main).launch {
            todoPreferences.saveFirstRun(false)
        }
    }
}