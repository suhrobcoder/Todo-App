package uz.suhrob.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.suhrob.todoapp.data.database.dao.CheckListWithItemsDao
import uz.suhrob.todoapp.data.database.dao.NoteDao
import uz.suhrob.todoapp.data.database.dao.TagDao
import uz.suhrob.todoapp.data.database.dao.TodoDao
import uz.suhrob.todoapp.data.database.entity.*

@Database(entities = [Note::class, Tag::class, Todo::class, CheckList::class, CheckListItem::class], version = 1)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
    abstract fun getTagDao(): TagDao
    abstract fun getTodoDao(): TodoDao
    abstract fun getCheckListWithItemsDao(): CheckListWithItemsDao
}