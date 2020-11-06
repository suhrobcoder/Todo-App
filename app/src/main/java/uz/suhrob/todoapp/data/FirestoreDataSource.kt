package uz.suhrob.todoapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uz.suhrob.todoapp.data.model.User
import uz.suhrob.todoapp.data.pref.TodoPreferences
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val pref: TodoPreferences
){
    suspend fun saveUser(user: User) {
        val usersCollection = firestore.collection("users")
        usersCollection.add(user).await()
    }

    suspend fun getUser(email: String): User {
        val usersCollection = firestore.collection("users")
        val query = usersCollection
            .whereEqualTo("email", email)
            .get()
            .await()
        return query.documents[0].toObject(User::class.java)!!
    }

    suspend fun updateProfilePicturePath(path: String) {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        val query = usersCollection
            .whereEqualTo("uid", firebaseAuth.currentUser!!.uid)
            .get()
            .await()
        val document = query.documents[0]
        val user = document.toObject(User::class.java)?.apply { profilePicture = path }!!
        pref.saveUser(user)
        usersCollection.document(document.id).set(user).await()
    }
}