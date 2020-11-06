package uz.suhrob.todoapp.data.repository.auth

import androidx.lifecycle.MutableLiveData
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.model.User

interface AuthRepository {
    fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): MutableLiveData<Resource<User>>

    fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): MutableLiveData<Resource<User>>

    fun sendPasswordResetRequest(
        email: String
    ): MutableLiveData<Resource<String>>
}