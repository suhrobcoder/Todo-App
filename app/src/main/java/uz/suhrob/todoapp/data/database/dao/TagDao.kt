package uz.suhrob.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.TagWithTasksCount

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT COUNT(todo.tag_id) as tasksCount, tag.id, tag.title, tag.color FROM tag LEFT JOIN todo ON tag.id == todo.tag_id GROUP BY tag.id")
    fun getAllTags(): Flow<List<TagWithTasksCount>>

    @Query("DELETE FROM tag")
    suspend fun clearTable()
}