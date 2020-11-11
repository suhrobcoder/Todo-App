package uz.suhrob.todoapp.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.model.FilterMode
import uz.suhrob.todoapp.databinding.FragmentMyTasksBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.util.setToolbar
import javax.inject.Inject

@AndroidEntryPoint
class MyTasksFragment : BaseFragment<FragmentMyTasksBinding>() {
    private val viewModel: HomeViewModel by activityViewModels()
    private var menu: Menu? = null

    @Inject
    lateinit var todoAdapter: TodoListAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyTasksBinding = FragmentMyTasksBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(binding.myTaskToolbar)
        setHasOptionsMenu(true)
        todoAdapter.apply {
            editListener = { todo -> viewModel.updateTodo(todo) }
            deleteListener = { todo -> viewModel.deleteTodo(todo) }
            todoClickListener = { todo ->
                TodoDialogFragment(todo) {
                    viewModel.updateTodo(it)
                }.show(childFragmentManager, "TodoDialogFragment")
            }
        }
        binding.todoRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoAdapter
        }
        binding.calendarView.setDateSelectedListener { _, date, selected ->
            if (selected) {
                binding.todoRecycler.layoutManager?.scrollToPosition(
                    todoAdapter.getDatePosition(
                        date
                    )
                )
            }
        }
        viewModel.allTodos.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it)
            binding.calendarView.setDecorateDays(todoAdapter.getDays())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_task_menu, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_incomplete -> {
                setMenuIcons(0)
                todoAdapter.filter(FilterMode.INCOMPLETE)
            }
            R.id.filter_complete -> {
                setMenuIcons(1)
                todoAdapter.filter(FilterMode.COMPLETED)
            }
            R.id.filter_all -> {
                setMenuIcons(2)
                todoAdapter.filter(FilterMode.ALL)
            }
        }
        return false
    }

    private fun setMenuIcons(index: Int) {
        menu?.getItem(0)?.subMenu?.getItem(0)?.setIcon(R.drawable.transparent_icon)
        menu?.getItem(0)?.subMenu?.getItem(1)?.setIcon(R.drawable.transparent_icon)
        menu?.getItem(0)?.subMenu?.getItem(2)?.setIcon(R.drawable.transparent_icon)
        menu?.getItem(0)?.subMenu?.getItem(index)?.setIcon(R.drawable.ic_filter_check)
    }
}