package uz.suhrob.todoapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.databinding.FragmentSigninBinding
import uz.suhrob.todoapp.ui.base.BaseFragment
import uz.suhrob.todoapp.ui.home.HomeActivity
import uz.suhrob.todoapp.util.*

class SignInFragment : BaseFragment<FragmentSigninBinding>() {
    private val viewModel: AuthViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSigninBinding = FragmentSigninBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signinToolbar.title = ""
        setToolbar(binding.signinToolbar)
        displayBackButton()
        binding.signinToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.forgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgetPasswordFragment2)
        }
        binding.toSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.signinBtn.setOnClickListener {
            val email = binding.signinEmail.text.toString()
            val password = binding.signinPassword.text.toString()
            var emailAndPasswordAreValid = true
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signinEmail.error = "Enter a valid email"
                emailAndPasswordAreValid = false
            }
            if (password.length < 6) {
                binding.signinPassword.error = "Password length must be at least 6 characters"
                emailAndPasswordAreValid = false
            }
            if (!emailAndPasswordAreValid) {
                return@setOnClickListener
            }
            viewModel.signInWithEmailAndPassword(email, password).observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> binding.signinBtn.setLoading(true)
                    is Resource.Error -> {
                        binding.signinBtn.setLoading(false)
                        toast(it.error)
                    }
                    is Resource.Success -> {
                        toast("Sign in is successfully")
                        startNewActivity(HomeActivity::class.java)
                    }
                }
            }
        }
    }
}