package uz.suhrob.todoapp.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.CheckList
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.CheckListWithItems
import uz.suhrob.todoapp.databinding.FragmentAddCheckListBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.custom.CheckListItemView
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.util.displayBackButton
import uz.suhrob.todoapp.util.onBackPressed
import uz.suhrob.todoapp.util.setToolbar
import uz.suhrob.todoapp.util.toast

@AndroidEntryPoint
class AddCheckListFragment : BaseFragment<FragmentAddCheckListBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCheckListBinding = FragmentAddCheckListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.addChecklistToolbar)
        displayBackButton()
        binding.addChecklistToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.addChecklistToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.addNewItem.setOnClickListener {
            CheckListItemView(requireContext()).apply {
                binding.checklistItems.addView(this)
                closeBtnListener = { binding.checklistItems.removeView(this) }
            }
        }
        binding.addChecklistBtn.setOnClickListener {
            val title = binding.checkListTitle.text.toString()
            if (title.isEmpty()) {
                binding.checkListTitle.error = "Title can't be empty"
                return@setOnClickListener
            }
            val checkListItems = arrayListOf<CheckListItem>()
            for (i in 0 until binding.checklistItems.childCount) {
                val itemView = binding.checklistItems.getChildAt(i) as CheckListItemView
                val item = itemView.getCheckListItem() ?: return@setOnClickListener
                checkListItems.add(item)
            }
            if (checkListItems.isEmpty()) {
                toast("Items is empty")
                return@setOnClickListener
            }
            viewModel.newCheckList(
                CheckListWithItems(
                    CheckList(
                        title,
                        binding.noteColorPicker.selectedColor
                    ),
                    checkListItems
                )
            )
            onBackPressed()
        }
    }
}