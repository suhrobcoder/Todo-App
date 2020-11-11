package uz.suhrob.todoapp.ui.home.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.Note
import uz.suhrob.todoapp.databinding.FragmentAddQuickNoteBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.util.displayBackButton
import uz.suhrob.todoapp.util.onBackPressed
import uz.suhrob.todoapp.util.setToolbar

@AndroidEntryPoint
class AddNoteFragment : BaseFragment<FragmentAddQuickNoteBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddQuickNoteBinding = FragmentAddQuickNoteBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNoteToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setToolbar(binding.addNoteToolbar)
        displayBackButton()
        binding.addNoteToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.addNoteBtn.setOnClickListener {
            val description = binding.noteDescription.text.toString()
            if (description.isEmpty()) {
                binding.noteDescription.error = "Description can't be empty"
                return@setOnClickListener
            }
            viewModel.newNote(
                Note(
                    description,
                    binding.noteColorPicker.selectedColor
                )
            )
            onBackPressed()
        }
    }
}