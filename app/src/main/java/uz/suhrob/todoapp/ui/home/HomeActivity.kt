package uz.suhrob.todoapp.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.databinding.ActivityHomeBinding
import uz.suhrob.todoapp.ui.home.dialogs.AddDialogFragment

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(R.id.home_navigation_container) as NavHostFragment).navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addTaskFragment, R.id.addNoteFragment, R.id.addCheckListFragment -> {
                    binding.addBtn.hide()
                    binding.homeBottomNavigationview.selectedItemId = 0
                }
                else -> binding.addBtn.show()
            }
        }
        binding.homeBottomNavigationview.background = null
        binding.homeBottomNavigationview.labelVisibilityMode =
            LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        binding.homeBottomNavigationview.menu.getItem(2).isEnabled = false
        binding.homeBottomNavigationview.setOnNavigationItemReselectedListener { }
        binding.addBtn.setOnClickListener {
            AddDialogFragment(
                addTaskListener = { navController.navigate(R.id.addTaskFragment) },
                addQuickNoteListener = { navController.navigate(R.id.addNoteFragment) },
                addCheckListListener = { navController.navigate(R.id.addCheckListFragment) }
            ).show(supportFragmentManager, "AddDialogFragment")
        }
        binding.homeBottomNavigationview.setupWithNavController(navController)
    }
}