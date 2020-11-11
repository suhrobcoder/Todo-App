package uz.suhrob.todoapp.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import uz.suhrob.todoapp.data.repository.auth.AuthRepository

class AuthViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ) = authRepository.signInWithEmailAndPassword(email, password)

    fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ) = authRepository.signUpWithEmailAndPassword(name, email, password)

    fun sendPasswordResetRequest(
        email: String
    ) = authRepository.sendPasswordResetRequest(email)
}