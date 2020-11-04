package uz.suhrob.todoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import uz.suhrob.todoapp.data.database.dao.CheckListWithItemsDao
import uz.suhrob.todoapp.data.database.dao.NoteDao
import uz.suhrob.todoapp.data.database.dao.TagDao
import uz.suhrob.todoapp.data.database.dao.TodoDao
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepository
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepositoryImpl
import uz.suhrob.todoapp.data.repository.note.NoteRepository
import uz.suhrob.todoapp.data.repository.note.NoteRepositoryImpl
import uz.suhrob.todoapp.data.repository.tag.TagRepository
import uz.suhrob.todoapp.data.repository.tag.TagRepositoryImpl
import uz.suhrob.todoapp.data.repository.todo.TodoRepository
import uz.suhrob.todoapp.data.repository.todo.TodoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @ActivityRetainedScoped
    @Provides
    fun provideTagRepository(
        tagDao: TagDao
    ): TagRepository = TagRepositoryImpl(tagDao)

    @ActivityRetainedScoped
    @Provides
    fun provideTodoRepository(
        todoDao: TodoDao
    ): TodoRepository = TodoRepositoryImpl(todoDao)

    @ActivityRetainedScoped
    @Provides
    fun provideNoteRepository(
        noteDao: NoteDao
    ): NoteRepository = NoteRepositoryImpl(noteDao)

    @ActivityRetainedScoped
    @Provides
    fun provideCheckListRepository(
        checkListWithItemsDao: CheckListWithItemsDao
    ): CheckListRepository = CheckListRepositoryImpl(checkListWithItemsDao)
}