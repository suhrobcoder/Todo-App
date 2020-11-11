package uz.suhrob.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.Todo

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Update
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo ORDER  BY due_date ASC")
    fun getAllTodos(): Flow<List<Todo>>

    @Query("DELETE FROM todo")
    suspend fun clearTable()
}