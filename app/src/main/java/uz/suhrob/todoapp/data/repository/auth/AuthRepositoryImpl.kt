package uz.suhrob.todoapp.data.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.model.User
import uz.suhrob.todoapp.data.pref.TodoPreferences

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val todoPreferences: TodoPreferences
) : AuthRepository {
    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): MutableLiveData<Resource<User>> {
        val authLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
        authLiveData.value = Resource.Loading
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            val user = getUserFromFireStore(email)
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
    ): MutableLiveData<Resource<User>> {
        val authLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
        authLiveData.value = Resource.Loading
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.user?.let {
                        val user = User(it.uid, name, it.email!!)
                        authLiveData.value = Resource.Success(user)
                        CoroutineScope(Dispatchers.IO).launch {
                            todoPreferences.saveUser(user)
                            saveUserToFirestore(user)
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

    override fun sendPasswordResetRequest(email: String): MutableLiveData<Resource<String>> {
        val authLiveData: MutableLiveData<Resource<String>> = MutableLiveData()
        authLiveData.value = Resource.Loading
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

    override suspend fun saveUserToFirestore(user: User) {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        usersCollection.add(user).await()
    }

    override suspend fun getUserFromFireStore(email: String): User {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        val query = usersCollection
            .whereEqualTo("email", email)
            .get()
            .await()
        if (query.isEmpty) {
            return User("", "", "")
        } else {
            return query.documents[0].toObject(User::class.java)!!
        }
    }

}