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
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.databinding.FragmentSignupBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeActivity
import uz.suhrob.todoapp.util.*

class SignUpFragment : BaseFragment<FragmentSignupBinding>() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupToolbar.title = ""
        setToolbar(binding.signupToolbar)
        displayBackButton()
        binding.signupToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toSignin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        binding.signupBtn.setOnClickListener {
            var credentialsAreValid = true
            val name = binding.signupName.text.toString()
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupPasswordConfirm.text.toString()
            if (name.isEmpty()) {
                binding.signupName.error = "Name mustn't be empty"
                credentialsAreValid = false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signupEmail.error = "Enter a valid email address"
                credentialsAreValid = false
            }
            if (password.length < 6) {
                binding.signupPassword.error = "Password length must be at least 6 characters"
                credentialsAreValid = false
            }
            if (password != confirmPassword) {
                binding.signupPasswordConfirm.error = "Enter same password here"
                credentialsAreValid = false
            }
            if (!credentialsAreValid) {
                return@setOnClickListener
            }
            lifecycleScope.launchWhenStarted {
                viewModel.signUpWithEmailAndPassword(name, email, password).collect {
                    when (it) {
                        is Resource.Loading -> binding.signupBtn.setLoading(true)
                        is Resource.Error -> {
                            binding.signupBtn.setLoading(false)
                            withContext(Dispatchers.Main) {
                                toast(it.error)
                            }
                        }
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                toast("Sign up is successfully")
                                startNewActivity(HomeActivity::class.java)
                            }
                        }
                    }
                }
            }
        }
    }
}