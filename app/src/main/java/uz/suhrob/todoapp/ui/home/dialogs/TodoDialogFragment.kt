package uz.suhrob.todoapp.ui.home.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.suhrob.todoapp.data.database.entity.Todo
import uz.suhrob.todoapp.databinding.DialogTodoBinding
import uz.suhrob.todoapp.util.toFormattedDate

class TodoDialogFragment(
    private val todo: Todo,
    private val completeListener: (Todo) -> Unit
) : DialogFragment() {
    private var _binding: DialogTodoBinding? = null
    private val binding: DialogTodoBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoTitle.text = todo.title
        binding.desciptionText.text = todo.description
        binding.dueDateText.text = todo.dueDate.toFormattedDate()
        binding.tagText.text = todo.tagId.toString()
        binding.completeBtn.setOnClickListener {
            completeListener.invoke(todo.apply { isDone = true })
            dismiss()
        }
    }
}