package uz.suhrob.todoapp.data.repository.note

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.dao.NoteDao
import uz.suhrob.todoapp.data.database.entity.Note

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {
    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override suspend fun clearTable() = noteDao.clearTable()
}