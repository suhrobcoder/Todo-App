package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class MyTasksFragment: BaseFragment<FragmentMyTasksBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyTasksBinding = FragmentMyTasksBinding.inflate(inflater, container, false)

}