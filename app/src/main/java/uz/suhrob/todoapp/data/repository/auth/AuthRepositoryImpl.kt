package uz.suhrob.todoapp.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.suhrob.todoapp.data.FirestoreDataSource
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.model.User
import uz.suhrob.todoapp.data.pref.TodoPreferences

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val todoPreferences: TodoPreferences,
    private val firestoreDataSource: FirestoreDataSource
) : AuthRepository {
    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): MutableStateFlow<Resource<User>> {
        val authLiveData: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Loading)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            val user = firestoreDataSource.getUser(email)
                            withContext(Dispatchers.Main) {
                                authLiveData.value = Resource.Success(user)
                            }
                            todoPreferences.saveUser(user)
                        }
                    }
                } else {
                    authLiveData.value = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> Resource.Error("Email or password is wrong")
                        else -> Resource.Error("Something is went wrong")
                    }
                }
            }
        return authLiveData
    }

    override fun signUpWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): MutableStateFlow<Resource<User>> {
        val authLiveData: MutableStateFlow<Resource<User>> = MutableStateFlow(Resource.Loading)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        val user = User(it.uid, name, it.email!!)
                        authLiveData.value = Resource.Success(user)
                        CoroutineScope(Dispatchers.IO).launch {
                            todoPreferences.saveUser(user)
                            firestoreDataSource.saveUser(user)
                        }
                    }
                } else {
                    authLiveData.value = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> Resource.Error("This is email is used")
                        else -> Resource.Error("Something went wrong")
                    }
                }
            }
        return authLiveData
    }

    override fun sendPasswordResetRequest(email: String): MutableStateFlow<Resource<String>> {
        val authLiveData: MutableStateFlow<Resource<String>> = MutableStateFlow(Resource.Loading)
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authLiveData.value = Resource.Success(email)
                } else {
                    authLiveData.value = Resource.Error("Something went wrong")
                }
            }
        return authLiveData
    }
}