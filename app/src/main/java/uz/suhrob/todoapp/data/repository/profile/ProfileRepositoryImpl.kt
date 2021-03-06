package uz.suhrob.todoapp.data.repository.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uz.suhrob.todoapp.data.FirestoreDataSource
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.database.dao.ProfileDao
import uz.suhrob.todoapp.data.pref.TodoPreferences

class ProfileRepositoryImpl(
    private val pref: TodoPreferences,
    private val profileDao: ProfileDao,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage,
    private val firestoreDataSource: FirestoreDataSource
) : ProfileRepository {
    override fun getUserName(): Flow<String?> = pref.userName

    override fun getUserEmail(): Flow<String?> = pref.userEmail

    override fun getProfilePicture(): Flow<String?> = pref.userProfilePicture

    override fun getCreatedTasksCount(): Flow<Int> = profileDao.getCreatedTasks()

    override fun getCompletedTasks(): Flow<Int> = profileDao.getCompletedTasks()

    override fun uploadPicture(arr: ByteArray): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        val path = firebaseStorage.getReference("profile").child(firebaseAuth.currentUser!!.uid)
        val uploadTask = path.putBytes(arr).await()
        if (uploadTask.task.isSuccessful) {
            val url = uploadTask.storage.downloadUrl.await()
            firestoreDataSource.updateProfilePicturePath(url.toString())
            emit(Resource.Success(url.toString()))
        } else {
            emit(Resource.Error(uploadTask.task.exception?.message ?: "Something went wrong"))
        }
    }

    override suspend fun removePicture() {
        firestoreDataSource.updateProfilePicturePath("")
    }
}