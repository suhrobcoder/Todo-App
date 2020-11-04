package uz.suhrob.todoapp.data.repository.tag

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Tag

interface TagRepository {
    suspend fun insertTag(tag: Tag)
    suspend fun updateTag(tag: Tag)
    suspend fun deleteTag(tag: Tag)
    fun getAllTags(): Flow<List<Tag>>
}