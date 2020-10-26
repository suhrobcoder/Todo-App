package uz.suhrob.todoapp.data.model

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

inline fun <reified T> Query.asLiveData(): FirestoreQueryLiveData<T> {
    return FirestoreQueryLiveData(this, T::class.java)
}

inline fun <reified T> DocumentReference.asLiveData(): FirestoreDocumentLiveData<T> {
    return FirestoreDocumentLiveData(this, T::class.java)
}

class FirestoreQueryLiveData<T>(
    private val query: Query,
    private val clazz: Class<T>
): LiveData<List<T>>() {
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration?.remove()
        listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            this.value = snapshot?.toObjects(clazz)
        }
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }
}

class FirestoreDocumentLiveData<T>(
    private val docRef: DocumentReference,
    private val clazz: Class<T>
): LiveData<T>() {
    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration?.remove()
        listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            this.value = snapshot?.toObject(clazz)
        }
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }
}