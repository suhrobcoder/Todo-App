package uz.suhrob.todoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT COUNT(id) FROM todo")
    fun getCreatedTasks(): Flow<Int>

    @Query("SELECT COUNT(id) FROM todo WHERE is_done = 1")
    fun getCompletedTasks(): Flow<Int>
}