package uz.suhrob.todoapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.suhrob.todoapp.databinding.FragmentAddCheckListBinding
import uz.suhrob.todoapp.ui.base.BaseFragment

class AddCheckListFragment : BaseFragment<FragmentAddCheckListBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddCheckListBinding = FragmentAddCheckListBinding.inflate(inflater, container, false)

}