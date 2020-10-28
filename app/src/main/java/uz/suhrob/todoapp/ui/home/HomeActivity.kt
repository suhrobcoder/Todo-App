package uz.suhrob.todoapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.CheckList
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.CheckListWithItems
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.databinding.ActivityHomeBinding

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
        binding.homeBottomNavigationview.labelVisibilityMode =
            LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        binding.homeBottomNavigationview.menu.getItem(2).isEnabled = false
        binding.addBtn.setOnClickListener {
            navController.navigate(R.id.addTaskFragment)
        }
        binding.homeBottomNavigationview.setupWithNavController(navController)
        viewModel.newTag(Tag(0, "Personal", Color.GREEN))
        viewModel.allTags.observe(this) {
            Log.d("AppDebug", it.toString())
        }
        val checkList = CheckList(0, "HomeWork", Color.GREEN)
        val items = listOf(
            CheckListItem(0, "MB", 1, false, Color.GREEN),
            CheckListItem(0, "DG", 1, false, Color.GREEN),
            CheckListItem(0, "FV", 1, false, Color.GREEN),
            CheckListItem(0, "AB", 1, false, Color.GREEN)
        )
        viewModel.newCheckList(CheckListWithItems(checkList, items))
        viewModel.allCheckLists.observe(this) {
            Log.d("AppDebug", it.toString())
        }
    }
}