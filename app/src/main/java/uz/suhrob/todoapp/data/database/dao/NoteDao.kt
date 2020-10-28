package uz.suhrob.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Note

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>
}