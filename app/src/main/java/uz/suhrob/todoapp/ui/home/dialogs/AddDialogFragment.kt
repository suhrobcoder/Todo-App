package uz.suhrob.todoapp.ui.home.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.suhrob.todoapp.databinding.DialogAddBinding

class AddDialogFragment(
    private val addTaskListener: () -> Unit,
    private val addQuickNoteListener: () -> Unit,
    private val addCheckListListener: () -> Unit
) : DialogFragment() {
    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addTask.setOnClickListener {
            addTaskListener.invoke()
            dismiss()
        }
        binding.addQuickNote.setOnClickListener {
            addQuickNoteListener.invoke()
            dismiss()
        }
        binding.addCheckList.setOnClickListener {
            addCheckListListener.invoke()
            dismiss()
        }
    }
}