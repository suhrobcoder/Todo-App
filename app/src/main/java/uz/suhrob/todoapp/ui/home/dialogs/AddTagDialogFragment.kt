package uz.suhrob.todoapp.ui.home.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.databinding.AddTagDialogLayoutBinding

class AddTagDialogFragment(
    private val addListener: ((Tag) -> Unit)
) : DialogFragment() {
    private var _binding: AddTagDialogLayoutBinding? = null
    private val binding: AddTagDialogLayoutBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = AddTagDialogLayoutBinding.inflate(inflater, container, false)
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
        binding.addTagBtn.setOnClickListener {
            if (binding.tagTitle.text.isNotEmpty()) {
                addListener.invoke(
                    Tag(
                        binding.tagTitle.text.toString(),
                        binding.colorPicker.selectedColor
                    )
                )
                dismiss()
            } else {
                binding.tagTitle.error = "Title mustn't be empty"
            }
        }
    }
}