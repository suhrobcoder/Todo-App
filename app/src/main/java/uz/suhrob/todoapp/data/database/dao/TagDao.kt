package uz.suhrob.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Tag

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag)

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tag")
    fun getAllTags(): Flow<List<Tag>>
}