package uz.suhrob.todoapp.ui.auth

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.databinding.FragmentForgetPasswordBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeActivity
import uz.suhrob.todoapp.util.displayBackButton
import uz.suhrob.todoapp.util.setToolbar
import uz.suhrob.todoapp.util.startNewActivity
import uz.suhrob.todoapp.util.toast

class ForgetPasswordFragment : BaseFragment<FragmentForgetPasswordBinding>() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentForgetPasswordBinding =
        FragmentForgetPasswordBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signinToolbar.title = ""
        setToolbar(binding.signinToolbar)
        displayBackButton()
        binding.signinToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.forgotBtn.setOnClickListener {
            val email = binding.forgotEmail.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.forgotEmail.error = "Enter a valid email"
                return@setOnClickListener
            }
            lifecycleScope.launchWhenStarted {
                viewModel.sendPasswordResetRequest(email).collect {
                    when (it) {
                        is Resource.Loading -> binding.forgotBtn.setLoading(true)
                        is Resource.Error -> {
                            binding.forgotBtn.setLoading(false)
                            withContext(Dispatchers.Main) {
                                toast(it.error)
                            }
                        }
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                toast("Password reset instructions are sent")
                                startNewActivity(HomeActivity::class.java)
                            }
                        }
                    }
                }
            }
        }
    }
}