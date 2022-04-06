package uz.suhrob.todoapp.ui.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.suhrob.todoapp.data.repository.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
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