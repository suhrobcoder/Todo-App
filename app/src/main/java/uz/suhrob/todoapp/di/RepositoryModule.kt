package uz.suhrob.todoapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import uz.suhrob.todoapp.data.FirestoreDataSource
import uz.suhrob.todoapp.data.database.dao.*
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepository
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepositoryImpl
import uz.suhrob.todoapp.data.repository.note.NoteRepository
import uz.suhrob.todoapp.data.repository.note.NoteRepositoryImpl
import uz.suhrob.todoapp.data.repository.profile.ProfileRepository
import uz.suhrob.todoapp.data.repository.profile.ProfileRepositoryImpl
import uz.suhrob.todoapp.data.repository.tag.TagRepository
import uz.suhrob.todoapp.data.repository.tag.TagRepositoryImpl
import uz.suhrob.todoapp.data.repository.todo.TodoRepository
import uz.suhrob.todoapp.data.repository.todo.TodoRepositoryImpl

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

    @ActivityRetainedScoped
    @Provides
    fun provideProfileRepository(
        pref: TodoPreferences,
        profileDao: ProfileDao,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage,
        firestoreDataSource: FirestoreDataSource
    ): ProfileRepository =
        ProfileRepositoryImpl(pref, profileDao, firebaseAuth, firebaseStorage, firestoreDataSource)
}