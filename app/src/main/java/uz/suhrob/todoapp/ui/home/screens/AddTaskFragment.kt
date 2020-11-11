package uz.suhrob.todoapp.ui.home.screens

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.Todo
import uz.suhrob.todoapp.databinding.FragmentAddTaskBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeViewModel
import uz.suhrob.todoapp.util.*
import java.util.*

@AndroidEntryPoint
class AddTaskFragment : BaseFragment<FragmentAddTaskBinding>() {
    private val calendar = Calendar.getInstance()
    private val viewModel: HomeViewModel by activityViewModels()
    private var tags = arrayOf<Tag>()
    private var selectedTag: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddTaskBinding = FragmentAddTaskBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTaskToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setToolbar(binding.addTaskToolbar)
        displayBackButton()
        binding.addTaskToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.addTaskBtn.setOnClickListener {
            val title = binding.taskTitle.text.toString()
            val description = binding.taskDescription.text.toString()
            if (title.isEmpty()) {
                binding.taskTitle.error = "Title can't be empty"
                return@setOnClickListener
            }
            viewModel.newTodo(
                Todo(
                    title,
                    description,
                    calendar.timeInMillis,
                    tags.first { it.title == selectedTag }.id
                )
            )
            onBackPressed()
        }
        binding.dueDateButton.setOnClickListener {
            showDatePickerDialog()
        }
        binding.taskTag.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Select tag")
                .setSingleChoiceItems(
                    tags.map { it.title }.toTypedArray(),
                    tags.indexOfFirst { it.title == selectedTag }
                ) { dialog, which ->
                    selectedTag = tags[which].title
                    binding.taskTag.text = selectedTag
                    dialog.dismiss()
                }.apply {
                    create().show()
                }
        }
        viewModel.allTags.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewModel.newTag(Tag("Personal", Color.RED))
                return@observe
            }
            tags = it.map { tagWithTasksCount -> tagWithTasksCount.tag }.toTypedArray()
            if (binding.taskTag.text.toString().isEmpty()) {
                binding.taskTag.text = tags[0].title
                selectedTag = tags[0].title
            }
        }
    }

    private fun showDatePickerDialog() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePickerDialog()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = calendar.timeInMillis - 1000
            show()
        }
    }

    private fun showTimePickerDialog() {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                setTimeText()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun setTimeText() {
        binding.dueDateButton.text =
            calendar.timeInMillis.toFormattedDate() + " " + calendar.timeInMillis.toFormattedTime()
    }
}