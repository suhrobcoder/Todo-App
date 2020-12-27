package uz.suhrob.todoapp.data.repository.auth

import kotlinx.coroutines.flow.MutableStateFlow
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.model.User

interface AuthRepository {
    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): MutableStateFlow<Resource<User>>

    fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): MutableStateFlow<Resource<User>>

    fun sendPasswordResetRequest(
        email: String
    ): MutableStateFlow<Resource<String>>
}