package uz.suhrob.todoapp.data.repository.tag

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.TagWithTasksCount

interface TagRepository {
    suspend fun insertTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
    fun getAllTags(): Flow<List<TagWithTasksCount>>
    suspend fun clearTable()
}