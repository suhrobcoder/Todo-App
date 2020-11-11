package uz.suhrob.todoapp.data.repository.tag

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.dao.TagDao
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.TagWithTasksCount

class TagRepositoryImpl(
    private val tagDao: TagDao
): TagRepository {
    override suspend fun insertTag(tag: Tag) = tagDao.insertTag(tag)

    override suspend fun updateTag(tag: Tag) = tagDao.updateTag(tag)

    override suspend fun deleteTag(tag: Tag) = tagDao.deleteTag(tag)

    override fun getAllTags(): Flow<List<TagWithTasksCount>> = tagDao.getAllTags()

    override suspend fun clearTable() = tagDao.clearTable()
}