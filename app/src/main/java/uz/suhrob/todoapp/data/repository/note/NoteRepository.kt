package uz.suhrob.todoapp.data.repository.note

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Note

interface NoteRepository {
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getAllNotes(): Flow<List<Note>>
}