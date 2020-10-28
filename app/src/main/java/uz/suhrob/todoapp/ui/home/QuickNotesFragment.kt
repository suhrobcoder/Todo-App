package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.databinding.FragmentProfileBinding
import uz.suhrob.todoapp.databinding.FragmentQuickNotesBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class QuickNotesFragment: BaseFragment<FragmentQuickNotesBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentQuickNotesBinding = FragmentQuickNotesBinding.inflate(inflater, container, false)

}