package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentAddQuickNoteBinding
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class AddNoteFragment: BaseFragment<FragmentAddQuickNoteBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddQuickNoteBinding = FragmentAddQuickNoteBinding.inflate(inflater, container, false)

}