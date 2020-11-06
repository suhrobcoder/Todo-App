package uz.suhrob.todoapp.ui.home

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.suhrob.todoapp.databinding.DialogChangeImageBinding

class ImageChangeDialog(
    private val fromCameraListener: (() -> Unit),
    private val fromStorageListener: (() -> Unit),
    private val removeListener: (() -> Unit)
) : DialogFragment() {
    private var _binding: DialogChangeImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        _binding = DialogChangeImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.changeImageCamera.setOnClickListener {
            fromCameraListener()
            dismiss()
        }
        binding.changeImageStorage.setOnClickListener {
            fromStorageListener()
            dismiss()
        }
        binding.removeImage.setOnClickListener {
            removeListener()
            dismiss()
        }
    }
}