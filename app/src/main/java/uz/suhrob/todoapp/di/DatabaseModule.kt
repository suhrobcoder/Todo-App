package uz.suhrob.todoapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import uz.suhrob.todoapp.data.database.TodoDatabase
import uz.suhrob.todoapp.data.database.dao.CheckListWithItemsDao
import uz.suhrob.todoapp.data.database.dao.NoteDao
import uz.suhrob.todoapp.data.database.dao.TagDao
import uz.suhrob.todoapp.data.database.dao.TodoDao
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object DatabaseModule {
    @ActivityRetainedScoped
    @Provides
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, TodoDatabase::class.java, "todo.db").build()

    @ActivityRetainedScoped
    @Provides
    fun provideNoteDao(
        database: TodoDatabase
    ): NoteDao = database.getNoteDao()

    @ActivityRetainedScoped
    @Provides
    fun provideTagDao(
        database: TodoDatabase
    ): TagDao = database.getTagDao()

    @ActivityRetainedScoped
    @Provides
    fun provideTodoDao(
        database: TodoDatabase
    ): TodoDao = database.getTodoDao()

    @ActivityRetainedScoped
    @Provides
    fun provideCheckListWithItemsDao(
        database: TodoDatabase
    ): CheckListWithItemsDao = database.getCheckListWithItemsDao()
}