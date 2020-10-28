package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentAddTaskBinding
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class AddTaskFragment: BaseFragment<FragmentAddTaskBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)

}