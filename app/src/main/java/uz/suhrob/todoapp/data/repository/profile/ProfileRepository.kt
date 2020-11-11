package uz.suhrob.todoapp.data.repository.profile

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.Resource

interface ProfileRepository {
    fun getUserName(): Flow<String?>
    fun getUserEmail(): Flow<String?>
    fun getProfilePicture(): Flow<String?>
    fun getCreatedTasksCount(): Flow<Int>
    fun getCompletedTasks(): Flow<Int>
    fun uploadPicture(arr: ByteArray): Flow<Resource<String>>
    suspend fun removePicture()
}