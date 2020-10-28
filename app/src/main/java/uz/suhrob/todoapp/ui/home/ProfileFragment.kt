package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.databinding.FragmentProfileBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class ProfileFragment: BaseFragment<FragmentProfileBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)

}