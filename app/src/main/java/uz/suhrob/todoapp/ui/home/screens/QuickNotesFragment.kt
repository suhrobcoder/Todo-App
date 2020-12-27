package uz.suhrob.todoapp.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.suhrob.todoapp.databinding.FragmentQuickNotesBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.ui.home.adapters.NotesAndCheckListsAdapter
import uz.suhrob.todoapp.util.setToolbar
import javax.inject.Inject

@AndroidEntryPoint
class QuickNotesFragment : BaseFragment<FragmentQuickNotesBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var notesAndCheckListsAdapter: NotesAndCheckListsAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuickNotesBinding = FragmentQuickNotesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.quickNotesToolbar)
        binding.quickNotesRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAndCheckListsAdapter
        }
        notesAndCheckListsAdapter.editListener = { item ->
            viewModel.updateCheckListItem(item)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.allCheckListAndNotes.collect {
                if (it.isEmpty()) {
                    binding.quickNotesRecyclerview.visibility = View.GONE
                    binding.noItems.visibility = View.VISIBLE
                } else {
                    binding.quickNotesRecyclerview.visibility = View.VISIBLE
                    binding.noItems.visibility = View.GONE
                }
                notesAndCheckListsAdapter.submitList(it)
            }
        }
    }
}