package uz.suhrob.todoapp.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import uz.suhrob.todoapp.databinding.FragmentMenuBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.ui.home.adapters.TagAdapter
import uz.suhrob.todoapp.ui.home.dialogs.AddTagDialogFragment
import uz.suhrob.todoapp.util.setToolbar
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()

    @Inject
    lateinit var tagAdapter: TagAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMenuBinding = FragmentMenuBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.menuToolbar)
        tagAdapter.apply {
            addListener = {
                AddTagDialogFragment { tag ->
                    viewModel.newTag(tag)
                }.show(childFragmentManager, "AddTagFragment")
            }
            tagListener = { tag ->

            }
        }
        binding.tagRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = tagAdapter
        }
        lifecycleScope.launchWhenStarted {
            viewModel.allTags.collect {
                tagAdapter.submitList(it)
            }
        }
    }
}